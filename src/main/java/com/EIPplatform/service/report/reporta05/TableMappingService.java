package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.DomMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.IndMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class TableMappingService {

    /** ==================== Helper record ==================== */
    private static class TableInfo {
        XWPFTable table;
        XWPFTableRow templateRow;
        int rowIndex;
        int colIndex;

        TableInfo(XWPFTable table, XWPFTableRow templateRow, int rowIndex, int colIndex) {
            this.table = table;
            this.templateRow = templateRow;
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
        }
    }

    /** ==================== COMMON HELPERS ==================== */

    private static TableInfo findTemplateRow(XWPFDocument doc, String marker) {
        for (XWPFTable table : doc.getTables()) {
            for (int i = 0; i < table.getRows().size(); i++) {
                XWPFTableRow row = table.getRow(i);

                for (int j = 0; j < row.getTableCells().size(); j++) {
                    String text = getCellText(row.getCell(j));
                    if (text != null && text.contains(marker)) {
                        log.info("Found marker '{}' at row {}, col {}", marker, i, j);
                        return new TableInfo(table, row, i, j);
                    }
                }
            }
        }
        return null;
    }

    private static XWPFTableRow cloneRow(XWPFTable table, XWPFTableRow templateRow, int position) {
        XWPFTableRow newRow = table.insertNewTableRow(position);

        for (int i = 0; i < templateRow.getTableCells().size(); i++) {
            XWPFTableCell templateCell = templateRow.getCell(i);
            XWPFTableCell newCell = newRow.addNewTableCell();
            copyCell(templateCell, newCell);
        }

        return newRow;
    }

    private static void copyCell(XWPFTableCell source, XWPFTableCell target) {
        // Clear target
        while (target.getParagraphs().size() > 0) {
            target.removeParagraph(0);
        }

        for (XWPFParagraph para : source.getParagraphs()) {
            XWPFParagraph newPara = target.addParagraph();
            copyParagraph(para, newPara);
        }
    }

    private static void copyParagraph(XWPFParagraph source, XWPFParagraph target) {
        target.setAlignment(source.getAlignment());
        target.setVerticalAlignment(source.getVerticalAlignment());

        for (XWPFRun run : source.getRuns()) {
            XWPFRun newRun = target.createRun();
            String text = run.getText(0);
            if (text != null) {
                newRun.setText(text);
            }
            copyRunFormatting(run, newRun);
        }
    }

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

    private static String getCellText(XWPFTableCell cell) {
        if (cell == null) return null;
        return cell.getText();
    }

    private static void setCellText(XWPFTableCell cell, String text) {
        if (cell == null) return;

        while (cell.getParagraphs().size() > 0) {
            cell.removeParagraph(0);
        }

        XWPFParagraph para = cell.addParagraph();
        XWPFRun run = para.createRun();
        run.setText(text != null ? text : "");
    }

    private static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    private static String formatDouble(Double value) {
        if (value == null) {
            return "";
        }
        return String.format("%.2f", value);
    }

    private static String formatInteger(Integer value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    /** For table 1.3 auto stats – insert column dynamically */
    private static void insertColumnAtIndex(XWPFTable table, int colIndex) {
        for (XWPFTableRow row : table.getRows()) {

            int totalCols = row.getTableCells().size();
            XWPFTableCell newCell = row.addNewTableCell();

            for (int i = totalCols - 1; i > colIndex; i--) {
                XWPFTableCell src = row.getCell(i - 1);
                XWPFTableCell dest = row.getCell(i);
                copyCell(src, dest);
            }

            XWPFTableCell inserted = row.getCell(colIndex);
            while (inserted.getParagraphs().size() > 0) {
                inserted.removeParagraph(0);
            }
            inserted.addParagraph().createRun().setText("");
        }
    }

    /** ==================== 1.1 & 1.2 – Wastewater Monitoring Exceedances ==================== */

    /**
     * Bảng 1: Waste Water Monitoring Exceedances (8 cột)
     * Template marker: {{TEMPLATE_ROW}}
     *
     * Cấu trúc: TT | Tên điểm | Ký hiệu | Thời gian | Vị trí | Chỉ tiêu | Kết quả |
     * QCVN
     */
    // ==================== BẢNG 1.1: MONITORING EXCEEDANCES - DOMESTIC ====================

    /**
     * Bảng 1.1: Waste Water Monitoring Exceedances - DOMESTIC (9 cột)
     * Template marker: {{TEMPLATE_ROW}}
     *
     * Cấu trúc: TT | Tên điểm | Ký hiệu | Thời gian | Kinh độ | Vĩ độ | Chỉ tiêu | Kết quả | QCVN
     */
    public static void fillDomesticWasteWaterMonitoringTable(
            XWPFDocument doc,
            List<DomMonitoringExceedancesCreateDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No DOMESTIC monitoring exceedances data to fill");
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
            DomMonitoringExceedancesCreateDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 9 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getPointName()); // Tên điểm
            setCellText(newRow.getCell(2), item.getPointSymbol()); // Ký hiệu
            setCellText(newRow.getCell(3), item.getMonitoringDate()); // Thời gian
            setCellText(newRow.getCell(4), item.getLongitude()); // Kinh độ
            setCellText(newRow.getCell(5), item.getLatitude()); // Vĩ độ
            setCellText(newRow.getCell(6), item.getExceededParam()); // Chỉ tiêu
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue())); // Kết quả
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit())); // QCVN
        }

        // Xóa template row
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("✓ Filled {} DOMESTIC monitoring exceedance records", exceedances.size());
    }

