package com.example.demo.controller;

import com.example.demo.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/feature-flags")
public class ClientFeatureFlagController {

    @Autowired
    private FeatureFlagService featureFlagService;

    @PutMapping("/{flagId}/override")
    public ResponseEntity<String> overrideFeatureFlag(
            @PathVariable Long clientId,
            @PathVariable Long flagId,
            @RequestParam boolean enable) {
        featureFlagService.setFlagForClient(clientId,flagId , enable);
        return ResponseEntity.ok("Client flag override saved");
    }

    @GetMapping("/enabled")
    public List<String> getEnabledFlags(@PathVariable Long clientId) {
        return featureFlagService.getEnabledFlags(clientId);
    }

    @GetMapping("/{flagId}")
    public boolean isFlagEnabled(@PathVariable Long clientId, @PathVariable Long flagId) {
        return featureFlagService.isFlagEnabled(clientId, flagId);
    }
}
