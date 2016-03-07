package cz.inited.ofy.controllers;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;

import com.googlecode.objectify.ObjectifyService;

import cz.inited.ofy.models.User;
import cz.inited.ofy.utils.CustomException;

public class UserController {
	
	private static final String USERNAME_MASK = "[a-zA-Z0-9][a-zA-Z0-9\\._\\-]{2,}";

	// zaregistruji si vsechny entity, ktere budu pouzivat
	static {
		ObjectifyService.register(User.class);
	}

	private static final UserController instance = new UserController();
	
	private UserController() {
	}

	public static UserController getInstance() {
		return instance;
	}
	
	
	public User loginPassword(String username, String password) throws CustomException {
		User u = ofy().load().type(User.class).id(username).now();
		if ((u == null) || (!u.getPassword().equals(encryptPassword(password)))) {
			throw new CustomException("Spatne jmeno heslo");
		}

		u.setLastLogged(new Date());
		u.setTicket(createTicket());
		ofy().save().entity(u).now();
		return u;		
	}
	
	public User loginTicket(String username, String ticket) throws CustomException {
		User u = ofy().load().type(User.class).id(username).now();
		if (!u.getTicket().equals(ticket)) {
			throw new CustomException("Spatny ticket");
		}
		return u;
	}
	
	public static String encryptPassword(String password) {
		// TODO: v pristi verzi nahradit silnejsi sifrou
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(password.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			return new String(Hex.encodeHex(resultByte));
		} catch (Exception e) {
			return password;
		}

	}
	
	public User loadUser(String username) {
		User u = ofy().load().type(User.class).id(username).now();
		return u;
	}

	/**
	 * Kontroluje, jestli je mozne zaregistrovat uzivatele s takovym username
	 * @param username
	 * @return true - lze pouzit, false - nelze pouzit
	 * 
	 */
	public boolean checkUsername(String username) {
		if (
			(username == null) || 
			(username.length()<3) || 
			(!username.matches(USERNAME_MASK))
		) {
			return false;
		}
		User u = ofy().load().type(User.class).id(username).now();
		return (u==null);
	}

	
	protected String createTicket() {
		return UUID.randomUUID().toString();
	}

	public User setUserPassword(User u, String password) {
		u.setPassword(encryptPassword(password));
		return u;
	}

	public User saveUser(User user) {
		ofy().save().entity(user).now();
		return user;
	}
	
}
