package com.example.demo.service;

import com.example.demo.model.Client;

import java.util.List;

public interface ClientService {
    Client registerClient(Client client);
    List<Client> getAllClient();
}
