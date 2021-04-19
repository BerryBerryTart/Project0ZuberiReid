package com.berry.dto;

public class AccQueryDTO {
	private String lessThan;
	private String greaterThan;
	
	public AccQueryDTO() {
		this.greaterThan = "";
	}
	
	public boolean lessThanNotEmpty() {
		if (this.lessThan == null || this.lessThan.trim().equals("") ) {
			return false;
		}
		return true;
	}

	public String getLessThan() {
		return lessThan;
	}

	public void setLessThan(String lessThan) {
		this.lessThan = lessThan;
	}

	public boolean greaterThanNotEmpty() {
		if (this.greaterThan == null || this.greaterThan.trim().equals("") ) {
			return false;
		}
		return true;
	}
	
	public String getGreaterThan() {
		return greaterThan;
	}

	public void setGreaterThan(String greaterThan) {
		this.greaterThan = greaterThan;
	}
	
	
}
