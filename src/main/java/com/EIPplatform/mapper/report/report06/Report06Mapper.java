package com.EIPplatform.mapper.report.report06;

import com.EIPplatform.mapper.report.report06.part02.OperationalActivityDataMapper;
import com.EIPplatform.mapper.report.report06.part03.InventoryResultDataMapper;
import com.EIPplatform.model.dto.report.report06.Report06DTO;
import com.EIPplatform.model.entity.report.report06.Report06;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class Report06Mapper {

    @Autowired
    protected OperationalActivityDataMapper operationalActivityDataMapper;

    @Autowired
    protected InventoryResultDataMapper inventoryResultDataMapper;

    // Chính là method thay thế hàm toDto() của bạn
    @Mapping(target = "businessDetailId", source = "businessDetail.businessDetailId")
    @Mapping(target = "facilityName",     source = "businessDetail.facilityName")
    @Mapping(target = "reportCode",       source = "reportName")

    // Map child chỉ khi không null (rất gọn!)
    @Mapping(target = "operationalActivityDataDTO",
            expression = "java(report.getOperationalActivityData() != null ? operationalActivityDataMapper.toDTO(report.getOperationalActivityData()) : null)")
    @Mapping(target = "inventoryResultDataDTO",
            expression = "java(report.getInventoryResultData() != null ? inventoryResultDataMapper.toDTO(report.getInventoryResultData()) : null)")

    public abstract Report06DTO toDto(Report06 report);

    // Bonus: nếu cần list
    public abstract List<Report06DTO> toDtoList(List<Report06> reports);
}