// ==================== BẢNG 1.2: MONITORING EXCEEDANCES - INDUSTRIAL ====================

    /**
     * Bảng 1.2: Waste Water Monitoring Exceedances - INDUSTRIAL (9 cột)
     * Template marker: {{TEMPLATE_ROW_2}}
     *
     * Cấu trúc: TT | Tên điểm | Ký hiệu | Thời gian | Kinh độ | Vĩ độ | Chỉ tiêu | Kết quả | QCVN
     */
    public static void fillIndustrialWasteWaterMonitoringTable(
            XWPFDocument doc,
            List<IndMonitoringExceedancesCreateDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No INDUSTRIAL monitoring exceedances data to fill");
            return;
        }

        // Tìm bảng với marker {{TEMPLATE_ROW_2}}
        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_ROW_2}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_ROW_2}} marker not found!");
            return;
        }

        // Clone và fill cho mỗi item
        for (int i = 0; i < exceedances.size(); i++) {
            IndMonitoringExceedancesCreateDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 9 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getPointName()); // Tên điểm
            setCellText(newRow.getCell(2), item.getPointSymbol()); // Ký hiệu
            setCellText(newRow.getCell(3), item.getMonitoringDate()); // Thời gian
            setCellText(newRow.getCell(4), item.getLongitude()); // Kinh độ
            setCellText(newRow.getCell(5), item.getLatitude()); // Vĩ độ
            setCellText(newRow.getCell(6), item.getExceededParam()); // Chỉ tiêu
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue())); // Kết quả
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit())); // QCVN
        }

        // Xóa template row
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("✓ Filled {} INDUSTRIAL monitoring exceedance records", exceedances.size());
    }
    public static void fillWasteWaterMonitoringTable(
            XWPFDocument doc,
            List<WasteWaterMonitoringExceedancesDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No monitoring exceedances data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_ROW}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_ROW}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            WasteWaterMonitoringExceedancesDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getPointName());
            setCellText(newRow.getCell(2), item.getPointSymbol());
            setCellText(newRow.getCell(3), item.getMonitoringDate());
            setCellText(newRow.getCell(4), item.getLongitude());
            setCellText(newRow.getCell(5), item.getLatitude());
            setCellText(newRow.getCell(6), item.getExceededParam());
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue()));
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} monitoring exceedance records (table 1.1)", exceedances.size());
    }

    public static void fillWasteWaterMonitoringTable2(
            XWPFDocument doc,
            List<WasteWaterMonitoringExceedancesDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No monitoring exceedances data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_ROW_2}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_ROW_2}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            WasteWaterMonitoringExceedancesDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getPointName());
            setCellText(newRow.getCell(2), item.getPointSymbol());
            setCellText(newRow.getCell(3), item.getMonitoringDate());
            setCellText(newRow.getCell(4), item.getLongitude());
            setCellText(newRow.getCell(5), item.getLatitude());
            setCellText(newRow.getCell(6), item.getExceededParam());
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue()));
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} monitoring exceedance records (table 1.2)", exceedances.size());
    }

    /** ==================== 1.3 – Auto Monitoring Stats (wastewater) ==================== */

    /**
     * Bảng 3: Auto Monitoring Stats (6 cột)
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

        TableInfo info = findTemplateRow(doc, "{{TEMPLATE_AUTO_STATS}}");
        if (info == null) {
            log.warn("Template row with {{TEMPLATE_AUTO_STATS}} marker not found!");
            return;
        }

        XWPFTable table = info.table;
        int markerRow = info.rowIndex;
        int markerCol = info.colIndex;

        XWPFTableRow headerRow = table.getRow(markerRow);
        XWPFTableRow rowDesign = table.getRow(markerRow + 1);
        XWPFTableRow rowReceived = table.getRow(markerRow + 2);
        XWPFTableRow rowError = table.getRow(markerRow + 3);
        XWPFTableRow rowRD = table.getRow(markerRow + 4);
        XWPFTableRow rowRE = table.getRow(markerRow + 5);

        setCellText(headerRow.getCell(markerCol), "Thông số");

        for (AutoWWMonitoringStatsDTO item : stats) {
            insertColumnAtIndex(table, markerCol + 1);
            int col = markerCol + 1;

            setCellText(headerRow.getCell(col), item.getParamName());
            setCellText(rowDesign.getCell(col), formatInteger(item.getValDesign()));
            setCellText(rowReceived.getCell(col), formatInteger(item.getValReceived()));
            setCellText(rowError.getCell(col), formatInteger(item.getValError()));
            setCellText(rowRD.getCell(col), formatDouble(item.getRatioReceivedDesign()));
            setCellText(rowRE.getCell(col), formatDouble(item.getRatioErrorReceived()));
        }

        headerRow.removeCell(markerCol);
        log.info("Filled {} auto monitoring stats columns (table 1.3)", stats.size());
    }

    /** ==================== 1.4 – Auto Monitoring Incidents (wastewater) ==================== */

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

        for (int i = 0; i < incidents.size(); i++) {
            AutoWWMonitoringIncidentsDTO item = incidents.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getIncidentName());
            setCellText(newRow.getCell(2), item.getIncidentTime());
            setCellText(newRow.getCell(3), item.getIncidentRemedy());
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} auto monitoring incidents records (table 1.4)", incidents.size());
    }

    /** ==================== 1.5 – QCVN Exceedances (wastewater) ==================== */

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

        for (int i = 0; i < exceedances.size(); i++) {
            AutoWWQcvnExceedancesDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getParamName());
            setCellText(newRow.getCell(2), formatInteger(item.getExceedDaysCount()));
            setCellText(newRow.getCell(3), formatDouble(item.getQcvnLimitValue()));
            setCellText(newRow.getCell(4), formatDouble(item.getExceedRatioPercent()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} QCVN exceedances records (table 1.5)", exceedances.size());
    }

    /** ==================== 2.1 – Air Monitoring Exceedances ==================== */

    public static void fillAirMonitoringTable(
            XWPFDocument doc,
            List<AirMonitoringExceedanceDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No air monitoring exceedances data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_ROW}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_ROW}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            AirMonitoringExceedanceDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getPointName());
            setCellText(newRow.getCell(2), item.getPointSymbol());
            setCellText(newRow.getCell(3), item.getMonitoringDate());
            setCellText(newRow.getCell(4), item.getLongitude());
            setCellText(newRow.getCell(5), item.getLatitude());
            setCellText(newRow.getCell(6), item.getExceededParam());
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue()));
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} air monitoring exceedance records (table 2.1)", exceedances.size());
    }

    /** ==================== 2.2 – Air Auto Monitoring Stats ==================== */

    public static void fillAirAutoMonitoringTable(
            XWPFDocument doc,
            List<AirAutoMonitoringStatDTO> stats) {

        if (stats == null || stats.isEmpty()) {
            log.info("No air auto monitoring stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_AUTO_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_AUTO_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < stats.size(); i++) {
            AirAutoMonitoringStatDTO item = stats.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getParamName());
            setCellText(newRow.getCell(2), formatInteger(item.getValDesign()));
            setCellText(newRow.getCell(3), formatInteger(item.getValReceived()));
            setCellText(newRow.getCell(4), formatInteger(item.getValError()));
            setCellText(newRow.getCell(5), formatDouble(item.getRatioReceivedDesign()));
            setCellText(newRow.getCell(6), formatDouble(item.getRatioErrorReceived()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} air auto monitoring stats records (table 2.2)", stats.size());
    }

    /** ==================== 2.3 – Air Auto Monitoring Incidents ==================== */

    public static void fillAirAutoMonitoringIncidentsTable(
            XWPFDocument doc,
            List<AirAutoMonitoringIncidentDTO> incidents) {

        if (incidents == null || incidents.isEmpty()) {
            log.info("No air auto monitoring incidents data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_AUTO_INCIDENTS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_AUTO_INCIDENTS}} marker not found!");
            return;
        }

        for (int i = 0; i < incidents.size(); i++) {
            AirAutoMonitoringIncidentDTO item = incidents.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getIncidentName());
            setCellText(newRow.getCell(2), item.getIncidentTime());
            setCellText(newRow.getCell(3), item.getIncidentRemedy());
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} air auto monitoring incidents records (table 2.3)", incidents.size());
    }

    /** ==================== 2.4 – Air QCVN Exceedances ==================== */

    public static void fillAirQcvnExceedancesTable(
            XWPFDocument doc,
            List<AirAutoQcvnExceedanceDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No air QCVN exceedances data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_QCVN_EXCEED}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_QCVN_EXCEED}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            AirAutoQcvnExceedanceDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getParamName());
            setCellText(newRow.getCell(2), formatInteger(item.getExceedDaysCount()));
            setCellText(newRow.getCell(3), formatInteger(item.getQcvnLimitValue()));
            setCellText(newRow.getCell(4), formatDouble(item.getExceedRatioPercent()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} air QCVN exceedances records (table 2.4)", exceedances.size());
    }

    /** ==================== 3.1 – Domestic Solid Waste ==================== */

    public static void fillDomesticSolidWasteStatsTable(
            XWPFDocument doc,
            List<DomesticSolidWasteStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No domestic solid waste stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_DOMESTIC_SOLID_WASTE_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_DOMESTIC_SOLID_WASTE_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            DomesticSolidWasteStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getWasteTypeName());
            setCellText(newRow.getCell(2), formatDouble(item.getVolumeCy()));
            setCellText(newRow.getCell(3), item.getReceiverOrg());
            setCellText(newRow.getCell(4), formatDouble(item.getVolumePy()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} domestic solid waste stats records (table 3.1)", exceedances.size());
    }

    /** ==================== 3.2 – Industrial Solid Waste ==================== */

    public static void fillIndustrialSolidWasteStatsTable(
            XWPFDocument doc,
            List<IndustrialSolidWasteStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No industrial solid waste stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_INDUSTRIAL_SOLID_WASTE_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_INDUSTRIAL_SOLID_WASTE_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            IndustrialSolidWasteStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getWasteGroup());
            setCellText(newRow.getCell(2), formatDouble(item.getVolumeCy()));
            setCellText(newRow.getCell(3), item.getReceiverOrg());
            setCellText(newRow.getCell(4), formatDouble(item.getVolumePy()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} industrial solid waste stats records (table 3.2)", exceedances.size());
    }

    /** ==================== 3.3 – Recycle Industrial Waste ==================== */

    public static void fillRecycleIndustrialWasteTable(
            XWPFDocument doc,
            List<RecycleIndustrialWasteStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No recycle industrial waste stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_RECYCLE_INDUSTRIAL_WASTE_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_RECYCLE_INDUSTRIAL_WASTE_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            RecycleIndustrialWasteStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getTransferOrg());
            setCellText(newRow.getCell(2), formatDouble(item.getVolumeCy()));
            setCellText(newRow.getCell(3), item.getWasteTypeDesc());
            setCellText(newRow.getCell(4), formatDouble(item.getVolumePy()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} recycle industrial waste stats records (table 3.3)", exceedances.size());
    }

    /** ==================== 3.4 – Other Solid Waste ==================== */

    public static void fillOtherSolidWasteStatsTable(
            XWPFDocument doc,
            List<OtherSolidWasteStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No other solid waste stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_OTHER_SOLID_WASTE_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_OTHER_SOLID_WASTE_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            OtherSolidWasteStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getWasteGroupOther());
            setCellText(newRow.getCell(2), formatDouble(item.getVolumeCy()));
            setCellText(newRow.getCell(3), item.getSelfTreatmentMethod());
            setCellText(newRow.getCell(4), item.getReceiverOrg());
            setCellText(newRow.getCell(5), formatDouble(item.getVolumePy()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} other solid waste stats records (table 3.4)", exceedances.size());
    }

    /** ==================== 4.1 – Hazardous Waste ==================== */

    public static void fillHazardousWasteStatsTable(
            XWPFDocument doc,
            List<HazardousWasteStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No hazardous waste stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_HAZARDOUS_WASTE_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_HAZARDOUS_WASTE_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            HazardousWasteStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getWasteName());
            setCellText(newRow.getCell(2), item.getHwCode());
            setCellText(newRow.getCell(3), formatDouble(item.getVolumeCy()));
            setCellText(newRow.getCell(4), item.getTreatmentMethod());
            setCellText(newRow.getCell(5), item.getReceiverOrg());
            setCellText(newRow.getCell(6), formatDouble(item.getVolumePy()));
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} hazardous waste stats records (table 4.1)", exceedances.size());
    }

    /** ==================== 4.2 – Exported Hazardous Waste ==================== */

    public static void fillExportedHwStatsTable(
            XWPFDocument doc,
            List<ExportedHwStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No exported HW stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_EXPORTED_HW_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_EXPORTED_HW_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            ExportedHwStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getWasteName());
            setCellText(newRow.getCell(2), item.getHwCode());
            setCellText(newRow.getCell(3), item.getBaselCode());
            setCellText(newRow.getCell(4), formatDouble(item.getVolume()));
            setCellText(newRow.getCell(5), item.getTransporterOrg());
            setCellText(newRow.getCell(6), item.getOverseasProcessorOrg());
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} exported HW stats records (table 4.2)", exceedances.size());
    }

    /** ==================== 4.3 – Self Treated Hazardous Waste ==================== */

    public static void fillSelfTreatedHwStatsTable(
            XWPFDocument doc,
            List<SelfTreatedHwStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No self-treated HW stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_SELF_TREATED_HW_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_SELF_TREATED_HW_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            SelfTreatedHwStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getWasteName());
            setCellText(newRow.getCell(2), item.getHwCode());
            setCellText(newRow.getCell(3), formatDouble(item.getVolume()));
            setCellText(newRow.getCell(4), item.getSelfTreatmentMethod());
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} self-treated HW stats records (table 4.3)", exceedances.size());
    }

    /** ==================== 7.1 – POP Inventory ==================== */

    public static void fillPopInventoryStatsTable(
            XWPFDocument doc,
            List<PopInventoryStatDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No POP inventory stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_POP_INVENTORY_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_POP_INVENTORY_STATS}} marker not found!");
            return;
        }

        for (int i = 0; i < exceedances.size(); i++) {
            PopInventoryStatDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);
            setCellText(newRow.getCell(0), String.valueOf(i + 1));
            setCellText(newRow.getCell(1), item.getPopName());
            setCellText(newRow.getCell(2), item.getCasCode());
            setCellText(newRow.getCell(3), formatDouble(item.getImportVolume()));
            setCellText(newRow.getCell(4), item.getConcentration());
            setCellText(newRow.getCell(5), formatDouble(item.getVolumeUsed()));
            setCellText(newRow.getCell(6), formatDouble(item.getVolumeStocked()));
            setCellText(newRow.getCell(7), item.getComplianceResult());
        }
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info("Filled {} POP inventory stats records (table 7.1)", exceedances.size());
    }
}
