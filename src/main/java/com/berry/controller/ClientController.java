package com.berry.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients", getAllClients);
		app.get("/clients/:id", getClientById);
	}

}
