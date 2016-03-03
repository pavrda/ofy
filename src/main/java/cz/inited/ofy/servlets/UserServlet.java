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

import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/user")
@Api(tags = { "user" })
public class UserServlet {
	
	
	static {
	    ObjectifyService.register( User.class );
	}

	@GET
	@Path("/getInfo")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Vraci jackpot, muj kredit, ...", 
    	notes = "Vola se v pravidelnych intervalech",
    	response = APIGetInfoResponse.class)
	public APIGetInfoResponse getInfo(@Context HttpServletRequest req) {
		APIGetInfoResponse res = new APIGetInfoResponse();
		res.setJackpot(12000);
		System.out.println(req.getSession().getAttribute("jara"));
		req.getSession().setAttribute("jara", "ahoj");
		return res;
	}

	
	
	/**
	 * Přihlásí uživatele
	 *
	 * @param username
	 * @param password
	 * @return
	 * 		<li>status - ok / error</li>
	 *         <li>code - kód v případě chyby. "bad password" - pokud nelze
	 *         přihlásit</li>
	 *         <li>msg - zpráva pro uživatele</li>
	 *         <li>... - informace o uživateli</li>
	 * @throws Exception 
	 *
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Login", 
    	notes = "Pres heslo nebo pres ticket",
    	response = APIGetInfoResponse.class)
	public APIGetInfoResponse login(
		@Context HttpServletRequest request,
		@FormParam("username") String sUsername,
		@FormParam("password") String sPassword,
		@FormParam("ticket") String sTicket
	) throws Exception {

		if ((sPassword != null) && (sTicket == null)) {
			User u = ofy().load().type(User.class).id(sUsername).now();
			if (!u.getPassword().equals(encryptPassword(sPassword))) {
				throw new Exception("Spatne jmeno heslo");
			}

			u.setLastLogged(new Date());
			u.setTicket(createTicket());
			ofy().save().entity(u);
			request.getSession().setAttribute("username", sUsername);

			return getInfo(request);
		}

		if ((sPassword == null) && (sTicket != null)) {
			User u = ofy().load().type(User.class).id(sUsername).now();
			if (!u.getTicket().equals(encryptPassword(sTicket))) {
				throw new Exception("Spatny ticket");
			}
			request.getSession().setAttribute("username", sUsername);

			return getInfo(request);
		}

		throw new Exception("Spatne jmeno heslo");
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
     */
	@POST
	@Path("/registerUser")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Registrace", 
    	notes = "Pres heslo nebo pres ticket",
    	response = APIGetInfoResponse.class)
    public APIGetInfoResponse registerUser(
    	@Context HttpServletRequest request,
		@FormParam("username") String username,
		@FormParam("password") String password    	
    ) {
        User u = getCurrentUser(request);
        if (u == null) {
            u = new User();
            u.setUsername(username);
        }
        
        u.setPassword(encryptPassword(password));

        ofy().save().entity(u);

/*
        MoneyAccount ac = new MoneyAccount(username);
        ss.persist(ac);

        request.getSession().setAttribute("username", u.getUsername());
        res.put("status", "ok");
        return res;
*/
        return getInfo(request);
    }
	
	
	
    public static String encryptPassword(String password) {
        //TODO: v pristi verzi nahradit silnejsi sifrou
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
	
	public User getCurrentUser(HttpServletRequest request) {
		String currentUser = (String) request.getSession().getAttribute("username");
		if (currentUser == null) {
			return null;
		}
		return ofy().load().type(User.class).id(currentUser).now();
	}
	
	public User checkCurrentUser(HttpServletRequest request) throws Exception {
		User u = getCurrentUser(request);
		if (u == null) {
			throw new Exception("Neprihlasen");
		}
		return u;
	}
	
}
