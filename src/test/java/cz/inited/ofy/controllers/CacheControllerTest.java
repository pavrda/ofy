package cz.inited.ofy.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import cz.inited.ofy.utils.Cacheable;
import cz.inited.ofy.utils.CustomException;

public class CacheControllerTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
			new LocalMemcacheServiceTestConfig());

	CacheController cacheController;
	Closeable session;
	MemcacheService cache;

	@Before
	public void setup() {
		helper.setUp();
		session = ObjectifyService.begin();
		cacheController = CacheController.getInstance();

		cache = mock(MemcacheService.class);
		when(cache.contains("keyInCache")).thenReturn(true);
		when(cache.get("keyInCache")).thenReturn("bar");
		when(cache.contains("keyNotInCache")).thenReturn(false);
		cacheController.setCache(cache);
	}

	@After
	public void teardown() {
		helper.tearDown();
		session.close();
		session = null;
	}

	@Test
	public void testGetSameValue() throws CustomException {
		String str = cacheController.get("test123", new Cacheable<String>() {

			@Override
			public String call() throws CustomException {
				return "foo";
			}
		});
		assertThat(str, is(equalTo("foo")));
	}

	@Test
	public void testKeyNotInCache() throws CustomException {
		String str = cacheController.get("keyNotInCache", new Cacheable<String>() {

			@Override
			public String call() throws CustomException {
				return "foo";
			}
		});
		assertThat(str, is(equalTo("foo")));
		verify(cache, times(1)).put(anyString(), anyString());
	}

	@Test
	public void testKeyInCache() throws CustomException {
		String str = cacheController.get("keyInCache", new Cacheable<String>() {

			@Override
			public String call() throws CustomException {
				return "foo";
			}
		});
		assertThat(str, is(equalTo("bar")));
	}


}
