package org.example.preonboarding.repository;

import org.example.preonboarding.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    boolean existsByUserIdAndJobOpeningId(Long userId, Long jobOpeningId);

}
