package com.EIPplatform.util;

import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;
import com.EIPplatform.service.report.reporta05.TableMappingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class ReportA05DocUtil {

    private static final String TEMPLATE_PATH =
            "templates/reportA05/ReportA05_template_ver2.docx";

    public byte[] generateReportDocument(ReportA05 report) {

        BusinessDetail business = report.getBusinessDetail();
        if (business == null) {
            throw new IllegalArgumentException("ReportA05 has no BusinessDetail linked");
        }

        // Các section entity (có thể null)
        WasteWaterData wasteWaterData = report.getWasteWaterData();
        AirEmissionData airEmissionData = report.getAirEmissionData();
        WasteManagementData wasteManagementData = report.getWasteManagementData();

        EnvPermits envPermits = business.getEnvPermits();
        List<BusinessHistoryConsumption> historyList = business.getBusinessHistoryConsumptions();

        // Date placeholders (header)
        LocalDate today = LocalDate.now();
        String day = String.valueOf(today.getDayOfMonth());
        String month = String.valueOf(today.getMonthValue());
        String year = String.valueOf(today.getYear());

        Map<String, String> data = new HashMap<>();

        // 1. General info (facility, permits, history, date)
        buildGeneralInfoData(data, business, envPermits, historyList, day, month, year, report);

        // 2. Section placeholders
        buildWasteWaterPlaceholders(data, wasteWaterData);
        buildAirEmissionPlaceholders(data, airEmissionData);
        buildWasteManagementPlaceholders(data, wasteManagementData);

        Resource resource = new ClassPathResource(TEMPLATE_PATH);
        log.info("Loading ReportA05 template from: {}", TEMPLATE_PATH);

        try (InputStream fis = resource.getInputStream();
             XWPFDocument doc = new XWPFDocument(fis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 5. Replace placeholders in top-level paragraphs (normal lines)
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                replacePlaceholders(paragraph, data, false); // normal line → "....." for empty
            }

            // 6. Replace placeholders in table cells (keep empty string for missing)
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replacePlaceholders(p, data, true); // in table → no "....."
                        }
                    }
                }
            }

            // 7. Fill dynamic tables from ENTITY lists
            fillTables(doc, wasteWaterData, airEmissionData, wasteManagementData);

            // 8. Write to bytes
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
        data.put("legal_representative", defaultString(business.getLegalRepresentative()));
        data.put("activity_type", defaultString(business.getActivityType()));
        data.put("scale_capacity", defaultString(business.getScaleCapacity()));

        data.put("iso_14001_certificate", defaultString(business.getISO_certificate_14001()));
        data.put("business_license_number", defaultString(business.getBusinessRegistrationNumber()));
        data.put("tax_code", defaultString(business.getTaxCode()));

        data.put("seasonal_period",
                business.getOperationType() != null ? business.getOperationType().name() : "");

        data.put("operating_frequency", "");

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

        // Report info
        data.put("report_year", report.getReportYear() != null ? report.getReportYear().toString() : "");
        data.put("reporting_period", defaultString(report.getReportingPeriod()));
        data.put("report_code", defaultString(report.getReportCode()));

        // Date line
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
        BusinessHistoryConsumption previous = sorted.size() > 1
                ? sorted.get(sorted.size() - 2)
                : null;

        data.put("product_volume_cy", toStringOrEmpty(current.getProductVolumeCy()));
        data.put("product_unit_cy", defaultString(current.getProductUnitCy()));
        data.put("fuel_consumption_cy", toStringOrEmpty(current.getFuelConsumptionCy()));
        data.put("fuel_unit_cy", defaultString(current.getFuelUnitCy()));
        data.put("electricity_consumption_cy", toStringOrEmpty(current.getElectricityConsumptionCy()));
        data.put("water_consumption_cy", toStringOrEmpty(current.getWaterConsumptionCy()));

        if (previous != null) {
            data.put("product_volume_py", toStringOrEmpty(previous.getProductVolumePy()));
            data.put("product_unit_py", defaultString(previous.getProductUnitPy()));
            data.put("fuel_consumption_py", toStringOrEmpty(previous.getFuelConsumptionPy()));
            data.put("fuel_unit_py", defaultString(previous.getFuelUnitPy()));
            data.put("electricity_consumption_py", toStringOrEmpty(previous.getElectricityConsumptionPy()));
            data.put("water_consumption_py", toStringOrEmpty(previous.getWaterConsumptionPy()));
        } else {
            data.put("product_volume_py", "");
            data.put("product_unit_py", "");
            data.put("fuel_consumption_py", "");
            data.put("fuel_unit_py", "");
            data.put("electricity_consumption_py", "");
            data.put("water_consumption_py", "");
        }
    }

    // ---------------------------------------------------------------------
    // 2. Waste water placeholders  (section 1.x)
    // ---------------------------------------------------------------------

    private void buildWasteWaterPlaceholders(Map<String, String> data, WasteWaterData ww) {
        if (ww == null) return;

        data.put("ww_treatment_desc", defaultString(ww.getTreatmentWwDesc()));
        data.put("connection_status_desc", defaultString(ww.getConnectionStatusDesc()));
        data.put("connection_diagram", defaultString(ww.getConnectionDiagram()));

        data.put("domestic_ww_cy", fmt(ww.getDomWwCy()));
        data.put("domestic_ww_py", fmt(ww.getDomWwPy()));

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

        // Auto monitoring (template key có 1 cái dùng GPS hoa)
        data.put("auto_station_location", defaultString(ww.getAutoStationLocation()));
        data.put("auto_station_GPS", defaultString(ww.getAutoStationGps()));     // khớp {{auto_station_GPS}}
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
    // 3. Air emission placeholders  (section 2.x)
    // ---------------------------------------------------------------------

    private void buildAirEmissionPlaceholders(Map<String, String> data, AirEmissionData air) {
        if (air == null) return;

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
        data.put("air_auto_station_GPS", defaultString(air.getAirAutoStationGps())); // khớp {{air_auto_station_GPS}}
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
    // 4. Waste management placeholders  (sections 3.x, 4.x, 7.x)
    // ---------------------------------------------------------------------

    private void buildWasteManagementPlaceholders(Map<String, String> data, WasteManagementData wm) {
        if (wm == null) return;

        data.put("sw_general_note", defaultString(wm.getSwGeneralNote()));
        data.put("incident_plan_development", defaultString(wm.getIncidentPlanDevelopment()));
        data.put("incident_prevention_measures", defaultString(wm.getIncidentPreventionMeasures()));
        data.put("incident_response_report", defaultString(wm.getIncidentResponseReport()));

        data.put("water_total_volume", fmt(wm.getWaterTotalVolumeKg()));
        data.put("water_estimation_method", defaultString(wm.getWaterEstimationMethod()));

        data.put("air_total_volume", fmt(wm.getAirTotalVolumeKg()));
        data.put("air_estimation_method", defaultString(wm.getAirEstimationMethod()));

        data.put("soil_total_volume", fmt(wm.getSoilTotalVolumeKg()));
        data.put("soil_estimation_method", defaultString(wm.getSoilEstimationMethod()));

        data.put("sewage_sludge_total_volume", fmt(wm.getSewageSludgeTotalVolumeKg()));
        data.put("sewage_sludge_estimation_method", defaultString(wm.getSewageSludgeEstimationMethod()));

        data.put("hw_onsite_total_volume", fmt(wm.getHwOnsiteTotalVolumeKg()));
        data.put("hw_onsite_estimation_method", defaultString(wm.getHwOnsiteEstimationMethod()));

        data.put("hw_recycle_total_volume", fmt(wm.getHwRecycleTotalVolumeKg()));
        data.put("hw_recycle_estimation_method", defaultString(wm.getHwRecycleEstimationMethod()));

        data.put("hw_disposal_total_volume", fmt(wm.getHwDisposalTotalVolumeKg()));
        data.put("hw_disposal_estimation_method", defaultString(wm.getHwDisposalEstimationMethod()));

    }

    // ---------------------------------------------------------------------
    // 5. Table filling (dynamic lists)
    // ---------------------------------------------------------------------

    private void fillTables(
            XWPFDocument doc,
            WasteWaterData ww,
            AirEmissionData air,
            WasteManagementData wm
    ) {

        // -------------------- 1.x WASTEWATER --------------------

        // 1.1 Exceedance (duplicate table 1.1 and 1.2 use same data structure)
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_ROW}}",
                ww == null ? null : ww.getMonitoringExceedances(),
                (item, row) -> row.cols(
                        item.getPointName(),
                        item.getPointSymbol(),
                        item.getMonitoringDate(),
                        item.getLongitude(),
                        item.getLatitude(),
                        item.getExceededParam(),
                        fmt(item.getResultValue()),
                        fmt(item.getQcvnLimit())
                ),
                true
        );

        // 1.2 Duplicate exceedance table
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_ROW_2}}",
                ww == null ? null : ww.getMonitoringExceedances(),
                (item, row) -> row.cols(
                        item.getPointName(),
                        item.getPointSymbol(),
                        item.getMonitoringDate(),
                        item.getLongitude(),
                        item.getLatitude(),
                        item.getExceededParam(),
                        fmt(item.getResultValue()),
                        fmt(item.getQcvnLimit())
                ),
                true
        );

        // 1.3 Auto Stats (VERTICAL)
        TableMappingService.mapVerticalTable(
                doc,
                "{{TEMPLATE_AUTO_STATS}}",
                ww == null ? null : List.of(
                        // Order must match template rows exactly
                        ww.getMonitoringStats().isEmpty() ? "" : ww.getMonitoringStats().get(0).getParamName(),
                        ww.getMonitoringStats().isEmpty() ? "" : fmt(ww.getMonitoringStats().get(0).getValDesign()),
                        ww.getMonitoringStats().isEmpty() ? "" : fmt(ww.getMonitoringStats().get(0).getValReceived()),
                        ww.getMonitoringStats().isEmpty() ? "" : fmt(ww.getMonitoringStats().get(0).getValError()),
                        ww.getMonitoringStats().isEmpty() ? "" : fmt(ww.getMonitoringStats().get(0).getRatioReceivedDesign()),
                        ww.getMonitoringStats().isEmpty() ? "" : fmt(ww.getMonitoringStats().get(0).getRatioErrorReceived())
                )
        );

        // 1.4 Auto Incidents
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AUTO_INCIDENTS}}",
                ww == null ? null : ww.getMonitoringIncidents(),
                (item, row) -> row.cols(
                        item.getIncidentName(),
                        item.getIncidentTime(),
                        item.getIncidentRemedy()
                ),
                true
        );

        // 1.5 QCVN Exceed
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_QCVN_EXCEED}}",
                ww == null ? null : ww.getQcvnExceedances(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getExceedDaysCount()),
                        fmt(item.getQcvnLimitValue()),
                        fmt(item.getExceedRatioPercent())
                ),
                false
        );


        // -------------------- 2.x AIR EMISSION --------------------

        // 2.1 Exceedances
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
                        fmt(item.getQcvnLimit())
                ),
                true
        );

        // 2.2 Auto Stats (VERTICAL)
        TableMappingService.mapVerticalTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_STATS}}",
                air == null ? null : List.of(
                        air.getAirAutoMonitoringStats().isEmpty() ? "" : air.getAirAutoMonitoringStats().get(0).getParamName(),
                        air.getAirAutoMonitoringStats().isEmpty() ? "" : fmt(air.getAirAutoMonitoringStats().get(0).getValDesign()),
                        air.getAirAutoMonitoringStats().isEmpty() ? "" : fmt(air.getAirAutoMonitoringStats().get(0).getValReceived()),
                        air.getAirAutoMonitoringStats().isEmpty() ? "" : fmt(air.getAirAutoMonitoringStats().get(0).getValError()),
                        air.getAirAutoMonitoringStats().isEmpty() ? "" : fmt(air.getAirAutoMonitoringStats().get(0).getRatioReceivedDesign()),
                        air.getAirAutoMonitoringStats().isEmpty() ? "" : fmt(air.getAirAutoMonitoringStats().get(0).getRatioErrorReceived())
                )
        );

        // 2.3 Auto Incidents
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_INCIDENTS}}",
                air == null ? null : air.getAirAutoMonitoringIncidents(),
                (item, row) -> row.cols(
                        item.getIncidentName(),
                        item.getIncidentTime(),
                        item.getIncidentRemedy()
                ),
                false
        );

        // 2.4 QCVN Exceed
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_QCVN_EXCEED}}",
                air == null ? null : air.getAirAutoQcvnExceedances(),
                (item, row) -> row.cols(
                        item.getParamName(),
                        fmt(item.getExceedDaysCount()),
                        fmt(item.getQcvnLimitValue()),
                        fmt(item.getExceedRatioPercent())
                ),
                false
        );


        // -------------------- 3–4–7. WASTE MANAGEMENT --------------------
        if (wm == null) return;

        // Domestic solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_DOMESTIC_SOLID_WASTE_STATS}}",
                wm.getDomesticSolidWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteTypeName(),
                        fmt(item.getVolumeCy()),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())
                ),
                true
        );

        // Industrial solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_INDUSTRIAL_SOLID_WASTE_STATS}}",
                wm.getIndustrialSolidWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteGroup(),
                        fmt(item.getVolumeCy()),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())
                ),
                true
        );

        // Recycle waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_RECYCLE_INDUSTRIAL_WASTE_STATS}}",
                wm.getRecycleIndustrialWasteStats(),
                (item, row) -> row.cols(
                        item.getTransferOrg(),
                        fmt(item.getVolumeCy()),
                        item.getWasteTypeDesc(),
                        fmt(item.getVolumePy())
                ),
                true
        );

        // Other solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_OTHER_SOLID_WASTE_STATS}}",
                wm.getOtherSolidWasteStats(),
                (item, row) -> row.cols(
                        item.getWasteGroupOther(),
                        fmt(item.getVolumeCy()),
                        item.getSelfTreatmentMethod(),
                        item.getReceiverOrg(),
                        fmt(item.getVolumePy())
                ),
                true
        );

        // Hazardous waste
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
                        fmt(item.getVolumePy())
                ),
                false
        );

        // Exported waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_EXPORTED_HW_STATS}}",
                wm.getExportedHwStats(),
                (item, row) -> row.cols(
                        item.getWasteName(),
                        item.getHwCode(),
                        item.getBaselCode(),
                        fmt(item.getVolumeKg()),
                        item.getTransporterOrg(),
                        item.getOverseasProcessorOrg()
                ),
                false
        );

        // Self-treated HW
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_SELF_TREATED_HW_STATS}}",
                wm.getSelfTreatedHwStats(),
                (item, row) -> row.cols(
                        item.getWasteName(),
                        item.getHwCode(),
                        fmt(item.getVolumeKg()),
                        item.getSelfTreatmentMethod()
                ),
                false
        );

        // POP stats
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
                        item.getComplianceResult()
                ),
                true
        );
    }

    private void replacePlaceholders(XWPFParagraph paragraph,
                                     Map<String, String> data,
                                     boolean inTable) {
        if (paragraph == null) return;

        String text = paragraph.getText();
        if (text == null || text.isEmpty()) return;

        String original = text;

        // 1. Replace known placeholders from data map
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String placeholder = "{{" + key + "}}";
            if (text.contains(placeholder)) {
                String rawValue = entry.getValue();
                String filledValue = inTable
                        ? defaultString(rawValue)          // tables: "" nếu null/empty
                        : defaultParagraphValue(rawValue); // paragraphs: "....." nếu null/empty

                text = text.replace(placeholder, filledValue);
            }
        }

        // 2. Remove stray / unknown placeholders nhưng giữ lại TEMPLATE_ cho TableMappingService
        text = text.replaceAll("\\{\\{(?!TEMPLATE_)[^}]+\\}\\}", "");

        // 3. If nothing changed, do nothing
        if (text.equals(original)) {
            return;
        }

        // 4. Clear all runs and recreate a single run with new text
        int runCount = paragraph.getRuns().size();
        for (int i = runCount - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private String defaultString(String s) {
        return (s == null || s.isBlank()) ? "" : s;
    }

    private String fmt(Object v) {
        if (v == null) return "";
        if (v instanceof Double d) return String.format("%.2f", d);
        if (v instanceof Integer i) return String.valueOf(i);
        return v.toString();
    }

    private String toStringOrEmpty(Number value) {
        return value != null ? value.toString() : "";
    }

    private String defaultParagraphValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ".....";
        }
        return value;
    }

    /**
     * Format LocalDate as dd/MM/yyyy; blank if null.
     */
    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
