package com.berry.dao;

import com.berry.exception.DatabaseException;

public interface Repo {
	public void connectToDB() throws DatabaseException;
}
