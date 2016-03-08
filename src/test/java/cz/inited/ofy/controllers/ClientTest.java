package cz.inited.ofy.controllers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import cz.inited.ofy.models.APIResponseBase;

public class ClientTest {

	public static void main(String[] args) {
		String ticket = "";
		String s = "";
		Response r2;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/api2");

		Form form = new Form();
		form.param("username", "bar");
		form.param("password", "foo");
		
		APIResponseBase res = target.path("system/init").request(MediaType.APPLICATION_JSON_TYPE).get()
				.readEntity(APIResponseBase.class);
		System.out.println(res.getStatus());

		System.out.println("GetInfo:");
		r2 = target.path("user/getInfo").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = r2.readEntity(String.class);
		System.out.println(s);

		System.out.println("Registrace:");
		r2 = target.path("user/registerUser").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		s = r2.readEntity(String.class);
		System.out.println(s);
		for (String cookieKey : r2.getCookies().keySet()) {
			System.out.println("cookie: " + cookieKey + ":" + r2.getCookies().get(cookieKey));
		}

		System.out.println("Login:");
		r2 = target.path("user/login").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		s = r2.readEntity(String.class);
		System.out.println(s);
		for (String cookieKey : r2.getCookies().keySet()) {
			System.out.println("cookie: " + cookieKey + ":" + r2.getCookies().get(cookieKey));
			if ("ticket".equals(cookieKey)) {
				ticket = r2.getCookies().get(cookieKey).getValue();
			}
		}

		System.out.println("GetUserInfo:");
		r2 = target.path("user/getUserInfo").request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		s = r2.readEntity(String.class);
		System.out.println(s);
		
		System.out.println("GetUserInfo:");
		r2 = target.path("user/getUserInfo").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = r2.readEntity(String.class);
		System.out.println(s);
		
		System.out.println("GetInfo:");
		r2 = target.path("user/getInfo").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = r2.readEntity(String.class);
		System.out.println(s);
		

		
		
	}

}
