package org.example.preonboarding.repository;

import org.example.preonboarding.domain.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {

    List<JobOpening> findByCompanyId(Long companyId);

}
