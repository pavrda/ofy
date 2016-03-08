package cz.inited.ofy.controllers;

import java.util.logging.Level;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import cz.inited.ofy.utils.Cacheable;
import cz.inited.ofy.utils.CustomException;

public class CacheController {

	private static final CacheController instance = new CacheController();
	private MemcacheService cache;

	private CacheController() {
        cache = MemcacheServiceFactory.getMemcacheService();
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));		
	}

	public static CacheController getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <E> E get(String key, Cacheable<E> getter) throws CustomException {
		if (cache.contains(key)) {
			return (E) cache.get(key);
		}
		E value = getter.call();
		cache.put(key, value);
		return value;
	}
	
	public void delete(String key) {
		cache.delete(key);
	}
	
	/**
	 * For unit test purpose
	 * @param cache
	 */
	protected void setCache(MemcacheService cache) {
		this.cache = cache;
	}
	
	public void put(String key, Object value) {
		cache.put(key, value);
	}
}
