package com.berry.app;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.controller.ClientController;
import com.berry.controller.Controller;

import io.javalin.Javalin;

public class Application {

	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		app = Javalin.create();
		
		app.before(ctx -> {
			String URI = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			logger.info(httpMethod + " request to endpoint '" + URI + "' received");
		});	
		
		app.error(404, ctx ->{
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("Error", "Path Not Found");
			ctx.json(errorMap);
			
			String URI = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			logger.info(httpMethod + " request to unknown endpoint '" + URI + "'");
		});
		
		mapControllers(new ClientController());
		
		app.start(5000);	
	}
	
	public static void mapControllers(Controller... controllers) {
		for (Controller c : controllers) {
			c.mapEndpoints(app);
		}
	}
}