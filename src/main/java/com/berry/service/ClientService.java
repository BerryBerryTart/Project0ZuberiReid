package com.berry.service;

import java.util.ArrayList;

import com.berry.dao.ClientRepo;
import com.berry.exception.BadParameterException;
import com.berry.exception.ClientNotFoundException;
import com.berry.model.Client;

public class ClientService {
	private ClientRepo clientRepo;
	
	public ClientService() {
		this.clientRepo = new ClientRepo();
	}
	
	public ArrayList<Client> getAllClients(){
		ArrayList<Client> clients = new ArrayList<Client>();
		clients = clientRepo.getAllClients();
		return clients;
	}
	
	public Client getClientById(String stringId) throws BadParameterException, ClientNotFoundException {
		Client client = null;
		try {
			int id = Integer.parseInt(stringId);
			client = clientRepo.getClientById(id);
			if (client == null) {
				throw new ClientNotFoundException("Client not found.");
			}
		}
		catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return client;
	}
	
}
