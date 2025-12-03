package com.EIPplatform.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.BusinessHistoryConsumption;
import com.EIPplatform.model.entity.businessInformation.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.service.report.reporta05.TableMappingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportA05DocUtil {

    private static final String TEMPLATE_PATH = "templates/reportA05/ReportA05_template_ver5.docx";

    public byte[] generateReportDocument(ReportA05 report, ReportA05DraftDTO draft) {

        BusinessDetail business = report.getBusinessDetail();
        if (business == null) {
            throw new IllegalArgumentException("ReportA05 has no BusinessDetail linked");
        }

        // DTO sections
        WasteWaterDataDTO wasteWaterData = draft.getWasteWaterData();
        AirEmissionDataDTO airEmissionData = draft.getAirEmissionData();
        WasteManagementDataDTO wasteManagementData = draft.getWasteManagementData();

        EnvPermits envPermits = business.getEnvPermits();
        List<BusinessHistoryConsumption> historyList = report.getBusinessDetail().getBusinessHistoryConsumptions();

        // Date placeholders
        LocalDate today = LocalDate.now();
        String day = String.valueOf(today.getDayOfMonth());
        String month = String.valueOf(today.getMonthValue());
        String year = String.valueOf(today.getYear());

        Map<String, String> data = new HashMap<>();
        Map<String, String> imageMapp = new HashMap<>();

        // 1. General info
        buildGeneralInfoData(data, business, envPermits, historyList, day, month, year, report);

        // 2–4 Section placeholders
        buildWasteWaterPlaceholders(data, wasteWaterData);
        buildAirEmissionPlaceholders(data, airEmissionData);
        buildWasteManagementPlaceholders(data, wasteManagementData);

        buildImagePlaceholders(imageMapp, wasteWaterData);
        data.put("inspeaction_remedy_report", defaultString(report.getInspectionRemedyReport()));
        Resource resource = new ClassPathResource(TEMPLATE_PATH);
        log.info("Loading ReportA05 template from: {}", TEMPLATE_PATH);

        try (InputStream fis = resource.getInputStream();
                XWPFDocument doc = new XWPFDocument(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Replace paragraphs
            for (XWPFParagraph para : doc.getParagraphs()) {
                replacePlaceholders(para, data, false);
            }

            // Replace inside tables
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replacePlaceholders(p, data, true);
                        }
                    }
                }
            }
            replaceImagePlaceholders(doc, imageMapp);
            // Fill dynamic tables
            fillTables(doc, wasteWaterData, airEmissionData, wasteManagementData);

            doc.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error while generating ReportA05 DOCX: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate ReportA05 document", e);
        }
    }

    // ---------------------------------------------------------------------
    // 1. General info
    // ---------------------------------------------------------------------

    private void buildGeneralInfoData(Map<String, String> data,
            BusinessDetail business,
            EnvPermits envPermits,
            List<BusinessHistoryConsumption> historyList,
            String day,
            String month,
            String year,
            ReportA05 report) {

        data.put("facility_name", defaultString(business.getFacilityName()));
        data.put("address", defaultString(business.getAddress()));
        data.put("phone_number", defaultString(business.getPhoneNumber()));
        //data.put("legal_representative", defaultString(business.getLegalRepresentative()));
        data.put("activity_type", defaultString(business.getActivityType()));
        //data.put("scale_capacity", defaultString(business.getScaleCapacity()));

        data.put("iso_14001_certificate", convertYesNoToVietnamese(business.getISO_certificate_14001()));
        data.put("business_license_number", defaultString(business.getBusinessRegistrationNumber()));
        data.put("tax_code", defaultString(business.getTaxCode()));

        if (business.getOperationType().name().equals("SEASONAL")) {
            data.put("operating_frequency", "Theo mùa");
            data.put("seasonal_period", business.getSeasonalDescription());

        } else {
            data.put("operating_frequency", "Thường xuyên");
            // data.put("seasonal_period", "");
        }

        if (envPermits != null) {
            data.put("env_permit_number", defaultString(envPermits.getPermitNumber()));
            data.put("env_permit_issue_date", formatDate(envPermits.getIssueDate()));
            data.put("env_permit_issuer", defaultString(envPermits.getIssuerOrg()));
        } else {
            data.put("env_permit_number", "");
            data.put("env_permit_issue_date", "");
            data.put("env_permit_issuer", "");
        }

        fillBusinessHistoryData(data, historyList);

        data.put("report_year", report.getReportYear() != null ? report.getReportYear().toString() : "");
        data.put("reporting_period", defaultString(report.getReportingPeriod()));
        data.put("report_code", defaultString(report.getReportCode()));

        data.put("dateStr", day);
        data.put("monthYearStr", month);
        data.put("yearStr", year);
    }

    private void fillBusinessHistoryData(Map<String, String> data,
            List<BusinessHistoryConsumption> historyList) {

        if (historyList == null || historyList.isEmpty()) {
            data.put("product_volume_cy", "");
            data.put("product_unit_cy", "");
            data.put("product_volume_py", "");
            data.put("product_unit_py", "");
            data.put("fuel_consumption_cy", "");
            data.put("fuel_unit_cy", "");
            data.put("fuel_consumption_py", "");
            data.put("fuel_unit_py", "");
            data.put("electricity_consumption_cy", "");
            data.put("electricity_consumption_py", "");
            data.put("water_consumption_cy", "");
            data.put("water_consumption_py", "");
            return;
        }

        List<BusinessHistoryConsumption> sorted = historyList.stream()
                .sorted(Comparator.comparing(BusinessHistoryConsumption::getCreatedAt))
                .toList();

        BusinessHistoryConsumption current = sorted.get(sorted.size() - 1);
        // BusinessHistoryConsumption previous = sorted.size() > 1
        // ? sorted.get(sorted.size() - 2)
        // : null;

        data.put("product_volume_cy", toStringOrEmpty(current.getProductVolumeCy()));
        data.put("product_unit_cy", convertUnitToVietnamese(current.getProductUnitCy()));
        data.put("fuel_consumption_cy", toStringOrEmpty(current.getFuelConsumptionCy()));
        data.put("fuel_unit_cy", convertUnitToVietnamese(current.getFuelUnitCy()));
        data.put("electricity_consumption_cy", toStringOrEmpty(current.getElectricityConsumptionCy()));
        data.put("water_consumption_cy", toStringOrEmpty(current.getWaterConsumptionCy()));

        data.put("product_volume_py", toStringOrEmpty(current.getProductVolumePy()));
        data.put("product_unit_py", convertUnitToVietnamese(current.getProductUnitPy()));
        data.put("fuel_consumption_py", toStringOrEmpty(current.getFuelConsumptionPy()));
        data.put("fuel_unit_py", convertUnitToVietnamese(current.getFuelUnitPy()));
        data.put("electricity_consumption_py", toStringOrEmpty(current.getElectricityConsumptionPy()));
        data.put("water_consumption_py", toStringOrEmpty(current.getWaterConsumptionPy()));

    }

    // ---------------------------------------------------------------------
    // 2. Waste Water placeholders (DTO version)
    // ---------------------------------------------------------------------

    private void buildWasteWaterPlaceholders(Map<String, String> data, WasteWaterDataDTO ww) {
        if (ww == null)
            return;

        data.put("ww_treatment_desc", defaultString(ww.getTreatmentWwDesc()));
        data.put("connection_status_desc", defaultString(ww.getConnectionStatusDesc()));
        // data.put("connection_diagram", defaultString(ww.getConnectionDiagram()));

        data.put("domestic_ww_cy", fmt(ww.getDomWwCy()));
        data.put("domestic_ww_py", fmt(ww.getDomWwPy()));
        data.put("domestic_ww_design", fmt(ww.getDomWwDesign()));

        data.put("industrial_ww_cy", fmt(ww.getIndustrialWwCy()));
        data.put("industrial_ww_py", fmt(ww.getIndustrialWwPy()));
        data.put("industrial_ww_design", fmt(ww.getIndustrialWwDesign()));

        data.put("cooling_water_cy", fmt(ww.getCoolingWaterCy()));
        data.put("cooling_water_py", fmt(ww.getCoolingWaterPy()));
        data.put("cooling_water_design", fmt(ww.getCoolingWaterDesign()));

        data.put("dom_monitor_period", defaultString(ww.getDomMonitorPeriod()));
        data.put("dom_monitor_freq", defaultString(ww.getDomMonitorFreq()));
        data.put("dom_monitor_locations", defaultString(ww.getDomMonitorLocations()));
        data.put("dom_sample_count", fmt(ww.getDomSampleCount()));
        data.put("dom_qcvn_standard", defaultString(ww.getDomQcvnStandard()));
        data.put("dom_agency_name", defaultString(ww.getDomAgencyName()));
        data.put("dom_agency_vimcerts", defaultString(ww.getDomAgencyVimcerts()));

        data.put("ind_monitor_period", defaultString(ww.getIndMonitorPeriod()));
        data.put("ind_monitor_freq", defaultString(ww.getIndMonitorFreq()));
        data.put("ind_monitor_locations", defaultString(ww.getIndMonitorLocations()));
        data.put("ind_sample_count", fmt(ww.getIndSampleCount()));
        data.put("ind_qcvn_standard", defaultString(ww.getIndQcvnStandard()));
        data.put("ind_agency_name", defaultString(ww.getIndAgencyName()));
        data.put("ind_agency_vimcerts", defaultString(ww.getIndAgencyVimcerts()));

        // Auto monitoring
        data.put("auto_station_location", defaultString(ww.getAutoStationLocation()));
        data.put("auto_station_GPS", defaultString(ww.getAutoStationGps()));
        data.put("auto_station_map", defaultString(ww.getAutoStationMap()));
        data.put("auto_source_desc", defaultString(ww.getAutoSourceDesc()));
        data.put("auto_data_frequency", defaultString(ww.getAutoDataFrequency()));
        data.put("auto_calibration_info", defaultString(ww.getAutoCalibrationInfo()));
        data.put("auto_incident_summary", defaultString(ww.getAutoIncidentSummary()));
        data.put("auto_downtime_desc", defaultString(ww.getAutoDowntimeDesc()));
        data.put("auto_exceed_days_summary", defaultString(ww.getAutoExceedDaysSummary()));
        data.put("auto_abnormal_reason", defaultString(ww.getAutoAbnormalReason()));
        data.put("auto_completeness_review", defaultString(ww.getAutoCompletenessReview()));
        data.put("auto_exceed_summary", defaultString(ww.getAutoExceedSummary()));
    }

    // ---------------------------------------------------------------------
    // 3. Air Emission (DTO)
    // ---------------------------------------------------------------------

    private void buildAirEmissionPlaceholders(Map<String, String> data, AirEmissionDataDTO air) {
        if (air == null)
            return;

        data.put("air_treatment_desc", defaultString(air.getAirTreatmentDesc()));
        data.put("air_emission_cy", fmt(air.getAirEmissionCy()));
        data.put("air_emission_py", fmt(air.getAirEmissionPy()));

        data.put("air_monitor_period", defaultString(air.getAirMonitorPeriod()));
        data.put("air_monitor_freq", defaultString(air.getAirMonitorFreq()));
        data.put("air_monitor_locations", defaultString(air.getAirMonitorLocations()));
        data.put("air_sample_count", fmt(air.getAirSampleCount()));
        data.put("air_qcvn_standard", defaultString(air.getAirQcvnStandard()));
        data.put("air_agency_name", defaultString(air.getAirAgencyName()));
        data.put("air_agency_vimcerts", defaultString(air.getAirAgencyVimcerts()));

        data.put("air_auto_station_location", defaultString(air.getAirAutoStationLocation()));
        data.put("air_auto_station_GPS", defaultString(air.getAirAutoStationGps()));
        data.put("air_auto_station_map", defaultString(air.getAirAutoStationMapFilePath()));
        data.put("air_auto_source_desc", defaultString(air.getAirAutoSourceDesc()));
        data.put("air_auto_data_frequency", defaultString(air.getAirAutoDataFrequency()));
        data.put("air_auto_param_list", defaultString(air.getAirAutoParamList()));
        data.put("air_auto_calibration_info", defaultString(air.getAirAutoCalibrationInfo()));
        data.put("air_auto_incident_summary", defaultString(air.getAirAutoIncidentSummary()));
        data.put("air_auto_downtime_desc", defaultString(air.getAirAutoDowntimeDesc()));

        data.put("air_auto_avg_calc_desc", defaultString(air.getAirAutoAvgCalcDesc()));
        data.put("air_auto_avg_compare_desc", defaultString(air.getAirAutoAvgCompareDesc()));
        data.put("air_auto_exceed_days_summary", defaultString(air.getAirAutoExceedDaysSummary()));
        data.put("air_auto_abnormal_reason", defaultString(air.getAirAutoAbnormalReason()));
        data.put("air_auto_completeness_review", defaultString(air.getAirAutoCompletenessReview()));
        data.put("air_auto_exceed_conclusion", defaultString(air.getAirAutoExceedConclusion()));
    }

    // ---------------------------------------------------------------------
    // 4. Waste Management (DTO)
    // ---------------------------------------------------------------------

    private void buildWasteManagementPlaceholders(Map<String, String> data, WasteManagementDataDTO wm) {
        if (wm == null)
            return;

        data.put("sw_general_note", defaultString(wm.getSwGeneralNote()));
        data.put("incident_plan_development", defaultString(wm.getIncidentPlanDevelopment()));
        data.put("incident_prevention_measures", defaultString(wm.getIncidentPreventionMeasures()));
        data.put("incident_response_report", defaultString(wm.getIncidentResponseReport()));

        data.put("water_total_volume", fmt(wm.getWaterTotalVolume()));
        data.put("water_estimation_method", defaultString(wm.getWaterEstimationMethod()));

        data.put("air_total_volume", fmt(wm.getAirTotalVolume()));
        data.put("air_estimation_method", defaultString(wm.getAirEstimationMethod()));

        data.put("soil_total_volume", fmt(wm.getSoilTotalVolume()));
        data.put("soil_estimation_method", defaultString(wm.getSoilEstimationMethod()));

        data.put("sewage_sludge_total_volume", fmt(wm.getSewageSludgeTotalVolume()));
        data.put("sewage_sludge_estimation_method", defaultString(wm.getSewageSludgeEstimationMethod()));

        data.put("hw_onsite_total_volume", fmt(wm.getHwOnsiteTotalVolume()));
        data.put("hw_onsite_estimation_method", defaultString(wm.getHwOnsiteEstimationMethod()));

        data.put("hw_recycle_total_volume", fmt(wm.getHwRecycleTotalVolume()));
        data.put("hw_recycle_estimation_method", defaultString(wm.getHwRecycleEstimationMethod()));

        data.put("hw_disposal_total_volume", fmt(wm.getHwDisposalTotalVolume()));
        data.put("hw_disposal_estimation_method", defaultString(wm.getHwDisposalEstimationMethod()));

    }

    private void buildImagePlaceholders(Map<String, String> imageMap, WasteWaterDataDTO ww) {
        if (ww == null)
            return;
        // imageMap.put("IMG_connection_diagram", ww.getConnectionDiagram());
        // imageMap.put("IMG_auto_station_map", ww.getAutoStationMap());
        imageMap.put("connection_diagram", ww.getConnectionDiagram());
    }

    // ---------------------------------------------------------------------
    // 5. Table filling (DTO versions)
    // ---------------------------------------------------------------------

    private void fillTables(
            XWPFDocument doc,
            WasteWaterDataDTO ww,
            AirEmissionDataDTO air,
            WasteManagementDataDTO wm) {

        // ------------------- WASTE WATER -------------------
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_ROW}}",
                ww == null ? null : ww.getDomMonitoringExceedances(),
                (item, row) -> row.cols(
                        item.getPointName(),
                        item.getPointSymbol(),
                        item.getMonitoringDate(),
                        item.getLongitude(),
                        item.getLatitude(),
                        item.getExceededParam(),
                        fmt(item.getResultValue()),
                        fmt(item.getQcvnLimit())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_ROW_2}}",
                ww == null ? null : ww.getIndMonitoringExceedances(),
                (item, row) -> row.cols(
                        item.getPointName(),
                        item.getPointSymbol(),
                        item.getMonitoringDate(),
                        item.getLongitude(),
                        item.getLatitude(),
                        item.getExceededParam(),
                        fmt(item.getResultValue()),
                        fmt(item.getQcvnLimit())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AUTO_STATS}}",
                air == null ? null : air.getAirAutoMonitoringStats(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getValDesign()),
                        fmt(item.getValReceived()),
                        fmt(item.getValError()),
                        fmt(item.getRatioReceivedDesign()),
                        fmt(item.getRatioErrorReceived())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AUTO_INCIDENTS}}",
                ww == null ? null : ww.getMonitoringIncidents(),
                (item, row) -> row.cols(
                        item.getIncidentName(),
                        item.getIncidentTime(),
                        item.getIncidentRemedy()),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_QCVN_EXCEED}}",
                ww == null ? null : ww.getQcvnExceedances(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getExceedDaysCount()),
                        fmt(item.getQcvnLimitValue()),
                        fmt(item.getExceedRatioPercent())),
                false);

        // ------------------- AIR -------------------
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_ROW}}",
                air == null ? null : air.getAirMonitoringExceedances(),
                (item, row) -> row.cols(
                        item.getPointName(),
                        item.getPointSymbol(),
                        fmt(item.getMonitoringDate()),
                        item.getLongitude(),
                        item.getLatitude(),
                        item.getExceededParam(),
                        fmt(item.getResultValue()),
                        fmt(item.getQcvnLimit())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_STATS}}",
                air == null ? null : air.getAirAutoMonitoringStats(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getValDesign()),
                        fmt(item.getValReceived()),
                        fmt(item.getValError()),
                        fmt(item.getRatioReceivedDesign()),
                        fmt(item.getRatioErrorReceived())),
                true);
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_STATS}}",
                air == null ? null : air.getAirAutoMonitoringStats(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getValDesign()),
                        fmt(item.getValReceived()),
                        fmt(item.getValError()),
                        fmt(item.getRatioReceivedDesign()),
                        fmt(item.getRatioErrorReceived())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_INCIDENTS}}",
                air == null ? null : air.getAirAutoMonitoringIncidents(),
                (item, row) -> row.cols(
                        item.getIncidentName(),
                        item.getIncidentTime(),
                        item.getIncidentRemedy()),
                false);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_QCVN_EXCEED}}",
                air == null ? null : air.getAirAutoQcvnExceedances(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getExceedDaysCount()),
                        fmt(item.getQcvnLimitValue()),
                        fmt(item.getExceedRatioPercent())),
                false);

        // ------------------- WASTE MANAGEMENT -------------------
        if (wm == null)
            return;

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_DOMESTIC_SOLID_WASTE_STATS}}",
                wm.getDomesticSolidWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteTypeName(),
                        fmt(item.getVolumeCy()),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_INDUSTRIAL_SOLID_WASTE_STATS}}",
                wm.getIndustrialSolidWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteGroup(),
                        fmt(item.getVolumeCy()),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_RECYCLE_INDUSTRIAL_WASTE_STATS}}",
                wm.getRecycleIndustrialWasteStats(),
                (item, row) -> row.cols(
                        item.getTransferOrg(),
                        fmt(item.getVolumeCy()),
                        item.getWasteTypeDesc(),
                        fmt(item.getVolumePy())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_OTHER_SOLID_WASTE_STATS}}",
                wm.getOtherSolidWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteGroupOther(),
                        fmt(item.getVolumeCy()),
                        item.getSelfTreatmentMethod(),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())),
                true);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_HAZARDOUS_WASTE_STATS}}",
                wm.getHazardousWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteName(),
                        item.getHwCode(),
                        fmt(item.getVolumeCy()),
                        item.getTreatmentMethod(),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())),
                false);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_EXPORTED_HW_STATS}}",
                wm.getExportedHwStats(),
                (item, row) -> row.cols(
                        item.getWasteName(),
                        item.getHwCode(),
                        item.getBaselCode(),
                        fmt(item.getVolume()),
                        item.getTransporterOrg(),
                        item.getOverseasProcessorOrg()),
                false);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_SELF_TREATED_HW_STATS}}",
                wm.getSelfTreatedHwStats(),
                (item, row) -> row.cols(
                        item.getWasteName(),
                        item.getHwCode(),
                        fmt(item.getVolume()),
                        item.getSelfTreatmentMethod()),
                false);

        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_POP_INVENTORY_STATS}}",
                wm.getPopInventoryStats(),
                (item, row) -> row.cols(
                        item.getPopName(),
                        item.getCasCode(),
                        item.getImportDate(),
                        fmt(item.getImportVolume()),
                        item.getConcentration(),
                        fmt(item.getVolumeUsed()),
                        fmt(item.getVolumeStocked()),
                        item.getComplianceResult()),
                true);
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private void replacePlaceholders(XWPFParagraph paragraph,
            Map<String, String> data,
            boolean inTable) {

        if (paragraph == null)
            return;

        String text = paragraph.getText();
        if (text == null || text.isEmpty())
            return;

        String original = text;

        // Replace known keys
        for (var entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String placeholder = "{{" + key + "}}";
            if (text.contains(placeholder)) {
                String filled = inTable
                        ? defaultString(value)
                        : defaultParagraphValue(value);

                text = text.replace(placeholder, filled);
            }
        }

        // Remove unknown placeholders
        text = text.replaceAll("\\{\\{(?!TEMPLATE_)[^}]+\\}\\}", "");

        // If nothing changed → skip
        if (original.equals(text))
            return;

        RunStyle normalStyle = null;

        // Rebuild run - XỬ LÝ XUỐNG DÒNG
        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        // Chuẩn hóa các loại xuống dòng về \n
        // Xử lý cả \r\n (Windows), \r (Mac cũ), \n (Unix/Linux)
        text = text.replace("\r\n", "\n").replace("\r", "\n");

        // Tách chuỗi theo \n và thêm line break
        String[] lines = text.split("\n", -1); // -1 để giữ lại cả dòng trống
        for (int i = 0; i < lines.length; i++) {
            XWPFRun run = paragraph.createRun();
            run.setText(lines[i]);

            applyRunStyle(normalStyle, run);

            // Thêm line break nếu không phải dòng cuối
            if (i < lines.length - 1) {
                run.addBreak(); // Xuống dòng trong Word
            }
        }
    }

    private void replaceImagePlaceholders(XWPFDocument doc, Map<String, String> imageMap) {

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            String text = paragraph.getText();
            if (text == null)
                continue;

            for (var entry : imageMap.entrySet()) {
                String key = entry.getKey();
                String imagePath = entry.getValue();
                String placeholder = "{{" + key + "}}";

                if (text.contains(placeholder)) {

                    // Xóa toàn bộ run
                    for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                        paragraph.removeRun(i);
                    }

                    // Chèn ảnh
                    addImageToParagraph(paragraph, imagePath);

                    break;
                }
            }
        }
    }

    private void addImageToParagraph(XWPFParagraph paragraph, String imagePath) {
        try (InputStream is = new FileInputStream(imagePath)) {
            XWPFRun run = paragraph.createRun();
            run.addPicture(
                    is,
                    XWPFDocument.PICTURE_TYPE_PNG,
                    imagePath,
                    Units.toEMU(450),
                    Units.toEMU(260));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String defaultString(String s) {
        return (s == null || s.isBlank()) ? "" : s;
    }

    private String fmt(Object v) {
        if (v == null)
            return "";
        if (v instanceof Double d)
            return String.format("%.2f", d);
        if (v instanceof Integer i)
            return String.valueOf(i);
        return v.toString();
    }

    private String toStringOrEmpty(Number value) {
        return value != null ? value.toString() : "";
    }

    private String defaultParagraphValue(String value) {
        return (value == null || value.trim().isEmpty()) ? "....." : value;
    }

    private String formatDate(LocalDate date) {
        if (date == null)
            return "";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String convertUnitToVietnamese(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            return "";
        }

        Map<String, String> unitMap = Map.of(
                "tons", "tấn",
                "kg", "kg",
                "liters", "lít",
                "m³", "m³",
                "kWh", "kWh",
                "units", "đơn vị",
                "other", "khác");

        return unitMap.getOrDefault(unit.trim(), unit);
    }

    private String convertYesNoToVietnamese(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            return "";
        }
        Map<String, String> unitMap = Map.of(
                "yes", "Có",
                "No", "Không");
        return unitMap.getOrDefault(unit.trim(), unit);
    }

    /**
     * Copy ALL run formatting from templateRun → newRun
     */
    private void copyRunStyle(XWPFRun source, XWPFRun target) {
        if (source == null || target == null)
            return;

        // Font family
        if (source.getFontFamily() != null)
            target.setFontFamily(source.getFontFamily());

        // Font size
        if (source.getFontSize() > 0)
            target.setFontSize(source.getFontSize());

        // Basic text styling
        target.setBold(source.isBold());
        target.setItalic(source.isItalic());
        target.setUnderline(source.getUnderline());

        // Color
        if (source.getColor() != null)
            target.setColor(source.getColor());

        // Additional styling
        target.setCapitalized(source.isCapitalized());
        target.setShadow(source.isShadowed());
        target.setSmallCaps(source.isSmallCaps());
        // target.setSubscript(source.getSubscript());
    }

    private static class RunStyle {
        String fontFamily;
        int fontSize;
        boolean bold;
        boolean italic;
        int underline;
        String color;
        boolean caps;
        boolean shadow;
        boolean smallCaps;
    }

    private RunStyle extractRunStyle(XWPFRun run) {
        if (run == null)
            return null;

        RunStyle rs = new RunStyle();
        rs.fontFamily = run.getFontFamily();
        rs.fontSize = run.getFontSize();
        rs.bold = run.isBold();
        rs.italic = run.isItalic();
        // rs.underline = run.getUnderline();
        rs.color = run.getColor();
        rs.caps = run.isCapitalized();
        rs.shadow = run.isShadowed();
        rs.smallCaps = run.isSmallCaps();

        return rs;
    }

    private void applyRunStyle(RunStyle style, XWPFRun target) {
        if (style == null || target == null)
            return;

        if (style.fontFamily != null)
            target.setFontFamily(style.fontFamily);

        if (style.fontSize > 0)
            target.setFontSize(style.fontSize);

        target.setBold(style.bold);
        target.setItalic(style.italic);
        // target.setUnderline(style.underline);

        if (style.color != null)
            target.setColor(style.color);

        target.setCapitalized(style.caps);
        target.setShadow(style.shadow);
        target.setSmallCaps(style.smallCaps);
    }

}
