package cz.inited.ofy.controllers;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import cz.inited.ofy.models.User;
import cz.inited.ofy.utils.CustomException;

public class UserControllerTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
			new LocalMemcacheServiceTestConfig());

	UserController userController;
	Closeable session;

	@Before
	public void setup() {
		helper.setUp();
		session = ObjectifyService.begin();
		userController = UserController.getInstance();
	}

	@After
	public void teardown() {
		helper.tearDown();
		session.close();
		session = null;
	}

	@Test
	public void testRegisterNewUser() throws CustomException {
		String username = "test" + new Date().getTime();
		User u = new User();
		u.setUsername(username);
		u.setEmail("test@test.com");
		userController.setUserPassword(u, "Heslo123");
		userController.saveUser(u);
		
		User u2 = userController.loadUser(username);
		assertNotNull("Uzivatel nenalezen", u2);
		assertThat(u2.getEmail(), is("test@test.com"));
		
		
		u2 = userController.loadUser("NEEXISTUJE");
		assertNull("Uzivatel nalezen", u2);
	}

	
	@Test
	public void testLoginPassword() throws CustomException {
		String username = "test" + new Date().getTime();
		User u = new User();
		u.setUsername(username);
		u.setEmail("test@test.com");
		userController.setUserPassword(u, "Heslo123");
		userController.saveUser(u);
		userController.loginPassword(username, "Heslo123");
	}

	@Test
	public void testLoginTicket() throws CustomException {
		String username = "test" + new Date().getTime();
		User u = new User();
		u.setUsername(username);
		u.setEmail("test@test.com");
		u.setTicket("1234567890");
		userController.saveUser(u);
		userController.loginTicket(username, "1234567890");
	}

	@Test
	public void testCreateTicket() {
		String ticket1 = userController.createTicket();
		String ticket2 = userController.createTicket();
		assertNotNull(ticket1);
		assertNotNull(ticket2);
		assertThat(ticket1, not(is(equalTo(ticket2))));
	}
	
	@Test
	public void testCheckUsername() {
		String username = "test" + new Date().getTime();
		User u = new User();
		u.setUsername(username);
		userController.saveUser(u);
		
		assertFalse(userController.checkUsername("a"));
		assertFalse(userController.checkUsername("!ramorido"));
		assertFalse(userController.checkUsername(username));
		assertTrue(userController.checkUsername("Test123"));		
	}
	
	
	@Test
	public void testFindByTicket() throws CustomException {
		String username = "test" + new Date().getTime();
		User u = new User();
		u.setUsername(username);
		u.setEmail("test@test.com");
		userController.setUserPassword(u, "Heslo123");
		userController.saveUser(u);

		u = userController.loginPassword(username, "Heslo123");
		String ticket = u.getTicket();
		assertNotNull(ticket);

		u = userController.findUserByTicket(ticket);
		assertThat(username, is(equalTo(u.getUsername())));
	}

	@Test
	public void testLogoutTicket() throws CustomException {
		String username = "test" + new Date().getTime();
		User u = new User();
		u.setUsername(username);
		u.setEmail("test@test.com");
		userController.setUserPassword(u, "Heslo123");
		userController.saveUser(u);

		u = userController.loginPassword(username, "Heslo123");
		String ticket = u.getTicket();
		assertNotNull(ticket);

		userController.logoutTicket(ticket);
		
		u = userController.findUserByTicket(ticket);
		assertNull(u);
	}
	
	
	
}
