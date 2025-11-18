package com.EIPplatform.repository.report.report06.part03;

import com.EIPplatform.model.entity.report.report06.part03.InventoryResultData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryResultDataRepository extends JpaRepository<InventoryResultData, UUID> {

    // Tìm theo report cha (OneToOne)
    Optional<InventoryResultData> findByReport06_Report06Id(UUID report06Id);

    boolean existsByReport06_Report06Id(UUID report06Id);

    // Query cơ bản với JOIN
    @Query("SELECT i FROM InventoryResultData i JOIN i.report06 r WHERE r.report06Id = :report06Id")
    Optional<InventoryResultData> findByReport06Id(@Param("report06Id") UUID report06Id);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InventoryResultData i JOIN i.report06 r WHERE r.report06Id = :report06Id")
    boolean existsByReport06Id(@Param("report06Id") UUID report06Id);

    // Query với JOIN FETCH để load đầy đủ con (emissionDatas, resultEntity, uncertaintyEvaluations)
    // Sử dụng @EntityGraph để eager load relationships, tránh LazyInitializationException
    @EntityGraph(attributePaths = {
            "emissionDatas.monthlyDatas",  // Load MonthlyEmissionData qua EmissionData
            "resultEntity",                // Load Result
            "uncertaintyEvaluations"       // Load UncertaintyEvaluation
    })
    @Query("SELECT i FROM InventoryResultData i WHERE i.report06.report06Id = :report06Id")
    Optional<InventoryResultData> findByReport06IdWithCollections(@Param("report06Id") UUID report06Id);

    // Nếu cần list tất cả theo operational data (ManyToOne)
    @EntityGraph(attributePaths = {"emissionDatas.monthlyDatas", "resultEntity", "uncertaintyEvaluations"})
    List<InventoryResultData> findByOperationalActivityData_operationalActivityDataId(UUID operationalActivityDataId);

    // Delete soft hoặc check tồn tại theo operational
    void deleteByReport06_Report06Id(UUID report06Id);
}