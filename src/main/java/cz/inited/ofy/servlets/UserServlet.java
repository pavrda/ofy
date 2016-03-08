package cz.inited.ofy.servlets;

import java.util.Date;

import javax.cache.CacheManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import cz.inited.ofy.controllers.CacheController;
import cz.inited.ofy.controllers.MoneyController;
import cz.inited.ofy.controllers.UserController;
import cz.inited.ofy.models.APICheckUsernameResponse;
import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.APIGetUserInfoResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.models.User;
import cz.inited.ofy.utils.Cacheable;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/user")
@Api(tags = { "user" })
public class UserServlet {

	private MoneyController moneyController;
	private UserController userController;
	private CacheController cacheController;
	
	public UserServlet() {
		moneyController = MoneyController.getInstance();
		userController = UserController.getInstance();
		cacheController = CacheController.getInstance();
	}
	

	@GET
	@Path("/getInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Vraci jackpot, muj kredit, ...", notes = "Vola se v pravidelnych intervalech", response = APIGetInfoResponse.class)
	public APIGetInfoResponse getInfo(@Context HttpServletRequest request) throws CustomException {
		String currentUser = getCurrentUser(request);
		return getInfo(currentUser);
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
	public Response login(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword) throws CustomException {

		User u = userController.loginPassword(sUsername, sPassword);
		cacheController.put("ticket-" + u.getTicket(), sUsername);
		NewCookie nc = new NewCookie("ticket", u.getTicket());
		APIGetUserInfoResponse res = getUserInfo(u);
		return Response
			.status(200)
			.entity(res)
			.type("application/json")
			.cookie(nc)
			.build(); 
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
	public Response registerUser(@Context HttpServletRequest request, @FormParam("username") String username,
			@FormParam("password") String password) throws CustomException {
		boolean novaRegistrace = false;
		String currentUser = getCurrentUser(request);
		User u;

		if (currentUser == null) {
			if (!userController.checkUsername(username)) {
				throw new CustomException("Spatne username, nelze pouzit");
			}
			u = new User();
			u.setUsername(username);
			u.setRegisterDate(new Date());
			u.setTicket(userController.createTicket());
			novaRegistrace = true;
		} else {
			if (!currentUser.equals(username)) {
				throw new CustomException("Uzivatelske jmeno nelze zmenit");
			}
			u = userController.loadUser(currentUser);
		}

		if (password.length() > 0) {
			userController.setUserPassword(u, password);
		}
		userController.saveUser(u);
		
		if (novaRegistrace) {
			moneyController.createMoneyAccount(username);
		}

		cacheController.put("ticket-" + u.getTicket(), username);
		NewCookie nc = new NewCookie("ticket", u.getTicket());
		APIGetUserInfoResponse res = getUserInfo(u);
		return Response
			.status(200)
			.entity(res)
			.type("application/json")
			.cookie(nc)
			.build();
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Odhlaseni", notes = "", response = APIResponseBase.class)
	public Response logout(@Context HttpServletRequest request) {
		APIResponseBase res = new APIResponseBase();
		res.setStatus("ok");
		NewCookie nc = new NewCookie(new javax.ws.rs.core.Cookie("ticket", ""), "", 0, new Date(0), false, false);
		
		return Response
				.status(200)
				.entity(res)
				.type("application/json")
				.cookie(nc)
				.build();
	}

	
	@POST
	@Path("/checkUsername")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Kontrola jestli lze username zaregistrovat", notes = "", response = APIResponseBase.class)
	public APICheckUsernameResponse checkUsername(@Context HttpServletRequest request, @FormParam("username") String username) {
		APICheckUsernameResponse res = new APICheckUsernameResponse();

		res.setUsername(username);
		res.setExists(userController.checkUsername(username)?"1":"0");
		
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
	@GET
	@Path("/getUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Vrací inforamci o přihlášeném uživateli", notes = "", response = APIGetUserInfoResponse.class)
	public APIGetUserInfoResponse getUserInfo(@Context HttpServletRequest request) throws CustomException {

		User u = userController.loadUser(checkCurrentUser(request));
		return getUserInfo(u);
	}
	

	private String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i].getValue();
				}
			}
		}
		return null;
	}
	
	public String getCurrentUser(HttpServletRequest request) {
		final String ticket = getCookie(request, "ticket");
		if (ticket == null) {
			return null;
		}
		try {
			return cacheController.get("ticket-" + ticket, new Cacheable<String>() {

				@Override
				public String call() throws CustomException {
					User u = userController.findUserByTicket(ticket);
					return (u == null)?null:u.getUsername();
				}
			});
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String checkCurrentUser(HttpServletRequest request) throws CustomException {
		String currentUser = getCurrentUser(request);
		if (currentUser == null) {
			throw new CustomException("Neprihlasen");
		}
		return currentUser;
	}

	private APIGetUserInfoResponse getUserInfo(User u) throws CustomException {
		APIGetUserInfoResponse res = new APIGetUserInfoResponse();
		APIGetInfoResponse info = getInfo(u.getUsername());
		u.setPassword("");
		res.setCredit(info.getCredit());
		res.setGameCredit(info.getGameCredit());
		res.setTicket(u.getTicket());
		res.setUser(u);
		res.setUsername(info.getUsername());		
		return res;
	}

	private APIGetInfoResponse getInfo(String username) throws CustomException {
		APIGetInfoResponse res = new APIGetInfoResponse();

		res.setGameCredit(
			cacheController.get("gameCredit", new Cacheable<Long>() {
				@Override
				public Long call() throws CustomException {
					return moneyController.getBalance("_game");
				}
			}));
				
		
		if (username != null) {
			res.setCredit(moneyController.getBalance(username));
		} else {
			res.setCredit(0L);
		}
		res.setUsername(username);
		return res;		
	}
	
}
