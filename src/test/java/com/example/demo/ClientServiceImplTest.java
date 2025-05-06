package com.example.demo;


import com.example.demo.Repository.ClientRepository;
import com.example.demo.model.Client;
import com.example.demo.service.Impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterClient_success() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Test Client");

        when(clientRepository.save(client)).thenReturn(client);

        Client saved = clientService.registerClient(client);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Test Client", saved.getName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testGetAllClient_returnsList() {
        Client c1 = new Client(1L, "Client A");
        Client c2 = new Client(2L, "Client B");

        when(clientRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Client> clients = clientService.getAllClient();

        assertEquals(2, clients.size());
        assertEquals("Client A", clients.get(0).getName());
        assertEquals("Client B", clients.get(1).getName());
        verify(clientRepository, times(1)).findAll();
    }
}
