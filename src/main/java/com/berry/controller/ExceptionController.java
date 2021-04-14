package com.berry.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.exception.AccountNotFoundException;
import com.berry.exception.BadParameterException;
import com.berry.exception.ClientCreationException;
import com.berry.exception.ClientNotFoundException;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ExceptionController implements Controller {

	private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	/*
	 * Exception handler
	 */
	private ExceptionHandler<BadParameterException> badParameterExceptionHandler = (e, ctx) -> {
		logger.warn("Bad parameter passed. " + e.getMessage());
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("Error", "Invalid Parameter");
		ctx.json(errorMap);
		ctx.status(400); // Provide an appropriate status code, such as 400
	};

	private ExceptionHandler<ClientNotFoundException> clientNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("No such client found. " + e.getMessage());
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("Error", "Client Not Found");
		ctx.json(errorMap);
		ctx.status(404); // 404 not found
	};
	
	private ExceptionHandler<ClientCreationException> clientCreationException = (e, ctx) -> {
		logger.warn("Invalid params for creation. " + e.getMessage());
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("Error", "Failed To Create Client");
		ctx.json(errorMap);
		ctx.status(400); // Provide an appropriate status code, such as 400
	};
	
	private ExceptionHandler<AccountNotFoundException> accNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("No such account found. " + e.getMessage());
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("Error", "Account Not Found");
		ctx.json(errorMap);
		ctx.status(404); // 404 not found
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(ClientNotFoundException.class, clientNotFoundExceptionHandler);
		app.exception(ClientCreationException.class, clientCreationException);
		app.exception(AccountNotFoundException.class, accNotFoundExceptionHandler);
	}

}
