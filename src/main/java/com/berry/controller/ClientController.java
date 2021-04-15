package com.berry.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		String fname = ctx.queryParam("fname");
		String lname = ctx.queryParam("lname");
		if (fname == null || lname == null) {
			throw new BadParameterException("Invalid Form Params");
		}
		Client client = clientService.createClient(fname, lname);
		if (client != null) {
			ctx.json(client);
			ctx.status(201);
		}
	};
	
	private Handler updateClient = ctx -> {
		String fname = ctx.queryParam("fname");
		String lname = ctx.queryParam("lname");
		if (fname == null || lname == null) {
			throw new BadParameterException("Invalid Form Params");
		}
		Client client = clientService.updateClient(fname, lname);
		if (client != null) {
			ctx.json(client);
			ctx.status(202);
		}
	};
	
	private Handler deleteClient = ctx -> {
		String id = ctx.pathParam("id");
		Client client = clientService.deleteClientById(id);
		if (client != null) {
			ctx.json(client);
			ctx.status(200);
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