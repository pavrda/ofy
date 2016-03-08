package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.ApiOperation;

public class BetServlet {

    /**
     * Zaregistruje novou sazku
     *
     * @param username
     * @return exists - 1 nebo 0, fullName - pokud existuje
     */
	@POST
	@Path("/betRegister")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIResponseBase betRegister(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword, @FormParam("ticket") String sTicket) throws CustomException {
		return null;
	}

	@POST
	@Path("/betList")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIResponseBase betList(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword, @FormParam("ticket") String sTicket) throws CustomException {
		return null;
	}
	

}
