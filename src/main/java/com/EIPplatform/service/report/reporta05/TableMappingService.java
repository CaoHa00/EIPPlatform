package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.util.List;

/**
 * Service xử lý NHIỀU BẢNG KHÁC NHAU
 * Mỗi bảng có:
 * - Template marker riêng
 * - Method fill riêng
 * - Cấu trúc cột riêng
 */
@Slf4j
public class TableMappingService {

    // ==================== BẢNG 1: MONITORING EXCEEDANCES ====================

    /**
     * Bảng 1: Waste Water Monitoring Exceedances (8 cột)
     * Template marker: {{TEMPLATE_ROW}}
     * 
     * Cấu trúc: TT | Tên điểm | Ký hiệu | Thời gian | Vị trí | Chỉ tiêu | Kết quả |
     * QCVN
     */
    public static void fillWasteWaterMonitoringTable(
            XWPFDocument doc,
            List<WasteWaterMonitoringExceedancesDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No monitoring exceedances data to fill");
            return;
        }

        // Tìm bảng với marker {{TEMPLATE_ROW}}
        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_ROW}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_ROW}} marker not found!");
            return;
        }

        // Clone và fill cho mỗi item
        for (int i = 0; i < exceedances.size(); i++) {
            WasteWaterMonitoringExceedancesDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 8 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getPointName()); // Tên điểm
            setCellText(newRow.getCell(2), item.getPointSymbol()); // Ký hiệu
            setCellText(newRow.getCell(3), item.getMonitoringDate()); // Thời gian
            setCellText(newRow.getCell(4), formatLocation(item.getLongitude(), item.getLatitude())); // Vị trí
            setCellText(newRow.getCell(5), item.getExceededParam()); // Chỉ tiêu
            setCellText(newRow.getCell(6), formatDouble(item.getResultValue())); // Kết quả
            setCellText(newRow.getCell(7), formatDouble(item.getQcvnLimit())); // QCVN
        }
        // Xóa template row
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info(" Filled {} monitoring exceedance records", exceedances.size());
    }

    // ==================== BẢNG 2: AUTO MONITORING STATS ====================

    /**
     * Bảng 2: Auto Monitoring Stats (6 cột)
     * Template marker: {{TEMPLATE_AUTO_STATS}}
     * 
     * Cấu trúc: TT | Thông số | Giá trị TB | Min | Max | Đơn vị
     */
    public static void fillAutoMonitoringStatsTable(
            XWPFDocument doc,
            List<AutoWWMonitoringStatsDTO> stats) {

        if (stats == null || stats.isEmpty()) {
            log.info("No auto monitoring stats data to fill");
            return;
        }

        // Tìm bảng với marker {{TEMPLATE_AUTO_STATS}}
        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AUTO_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AUTO_STATS}} marker not found!");
            return;
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);

        for (int i = 0; i < stats.size(); i++) {
            AutoWWMonitoringStatsDTO item = stats.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 6 cells (adjust theo cấu trúc bảng thực tế của bạn)
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getParamName()); // Thông số
            setCellText(newRow.getCell(2), formatInteger(item.getValDesign()));
            setCellText(newRow.getCell(3), formatInteger(item.getValReceived()));
            setCellText(newRow.getCell(4), formatInteger(item.getValError()));
            setCellText(newRow.getCell(5), formatDouble(item.getRatioReceivedDesign()));
            setCellText(newRow.getCell(6), formatDouble(item.getRatioErrorReceived()));
        }

        log.info(" Filled {} auto monitoring stats records", stats.size());
    }

    // ==================== BẢNG 3: AUTO MONITORING INCIDENTS ====================

    /**
     * Bảng 3: Auto Monitoring Incidents (5 cột)
     * Template marker: {{TEMPLATE_AUTO_INCIDENTS}}
     * 
     * Cấu trúc: TT | Ngày sự cố | Mô tả | Thời gian | Biện pháp xử lý
     */
    public static void fillAutoMonitoringIncidentsTable(
            XWPFDocument doc,
            List<AutoWWMonitoringIncidentsDTO> incidents) {

        if (incidents == null || incidents.isEmpty()) {
            log.info("No auto monitoring incidents data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AUTO_INCIDENTS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AUTO_INCIDENTS}} marker not found!");
            return;
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);

        for (int i = 0; i < incidents.size(); i++) {
            AutoWWMonitoringIncidentsDTO item = incidents.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + i);

            // Fill 5 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getIncidentName()); // Ngày sự cố
            setCellText(newRow.getCell(2), item.getIncidentTime()); // Mô tả
            setCellText(newRow.getCell(3), item.getIncidentRemedy()); // Thời gian
        }

        log.info(" Filled {} auto monitoring incidents records", incidents.size());
    }

    // ==================== BẢNG 4: QCVN EXCEEDANCES ====================

    /**
     * Bảng 4: QCVN Exceedances (6 cột)
     * Template marker: {{TEMPLATE_QCVN_EXCEED}}
     * 
     * Cấu trúc: TT | Ngày vượt | Thông số | Giá trị đo | Giới hạn QCVN | Nguyên
     * nhân
     */
    public static void fillQcvnExceedancesTable(
            XWPFDocument doc,
            List<AutoWWQcvnExceedancesDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No QCVN exceedances data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_QCVN_EXCEED}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_QCVN_EXCEED}} marker not found!");
            return;
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);

        for (int i = 0; i < exceedances.size(); i++) {
            AutoWWQcvnExceedancesDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + i);

            // Fill 6 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getParamName()); // Ngày vượt
            setCellText(newRow.getCell(2), formatInteger(item.getExceedDaysCount())); // Thông số
            setCellText(newRow.getCell(3), formatDouble(item.getQcvnLimitValue())); // Giá
            setCellText(newRow.getCell(4), formatDouble(item.getExceedRatioPercent())); // Giới

        }

        log.info(" Filled {} QCVN exceedances records", exceedances.size());
    }

    // ==================== HELPER CLASS & METHODS ====================

    /**
     * Class lưu thông tin về template row tìm được
     */
    private static class TableInfo {
        XWPFTable table;
        XWPFTableRow templateRow;
        int rowIndex;

        TableInfo(XWPFTable table, XWPFTableRow templateRow, int rowIndex) {
            this.table = table;
            this.templateRow = templateRow;
            this.rowIndex = rowIndex;
        }
    }

    /**
     * Tìm template row theo marker (refactored để tái sử dụng)
     */
    private static TableInfo findTemplateRow(XWPFDocument doc, String marker) {
        for (XWPFTable table : doc.getTables()) {
            for (int i = 0; i < table.getRows().size(); i++) {
                XWPFTableRow row = table.getRow(i);
                String cellText = getCellText(row.getCell(0));

                if (cellText != null && cellText.contains(marker)) {
                    log.info("Found template row with marker '{}' at index: {}", marker, i);
                    return new TableInfo(table, row, i);
                }
            }
        }
        return null;
    }

    /**
     * Clone một hàng trong bảng với đầy đủ formatting
     */
    private static XWPFTableRow cloneRow(XWPFTable table, XWPFTableRow templateRow, int position) {
        XWPFTableRow newRow = table.insertNewTableRow(position);

        for (int i = 0; i < templateRow.getTableCells().size(); i++) {
            XWPFTableCell templateCell = templateRow.getCell(i);
            XWPFTableCell newCell = newRow.addNewTableCell();
            copyCell(templateCell, newCell);
        }

        return newRow;
    }

    /**
     * Copy cell với formatting
     */
    private static void copyCell(XWPFTableCell source, XWPFTableCell target) {
        for (XWPFParagraph para : source.getParagraphs()) {
            XWPFParagraph newPara = target.addParagraph();
            copyParagraph(para, newPara);
        }
    }

    /**
     * Copy paragraph với formatting
     */
    private static void copyParagraph(XWPFParagraph source, XWPFParagraph target) {
        target.setAlignment(source.getAlignment());

        for (XWPFRun run : source.getRuns()) {
            XWPFRun newRun = target.createRun();
            String text = run.getText(0);
            if (text != null) {
                newRun.setText(text);
            }
            copyRunFormatting(run, newRun);
        }
    }

    /**
     * Copy run formatting
     */
    private static void copyRunFormatting(XWPFRun source, XWPFRun target) {
        try {
            if (source.getFontFamily() != null) {
                target.setFontFamily(source.getFontFamily());
            }
            if (source.getFontSize() != -1) {
                target.setFontSize(source.getFontSize());
            }
            target.setBold(source.isBold());
            target.setItalic(source.isItalic());
            target.setUnderline(source.getUnderline());
            if (source.getColor() != null) {
                target.setColor(source.getColor());
            }
        } catch (Exception e) {
            log.warn("Could not copy all formatting: {}", e.getMessage());
        }
    }

    /**
     * Lấy text từ cell
     */
    private static String getCellText(XWPFTableCell cell) {
        if (cell == null)
            return null;
        return cell.getText();
    }

    /**
     * Set text cho cell
     */
    private static void setCellText(XWPFTableCell cell, String text) {
        if (cell == null)
            return;

        while (cell.getParagraphs().size() > 0) {
            cell.removeParagraph(0);
        }

        XWPFParagraph para = cell.addParagraph();
        XWPFRun run = para.createRun();
        run.setText(text != null ? text : "");
    }

    /**
     * Format location (kinh độ + vĩ độ)
     */
    private static String formatLocation(String longitude, String latitude) {
        if (longitude == null && latitude == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (longitude != null && !longitude.isEmpty()) {
            sb.append(longitude);
        }
        if (latitude != null && !latitude.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(latitude);
        }
        return sb.toString();
    }

    /**
     * Format Double thành String (2 chữ số thập phân)
     */
    private static String formatDouble(Double value) {
        if (value == null) {
            return "";
        }
        return String.format("%.2f", value);
    }

    /**
     * Format Integer thành String (không có chữ số thập phân)
     */
    private static String formatInteger(Integer value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }
}