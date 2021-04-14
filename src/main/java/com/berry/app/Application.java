package com.berry.app;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;

public class Application {

	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		app = Javalin.create();
		
		app.error(404, ctx ->{
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("Error", "Path Not Found");
			ctx.json(errorMap);
			
			String URI = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			logger.info(httpMethod + " request to unknown endpoint '" + URI + "'");
		});
		
		app.start(5000);	
	}
}