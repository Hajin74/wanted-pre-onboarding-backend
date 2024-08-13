package org.example.preonboarding.repository;

import org.example.preonboarding.domain.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {

    List<JobOpening> findByCompanyId(Long companyId);

    @Query(value = "SELECT j.* FROM job_opening j JOIN company c ON j.company_id = c.id "
            + "WHERE j.job_position LIKE CONCAT('%', :keyword, '%') "
            + "OR j.tech_stack LIKE CONCAT('%', :keyword, '%') "
            + "OR c.country LIKE CONCAT('%', :keyword, '%') "
            + "OR c.name LIKE CONCAT('%', :keyword, '%') "
            + "OR c.region LIKE CONCAT('%', :keyword, '%')",
            nativeQuery = true)
    List<JobOpening> findByKeyword(String keyword);

}
