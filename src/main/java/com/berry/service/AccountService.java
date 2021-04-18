package com.berry.service;

import java.util.ArrayList;

import com.berry.dao.AccountRepo;
import com.berry.exception.BadParameterException;
import com.berry.exception.DatabaseException;
import com.berry.exception.NotFoundException;
import com.berry.model.Account;

public class AccountService {
	private AccountRepo accountRepo;
	
	public AccountService() {
		this.accountRepo = new AccountRepo();
	}
	
	public ArrayList<Account> getAllAccounts(String stringId) throws DatabaseException, BadParameterException, NotFoundException {
		ArrayList<Account> accs = new ArrayList<Account>();
		
		int id = 0;
		try {
			id = Integer.parseInt(stringId);
			accs = accountRepo.getAllAccounts(id);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		
		
		return accs;
	}
	
}
