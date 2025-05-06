package com.example.demo.Repository;

import com.example.demo.model.Client;
import com.example.demo.model.ClientFeatureFlag;
import com.example.demo.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientFeatureFlagRepository extends JpaRepository<ClientFeatureFlag, Long> {

    Optional<ClientFeatureFlag> findByClientAndFeatureFlag(Client client, FeatureFlag flag);
    List<ClientFeatureFlag> findByClientAndIsEnabledTrue(Client client);

}
