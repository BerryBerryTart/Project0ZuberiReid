package com.berry.dto;

public class ClientDTO {
	private String fname;
	private String lname;
	
	public ClientDTO() {
		super();
	}
	
	public ClientDTO(String fname, String lname) {
		this.fname = fname;
		this.lname = lname;
	}

	public boolean noFieldEmpty() {
		if (this.fname == null || this.lname == null) {
			return false;
		}
		else if (this.fname.equals("") || this.lname.equals("")) {
			return false;
		}
		
		return true;
	}
	
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}
	
}
