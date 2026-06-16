package com.example.darkcloud.controller;

import com.example.darkcloud.model.Relationship;
import com.example.darkcloud.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/relationships")
@CrossOrigin(origins = "*")
public class RelationshipController {
    @Autowired
    private RelationshipRepository repo;

    @GetMapping
    public List<Relationship> getAll() { return repo.findAll(); }
}