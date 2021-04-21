package com.berry.dto;

public class AccQueryDTO {
	private String lessThan;
	private String greaterThan;
	
	public AccQueryDTO(String greaterThan, String lessThan) {
		this.greaterThan = "";
		this.lessThan = "";
	}
	
	public AccQueryDTO() {
		this.greaterThan = "";
		this.lessThan = "";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((greaterThan == null) ? 0 : greaterThan.hashCode());
		result = prime * result + ((lessThan == null) ? 0 : lessThan.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccQueryDTO other = (AccQueryDTO) obj;
		if (greaterThan == null) {
			if (other.greaterThan != null)
				return false;
		} else if (!greaterThan.equals(other.greaterThan))
			return false;
		if (lessThan == null) {
			if (other.lessThan != null)
				return false;
		} else if (!lessThan.equals(other.lessThan))
			return false;
		return true;
	}	
}
