package cz.inited.ofy.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import cz.inited.ofy.models.APICheckUsernameResponse;
import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.APIGetUserInfoResponse;
import cz.inited.ofy.models.APIResponseBase;

@Category(IntegrationTests.class)
public class UserServletTest {

	@Test
	public void TestUserServlet() {
		String ticket = "";
		String s = "";
		String username = "test" + new Date().getTime();
		String password = "Heslo123";
		Response res;
		APIResponseBase responseBase;
		APIGetInfoResponse getInfoResponse;
		APIGetUserInfoResponse getUserInfoResponse;
		APICheckUsernameResponse checkUsernameResponse;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/api2");

		Form form = new Form();
		form.param("username", username);
		form.param("password", password);


		// Inicializace systemu
		//
		System.out.println("Init:");
		res = target.path("system/init").request(MediaType.APPLICATION_JSON_TYPE).get();
		s = res.readEntity(String.class);
		System.out.println(s);
		responseBase = getEntity(s, APIResponseBase.class);
		assertThat(responseBase.getStatus(), is(equalTo("ok")));


		// GetInfo bez prihlaseni
		//
		System.out.println("GetInfo:");
		res = target.path("user/getInfo").request(MediaType.APPLICATION_JSON_TYPE).get();
		s = res.readEntity(String.class);
		System.out.println(s);
		getInfoResponse = getEntity(s, APIGetInfoResponse.class);
		assertThat(getInfoResponse.getStatus(), is(equalTo("ok")));
		assertNull(getInfoResponse.getUsername());


		// CheckUsername pred registraci
		//
		System.out.println("CheckUsername:");
		
		res = target.path("user/checkUsername").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		s = res.readEntity(String.class);
		System.out.println(s);
		checkUsernameResponse = getEntity(s, APICheckUsernameResponse.class);
		assertThat(checkUsernameResponse.getStatus(), is(equalTo("ok")));
		assertThat(checkUsernameResponse.getUsername(), is(equalTo(username)));
		assertThat(checkUsernameResponse.getExists(), is(equalTo("0")));


		// Registrace uzivatele
		//
		System.out.println("Registrace:");
		res = target.path("user/registerUser").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		s = res.readEntity(String.class);
		System.out.println(s);
		getUserInfoResponse = getEntity(s, APIGetUserInfoResponse.class);

		int i = 0;
		for (String cookieKey : res.getCookies().keySet()) {
			System.out.println("cookie: " + cookieKey + ":" + res.getCookies().get(cookieKey));
			ticket = res.getCookies().get(cookieKey).getValue();
			i++;
		}
		assertThat(i, is(equalTo(1)));
		assertNotNull(ticket);
		assertThat(getUserInfoResponse.getTicket(), is(equalTo(ticket)));
		assertThat(getUserInfoResponse.getUsername(), is(equalTo(username)));


		// CheckUsername po registraci
		//
		System.out.println("CheckUsername:");
		
		res = target.path("user/checkUsername").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		s = res.readEntity(String.class);
		System.out.println(s);
		checkUsernameResponse = getEntity(s, APICheckUsernameResponse.class);
		assertThat(checkUsernameResponse.getStatus(), is(equalTo("ok")));
		assertThat(checkUsernameResponse.getUsername(), is(equalTo(username)));
		assertThat(checkUsernameResponse.getExists(), is(equalTo("1")));


		// Registrace uzivatele
		//
		System.out.println("Login:");
		res = target.path("user/login").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		s = res.readEntity(String.class);
		System.out.println(s);
		getUserInfoResponse = getEntity(s, APIGetUserInfoResponse.class);
		i = 0;
		for (String cookieKey : res.getCookies().keySet()) {
			System.out.println("cookie: " + cookieKey + ":" + res.getCookies().get(cookieKey));
			if ("ticket".equals(cookieKey)) {
				ticket = res.getCookies().get(cookieKey).getValue();
			}
			i++;
		}
		assertThat(i, is(equalTo(1)));
		assertNotNull(ticket);
		assertThat(getUserInfoResponse.getTicket(), is(equalTo(ticket)));
		assertThat(getUserInfoResponse.getUsername(), is(equalTo(username)));


		// GetUserInfo bez prihlaseni
		//
		System.out.println("GetUserInfo:");
		res = target.path("user/getUserInfo").request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		s = res.readEntity(String.class);
		System.out.println(s);
		getUserInfoResponse = getEntity(s, APIGetUserInfoResponse.class);
		assertThat(getUserInfoResponse.getStatus(), is(equalTo("error")));


		// GetUserInfo s prihlasenim
		//
		System.out.println("GetUserInfo:");
		res = target.path("user/getUserInfo").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = res.readEntity(String.class);
		System.out.println(s);
		getUserInfoResponse = getEntity(s, APIGetUserInfoResponse.class);
		assertThat(getUserInfoResponse.getStatus(), is(equalTo("ok")));
		assertThat(getUserInfoResponse.getUsername(), is(equalTo(username)));
		assertThat(getUserInfoResponse.getTicket(), is(equalTo(ticket)));


		// GetInfo s prihlasenim
		//
		System.out.println("GetInfo:");
		res = target.path("user/getInfo").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = res.readEntity(String.class);
		System.out.println(s);
		getInfoResponse = getEntity(s, APIGetInfoResponse.class);
		assertThat(getInfoResponse.getStatus(), is(equalTo("ok")));
		assertThat(getUserInfoResponse.getUsername(), is(equalTo(username)));


		// Logout
		//
		System.out.println("Logout:");
		res = target.path("user/logout").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = res.readEntity(String.class);
		System.out.println(s);
		responseBase = getEntity(s, APIResponseBase.class);
		assertThat(responseBase.getStatus(), is(equalTo("ok")));
		i = 0;
		Date expiry = null;
		for (String cookieKey : res.getCookies().keySet()) {
			System.out.println("cookie: " + cookieKey + ":" + res.getCookies().get(cookieKey));
			if ("ticket".equals(cookieKey)) {
				ticket = res.getCookies().get(cookieKey).getValue();
				expiry = res.getCookies().get(cookieKey).getExpiry();
			}
			i++;
		}
		assertThat(i, is(equalTo(1)));
		assertThat(expiry, is(equalTo(new Date(0))));


		// GetInfo s proslym ticketem
		//		
		System.out.println("GetInfo:");
		res = target.path("user/getInfo").request(MediaType.APPLICATION_JSON_TYPE).cookie(new Cookie("ticket", ticket))
				.get();
		s = res.readEntity(String.class);
		System.out.println(s);
		getInfoResponse = getEntity(s, APIGetInfoResponse.class);
		assertThat(getInfoResponse.getStatus(), is(equalTo("ok")));
		assertNull(getInfoResponse.getUsername());
	}


	public static <T> T getEntity(String string, Class<T> c) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JaxbAnnotationModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(string, c);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

}
