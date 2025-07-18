package com.zl.utils.excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ExcelReaderUtils {

    public static List<Map<String, String>> readExcel(String filePath) throws Exception {
        OPCPackage pkg = OPCPackage.open(filePath);
        XSSFReader reader = new XSSFReader(pkg);
        SharedStringsTable sst = (SharedStringsTable) reader.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        // 处理第一个 sheet
        InputStream sheet = reader.getSheetsData().next();
        // saveStreamToFile(sheet, "D:\\keywordHotTrend");
        InputSource sheetSource = new InputSource(sheet);
        parser.parse(sheetSource);
        ExcelToMapHandler handler = (ExcelToMapHandler) parser.getContentHandler();
        sheet.close();
        sheetSource.getByteStream().close();
        pkg.close();
        return handler.getRows();
    }

    public static XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ExcelToMapHandler handler = new ExcelToMapHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\data\\back\\BaiduSyncdisk\\电商订单备份\\未导入\\小红书-真维斯官方旗舰店-2024-12-06.xlsx";
            List<Map<String, String>> data = readExcel(filePath);
            for (Map<String, String> row : data) {
                // System.out.println(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveStreamToFile(InputStream inputStream, String filePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
