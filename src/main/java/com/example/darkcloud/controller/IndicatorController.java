package com.example.darkcloud.controller;

import com.example.darkcloud.model.Indicator;
import com.example.darkcloud.repository.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/indicators")
@CrossOrigin(origins = "*")
public class IndicatorController {
    @Autowired
    private IndicatorRepository repo;

    @GetMapping
    public List<Indicator> getAll() { return repo.findAll(); }
}