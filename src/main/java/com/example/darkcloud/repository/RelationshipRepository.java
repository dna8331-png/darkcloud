package com.example.darkcloud.repository;

import com.example.darkcloud.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}