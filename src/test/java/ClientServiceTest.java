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

import com.berry.dao.ConnectionUtil;
import com.berry.exception.BadParameterException;
import com.berry.exception.CreationException;
import com.berry.exception.DatabaseException;
import com.berry.exception.DeletionException;
import com.berry.exception.NotFoundException;
import com.berry.dto.ClientDTO;
import com.berry.dao.ClientRepo;
import com.berry.model.Client;
import com.berry.service.ClientService;

public class ClientServiceTest {
	private static ClientRepo mockClientRepo;
	private static Connection mockConn;

	private ClientService clientService;

	@Before
	public void beforeTest() {
		clientService = new ClientService(mockClientRepo);
	}

	@BeforeClass
	public static void setUp() throws CreationException, DatabaseException {
		mockClientRepo = mock(ClientRepo.class);
		mockConn = mock(Connection.class);
		
		//GET ALL CLIENTS
		when(mockClientRepo.getAllClients())
				.thenReturn(getMockClientList());
		
		//GET CLIENT BY ID
		when(mockClientRepo.getClientById(eq(1))).thenReturn(new Client("Jesse", "George", 1));
		when(mockClientRepo.getClientById(eq(2))).thenReturn(null);

		//CREATE CLIENT
		when(mockClientRepo.createClient(eq(new ClientDTO("Jesse", "George"))))
				.thenReturn(new Client("Jesse", "George"));
		when(mockClientRepo.createClient(eq(new ClientDTO("Not", "Found"))))
				.thenReturn(null);

		//UPDATE CLIENT
		when(mockClientRepo.updateClient(eq(1), eq(new ClientDTO("Jesse", "George"))))
				.thenReturn(new Client("Jesse", "George", 1));
		when(mockClientRepo.updateClient(eq(2), eq(new ClientDTO("Not", "Found"))))
				.thenReturn(null);
		
		//DELETE CLIENT
		when(mockClientRepo.deleteClientById(eq(1)))
			.thenReturn(true);
		when(mockClientRepo.deleteClientById(eq(2)))
			.thenThrow(new DatabaseException());
	}
	
	//BEGIN TEST GET ALL CLIENTS
	
	@Test
	public void test_Fetch_All_Clients() throws DatabaseException{
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			ArrayList<Client> actual = clientService.getAllClients();

			ArrayList<Client> expected = new ArrayList<Client>();
			expected.add(new Client("Harry", "Potter", 1));
			expected.add(new Client("Barry", "Botter", 2));
			expected.add(new Client("Larry", "Lotter", 3));;

			assertEquals(expected, actual);
		}
	}

	// BEGIN TEST GET CLIENT BY ID

	@Test
	public void test_Fetch_Client_By_ID() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			Client actual = clientService.getClientById("1");

			Client expected = new Client("Jesse", "George", 1);

			assertEquals(expected, actual);
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Fetch_Client_By_ID_Bad_Param() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.getClientById("One");
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = NotFoundException.class)
	public void test_Fetch_Client_By_ID_No_Client_Found() throws DatabaseException, BadParameterException, NotFoundException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.getClientById("2");
			fail("Not found exception not thrown");
		}
	}

	// BEGIN TEST CREATE CLIENT

	@Test
	public void test_CreatePath() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			Client actual = clientService.createClient(new ClientDTO("Jesse", "George"));

			Client expected = new Client("Jesse", "George");

			assertEquals(expected, actual);
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Create_Empty_Params() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);
			clientService.createClient(new ClientDTO("", ""));
			fail("Bad parameter exception not thrown");
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Create_Empty_First_Name() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.createClient(new ClientDTO("", "George"));
			fail("Bad parameter exception not thrown");
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Create_Empty_Last_Name() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.createClient(new ClientDTO("Jesse", ""));
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = CreationException.class)
	public void test_Create_Something_Went_Wrong() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.createClient(new ClientDTO("Not", "Found"));
			fail("Creation exception not thrown");
		}
	}

	// BEGIN TEST UPDATE CLIENT

	@Test
	public void test_Update_Client() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			Client actual = clientService.updateClient("1", new ClientDTO("Jesse", "George"));

			Client expected = new Client("Jesse", "George", 1);

			assertEquals(expected, actual);
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Update_Empty_Params() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.updateClient("1", new ClientDTO("", ""));
			fail("Bad parameter exception not thrown");
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Update_Empty_First_Name() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.updateClient("1", new ClientDTO("", "George"));
			fail("Bad parameter exception not thrown");
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Update_Empty_Last_Name() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.updateClient("1", new ClientDTO("Jesse", ""));
			fail("Bad parameter exception not thrown");
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Update_Bad_ID() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.updateClient("ONE", new ClientDTO("Jesse", "George"));
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = CreationException.class)
	public void test_Update_Client_Not_Found() throws DatabaseException, CreationException, BadParameterException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.updateClient("2", new ClientDTO("Not", "Found"));
			fail("Creation exception not thrown");
		}
	}

	// BEGIN TEST DELETE CLIENT

	@Test
	public void test_Delete_Client() throws DatabaseException, BadParameterException, DeletionException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			assertEquals(clientService.deleteClientById("1"), true);
		}
	}

	@Test(expected = BadParameterException.class)
	public void test_Delete_Client_Bad_Param() throws DatabaseException, BadParameterException, DeletionException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			clientService.deleteClientById("One");
			fail("Bad parameter exception not thrown");
		}
	}
	
	@Test(expected = DatabaseException.class)
	public void test_Delete_Client_Wrong_Param() throws DatabaseException, BadParameterException, DeletionException {
		try (MockedStatic<ConnectionUtil> mockedConnectionUtil = mockStatic(ConnectionUtil.class)) {
			mockedConnectionUtil.when(ConnectionUtil::connectToDB).thenReturn(mockConn);

			assertEquals(clientService.deleteClientById("2"), true);
			fail("database exception not thrown");
		}
	}
	
	//PRIVATE HELPER METHODS
	private static ArrayList<Client> getMockClientList(){
		ArrayList<Client> clients = new ArrayList<Client>();
		clients.add(new Client("Harry", "Potter", 1));
		clients.add(new Client("Barry", "Botter", 2));
		clients.add(new Client("Larry", "Lotter", 3));
		return clients;
	}
	

}
