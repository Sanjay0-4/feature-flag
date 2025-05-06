package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ClientFeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private FeatureFlag featureFlag;

    private boolean isEnabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public FeatureFlag getFeatureFlag() {
        return featureFlag;
    }

    public void setFeatureFlag(FeatureFlag featureFlag) {
        this.featureFlag = featureFlag;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public ClientFeatureFlag() {
    }

    public ClientFeatureFlag(Client client, FeatureFlag featureFlag, boolean isEnabled) {
        this.client = client;
        this.featureFlag = featureFlag;
        this.isEnabled = isEnabled;
    }
}