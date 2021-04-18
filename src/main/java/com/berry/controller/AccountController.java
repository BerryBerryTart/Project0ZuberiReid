package com.berry.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.berry.model.Account;
import com.berry.service.AccountService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController implements Controller {
	private AccountService accountService;
	
	public AccountController() {
		this.accountService = new AccountService();
	}

	private Handler getAllAccounts = ctx -> {
		String id = ctx.pathParam("id");
		ArrayList<Account> accs = accountService.getAllAccounts(id);
		Map<String, List<Account>> jsonMap = new HashMap<String, List<Account>>();
		
		jsonMap.put("accounts", accs);
		
		ctx.json(jsonMap);
		ctx.status(200);
	};
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients/:id/accounts", getAllAccounts);	
	}
}
