package com.EIPplatform.service.report.reporta05;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

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
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            setCellText(newRow.getCell(4), item.getLongitude()); // kinh do
            setCellText(newRow.getCell(5), item.getLatitude()); // vi do
            setCellText(newRow.getCell(6), item.getExceededParam()); // Chỉ tiêu
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue())); // Kết quả
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit())); // QCVN
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit())); // QCVN
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
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

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
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 6 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getParamName()); // Ngày vượt
            setCellText(newRow.getCell(2), formatInteger(item.getExceedDaysCount())); // Thông số
            setCellText(newRow.getCell(3), formatDouble(item.getQcvnLimitValue())); // Giá
            setCellText(newRow.getCell(4), formatDouble(item.getExceedRatioPercent())); // Giới

        }

        log.info(" Filled {} QCVN exceedances records", exceedances.size());
    }

    // Bảng 2.1
    public static void fillAirMonitoringTable(
            XWPFDocument doc,
            List<AirMonitoringExceedanceDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No monitoring exceedances data to fill");
            return;
        }

        // Tìm bảng với marker {{TEMPLATE_AIR_ROW}}
        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_ROW}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_ROW}} marker not found!");
            return;
        }

        // Clone và fill cho mỗi item
        for (int i = 0; i < exceedances.size(); i++) {
            AirMonitoringExceedanceDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 8 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getPointName()); // Tên điểm
            setCellText(newRow.getCell(2), item.getPointSymbol()); // Ký hiệu
            setCellText(newRow.getCell(3), formatDate(item.getMonitoringDate())); // Thời gian
            setCellText(newRow.getCell(4), item.getLongitude()); // kinh do
            setCellText(newRow.getCell(5), item.getLatitude()); // vi do
            setCellText(newRow.getCell(6), item.getExceededParam()); // Chỉ tiêu
            setCellText(newRow.getCell(7), formatDouble(item.getResultValue())); // Kết quả
            setCellText(newRow.getCell(8), formatDouble(item.getQcvnLimit())); // QCVN
        }
        // Xóa template row
        tableInfo.table.removeRow(tableInfo.rowIndex);

        log.info(" Filled {} monitoring exceedance records", exceedances.size());
    }

    // bảng 2.2 Thống kê số liệu quan trắc khí thải
    public static void fillAirAutoMonitoringTable(
            XWPFDocument doc,
            List<AirAutoMonitoringStatDTO> stats) {

        if (stats == null || stats.isEmpty()) {
            log.info("No auto monitoring stats data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_AUTO_STATS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_AUTO_STATS}} marker not found!");
            return;
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);

        for (int i = 0; i < stats.size(); i++) {
            AirAutoMonitoringStatDTO item = stats.get(i);
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

    // Bảng 2.3 Sự cố quan trắc khí thải tự động
    public static void fillAirAutoMonitoringIncidentsTable(
            XWPFDocument doc,
            List<AirAutoMonitoringIncidentDTO> incidents) {

        if (incidents == null || incidents.isEmpty()) {
            log.info("No auto monitoring incidents data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_AUTO_INCIDENTS}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_AUTO_INCIDENTS}} marker not found!");
            return;
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);

        for (int i = 0; i < incidents.size(); i++) {
            AirAutoMonitoringIncidentDTO item = incidents.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 5 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getIncidentName()); // Ngày sự cố
            setCellText(newRow.getCell(2), item.getIncidentTime()); // Mô tả
            setCellText(newRow.getCell(3), item.getIncidentRemedy()); // Thời gian
        }

        log.info(" Filled {} auto monitoring incidents records", incidents.size());
    }

    // bảng 2.4 Các chỉ tiêu vượt QCVN
    public static void fillAirQcvnExceedancesTable(
            XWPFDocument doc,
            List<AirAutoQcvnExceedanceDTO> exceedances) {

        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No QCVN exceedances data to fill");
            return;
        }

        TableInfo tableInfo = findTemplateRow(doc, "{{TEMPLATE_AIR_QCVN_EXCEED}}");
        if (tableInfo == null) {
            log.warn("Template row with {{TEMPLATE_AIR_QCVN_EXCEED}} marker not found!");
            return;
        }

        tableInfo.table.removeRow(tableInfo.rowIndex);

        for (int i = 0; i < exceedances.size(); i++) {
            AirAutoQcvnExceedanceDTO item = exceedances.get(i);
            XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow, tableInfo.rowIndex + 1 + i);

            // Fill 6 cells
            setCellText(newRow.getCell(0), String.valueOf(i + 1)); // TT
            setCellText(newRow.getCell(1), item.getParamName()); // Ngày vượt
            setCellText(newRow.getCell(2), formatInteger(item.getExceedDaysCount())); // Thông số
            setCellText(newRow.getCell(3), formatInteger(item.getQcvnLimitValue())); // Giá
            setCellText(newRow.getCell(4), formatDouble(item.getExceedRatioPercent())); // Giới

        }

    }

    // Bảng 3.1 Thống
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
    }

    // Bảng 3.2 Thống kê chất thải rắn công nghiệp
    public static void fillIndustrialSolidWasteStatsTable(
            XWPFDocument doc,
            List<IndustrialSolidWasteStatDTO> exceedances) {
        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No domestic solid waste stats data to fill");
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
    }

    // Bảng 3.3 Thống kê sử dụng CTRCNTT
    public static void fillRecycleIndustrialWasteTable(
            XWPFDocument doc,
            List<RecycleIndustrialWasteStatDTO> exceedances) {
        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No domestic solid waste stats data to fill");
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
    }

    // bảng 3.4 Thống kê các loại CTRTT khác (nếu có) do doanh nghiệp tự xử lý
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
    }

    // bảng 4.1 Thống kê CTNH
    public static void fillHazardousWasteStatsTable(
            XWPFDocument doc,
            List<HazardousWasteStatDTO> exceedances) {
        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No other solid waste stats data to fill");
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
    }
    // bảng 4.2 Thống kê CTNH được nhập khẩ

    public static void fillExportedHwStatsTable(
            XWPFDocument doc,
            List<ExportedHwStatDTO> exceedances) {
        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No other solid waste stats data to fill");
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
            setCellText(newRow.getCell(4), formatDouble(item.getVolumeKg()));
            setCellText(newRow.getCell(5), item.getTransporterOrg());
            setCellText(newRow.getCell(6), item.getOverseasProcessorOrg());
        }
    }

    // bảng 4.3 Thống kê CTNH tự xử lý tại cơ sở (nếu có)
    public static void fillSeftTreatedHwStatsTable(
            XWPFDocument doc,
            List<SelfTreatedHwStatDTO> exceedances) {
        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No other solid waste stats data to fill");
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
            setCellText(newRow.getCell(3), formatDouble(item.getVolumeKg()));
            setCellText(newRow.getCell(4), item.getSelfTreatmentMethod());

        }

    }
    // bảng 7.1 Thôn tin về chủng loại và khối lượn

    public static void fillPopInventoryStatsTable(
            XWPFDocument doc,
            List<PopInventoryStatDTO> exceedances) {
        if (exceedances == null || exceedances.isEmpty()) {
            log.info("No other solid waste stats data to fill");
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
    }
    // bảng 7.2 ước tính chất ô nhiễm phát thải vào môi trường
    // public static void fillPopEmissionStatsTable(
    // XWPFDocument doc,
    // List<PopInventoryStatDTO> exceedances) {
    // if (exceedances == null || exceedances.isEmpty()) {
    // log.info("No other solid waste stats data to fill");
    // return;
    // }
    // TableInfo tableInfo = findTemplateRow(doc,
    // "{{TEMPLATE_POP_EMISSION_ESTIMATION}}");
    // if (tableInfo == null) {
    // log.warn("Template row with {{TEMPLATE_POP_EMISSION_ESTIMATION}} marker not
    // found!");
    // return;
    // }
    // for (int i = 0; i < exceedances.size(); i++) {
    // PopInventoryStatDTO item = exceedances.get(i);
    // XWPFTableRow newRow = cloneRow(tableInfo.table, tableInfo.templateRow,
    // tableInfo.rowIndex + 1 + i);
    // setCellText(newRow.getCell(0), String.valueOf(i + 1));
    // setCellText(newRow.getCell(1), item.getPopName());
    // setCellText(newRow.getCell(2), item.getCasCode());
    // setCellText(newRow.getCell(3), formatDouble(item.getAirEmission()));
    // setCellText(newRow.getCell(4), formatDouble(item.getWaterEmission()));
    // setCellText(newRow.getCell(5), formatDouble(item.getSoilEmission()));
    // }
    // }
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
     * Format LocalDate thành String theo pattern yyyy-MM-dd
     */
    private static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
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