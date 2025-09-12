package com.zl.utils.excel.other;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelToMapHandler extends DefaultHandler {

    private final SharedStringsTable sst;
    private final List<Map<String, String>> rows;
    private final List<String> headers;
    private String lastContents;
    private boolean nextIsString;
    private Map<String, String> currentRow;
    private boolean isFirstRow;

    public ExcelToMapHandler(SharedStringsTable sst) {
        this.sst = sst;
        this.rows = new ArrayList<>();
        this.headers = new ArrayList<>();
        this.isFirstRow = true;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        currentRow = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (name.equals("row")) {
            currentRow = new HashMap<>();
        } else if (name.equals("c")) {
            lastContents = "";
            String cellType = attributes.getValue("t");
            nextIsString = cellType != null && cellType.equals("s");
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (name.equals("t") || name.equals("v")) {
            // Trim whitespace and check for empty content
            String value = lastContents.trim();
            // Check if the cell is empty or contains only whitespace
            if (nextIsString) {
                int idx = Integer.parseInt(value);
                value = new XSSFRichTextString(String.valueOf(sst.getItemAt(idx))).toString();
            }
            if (isFirstRow) {
                headers.add(value);
            } else {
                // Use column index as key if header is missing or empty
                currentRow.put(headers.get(currentRow.size()), value);
            }
        } else if (name.equals("row")) {
            if (!isFirstRow && !currentRow.isEmpty()) {
                rows.add(new HashMap<>(currentRow)); // Create a copy of the row to add to the list
            } else {
                isFirstRow = false;
            }
            currentRow.clear(); // Clear the current row after adding it to the list
        } else if (name.equals("c")) {
            nextIsString = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);
        if (lastContents.isEmpty()) {
            lastContents = " "; // Or set a default value like "N/A"
        }
    }
}
