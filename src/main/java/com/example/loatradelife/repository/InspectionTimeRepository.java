package com.example.loatradelife.repository;

import com.example.loatradelife.domain.InspectionTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InspectionTimeRepository extends JpaRepository<InspectionTime, Long> {
    Optional<InspectionTime> findTopByOrderByIdDesc();
}