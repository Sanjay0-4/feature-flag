package com.example.demo.controller;

import com.example.demo.dto.FeatureFlagDTO;
import com.example.demo.model.FeatureFlag;
import com.example.demo.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagController {

    @Autowired
    private FeatureFlagService featureFlagService;

    @PostMapping
    public ResponseEntity<FeatureFlag> createFlag(@RequestBody FeatureFlagDTO flag) {
        return ResponseEntity.ok(featureFlagService.createFeatureFlag(flag));
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getAllFlags() {
        return ResponseEntity.ok(featureFlagService.getAllFlags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlag> getFlagById(@PathVariable Long id) {
        return ResponseEntity.ok(featureFlagService.getFlagById(id));
    }


}
