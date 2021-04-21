package com.berry.service;

import java.util.ArrayList;

import com.berry.dao.ClientRepo;
import com.berry.dto.ClientDTO;
import com.berry.exception.BadParameterException;
import com.berry.exception.CreationException;
import com.berry.exception.NotFoundException;
import com.berry.exception.DatabaseException;
import com.berry.exception.DeletionException;
import com.berry.model.Client;

public class ClientService {
	private ClientRepo clientRepo;

	public ClientService() {
		this.clientRepo = new ClientRepo();
	}
	
	public ClientService(ClientRepo clientRepo) {
		this.clientRepo = clientRepo;
	}

	public Client createClient(ClientDTO clientDTO) throws CreationException, DatabaseException, BadParameterException {
		Client client = null;
		if(clientDTO.noFieldEmpty() == false) {
			throw new BadParameterException("First/Last Name Must Not Be Empty");
		}
		
		client = clientRepo.createClient(clientDTO);
		
		if (client == null) {
			throw new CreationException("Failed To Create Client");
		}
		return client;
	}

	public ArrayList<Client> getAllClients() throws DatabaseException {
		ArrayList<Client> clients = new ArrayList<Client>();
		
		clients = clientRepo.getAllClients();
		
		return clients;
	}

	public Client getClientById(String stringId) throws BadParameterException, NotFoundException, DatabaseException {
		Client client = null;
		try {
			int id = Integer.parseInt(stringId);
			
			client = clientRepo.getClientById(id);
			
			if (client == null) {
				throw new NotFoundException("Client not found.");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return client;
	}
	
	public Client updateClient(String stringId, ClientDTO clientDTO) throws CreationException, DatabaseException, BadParameterException {
		Client client = null;
		try {
			int id = Integer.parseInt(stringId);
			
			if(clientDTO.noFieldEmpty() == false) {
				throw new BadParameterException("First/Last Name Must Not Be Empty");
			}
			
			client = clientRepo.updateClient(id, clientDTO);
			
			if (client == null) {
				throw new CreationException("Failed To Update Client");
			}
		}catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		
		return client;
	}
	
	public boolean deleteClientById(String stringId) throws BadParameterException, DatabaseException, DeletionException {
		boolean success = false;
		try {
			int id = Integer.parseInt(stringId);
			
			success = clientRepo.deleteClientById(id);
			
			if (success == false) {
				throw new DeletionException("Something went wrong with deletion.");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return success;
	}

}
