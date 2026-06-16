package com.example.darkcloud.repository;

import com.example.darkcloud.model.Threat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreatRepository extends JpaRepository<Threat, Long> {
}