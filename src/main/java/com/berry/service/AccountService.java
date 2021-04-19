package com.berry.service;

import java.util.ArrayList;

import com.berry.dao.AccountRepo;
import com.berry.dto.AccQueryDTO;
import com.berry.dto.AccountDTO;
import com.berry.exception.BadParameterException;
import com.berry.exception.CreationException;
import com.berry.exception.DatabaseException;
import com.berry.exception.NotFoundException;
import com.berry.model.Account;

public class AccountService {
	private AccountRepo accountRepo;
	
	public AccountService() {
		this.accountRepo = new AccountRepo();
	}
	
	public ArrayList<Account> getAllAccounts(String stringId, AccQueryDTO accQueryDTO) throws DatabaseException, BadParameterException, NotFoundException {
		ArrayList<Account> accs = new ArrayList<Account>();
		int id = 0;
		try {
			id = Integer.parseInt(stringId);
			accs = accountRepo.getAllAccounts(id, accQueryDTO);
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return accs;
	}
	
	public Account getAccountById(String stringId, String StringAccId) throws BadParameterException, DatabaseException, NotFoundException {
		Account acc = null;
		try {
			int id = Integer.parseInt(stringId);
			int accId = Integer.parseInt(StringAccId);
			
			acc = accountRepo.getAccountFromID(id, accId);
			if (acc == null) {
				throw new NotFoundException("Client not found.");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return acc;
	}
	
	public Account createAccount(String stringId, AccountDTO accountDTO) throws BadParameterException, CreationException, DatabaseException, NotFoundException {
		Account acc = null;
		if (accountDTO.noFieldEmpty() == false) {
			throw new BadParameterException("All Fields Are Required");
		}
		try {
			int id = Integer.parseInt(stringId);
			acc = accountRepo.createAccount(id, accountDTO);			
		
			if (acc == null) {
				throw new CreationException("Failed To Create Account");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return acc;
	}
	
	public Account updateAccount(String stringId, String stringAccId, AccountDTO accountDTO) throws BadParameterException, CreationException, DatabaseException, NotFoundException {
		Account acc = null;
		if (accountDTO.noFieldEmpty() == false) {
			throw new BadParameterException("All Fields Are Required");
		}
		try {
			int id = Integer.parseInt(stringId);
			int accId = Integer.parseInt(stringAccId);
			acc = accountRepo.updateAccount(id, accId, accountDTO);			
		
			if (acc == null) {
				throw new CreationException("Failed To Update Account");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return acc;
	}
	
	public boolean deleteAccount(String stringId, String stringAccId) throws BadParameterException, DatabaseException, NotFoundException {
		boolean success = false;
		try {
			int id = Integer.parseInt(stringId);
			int accId = Integer.parseInt(stringAccId);
			
			success = accountRepo.deleteAccount(id, accId);
			
		} catch (NumberFormatException e) {
			throw new BadParameterException("Param must be an integer.");
		}
		return success;
	}
	
}
