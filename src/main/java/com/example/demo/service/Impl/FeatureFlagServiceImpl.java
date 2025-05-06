package com.example.demo.service.Impl;

import com.example.demo.Repository.ClientFeatureFlagRepository;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.FeatureFlagRepository;
import com.example.demo.dto.FeatureFlagDTO;
import com.example.demo.excpetion.FeatureFlagException;
import com.example.demo.model.*;
import com.example.demo.service.FeatureFlagService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureFlagServiceImpl implements FeatureFlagService {

    @Autowired
    private FeatureFlagRepository featureFlagRepository;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ClientFeatureFlagRepository clientFlagRepo;

    public FeatureFlag createFeatureFlag(FeatureFlagDTO dto) {
        FeatureFlag flag = new FeatureFlag();
        flag.setName(dto.getName());
        flag.setDescription(dto.getDescription());
        if (dto.getParentId() != null) {
            FeatureFlag parent = featureFlagRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            flag.setParent(parent);
        }
        return featureFlagRepository.save(flag);
    }

    @Override
    public List<FeatureFlag> getAllFlags() {
        return featureFlagRepository.findAll();
    }

    @Override
    public FeatureFlag getFlagById(Long flagId) {
        return featureFlagRepository.findById(flagId)
                .orElseThrow(() -> new FeatureFlagException("Flag not found with id: " + flagId));
    }

    @Transactional
    public void setFlagForClient(Long clientId, Long flagId, boolean enable) {
        Client client = clientRepo.findById(clientId).orElseThrow();
        FeatureFlag flag = featureFlagRepository.findById(flagId).orElseThrow();

        if (enable && flag.getParent() != null) {
            ClientFeatureFlag parentFlag = clientFlagRepo
                    .findByClientAndFeatureFlag(client, flag.getParent())
                    .orElseThrow(() -> new FeatureFlagException("Parent flag not set"));
            if (!parentFlag.isEnabled()) {
                throw new FeatureFlagException("Cannot enable child flag when parent is disabled");
            }
        }

        ClientFeatureFlag clientFlag = clientFlagRepo
                .findByClientAndFeatureFlag(client, flag)
                .orElse(new ClientFeatureFlag(client, flag, enable));

        clientFlag.setEnabled(enable);
        clientFlagRepo.save(clientFlag);
    }

    public List<String> getEnabledFlags(Long clientId) {
        Client client = clientRepo.findById(clientId).orElseThrow();
        return clientFlagRepo.findByClientAndIsEnabledTrue(client)
                .stream()
                .map(f -> f.getFeatureFlag().getName())
                .toList();
    }

    public boolean isFlagEnabled(Long clientId, Long flagId) {
        Client client = clientRepo.findById(clientId).orElseThrow();
        FeatureFlag flag = featureFlagRepository.findById(flagId).orElseThrow();
        return clientFlagRepo.findByClientAndFeatureFlag(client, flag)
                .map(ClientFeatureFlag::isEnabled)
                .orElse(false);
    }


}
