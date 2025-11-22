package com.EIPplatform.service.report.reporta05;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;

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

    /* -------------------------------------------------------------
     * PUBLIC API (Used by ReportService)
     * ------------------------------------------------------------- */

    public static <T> void mapTable(
            XWPFDocument doc,
            String marker,
            List<T> dataList,
            BiConsumer<T, XWPFTableRow> rowFiller) {

        // 1. No data -> remove table
        if (dataList == null || dataList.isEmpty()) {
            removeTableByMarker(doc, marker);
            log.info("Removed table '{}' because data is empty", marker);
            return;
        }

        // 2. Locate template row
        TableInfo tableInfo = findTemplateRow(doc, marker);
        if (tableInfo == null) {
            log.warn("Template '{}' not found in document", marker);
            return;
        }

        // 3. Insert rows for each item
        for (int i = 0; i < dataList.size(); i++) {
            XWPFTableRow newRow =
                    cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            T item = dataList.get(i);
            rowFiller.accept(item, newRow);
        }

        // 4. Remove template row
        tableInfo.table.removeRow(tableInfo.rowIndex);
    }

    /* -------------------------------------------------------------
     * TABLE SEARCH HELPERS
     * ------------------------------------------------------------- */

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

    /* -------------------------------------------------------------
     * TABLE REMOVAL
     * ------------------------------------------------------------- */

    public static void removeTableByMarker(XWPFDocument doc, String marker) {
        if (marker == null || marker.isEmpty()) return;

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

    /* -------------------------------------------------------------
     * ROW + CELL CLONING
     * ------------------------------------------------------------- */

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

    /* -------------------------------------------------------------
     * SET TEXT (TABLE-SAFE)
     * ------------------------------------------------------------- */

    public static void setCell(XWPFTableRow row, int index, String value) {
        if (index >= row.getTableCells().size()) return;

        XWPFTableCell cell = row.getCell(index);

        value = (value == null || value.trim().isEmpty()) ? "" : value;

        cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        XWPFRun run = p.createRun();
        run.setText(value);
    }

    /* -------------------------------------------------------------
     * FORMATTERS
     * ------------------------------------------------------------- */

    public static String fmt(Double d) {
        return d == null ? "" : String.format("%.2f", d);
    }

    public static String fmt(Integer i) {
        return i == null ? "" : String.valueOf(i);
    }

    public static String fmt(LocalDate d) {
        return d == null ? "" : d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
