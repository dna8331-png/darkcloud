package com.example.darkcloud.repository;

import com.example.darkcloud.model.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
}