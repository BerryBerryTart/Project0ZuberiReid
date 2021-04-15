package com.berry.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapFactory {
	public static Map<String, String> getErrorMap(String errorMsg){
		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("Error", errorMsg);
		return errorMap;
	}
}
