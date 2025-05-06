package com.example.demo;


import com.example.demo.Repository.ClientFeatureFlagRepository;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.FeatureFlagRepository;
import com.example.demo.dto.FeatureFlagDTO;

import com.example.demo.excpetion.FeatureFlagException;
import com.example.demo.model.Client;
import com.example.demo.model.ClientFeatureFlag;
import com.example.demo.model.FeatureFlag;
import com.example.demo.service.Impl.FeatureFlagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeatureFlagServiceImplTest {

    @InjectMocks
    private FeatureFlagServiceImpl service;

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Mock
    private ClientRepository clientRepo;

    @Mock
    private ClientFeatureFlagRepository clientFlagRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeatureFlag_withParent() {
        FeatureFlagDTO dto = new FeatureFlagDTO("child-flag", "desc", 1L);
        FeatureFlag parent = new FeatureFlag();
        parent.setId(1L);

        when(featureFlagRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(featureFlagRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FeatureFlag created = service.createFeatureFlag(dto);

        assertEquals("child-flag", created.getName());
        assertEquals(parent, created.getParent());
        verify(featureFlagRepository).save(any());
    }

    @Test
    void testSetFlagForClient_enableChildWithoutParentEnabled_throwsException() {
        Long clientId = 1L, parentId = 2L, childId = 3L;

        Client client = new Client();
        client.setId(clientId);

        FeatureFlag parent = new FeatureFlag();
        parent.setId(parentId);

        FeatureFlag child = new FeatureFlag();
        child.setId(childId);
        child.setParent(parent);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(featureFlagRepository.findById(childId)).thenReturn(Optional.of(child));
        when(clientFlagRepo.findByClientAndFeatureFlag(client, parent))
                .thenReturn(Optional.of(new ClientFeatureFlag(client, parent, false)));

        assertThrows(FeatureFlagException.class, () ->
                service.setFlagForClient(clientId, childId, true));
    }

    @Test
    void testSetFlagForClient_enableWithValidParent_succeeds() {
        Long clientId = 1L;
        Long parentId = 2L;
        Long childId = 3L;

        Client client = new Client();
        client.setId(clientId);

        FeatureFlag parent = new FeatureFlag();
        parent.setId(parentId);

        FeatureFlag child = new FeatureFlag();
        child.setId(childId);
        child.setParent(parent);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(featureFlagRepository.findById(childId)).thenReturn(Optional.of(child));
        when(featureFlagRepository.findById(parentId)).thenReturn(Optional.of(parent));

        when(clientFlagRepo.findByClientAndFeatureFlag(client, parent))
                .thenReturn(Optional.of(new ClientFeatureFlag(client, parent, true)));

        when(clientFlagRepo.findByClientAndFeatureFlag(client, child))
                .thenReturn(Optional.empty());

        service.setFlagForClient(clientId, childId, true);

        verify(clientFlagRepo).save(any(ClientFeatureFlag.class));
    }

    @Test
    void testGetEnabledFlags_returnsCorrectNames() {
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        FeatureFlag flag1 = new FeatureFlag(); flag1.setName("f1");
        FeatureFlag flag2 = new FeatureFlag(); flag2.setName("f2");

        ClientFeatureFlag cf1 = new ClientFeatureFlag(client, flag1, true);
        ClientFeatureFlag cf2 = new ClientFeatureFlag(client, flag2, true);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(clientFlagRepo.findByClientAndIsEnabledTrue(client))
                .thenReturn(List.of(cf1, cf2));

        List<String> flags = service.getEnabledFlags(clientId);

        assertEquals(List.of("f1", "f2"), flags);
    }

    @Test
    void testIsFlagEnabled_returnsTrue() {
        Long clientId = 1L, flagId = 2L;
        Client client = new Client(); client.setId(clientId);
        FeatureFlag flag = new FeatureFlag(); flag.setId(flagId);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(featureFlagRepository.findById(flagId)).thenReturn(Optional.of(flag));
        when(clientFlagRepo.findByClientAndFeatureFlag(client, flag))
                .thenReturn(Optional.of(new ClientFeatureFlag(client, flag, true)));

        assertTrue(service.isFlagEnabled(clientId, flagId));
    }

    @Test
    void testIsFlagEnabled_returnsFalseWhenNotSet() {
        Long clientId = 1L, flagId = 2L;
        Client client = new Client(); client.setId(clientId);
        FeatureFlag flag = new FeatureFlag(); flag.setId(flagId);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(featureFlagRepository.findById(flagId)).thenReturn(Optional.of(flag));
        when(clientFlagRepo.findByClientAndFeatureFlag(client, flag)).thenReturn(Optional.empty());

        assertFalse(service.isFlagEnabled(clientId, flagId));
    }
}
