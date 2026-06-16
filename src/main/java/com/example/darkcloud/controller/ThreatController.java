package com.example.darkcloud.controller;

import com.example.darkcloud.model.Threat;
import com.example.darkcloud.repository.ThreatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/threats")
@CrossOrigin(origins = "*")
public class ThreatController {
    @Autowired
    private ThreatRepository repo;

    @GetMapping
    public List<Threat> getAll() { return repo.findAll(); }
}