package com.berry.service;

import java.util.ArrayList;

import com.berry.dao.ClientRepo;
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

	public Client createClient(String fname, String lname) throws CreationException, DatabaseException {
		Client client = null;
		if (fname == null || lname == null) {
			throw new CreationException("Invalid Creation Params");
		}
		
		client = clientRepo.createClient(fname, lname);
		
		if (client == null) {
			throw new CreationException("Failed To Create");
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
	
	public Client updateClient(String fname, String lname, String stringId) throws CreationException, DatabaseException, BadParameterException {
		Client client = null;
		try {
			int id = Integer.parseInt(stringId);
			
			client = clientRepo.updateClient(fname, lname, id);
			
			if (client == null) {
				throw new CreationException("Failed To Update");
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
