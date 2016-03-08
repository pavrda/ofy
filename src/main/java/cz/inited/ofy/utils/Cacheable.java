package cz.inited.ofy.utils;

public interface Cacheable <E> {
	public E call() throws CustomException;
}
