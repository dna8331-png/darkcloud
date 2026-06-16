package com.example.darkcloud.controller;

import com.example.darkcloud.model.Actor;
import com.example.darkcloud.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actors")
@CrossOrigin(origins = "*")
public class ActorController {
    @Autowired
    private ActorRepository repo;

    @GetMapping
    public List<Actor> getAll() { return repo.findAll(); }
}