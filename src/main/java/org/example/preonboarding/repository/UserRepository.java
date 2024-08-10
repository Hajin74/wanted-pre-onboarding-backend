package org.example.preonboarding.repository;

import org.example.preonboarding.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
