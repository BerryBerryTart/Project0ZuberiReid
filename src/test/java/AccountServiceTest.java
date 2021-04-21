import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.berry.dao.AccountRepo;
import com.berry.dao.ConnectionUtil;
import com.berry.dto.AccQueryDTO;
import com.berry.dto.AccountDTO;
import com.berry.exception.BadParameterException;
import com.berry.exception.CreationException;
import com.berry.exception.DatabaseException;
import com.berry.exception.NotFoundException;
import com.berry.model.Account;
import com.berry.service.AccountService;

public class AccountServiceTest {
	private static AccountRepo mockAccountRepo;
	private static Connection mockConn;
	
	private AccountService accountService;

	@Before
	public void beforeTest() {
		accountService = new AccountService(mockAccountRepo);
	}
	
	@BeforeClass
	public static void setUp() throws DatabaseException, NotFoundException, BadParameterException {
		mockAccountRepo = mock(AccountRepo.class);
		mockConn = mock(Connection.class);
		
		//GET ALL ACCOUNTS
		when(mockAccountRepo.getAllAccounts(eq(1), eq(new AccQueryDTO()) ))
			.thenReturn(getMockAccountList());
		when(mockAccountRepo.getAllAccounts(eq(1), eq(new AccQueryDTO("100", "100000")) ))
			.thenReturn(getMockAccountList());
		when(mockAccountRepo.getAllAccounts(eq(2), eq(new AccQueryDTO()) ))
			.thenThrow(new NotFoundException());
		when(mockAccountRepo.getAllAccounts(eq(3), eq(new AccQueryDTO("One", "Two")) ))
			.thenThrow(new BadParameterException());
		
		//GET ACCOUNT BY ID
		when(mockAccountRepo.getAccountFromID(eq(1), eq(1)))
			.thenReturn(new Account("Savings", 10101010, 100.50));
		when(mockAccountRepo.getAccountFromID(eq(1), eq(2)))
			.thenReturn(null);
		when(mockAccountRepo.getAccountFromID(eq(2), eq(2)))
			.thenThrow(new NotFoundException("No Client Found"));
		
		//CREATE ACCOUNT
		when(mockAccountRepo.createAccount (eq(1), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenReturn(new Account("Savings", 10101010, 100.50));
		when(mockAccountRepo.createAccount (eq(2), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenReturn(null);
		when(mockAccountRepo.createAccount (eq(3), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenThrow(new NotFoundException("No Client Found"));
		when(mockAccountRepo.createAccount (eq(4), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenThrow(new DatabaseException());
		
		//UPDATE ACCOUNT
		when(mockAccountRepo.updateAccount (eq(1), eq(1), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenReturn(new Account("Savings", 10101010, 100.50));
		when(mockAccountRepo.updateAccount (eq(1), eq(2), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenThrow(new DatabaseException());
		when(mockAccountRepo.updateAccount (eq(2), eq(1), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenThrow(new NotFoundException("No Client Found"));
		when(mockAccountRepo.updateAccount (eq(3), eq(3), eq(new AccountDTO(10101010, 100.50, "Savings")) ))
			.thenReturn(null);
		
		//DELETE ACCOUNT
		when(mockAccountRepo.deleteAccount(eq(1), eq(1)))
			.thenReturn(true);
		when(mockAccountRepo.deleteAccount(eq(1), eq(2)))
			.thenThrow(new DatabaseException());
	}
	
	//BEGIN TESTS GET ALL ACCOUNTS
	
	@Test
	public void test_Fetch_All_Accounts() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			ArrayList<Account> actual = accountService.getAllAccounts("1", new AccQueryDTO());
			
			ArrayList<Account> expected = new ArrayList<Account>();
			expected.add(new Account("Savings", 10101010, 100.50));
			expected.add(new Account("Checking", 10101011, 1.99));
			
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void test_Fetch_All_Accounts_With_Valid_Params() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			ArrayList<Account> actual = accountService.getAllAccounts("1", new AccQueryDTO("100", "100000"));
			
			ArrayList<Account> expected = new ArrayList<Account>();
			expected.add(new Account("Savings", 10101010, 100.50));
			expected.add(new Account("Checking", 10101011, 1.99));
			
			assertEquals(expected, actual);
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void test_Fetch_All_Accounts_Client_Not_Found() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.getAllAccounts("2", new AccQueryDTO());
			fail("Not found exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Accounts_Illegal_Account_Parameter() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			AccQueryDTO accDTO = new AccQueryDTO();
			
			accountService.getAllAccounts("TWO", accDTO);
			fail("Bad param exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Accounts_Illegal_Lessthan_DTO() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			AccQueryDTO accDTO = new AccQueryDTO();
			accDTO.setLessThan("One");
			
			accountService.getAllAccounts("2", accDTO);
			fail("Bad param exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Accounts_Illegal_Greaterthan_DTO() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			AccQueryDTO accDTO = new AccQueryDTO();
			accDTO.setGreaterThan("One");
			
			accountService.getAllAccounts("2", accDTO);
			fail("Bad param exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Accounts_Illegal_Params_DTO() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			AccQueryDTO accDTO = new AccQueryDTO();
			accDTO.setLessThan("One");
			accDTO.setGreaterThan("One");
			
			accountService.getAllAccounts("2", accDTO);
			fail("Bad param exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Accounts_Illegal_Params_DTO_Data_Layer_Exception() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
						
			accountService.getAllAccounts("3", new AccQueryDTO("One", "Two"));
			fail("Bad param exception not thrown");
		}
	}
	
	//BEGIN TESTS GET ACCOUNT BY ID
	
	@Test
	public void test_Fetch_All_Account_By_Id() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			Account actual = accountService.getAccountById("1", "1");
			
			Account expected = new Account("Savings", 10101010, 100.50);
			
			assertEquals(expected, actual);
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void test_Fetch_All_Account_By_Id_Account_Not_Found() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.getAccountById("1", "2");
			fail("Not found exception not thrown");
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void test_Fetch_All_Account_By_Id_Client_Not_Found() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.getAccountById("2", "2");
			fail("Not found exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Account_By_Id_Client_Bad_Param() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.getAccountById("Two", "2");
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Account_By_Id_Account_Bad_Param() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.getAccountById("2", "Two");
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Fetch_All_Account_By_Id_Client_Both_Param() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.getAccountById("Two", "Two");
			fail("Bad parameter exception not thrown");
		}
	}
	
	//BEGIN TESTS CREATE ACCOUNT
	
	@Test
	public void test_Create_Account() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			Account actual = accountService.createAccount("1", new AccountDTO(10101010, 100.50, "Savings"));
			
			Account expected = new Account("Savings", 10101010, 100.50);
			
			assertEquals(expected, actual);
		}
	}
	
	@Test(expected = CreationException.class)
	public void test_Create_Account_Failure_To_Create() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.createAccount("2", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Creation exception not thrown");
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void test_Create_Account_Client_Not_Found() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.createAccount("3", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Not found exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Create_Account_Illegal_Parameter() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.createAccount("ONE", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_Create_Account_Layer_Error_Parameter() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.createAccount("4", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Database exception not thrown");
		}
	}
	
	//BEGIN TESTS UPDATE ACCOUNT
	@Test
	public void test_Update_Account() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			Account actual = accountService.updateAccount("1", "1", new AccountDTO(10101010, 100.50, "Savings"));
			
			Account expected = new Account("Savings", 10101010, 100.50);
			
			assertEquals(expected, actual);
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_Update_Account_Failure_To_Create() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.updateAccount("1", "2", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Database exception not thrown");
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void test_Update_Account_Client_Not_Found() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.updateAccount("2", "1", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Not found exception not thrown");
		}
	}
	
	@Test(expected = CreationException.class)
	public void test_Update_Account_Layer_Error() throws DatabaseException, BadParameterException, NotFoundException, CreationException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.updateAccount("3", "3", new AccountDTO(10101010, 100.50, "Savings"));
			fail("Creation exception not thrown");
		}
	}
	
	//BEGIN TESTS DELETE ACCOUNT
	@Test
	public void test_Delete_Account() throws DatabaseException, BadParameterException, NotFoundException{
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			assertEquals(accountService.deleteAccount("1","1"), true);
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_Delete_Account_Deletion_Error() throws DatabaseException, BadParameterException, NotFoundException{
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.deleteAccount("1","2");
			fail("database exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Delete_Account_Illegal_Client_Param() throws DatabaseException, BadParameterException, NotFoundException{
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.deleteAccount("One","2");
			fail("bad param exception not thrown");
		}
	}
	
	@Test(expected = BadParameterException.class)
	public void test_Delete_Account_Illegal_Account_Param() throws DatabaseException, BadParameterException, NotFoundException{
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			
			accountService.deleteAccount("1","Two");
			fail("bad param exception not thrown");
		}
	}
	
	//PRIVATE HELPER METHOD
	private static ArrayList<Account> getMockAccountList(){
		ArrayList<Account> accs = new ArrayList<Account>();
		accs.add(new Account("Savings", 10101010, 100.50));
		accs.add(new Account("Checking", 10101011, 1.99));
		return accs;
	}
}
