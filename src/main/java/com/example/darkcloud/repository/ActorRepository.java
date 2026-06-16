package com.example.darkcloud.repository;

import com.example.darkcloud.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}