package com.example.demo.excpetion;

public class FeatureFlagException extends RuntimeException {
    public FeatureFlagException(String message) {
        super(message);
    }
}
