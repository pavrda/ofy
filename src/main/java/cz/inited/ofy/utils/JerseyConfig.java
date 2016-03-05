package cz.inited.ofy.utils;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CustomExceptionMapper.class);
    }
	
}
