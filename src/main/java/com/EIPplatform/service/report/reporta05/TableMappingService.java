package com.EIPplatform.service.report.reporta05;

import java.util.List;
import java.util.function.BiConsumer;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import lombok.extern.slf4j.Slf4j;

/**
 * UNIVERSAL TABLE MAPPING SERVICE
 * Supports:
 * - Detect template tables
 * - Remove table when data empty
 * - Clone template rows
 * - Fill rows dynamically (lambda-based)
 * - Auto formatting & null handling
 */
@Slf4j
public class TableMappingService {

    public static <T> void mapTable(
            XWPFDocument doc,
            String marker,
            List<T> dataList,
            BiConsumer<T, RowWriter> rowFiller,
            boolean needIndexing) {
        if (dataList == null || dataList.isEmpty()) {
            removeTableByMarker(doc, marker);
            log.info("Removed table '{}' because data is empty", marker);
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, marker);
        if (tableInfo == null) {
            log.warn("Template '{}' not found in document", marker);
            return;
        }

        for (int i = 0; i < dataList.size(); i++) {
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            RowWriter writer = new RowWriter(newRow);

            if (needIndexing) {
                writer.col(String.valueOf(i + 1));
            }

            rowFiller.accept(dataList.get(i), writer);
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);
    }

    private static class TableInfo {
        XWPFTable table;
        XWPFTableRow templateRow;
        int rowIndex;
        int colIndex;

        TableInfo(XWPFTable table, XWPFTableRow row, int rIdx, int cIdx) {
            this.table = table;
            this.templateRow = row;
            this.rowIndex = rIdx;
            this.colIndex = cIdx;
        }
    }

    private static TableInfo findTemplateRow(XWPFDocument doc, String marker) {
        for (XWPFTable t : doc.getTables()) {
            for (int r = 0; r < t.getNumberOfRows(); r++) {
                XWPFTableRow row = t.getRow(r);

                for (int c = 0; c < row.getTableCells().size(); c++) {
                    String txt = row.getCell(c).getText();

                    if (txt != null && txt.contains(marker)) {
                        return new TableInfo(t, row, r, c);
                    }
                }
            }
        }
        return null;
    }

    public static void removeTableByMarker(XWPFDocument doc, String marker) {
        if (marker == null || marker.isEmpty())
            return;

        List<IBodyElement> bodyElements = doc.getBodyElements();

        for (int i = 0; i < bodyElements.size(); i++) {
            IBodyElement element = bodyElements.get(i);

            if (element instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) element;

                // search inside table for marker
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        if (cell.getText() != null && cell.getText().contains(marker)) {
                            doc.removeBodyElement(i);
                            log.info("Removed table for marker: {}", marker);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void mapVerticalTable(
            XWPFDocument doc,
            String marker,
            List<String> values) {
        if (values == null || values.isEmpty()) {
            removeTableByMarker(doc, marker);
            log.info("Removed VERTICAL table '{}' because values empty", marker);
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, marker);
        if (tableInfo == null) {
            log.warn("Vertical template '{}' not found", marker);
            return;
        }

        XWPFTable table = tableInfo.table;

        int fillColumn = tableInfo.colIndex;

        for (int r = tableInfo.rowIndex; r < table.getNumberOfRows(); r++) {
            int valueIndex = r - tableInfo.rowIndex;

            if (valueIndex >= values.size())
                break;

            String val = values.get(valueIndex);
            setCell(table.getRow(r), fillColumn, val);
        }

        XWPFTableRow markerRow = table.getRow(tableInfo.rowIndex);
        XWPFTableCell cell = markerRow.getCell(tableInfo.colIndex);
        cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        XWPFRun run = p.createRun();
        run.setText(""); // clear marker
    }

    private static XWPFTableRow cloneRow(XWPFTable table, XWPFTableRow templateRow, int insertPos) {
        XWPFTableRow newRow = table.insertNewTableRow(insertPos);

        for (XWPFTableCell cell : templateRow.getTableCells()) {
            XWPFTableCell newCell = newRow.addNewTableCell();
            copyCell(cell, newCell);
        }

        return newRow;
    }

    private static void copyCell(XWPFTableCell src, XWPFTableCell target) {
        target.getCTTc().setTcPr(src.getCTTc().getTcPr());

        target.removeParagraph(0);

        for (XWPFParagraph p : src.getParagraphs()) {
            XWPFParagraph np = target.addParagraph();
            copyParagraph(p, np);
        }
    }

    private static void copyParagraph(XWPFParagraph src, XWPFParagraph target) {
        target.getCTP().setPPr(src.getCTP().getPPr());

        for (XWPFRun run : src.getRuns()) {
            XWPFRun nr = target.createRun();
            nr.setText(run.text());
            nr.getCTR().setRPr(run.getCTR().getRPr());
        }
    }

    public static void setCell(XWPFTableRow row, int index, String value) {
        if (index >= row.getTableCells().size())
            return;

        XWPFTableCell cell = row.getCell(index);

        value = (value == null || value.trim().isEmpty()) ? "" : value;

        cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        XWPFRun run = p.createRun();
        run.setText(value);
    }

    public static class RowWriter {
        private final XWPFTableRow row;
        private int colIndex = 0;

        public RowWriter(XWPFTableRow row) {
            this.row = row;
        }

        public RowWriter col(String value) {
            setCell(row, colIndex++, value);
            return this;
        }

        public RowWriter cols(String... values) {
            for (String val : values) {
                col(val);
            }
            return this;
        }
    }

}
