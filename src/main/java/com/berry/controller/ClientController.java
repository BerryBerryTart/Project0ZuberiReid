package com.berry.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.berry.dto.ClientDTO;
import com.berry.exception.BadParameterException;
import com.berry.model.Client;
import com.berry.service.ClientService;

import io.javalin.Javalin;
import io.javalin.http.Handler;


public class ClientController implements Controller {
	private ClientService clientService;	
	
	public ClientController() {
		this.clientService = new ClientService();
	}

	private Handler getAllClients = ctx -> {
		ArrayList<Client> clients = clientService.getAllClients();
		Map<String, List<Client>> jsonMap = new HashMap<String, List<Client>>();
		
		jsonMap.put("clients", clients);
		ctx.json(jsonMap);
		ctx.status(200);
	};

	private Handler getClientById = ctx -> {
		String id = ctx.pathParam("id");
		Client client = clientService.getClientById(id);
		if (client != null) {
			ctx.json(client);
			ctx.status(200);
		}
	};
	
	private Handler createClient = ctx -> {
		ClientDTO clientDTO;
		try {
			clientDTO = ctx.bodyAsClass(ClientDTO.class);
		} catch (Exception e) {
			throw new BadParameterException(e.getMessage());
		}
		Client client = clientService.createClient(clientDTO);
		if (client != null) {
			ctx.json(client);
			ctx.status(201);
		}
	};
	
	private Handler updateClient = ctx -> {
		ClientDTO clientDTO;
		try {
			clientDTO = ctx.bodyAsClass(ClientDTO.class);
		} catch (Exception e) {
			throw new BadParameterException(e.getMessage());
		}
		String id = ctx.pathParam("id");
		Client client = clientService.updateClient(id, clientDTO);
		if (client != null) {
			ctx.json(client);
			ctx.status(202);
		}
	};
	
	private Handler deleteClient = ctx -> {
		String id = ctx.pathParam("id");
		boolean success = clientService.deleteClientById(id);
		if (success == true) {
			ctx.status(204);
		}
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients", getAllClients);
		app.get("/clients/:id", getClientById);
		app.post("/clients", createClient);
		app.put("/clients/:id", updateClient);
		app.delete("/clients/:id", deleteClient);
	}

}
