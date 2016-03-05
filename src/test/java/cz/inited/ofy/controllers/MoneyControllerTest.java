package cz.inited.ofy.controllers;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import cz.inited.ofy.utils.CustomException;

public class MoneyControllerTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
			new LocalMemcacheServiceTestConfig());

	MoneyController moneyController;
	Closeable session;

	@Before
	public void setup() {
		helper.setUp();
		session = ObjectifyService.begin();
		moneyController = MoneyController.getInstance();
	}

	@After
	public void teardown() {
		helper.tearDown();
		session.close();
		session = null;
	}

	@Test
	public void testNewAccount() throws CustomException {
		String title = "test" + new Date().getTime();
		moneyController.createMoneyAccount(title);
		assertThat(moneyController.getBalance(title), is(equalTo(0L)));
	}

}
