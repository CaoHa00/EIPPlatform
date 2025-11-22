package com.EIPplatform.util;

import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.report05.ReportA05;
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

    /**
     * Main entry point:
     * Build placeholder map + fill dynamic tables + return DOCX bytes.
     */
    public byte[] generateReportDocument(BusinessDetail business,
                                         ReportA05DraftDTO draftData) {

        // 1. Extract draft sections (may be null)
        WasteWaterDataDTO wasteWaterDataDTO = draftData != null ? draftData.getWasteWaterData() : null;
        AirEmissionDataDTO airEmissionDataDTO = draftData != null ? draftData.getAirEmissionData() : null;
        WasteManagementDataDTO wasteManagementDataDTO = draftData != null ? draftData.getWasteManagementData() : null;

        EnvPermits envPermits = business.getEnvPermits();
        List<BusinessHistoryConsumption> historyList = business.getBusinessHistoryConsumptions();

        // 2. Date placeholders (header)
        LocalDate today = LocalDate.now();
        String day = String.valueOf(today.getDayOfMonth());
        String month = String.valueOf(today.getMonthValue());
        String year = String.valueOf(today.getYear());

        // 3. Build placeholder map
        Map<String, String> data = new HashMap<>();

        buildGeneralInfoData(data, business, envPermits, historyList, day, month, year);
        buildWasteWaterPlaceholders(data, wasteWaterDataDTO);
        buildAirEmissionPlaceholders(data, airEmissionDataDTO);
        buildWasteManagementPlaceholders(data, wasteManagementDataDTO);

        // 4. Load DOCX template
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

            // 7. Fill / remove dynamic tables
            fillTables(doc, wasteWaterDataDTO, airEmissionDataDTO, wasteManagementDataDTO);

            // 8. Write to bytes
            doc.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error while generating ReportA05 DOCX: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate ReportA05 document", e);
        }
    }

    // ---------------------------------------------------------------------
    // Placeholder builders
    // ---------------------------------------------------------------------

    private void buildGeneralInfoData(Map<String, String> data,
                                      BusinessDetail business,
                                      EnvPermits envPermits,
                                      List<BusinessHistoryConsumption> historyList,
                                      String day,
                                      String month,
                                      String year) {

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

        if (envPermits != null) {
            data.put("env_permit_number", defaultString(envPermits.getPermitNumber()));
            data.put("env_permit_issue_date", formatDate(envPermits.getIssueDate()));
            data.put("env_permit_issuer", defaultString(envPermits.getIssuerOrg()));
            data.put("env_permit_file", defaultString(envPermits.getPermitFilePath()));
        } else {
            data.put("env_permit_number", "");
            data.put("env_permit_issue_date", "");
            data.put("env_permit_issuer", "");
            data.put("env_permit_file", "");
        }

        fillBusinessHistoryData(data, historyList);

        data.put("dateStr", day);
        data.put("monthYearStr", month);
        data.put("yearStr", year);
    }

    private void buildWasteWaterPlaceholders(Map<String, String> data,
                                             WasteWaterDataDTO wasteWaterDataDTO) {
        if (wasteWaterDataDTO == null) {
            return;
        }

        // Công trình xử lý nước thải
        data.put("ww_treatment_desc",
                defaultString(wasteWaterDataDTO.getTreatmentWwDesc()));

        // Nước thải sinh hoạt
        data.put("domestic_ww_cy", toStringOrEmpty(wasteWaterDataDTO.getDomWwCy()));
        data.put("domestic_ww_py", toStringOrEmpty(wasteWaterDataDTO.getDomWwPy()));
        data.put("domestic_ww_design", toStringOrEmpty(wasteWaterDataDTO.getDomWwDesign()));

        // Nước thải công nghiệp
        data.put("industrial_ww_cy", toStringOrEmpty(wasteWaterDataDTO.getIndustrialWwCy()));
        data.put("industrial_ww_py", toStringOrEmpty(wasteWaterDataDTO.getIndustrialWwPy()));
        data.put("industrial_ww_design", toStringOrEmpty(wasteWaterDataDTO.getIndustrialWwDesign()));

        // Nước làm mát
        data.put("cooling_water_cy", toStringOrEmpty(wasteWaterDataDTO.getCoolingWaterCy()));
        data.put("cooling_water_py", toStringOrEmpty(wasteWaterDataDTO.getCoolingWaterPy()));
        data.put("cooling_water_design", toStringOrEmpty(wasteWaterDataDTO.getCoolingWaterDesign()));

        // Đấu nối XLNT tập trung
        data.put("connection_status_desc", defaultString(wasteWaterDataDTO.getConnectionStatusDesc()));

        // Quan trắc nước thải sinh hoạt
        data.put("dom_monitor_period", defaultString(wasteWaterDataDTO.getDomMonitorPeriod()));
        data.put("dom_monitor_freq", defaultString(wasteWaterDataDTO.getDomMonitorFreq()));
        data.put("dom_monitor_locations", defaultString(wasteWaterDataDTO.getDomMonitorLocations()));
        data.put("dom_sample_count", toStringOrEmpty(wasteWaterDataDTO.getDomSampleCount()));
        data.put("dom_qcvn_standard", defaultString(wasteWaterDataDTO.getDomQcvnStandard()));
        data.put("dom_agency_name", defaultString(wasteWaterDataDTO.getDomAgencyName()));
        data.put("dom_agency_vimcerts", defaultString(wasteWaterDataDTO.getDomAgencyVimcerts()));

        // Quan trắc nước thải công nghiệp
        data.put("ind_monitor_period", defaultString(wasteWaterDataDTO.getIndMonitorPeriod()));
        data.put("ind_monitor_freq", defaultString(wasteWaterDataDTO.getIndMonitorFreq()));
        data.put("ind_monitor_locations", defaultString(wasteWaterDataDTO.getIndMonitorLocations()));
        data.put("ind_sample_count", toStringOrEmpty(wasteWaterDataDTO.getIndSampleCount()));
        data.put("ind_qcvn_standard", defaultString(wasteWaterDataDTO.getIndQcvnStandard()));
        data.put("ind_agency_name", defaultString(wasteWaterDataDTO.getIndAgencyName()));
        data.put("ind_agency_vimcerts", defaultString(wasteWaterDataDTO.getIndAgencyVimcerts()));

        // Quan trắc nước thải tự động
        data.put("auto_station_location", defaultString(wasteWaterDataDTO.getAutoStationLocation()));
        data.put("auto_station_GPS", defaultString(wasteWaterDataDTO.getAutoStationGps()));
        data.put("auto_station_map", defaultString(wasteWaterDataDTO.getAutoStationMap()));
        data.put("auto_source_desc", defaultString(wasteWaterDataDTO.getAutoSourceDesc()));
        data.put("auto_data_frequency", defaultString(wasteWaterDataDTO.getAutoDataFrequency()));
        data.put("auto_calibration_info", defaultString(wasteWaterDataDTO.getAutoCalibrationInfo()));

        // Tình trạng hoạt động trạm
        data.put("auto_incident_summary", defaultString(wasteWaterDataDTO.getAutoIncidentSummary()));
        data.put("auto_downtime_desc", defaultString(wasteWaterDataDTO.getAutoDowntimeDesc()));

        // Nhận xét & kết luận
        data.put("auto_exceed_days_summary", defaultString(wasteWaterDataDTO.getAutoExceedDaysSummary()));
        data.put("auto_abnormal_reason", defaultString(wasteWaterDataDTO.getAutoAbnormalReason()));
        data.put("auto_completeness_review", defaultString(wasteWaterDataDTO.getAutoCompletenessReview()));
        data.put("auto_exceed_summary", defaultString(wasteWaterDataDTO.getAutoExceedSummary()));
    }

    private void buildAirEmissionPlaceholders(Map<String, String> data,
                                              AirEmissionDataDTO airEmissionDataDTO) {
        if (airEmissionDataDTO == null) {
            return;
        }

        data.put("air_treatment_desc", defaultString(airEmissionDataDTO.getAirTreatmentDesc()));
        data.put("air_emission_cy", toStringOrEmpty(airEmissionDataDTO.getAirEmissionCy()));
        data.put("air_emission_py", toStringOrEmpty(airEmissionDataDTO.getAirEmissionPy()));

        data.put("air_monitor_period", defaultString(airEmissionDataDTO.getAirMonitorPeriod()));
        data.put("air_monitor_freq", defaultString(airEmissionDataDTO.getAirMonitorFreq()));
        data.put("air_monitor_locations", defaultString(airEmissionDataDTO.getAirMonitorLocations()));
        data.put("air_sample_count", toStringOrEmpty(airEmissionDataDTO.getAirSampleCount()));
        data.put("air_qcvn_standard", defaultString(airEmissionDataDTO.getAirQcvnStandard()));
        data.put("air_agency_name", defaultString(airEmissionDataDTO.getAirAgencyName()));
        data.put("air_agency_vimcerts", defaultString(airEmissionDataDTO.getAirAgencyVimcerts()));

        data.put("air_auto_station_location", defaultString(airEmissionDataDTO.getAirAutoStationLocation()));
        data.put("air_auto_station_GPS", defaultString(airEmissionDataDTO.getAirAutoStationGps()));
        data.put("air_auto_station_map", defaultString(airEmissionDataDTO.getAirAutoStationMapFilePath()));
        data.put("air_auto_source_desc", defaultString(airEmissionDataDTO.getAirAutoSourceDesc()));
        data.put("air_auto_data_frequency", defaultString(airEmissionDataDTO.getAirAutoDataFrequency()));
        data.put("air_auto_param_list", defaultString(airEmissionDataDTO.getAirAutoParamList()));
        data.put("air_auto_calibration_info", defaultString(airEmissionDataDTO.getAirAutoCalibrationInfo()));
        data.put("air_auto_incident_summary", defaultString(airEmissionDataDTO.getAirAutoIncidentSummary()));
        data.put("air_auto_downtime_desc", defaultString(airEmissionDataDTO.getAirAutoDowntimeDesc()));

        data.put("air_auto_avg_calc_desc", defaultString(airEmissionDataDTO.getAirAutoAvgCalcDesc()));
        data.put("air_auto_avg_compare_desc", defaultString(airEmissionDataDTO.getAirAutoAvgCompareDesc()));
        data.put("air_auto_exceed_days_summary", defaultString(airEmissionDataDTO.getAirAutoExceedDaysSummary()));
        data.put("air_auto_abnormal_reason", defaultString(airEmissionDataDTO.getAirAutoAbnormalReason()));
        data.put("air_auto_completeness_review", defaultString(airEmissionDataDTO.getAirAutoCompletenessReview()));
        data.put("air_auto_exceed_conclusion", defaultString(airEmissionDataDTO.getAirAutoExceedConclusion()));
    }

    private void buildWasteManagementPlaceholders(Map<String, String> data,
                                                  WasteManagementDataDTO wasteManagementDataDTO) {
        if (wasteManagementDataDTO == null) {
            return;
        }

        data.put("sw_general_note", defaultString(wasteManagementDataDTO.getSwGeneralNote()));
        data.put("incident_plan_development",
                defaultString(wasteManagementDataDTO.getIncidentPlanDevelopment()));
        data.put("incident_prevention_measures",
                defaultString(wasteManagementDataDTO.getIncidentPreventionMeasures()));
        data.put("incident_response_report",
                defaultString(wasteManagementDataDTO.getIncidentResponseReport()));
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
    // Dynamic tables (water + air + basic waste removal)
    // ---------------------------------------------------------------------
    private void fillTables(
            XWPFDocument doc,
            WasteWaterDataDTO ww,
            AirEmissionDataDTO air,
            WasteManagementDataDTO wm
    ) {

        /* =========================================================
         * 1. WASTEWATER (1.x)
         * ========================================================= */

        // 1.1 / 1.2 – Wastewater monitoring exceedances (shared template row)
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_ROW}}",
                (ww == null) ? null : ww.getMonitoringExceedances(),
                (item, row) -> {
                    // WasteWaterMonitoringExceedancesDTO
                    TableMappingService.setCell(row, 0, item.getPointName());
                    TableMappingService.setCell(row, 1, item.getPointSymbol());
                    TableMappingService.setCell(row, 2, item.getMonitoringDate());
                    TableMappingService.setCell(row, 3, item.getLongitude());
                    TableMappingService.setCell(row, 4, item.getLatitude());
                    TableMappingService.setCell(row, 5, item.getExceededParam());
                    TableMappingService.setCell(row, 6, TableMappingService.fmt(item.getResultValue()));
                    TableMappingService.setCell(row, 7, TableMappingService.fmt(item.getQcvnLimit()));
                }
        );

        // 1.3 – Auto wastewater monitoring stats
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AUTO_STATS}}",
                (ww == null) ? null : ww.getMonitoringStats(),
                (item, row) -> {
                    // AutoWWMonitoringStatsDTO
                    TableMappingService.setCell(row, 0, item.getParamName());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getValDesign()));
                    TableMappingService.setCell(row, 2, TableMappingService.fmt(item.getValReceived()));
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getValError()));
                    TableMappingService.setCell(row, 4, TableMappingService.fmt(item.getRatioReceivedDesign()));
                    TableMappingService.setCell(row, 5, TableMappingService.fmt(item.getRatioErrorReceived()));
                }
        );

        // 1.4 – Auto wastewater monitoring incidents
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AUTO_INCIDENTS}}",
                (ww == null) ? null : ww.getMonitoringIncidents(),
                (item, row) -> {
                    // AutoWWMonitoringIncidentsDTO
                    TableMappingService.setCell(row, 0, item.getIncidentName());
                    TableMappingService.setCell(row, 1, item.getIncidentTime());
                    TableMappingService.setCell(row, 2, item.getIncidentRemedy());
                }
        );

        // 1.5 – Wastewater QCVN exceedances (auto station)
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_QCVN_EXCEED}}",
                (ww == null) ? null : ww.getQcvnExceedances(),
                (item, row) -> {
                    // AutoWWQcvnExceedancesDTO
                    TableMappingService.setCell(row, 0, item.getParamName());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getExceedDaysCount()));
                    TableMappingService.setCell(row, 2, TableMappingService.fmt(item.getQcvnLimitValue()));
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getExceedRatioPercent()));
                }
        );


        /* =========================================================
         * 2. AIR EMISSIONS (2.x)
         * ========================================================= */

        // 2.1 – Manual air monitoring exceedances
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_ROW}}",
                (air == null) ? null : air.getAirMonitoringExceedances(),
                (item, row) -> {
                    // AirMonitoringExceedanceDTO
                    TableMappingService.setCell(row, 0, item.getPointName());
                    TableMappingService.setCell(row, 1, item.getPointSymbol());
                    TableMappingService.setCell(row, 2, item.getMonitoringDate());
                    TableMappingService.setCell(row, 3, item.getLongitude());
                    TableMappingService.setCell(row, 4, item.getLatitude());
                    TableMappingService.setCell(row, 5, item.getExceededParam());
                    TableMappingService.setCell(row, 6, TableMappingService.fmt(item.getResultValue()));
                    TableMappingService.setCell(row, 7, TableMappingService.fmt(item.getQcvnLimit()));
                }
        );

        // 2.2 – Air auto monitoring stats
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_STATS}}",
                (air == null) ? null : air.getAirAutoMonitoringStats(),
                (item, row) -> {
                    // AirAutoMonitoringStatDTO
                    TableMappingService.setCell(row, 0, item.getParamName());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getValDesign()));
                    TableMappingService.setCell(row, 2, TableMappingService.fmt(item.getValReceived()));
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getValError()));
                    TableMappingService.setCell(row, 4, TableMappingService.fmt(item.getRatioReceivedDesign()));
                    TableMappingService.setCell(row, 5, TableMappingService.fmt(item.getRatioErrorReceived()));
                }
        );

        // 2.3 – Air auto monitoring incidents
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_AUTO_INCIDENTS}}",
                (air == null) ? null : air.getAirAutoMonitoringIncidents(),
                (item, row) -> {
                    // AirAutoMonitoringIncidentDTO
                    TableMappingService.setCell(row, 0, item.getIncidentName());
                    TableMappingService.setCell(row, 1, item.getIncidentTime());
                    TableMappingService.setCell(row, 2, item.getIncidentRemedy());
                }
        );

        // 2.4 – Air QCVN exceedances (auto station)
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_AIR_QCVN_EXCEED}}",
                (air == null) ? null : air.getAirAutoQcvnExceedances(),
                (item, row) -> {
                    // AirAutoQcvnExceedanceDTO
                    TableMappingService.setCell(row, 0, item.getParamName());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getExceedDaysCount()));
                    TableMappingService.setCell(row, 2, TableMappingService.fmt(item.getQcvnLimitValue()));
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getExceedRatioPercent()));
                }
        );


        /* =========================================================
         * 3–4–7. SOLID & HAZARDOUS WASTE (3.x, 4.x, 7.x)
         * ========================================================= */

        if (wm == null) {
            // Rely on mapTable(null) + removeTableByMarker if you want,
            // or just leave as-is (tables will keep "....." / placeholders)
            return;
        }

        // 3.1 – Domestic solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_DOMESTIC_SOLID_WASTE_STATS}}",
                wm.getDomesticSolidWasteStats(),
                (item, row) -> {
                    // DomesticSolidWasteStatDTO
                    TableMappingService.setCell(row, 0, item.getWasteTypeName());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getVolumeCy()));
                    TableMappingService.setCell(row, 2, item.getReceiverOrg());
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getVolumePy()));
                }
        );

        // 3.2 – Industrial solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_INDUSTRIAL_SOLID_WASTE_STATS}}",
                wm.getIndustrialSolidWasteStats(),
                (item, row) -> {
                    // IndustrialSolidWasteStatDTO
                    TableMappingService.setCell(row, 0, item.getWasteGroup());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getVolumeCy()));
                    TableMappingService.setCell(row, 2, item.getReceiverOrg());
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getVolumePy()));
                }
        );

        // 3.3 – Recycled industrial solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_RECYCLE_INDUSTRIAL_WASTE_STATS}}",
                wm.getRecycleIndustrialWasteStats(),
                (item, row) -> {
                    // RecycleIndustrialWasteStatDTO
                    TableMappingService.setCell(row, 0, item.getTransferOrg());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getVolumeCy()));
                    TableMappingService.setCell(row, 2, item.getWasteTypeDesc());
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getVolumePy()));
                }
        );

        // 3.4 – Other solid waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_OTHER_SOLID_WASTE_STATS}}",
                wm.getOtherSolidWasteStats(),
                (item, row) -> {
                    // OtherSolidWasteStatDTO
                    TableMappingService.setCell(row, 0, item.getWasteGroupOther());
                    TableMappingService.setCell(row, 1, TableMappingService.fmt(item.getVolumeCy()));
                    TableMappingService.setCell(row, 2, item.getSelfTreatmentMethod());
                    TableMappingService.setCell(row, 3, item.getReceiverOrg());
                    TableMappingService.setCell(row, 4, TableMappingService.fmt(item.getVolumePy()));
                }
        );

        // 4.1 – Hazardous waste stats
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_HAZARDOUS_WASTE_STATS}}",
                wm.getHazardousWasteStats(),
                (item, row) -> {
                    // HazardousWasteStatDTO
                    TableMappingService.setCell(row, 0, item.getWasteName());
                    TableMappingService.setCell(row, 1, item.getHwCode());
                    TableMappingService.setCell(row, 2, TableMappingService.fmt(item.getVolumeCy()));
                    TableMappingService.setCell(row, 3, item.getTreatmentMethod());
                    TableMappingService.setCell(row, 4, item.getReceiverOrg());
                    TableMappingService.setCell(row, 5, TableMappingService.fmt(item.getVolumePy()));
                }
        );

        // 4.2 – Exported hazardous waste stats
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_EXPORTED_HW_STATS}}",
                wm.getExportedHwStats(),
                (item, row) -> {
                    // ExportedHwStatDTO
                    TableMappingService.setCell(row, 0, item.getWasteName());
                    TableMappingService.setCell(row, 1, item.getHwCode());
                    TableMappingService.setCell(row, 2, item.getBaselCode());
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getVolumeKg()));
                    TableMappingService.setCell(row, 4, item.getTransporterOrg());
                    TableMappingService.setCell(row, 5, item.getOverseasProcessorOrg());
                }
        );

        // 4.3 – Self-treated hazardous waste
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_SELF_TREATED_HW_STATS}}",
                wm.getSelfTreatedHwStats(),
                (item, row) -> {
                    // SelfTreatedHwStatDTO
                    TableMappingService.setCell(row, 0, item.getWasteName());
                    TableMappingService.setCell(row, 1, item.getHwCode());
                    TableMappingService.setCell(row, 2, TableMappingService.fmt(item.getVolumeKg()));
                    TableMappingService.setCell(row, 3, item.getSelfTreatmentMethod());
                }
        );

        // 7.x – POP inventory stats
        TableMappingService.mapTable(
                doc,
                "{{TEMPLATE_POP_INVENTORY_STATS}}",
                wm.getPopInventoryStats(),
                (item, row) -> {
                    // PopInventoryStatDTO
                    TableMappingService.setCell(row, 0, item.getPopName());
                    TableMappingService.setCell(row, 1, item.getCasCode());
                    TableMappingService.setCell(row, 2, item.getImportDate());
                    TableMappingService.setCell(row, 3, TableMappingService.fmt(item.getImportVolume()));
                    TableMappingService.setCell(row, 4, item.getConcentration());
                    TableMappingService.setCell(row, 5, TableMappingService.fmt(item.getVolumeUsed()));
                    TableMappingService.setCell(row, 6, TableMappingService.fmt(item.getVolumeStocked()));
                    TableMappingService.setCell(row, 7, item.getComplianceResult());
                }
        );
    }


    // ---------------------------------------------------------------------
    // Placeholder replacement
    // ---------------------------------------------------------------------

    /**
     * Replace {{key}} with value in a paragraph.
     * - inTable = false → null/empty → "....."
     * - inTable = true  → null/empty → ""
     */
    private void replacePlaceholders(XWPFParagraph paragraph,
                                     Map<String, String> data,
                                     boolean inTable) {
        if (paragraph == null) return;
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) return;

        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text == null) continue;

            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String placeholder = "{{" + key + "}}";
                if (text.contains(placeholder)) {
                    String rawValue = entry.getValue();
                    String filledValue = inTable
                            ? defaultString(rawValue)
                            : defaultParagraphValue(rawValue);

                    text = text.replace(placeholder, filledValue);
                    run.setText(text, 0);
                }
            }
        }
    }

    // ---------------------------------------------------------------------
    // Small helpers
    // ---------------------------------------------------------------------

    private String defaultString(String value) {
        return value != null ? value : "";
    }

    /**
     * For normal lines: show "....." if user did not input anything.
     */
    private String defaultParagraphValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ".....";
        }
        return value;
    }

    private String toStringOrEmpty(Number value) {
        return value != null ? value.toString() : "";
    }

    /**
     * Format LocalDate (issue date) as dd/MM/yyyy; blank if null.
     */
    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
