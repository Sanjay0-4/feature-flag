package com.example.demo.Repository;

import com.example.demo.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

    Optional<FeatureFlag> findById(Long id);
}
