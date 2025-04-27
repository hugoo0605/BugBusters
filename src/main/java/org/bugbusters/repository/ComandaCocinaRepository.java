package org.bugbusters.repository;

import org.bugbusters.entity.ComandaCocina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComandaCocinaRepository extends JpaRepository<ComandaCocina, Long> {
}