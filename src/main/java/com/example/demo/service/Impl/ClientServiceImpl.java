package com.example.demo.service.Impl;

import com.example.demo.Repository.ClientRepository;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client registerClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }
}
