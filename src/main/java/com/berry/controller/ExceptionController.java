package com.berry.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.exception.AccountCreationException;
import com.berry.exception.AccountNotFoundException;
import com.berry.exception.BadParameterException;
import com.berry.exception.CreationException;
import com.berry.exception.NotFoundException;
import com.berry.exception.DatabaseException;
import com.berry.exception.ErrorMapFactory;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ExceptionController implements Controller {

	private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	/*
	 * Exception handler
	 */
	private ExceptionHandler<BadParameterException> badParameterExceptionHandler = (e, ctx) -> {
		logger.warn("Bad parameter passed. " + e.getMessage());
		ctx.json(ErrorMapFactory.getErrorMap("Invalid Parameter"));
		ctx.status(400); // Provide an appropriate status code, such as 400
	};

	private ExceptionHandler<NotFoundException> clientNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("No such client found. " + e.getMessage());
		ctx.json(ErrorMapFactory.getErrorMap("Client Not Found"));
		ctx.status(404); // 404 not found
	};
	
	private ExceptionHandler<CreationException> creationException = (e, ctx) -> {
		logger.warn("Invalid Params For Client Creation. " + e.getMessage());
		ctx.json(ErrorMapFactory.getErrorMap("Failed To Update Client"));
		ctx.status(400); // Provide an appropriate status code, such as 400
	};
	
	private ExceptionHandler<AccountNotFoundException> accNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("No such account found. " + e.getMessage());
		ctx.json(ErrorMapFactory.getErrorMap("Account Not Found"));
		ctx.status(404); // 404 not found
	};
	
	private ExceptionHandler<AccountCreationException> accCreationException = (e, ctx) -> {
		logger.warn("Invalid Params For Account Creation. " + e.getMessage());
		ctx.json(ErrorMapFactory.getErrorMap(e.getMessage()));
		ctx.status(400); // Provide an appropriate status code, such as 400
	};
	
	private ExceptionHandler<DatabaseException> databaseException = (e, ctx) -> {
		logger.warn("DB Error: " + e.getMessage());
		ctx.json(ErrorMapFactory.getErrorMap("Database Error!"));
		ctx.status(500);
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(NotFoundException.class, clientNotFoundExceptionHandler);
		app.exception(CreationException.class, creationException);
		app.exception(AccountNotFoundException.class, accNotFoundExceptionHandler);
		app.exception(AccountCreationException.class, accCreationException);
		app.exception(DatabaseException.class, databaseException);
	}

}
