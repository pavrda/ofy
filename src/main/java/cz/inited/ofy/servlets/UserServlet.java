package cz.inited.ofy.servlets;

import java.util.Date;

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
@Api(tags = { "Users" })
@Produces(MediaType.APPLICATION_JSON)
public class UserServlet {

	private MoneyController moneyController;
	private UserController userController;
	private CacheController cacheController;
	
	public UserServlet() {
		moneyController = MoneyController.getInstance();
		userController = UserController.getInstance();
		cacheController = CacheController.getInstance();
	}
	

	/**
	 * Vraci akualni informace: jackpot, muj kredit, sazky...
	 * Vola se v pravidelnych intervalech a vraci informace, ktere se mohou na backendu zmenit cizim zavinenim - zmena kreditu, obdrzeni vyzvy, ...
	 * 
	 * @param request
	 * @return
	 * @throws CustomException
	 */
	@GET
	@Path("/getInfo")
	@ApiOperation(value = "Vraci akualni informace: jackpot, muj kredit, sazky...", notes = "Vola se v pravidelnych intervalech a vraci informace, ktere se mohou na backendu zmenit cizim zavinenim - zmena kreditu, obdrzeni vyzvy, ...", response = APIGetInfoResponse.class)
	public APIGetInfoResponse getInfo(@Context HttpServletRequest request) throws CustomException {
		String currentUser = getCurrentUser(request);
		return getInfo(currentUser);
	}

	/**
	 * Prihlaseni uzivatele pres jmeno a heslo
	 * 
	 * @param request
	 * @param sUsername
	 * @param sPassword
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/login")
	@ApiOperation(value = "Login", notes = "Prihlaseni uzivatele pres jmeno a heslo. Na oplatku uzivatel dostane ticket ve forme cookie. Ten musi posilat pri volani dalsich requestu", response = APIGetInfoResponse.class)
	public Response login(
			@Context HttpServletRequest request,
			@FormParam("username") String sUsername,
			@FormParam("password") String sPassword
		) throws CustomException {

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
	@ApiOperation(value = "Registrace", notes = "Nova registrace nebo aktualizace. Registrace provede i login uzivatele", response = APIGetInfoResponse.class)
	public Response registerUser(@Context HttpServletRequest request,
			@FormParam("username") String username,
			@FormParam("password") String password,

			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("dateOfBirth") Date dateOfBirth,
			@FormParam("email") String email,
			@FormParam("phone") String phone,
			@FormParam("friendPhone") String friendPhone,

			@FormParam("street") String street,
			@FormParam("houseNr") String houseNr,
			@FormParam("city") String city,
			@FormParam("postcode") String postcode,
			@FormParam("county") String county,
			@FormParam("country") String country) throws CustomException {
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
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setDateOfBirth(dateOfBirth);
		u.setEmail(email);
		u.setPhone(phone);
		u.setFriendPhone(friendPhone);

		u.setStreet(street);
		u.setHouseNr(houseNr);
		u.setCity(city);
		u.setPostcode(postcode);
		u.setCounty(county);
		u.setCountry(country);

		userController.validateUser(u);
		userController.saveUser(u);

		if (novaRegistrace) {
			moneyController.createMoneyAccount(username);
		}

		cacheController.put("ticket-" + u.getTicket(), username);
		NewCookie nc = new NewCookie("ticket", u.getTicket());
		APIGetUserInfoResponse res = getUserInfo(u);
		return Response.status(200).entity(res).type("application/json").cookie(nc).build();
	}
	
	/**
	 * Odhlaseni uzivatele
	 * Posle prazdne cookie, smaze ho
	 * @param request
	 * @return
	 */
	@GET
	@Path("/logout")
	@ApiOperation(value = "Odhlaseni", notes = "Posle prazdne cookie, smaze ho", response = APIResponseBase.class)
	public Response logout(@Context HttpServletRequest request) {
		String ticket = getCookie(request, "ticket");
		if ((ticket != null) && (!"".equals(ticket))) {
			System.out.println("ticket:" + ticket);
			userController.logoutTicket(ticket);
			cacheController.delete("ticket-" + ticket);
		}

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

	/**
	 * Kontrola jestli lze username zaregistrovat
	 * Kontroluje:
	 * 		1. jestli obsahuje spravne znaky,
	 * 		2. jestli username uz nekdo nepouziva
	 * 
	 * @param request
	 * @param username
	 * @return
	 */
	@POST
	@Path("/checkUsername")
	@ApiOperation(value = "Kontrola jestli lze username zaregistrovat", notes = "Kontroluje: 1. jestli obsahuje spravne znaky, 2. jestli username uz nekdo nepouziva", response = APICheckUsernameResponse.class)
	public APICheckUsernameResponse checkUsername(@Context HttpServletRequest request, @FormParam("username") String username) {
		APICheckUsernameResponse res = new APICheckUsernameResponse();

		res.setUsername(username);
		res.setExists(userController.checkUsername(username)?"0":"1");
		
		res.setStatus("ok");
		return res;
	}
	
	/**
	 * Vrací inforamci o přihlášeném uživateli
	 * Do formulare Muj profil
	 * 
	 * @param request
	 * @return
	 * @throws CustomException
	 */
	@GET
	@Path("/getUserInfo")
	@ApiOperation(value = "Vrací inforamci o přihlášeném uživateli", notes = "Do formulare Muj profil", response = APIGetUserInfoResponse.class)
	public APIGetUserInfoResponse getUserInfo(@Context HttpServletRequest request) throws CustomException {

		User u = userController.loadUser(checkCurrentUser(request));
		return getUserInfo(u);
	}
	

	/**
	 * Vytahne cookie z HTTP requestu
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
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
	
	/**
	 * Vrati username aktualne prihlaseneho uzivatele
	 * Kdy nejsem prihlasen, vraci NULL
	 * 
	 * @param request
	 * @return
	 */
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
			// Nemelo by se stat. Zapisu chybu a pokracuji jako neprihlaseny
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Vrati username aktualne prihlaseneho uzivatele
	 * Kdy nejsem prihlasen, vraci exception
	 * 
	 * @param request
	 * @return
	 */
	public String checkCurrentUser(HttpServletRequest request) throws CustomException {
		String currentUser = getCurrentUser(request);
		if (currentUser == null) {
			throw new CustomException("Neprihlasen");
		}
		return currentUser;
	}

	/**
	 * Naplni strukturu APIGetUSerInfoResponse pro zadaneho uzivatele
	 * @param u
	 * @return
	 * @throws CustomException
	 */
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

	/**
	 * Naplni strukturu APIGetInfoResponse pro zadaneho uzivatele
	 * @param username
	 * @return
	 * @throws CustomException
	 */
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
