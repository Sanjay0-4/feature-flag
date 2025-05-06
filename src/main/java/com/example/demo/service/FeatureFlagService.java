package com.example.demo.service;


import com.example.demo.dto.FeatureFlagDTO;
import com.example.demo.model.FeatureFlag;

import java.util.List;

public interface FeatureFlagService {
    FeatureFlag createFeatureFlag(FeatureFlagDTO featureFlag);
    List<FeatureFlag> getAllFlags();
    FeatureFlag getFlagById(Long flagId);
    List<String> getEnabledFlags(Long clientId);
    boolean isFlagEnabled(Long clientId, Long flagId);
    void setFlagForClient(Long clientId, Long flagId, boolean enable);

}
