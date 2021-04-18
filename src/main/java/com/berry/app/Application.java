package com.berry.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.controller.AccountController;
import com.berry.controller.ClientController;
import com.berry.controller.Controller;
import com.berry.controller.ExceptionController;
import com.berry.dao.DatabaseInit;

import io.javalin.Javalin;

public class Application {
	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	public static String fileIniPath;
	
	public static void main(String[] args) {
		if (args.length != 0) {
			fileIniPath = args[0];
		}
		else {
			fileIniPath = "src/main/resources/db.ini";
		}
		
		if (DatabaseInit.initDB() == true) {
			logger.info("Successfully Intialised Database");
		} else {
			logger.info("Database Exists And Is Ready");
		}
		
		app = Javalin.create();

		app.before(ctx -> {
			String URI = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			logger.info(httpMethod + " request to endpoint '" + URI + "' received");
		});

		mapControllers(new ClientController(), new AccountController(), new ExceptionController());

		app.start(5000);
	}

	public static void mapControllers(Controller... controllers) {
		for (Controller c : controllers) {
			c.mapEndpoints(app);
		}
	}
}