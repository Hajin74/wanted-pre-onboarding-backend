package org.example.preonboarding.repository;

import org.example.preonboarding.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsById(Long id);
}
