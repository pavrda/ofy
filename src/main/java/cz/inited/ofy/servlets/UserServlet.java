package cz.inited.ofy.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Hex;

import com.googlecode.objectify.ObjectifyService;

import cz.inited.ofy.controllers.MoneyController;
import cz.inited.ofy.models.APICheckUsernameResponse;
import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.APIGetUserInfoResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.models.MoneyAccount;
import cz.inited.ofy.models.User;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/user")
@Api(tags = { "user" })
public class UserServlet {

	private static final String USERNAME_MASK = "[a-zA-Z0-9][a-zA-Z0-9\\._\\-]{2,}";
	private MoneyController moneyController;
	
	// zaregistruji si vsechny entity, ktere budu pouzivat
	static {
		ObjectifyService.register(User.class);
		ObjectifyService.register(MoneyAccount.class);
	}	
	
	public UserServlet() {
		moneyController = MoneyController.getInstance();
	}
	

	@GET
	@Path("/getInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Vraci jackpot, muj kredit, ...", notes = "Vola se v pravidelnych intervalech", response = APIGetInfoResponse.class)
	public APIGetInfoResponse getInfo(@Context HttpServletRequest request) throws CustomException {
		APIGetInfoResponse res = new APIGetInfoResponse();

		res.setGameCredit(moneyController.getBalance("_game"));
		
		String currentUser = getCurrentUser(request);
		if (currentUser != null) {
			res.setCredit(moneyController.getBalance(currentUser));
		} else {
			res.setCredit(0L);
		}
		return res;
	}

	/**
	 * Přihlásí uživatele
	 *
	 * @param username
	 * @param password
	 * @return
	 *         <li>status - ok / error</li>
	 *         <li>code - kód v případě chyby. "bad password" - pokud nelze
	 *         přihlásit</li>
	 *         <li>msg - zpráva pro uživatele</li>
	 *         <li>... - informace o uživateli</li>
	 * @throws CustomException
	 *
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIGetInfoResponse login(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword, @FormParam("ticket") String sTicket) throws CustomException {

		if ((sPassword != null) && (sTicket == null)) {
			User u = ofy().load().type(User.class).id(sUsername).now();
			if ((u == null) || (!u.getPassword().equals(encryptPassword(sPassword)))) {
				throw new CustomException("Spatne jmeno heslo");
			}

			u.setLastLogged(new Date());
			u.setTicket(createTicket());
			ofy().save().entity(u);
			request.getSession().setAttribute("username", sUsername);

			return getUserInfo(request);
		}

		if ((sPassword == null) && (sTicket != null)) {
			User u = ofy().load().type(User.class).id(sUsername).now();
			if (!u.getTicket().equals(encryptPassword(sTicket))) {
				throw new CustomException("Spatny ticket");
			}
			request.getSession().setAttribute("username", sUsername);

			return getUserInfo(request);
		}

		throw new CustomException("Spatne jmeno heslo");
	}

	/**
	 * Registrace nového uživatele v systému
	 *
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param ...
	 * @return ok nebo error
	 * @throws CustomException 
	 */
	@POST
	@Path("/registerUser")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Registrace", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIGetInfoResponse registerUser(@Context HttpServletRequest request, @FormParam("username") String username,
			@FormParam("password") String password) throws CustomException {
		boolean novaRegistrace = false;
		String currentUser = getCurrentUser(request);
		User u;

		if (currentUser == null) {
			if (!checkUsername(username)) {
				throw new CustomException("Spatne username, nelze pouzit");
			}
			u = new User();
			u.setUsername(username);
			u.setRegisterDate(new Date());
			novaRegistrace = true;
		} else {
			if (!currentUser.equals(username)) {
				throw new CustomException("Uzivatelske jmeno nelze zmenit");
			}
			u = ofy().load().type(User.class).id(currentUser).now();
		}

		if (password.length() > 0) {
			u.setPassword(encryptPassword(password));
		}
		ofy().save().entity(u);
		
		if (novaRegistrace) {
			MoneyAccount ac = new MoneyAccount();
			ac.setTitle(username);
			ac.setBalance(0);
			ac.setLastUpdate(new Date());
			ofy().save().entity(ac);
			request.getSession().setAttribute("username", username);
		}

		return getInfo(request);
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Odhlaseni", notes = "", response = APIResponseBase.class)
	public APIResponseBase logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
		APIResponseBase res = new APIResponseBase();
		res.setStatus("ok");
		return res;
	}

	
	@POST
	@Path("/checkUsername")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Kontrola jestli lze username zaregistrovat", notes = "", response = APIResponseBase.class)
	public APICheckUsernameResponse checkUsername(@Context HttpServletRequest request, @FormParam("username") String username) {
		APICheckUsernameResponse res = new APICheckUsernameResponse();

		res.setUsername(username);
		res.setExists(checkUsername(username)?"1":"0");
		
		res.setStatus("ok");
		return res;
	}
	
    /**
     * Vrací inforamci o přihlášeném uživateli
     *
     * @return <li>status</li>
     * <li>username</li>
     * <li>primaryRole</li>
     */
	@POST
	@Path("/getUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Vrací inforamci o přihlášeném uživateli", notes = "", response = APIGetUserInfoResponse.class)
	public APIGetUserInfoResponse getUserInfo(@Context HttpServletRequest request) throws CustomException {
		APIGetUserInfoResponse res = new APIGetUserInfoResponse();

		APIGetInfoResponse info = getInfo(request);
		User u = ofy().load().type(User.class).id(info.getUsername()).now();
		u.setPassword("");
		res.setCredit(info.getCredit());
		res.setGameCredit(info.getGameCredit());
		res.setTicket(u.getTicket());
		res.setUser(u);
		res.setUsername(info.getUsername());		
		return res;
	}
	

	/**
	 * Kontroluje, jestli je mozne zaregistrovat uzivatele s takovym username
	 * @param username
	 * @return true - lze pouzit, false - nelze pouzit
	 * 
	 */
	private boolean checkUsername(String username) {
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

	protected String createTicket() {
		return UUID.randomUUID().toString();
	}

	public String getCurrentUser(HttpServletRequest request) {
		String currentUser = (String) request.getSession().getAttribute("username");
		return currentUser;
/*		
		if (currentUser == null) {
			return null;
		}
		return ofy().load().type(User.class).id(currentUser).now();
*/
	}

	public String checkCurrentUser(HttpServletRequest request) throws CustomException {
		String currentUser = getCurrentUser(request);
		if (currentUser == null) {
			throw new CustomException("Neprihlasen");
		}
		return currentUser;
	}

}
