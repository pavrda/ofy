package cz.inited.ofy.controllers;

import cz.inited.ofy.utils.Cacheable;

public class CacheControler {

	private static final CacheControler instance = new CacheControler();

	private CacheControler() {
	}

	public static CacheControler getInstance() {
		return instance;
	}

	public <E> E get(String key, Cacheable<E> getter) {
		return getter.call(key);
	}
}
