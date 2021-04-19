package com.berry.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.berry.dto.AccQueryDTO;
import com.berry.dto.AccountDTO;
import com.berry.exception.BadParameterException;
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
		AccQueryDTO accQueryDTO = new AccQueryDTO();

		accQueryDTO.setLessThan(ctx.queryParam("amountLessThan"));
		accQueryDTO.setGreaterThan(ctx.queryParam("amountGreaterThan"));

		ArrayList<Account> accs = accountService.getAllAccounts(id, accQueryDTO);
		Map<String, List<Account>> jsonMap = new HashMap<String, List<Account>>();

		jsonMap.put("accounts", accs);

		ctx.json(jsonMap);
		ctx.status(200);
	};

	private Handler getAccountById = ctx -> {
		String id = ctx.pathParam("id");
		String accID = ctx.pathParam("acc");
		Account acc = accountService.getAccountById(id, accID);
		if (acc != null) {
			ctx.json(acc);
			ctx.status(200);
		}
	};

	private Handler createAccount = ctx -> {
		AccountDTO accountDTO;
		String id = ctx.pathParam("id");
		try {
			accountDTO = ctx.bodyAsClass(AccountDTO.class);
		} catch (Exception e) {
			throw new BadParameterException(e.getMessage());
		}
		Account acc = accountService.createAccount(id, accountDTO);
		if (acc != null) {
			ctx.json(acc);
			ctx.status(201);
		}
	};

	private Handler updateAccount = ctx -> {
		AccountDTO accountDTO;
		String id = ctx.pathParam("id");
		String accID = ctx.pathParam("acc");
		try {
			accountDTO = ctx.bodyAsClass(AccountDTO.class);
		} catch (Exception e) {
			throw new BadParameterException(e.getMessage());
		}
		Account acc = accountService.updateAccount(id, accID, accountDTO);
		if (acc != null) {
			ctx.json(acc);
			ctx.status(201);
		}
	};

	private Handler deleteAccount = ctx -> {
		String id = ctx.pathParam("id");
		String accID = ctx.pathParam("acc");
		boolean success = accountService.deleteAccount(id, accID);
		if (success == true) {
			ctx.status(204);
		}
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/clients/:id/accounts", getAllAccounts);
		app.get("/clients/:id/accounts/:acc", getAccountById);
		app.post("/clients/:id/accounts", createAccount);
		app.put("/clients/:id/accounts/:acc", updateAccount);
		app.delete("/clients/:id/accounts/:acc", deleteAccount);
	}
}
