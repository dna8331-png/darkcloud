package com.example.darkcloud.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.darkcloud.model.Threat;
import com.example.darkcloud.repository.ThreatRepository;

@RestController
@RequestMapping("/api/threats")
@CrossOrigin(origins = "*")
public class ThreatController {

    @Autowired
    private ThreatRepository repo;

    @Value("${otx.api.key}")
    private String otxApiKey;

    // ── Get all threats from database ────────────────────────────────────────
    @GetMapping
    public List<Threat> getAll() {
        return repo.findAll();
    }

    // ── Fetch live threats from AlienVault OTX and save to database ──────────
    @GetMapping("/fetch-live")
    public List<Threat> fetchLiveThreats() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-OTX-API-KEY", otxApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = "https://otx.alienvault.com/api/v1/pulses/subscribed?limit=10";

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            Map body = response.getBody();
            if (body != null && body.containsKey("results")) {
                List<Map> pulses = (List<Map>) body.get("results");

                for (Map pulse : pulses) {
                    Threat threat = new Threat();

                    String name = (String) pulse.getOrDefault("name", "Unknown Threat");
                    String description = (String) pulse.getOrDefault("description", "Threat detected from AlienVault OTX");

                    // Set threat type based on tags
                    List<String> tags = (List<String>) pulse.getOrDefault("tags", new ArrayList<>());
                    String type = detectType(tags, name);

                    // Set severity based on TLP
                    String tlp = (String) pulse.getOrDefault("TLP", "white");
                    String severity = detectSeverity(tlp, tags);

                    threat.setType(type);
                    threat.setDescription(description.length() > 200 ? description.substring(0, 200) : description);
                    threat.setSeverity(severity);
                    threat.setStatus("Active");

                    // Save only if not duplicate
                    boolean exists = repo.findAll().stream()
                        .anyMatch(t -> t.getDescription().equals(threat.getDescription()));

                    if (!exists) {
                        repo.save(threat);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("OTX Fetch Error: " + e.getMessage());
        }

        return repo.findAll();
    }

    // ── Detect threat type from tags ─────────────────────────────────────────
    private String detectType(List<String> tags, String name) {
        String combined = (tags.toString() + " " + name).toLowerCase();
        if (combined.contains("malware") || combined.contains("virus")) return "Malware";
        if (combined.contains("phishing") || combined.contains("social")) return "Social Engineering";
        if (combined.contains("ransomware")) return "Ransomware";
        if (combined.contains("breach") || combined.contains("leak")) return "Data Breach";
        if (combined.contains("brute") || combined.contains("ddos")) return "Brute Force";
        if (combined.contains("apt") || combined.contains("espionage")) return "APT Attack";
        return "Threat Intelligence";
    }

    // ── Detect severity from TLP ──────────────────────────────────────────────
    private String detectSeverity(String tlp, List<String> tags) {
        String combined = (tags.toString() + " " + tlp).toLowerCase();
        if (combined.contains("red") || combined.contains("critical")) return "CRITICAL";
        if (combined.contains("amber") || combined.contains("high")) return "HIGH";
        if (combined.contains("green") || combined.contains("medium")) return "MEDIUM";
        return "LOW";
    }

    // ── Add threat manually ───────────────────────────────────────────────────
    @PostMapping
    public Threat add(@RequestBody Threat threat) {
        return repo.save(threat);
    }

    // ── Delete threat ─────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}