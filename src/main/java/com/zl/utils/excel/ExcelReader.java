package com.zl.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.*;

public class ExcelReader {

    /**
     * 读取Excel文件并返回List<Map<String, String>>格式的数据
     * @param filePath Excel文件路径
     * @return 包含所有行数据的列表，每行数据是一个Map，键为列名，值为单元格内容
     * @throws Exception 如果读取过程中发生错误
     */
    public static List<Map<String, String>> readExcel(String filePath) throws Exception {
        File file = new File(filePath);
        long fileSize = file.length();
        long threshold = 50 * 1024 * 1024; // 50MB阈值

        if (fileSize < threshold) {
            return readWithPOI(file);
        } else {
            return readWithEventModel(file);
        }
    }

    // 适用于较小文件的传统POI读取方式
    private static List<Map<String, String>> readWithPOI(File file) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);

            // 读取表头（第一行）
            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    headers.add(getCellValueAsString(cell));
                }
            }

            // 读取数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = cell != null ? getCellValueAsString(cell) : "";
                    rowData.put(headers.get(j), value);
                }
                result.add(rowData);

                // 每处理1000行检查是否需要暂停以避免内存溢出
                if (i % 1000 == 0) {
                    System.gc();
                }
            }
        }

        return result;
    }

    // 适用于大文件的基于事件模型的读取方式
    private static List<Map<String, String>> readWithEventModel(File file) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(file)) {
            XSSFReader reader = new XSSFReader(pkg);
            SharedStringsTable sst = (SharedStringsTable) reader.getSharedStringsTable();
            StylesTable styles = reader.getStylesTable();

            // 创建自定义的Sheet处理器
            SheetHandler sheetHandler = new SheetHandler();

            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(new XSSFSheetXMLHandler(styles, sst, sheetHandler, false));

            // 获取第一个工作表
            Iterator<InputStream> sheets = reader.getSheetsData();
            if (sheets.hasNext()) {
                InputStream sheetStream = sheets.next();
                InputSource sheetSource = new InputSource(sheetStream);
                parser.parse(sheetSource);
                sheetStream.close();

                return sheetHandler.getResult();
            }
        }

        return Collections.emptyList();
    }

    // 获取单元格值的字符串表示
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 避免科学计数法显示
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    return cell.getStringCellValue();
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    // 自定义Sheet处理器，用于事件模型
    private static class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
        private List<Map<String, String>> result = new ArrayList<>();
        private List<String> headers = new ArrayList<>();
        private Map<String, String> currentRow;
        private int currentCol = -1;
        private boolean isHeaderRow = true;

        @Override
        public void startRow(int rowNum) {
            if (rowNum == 0) {
                // 第一行是表头
                isHeaderRow = true;
                headers.clear();
            } else {
                // 数据行
                isHeaderRow = false;
                currentRow = new LinkedHashMap<>();
            }
            currentCol = -1;
        }

        @Override
        public void endRow(int rowNum) {
            if (isHeaderRow) {
                isHeaderRow = false;
            } else if (currentRow != null) {
                result.add(currentRow);
                currentRow = null;

                // 每处理10000行检查内存使用情况
                if (result.size() % 10000 == 0) {
                    System.gc();
                }
            }
        }



        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            // 解析列索引（例如"A1" -> 0, "B1" -> 1）
            if (cellReference != null) {
                String colStr = cellReference.replaceAll("\\d", "");
                currentCol = colToIndex(colStr);
            } else {
                currentCol++;
            }

            if (isHeaderRow) {
                // 处理表头
                if (currentCol >= 0) {
                    // 确保headers列表足够大
                    while (headers.size() <= currentCol) {
                        headers.add("");
                    }
                    headers.set(currentCol, formattedValue != null ? formattedValue : "");
                }
            } else if (currentRow != null && currentCol >= 0 && currentCol < headers.size()) {
                // 处理数据行
                String columnName = headers.get(currentCol);
                currentRow.put(columnName, formattedValue != null ? formattedValue : "");
            }
        }

        // 将Excel列字母转换为索引（例如"A"->0, "B"->1, "AA"->26）
        private int colToIndex(String colStr) {
            int index = 0;
            for (int i = 0; i < colStr.length(); i++) {
                char c = colStr.charAt(i);
                index = index * 26 + (c - 'A' + 1);
            }
            return index - 1;
        }

        public List<Map<String, String>> getResult() {
            return result;
        }
    }

    // 使用示例
    public static void main(String[] args) {
        try {
            String filePath = "large_file.xlsx";
            List<Map<String, String>> data = readExcel(filePath);

            // 打印前10行数据
            int count = 0;
            for (Map<String, String> row : data) {
                System.out.println("Row " + (count + 1) + ": " + row);
                count++;
                if (count >= 10) break; // 只打印前10行
            }

            System.out.println("Total rows processed: " + data.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
