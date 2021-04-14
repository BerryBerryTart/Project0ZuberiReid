package com.berry.controller;

import io.javalin.Javalin;

public interface Controller {
	void mapEndpoints(Javalin app);
}
