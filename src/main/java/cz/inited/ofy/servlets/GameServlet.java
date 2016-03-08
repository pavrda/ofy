package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.models.User;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/game")
@Api(tags = {"game"})
public class GameServlet {

    /**
     * Hrac zacal hrat. Rozdel prachy a zaznamenej hru
     *
     * @param agreement1 datum a cas souhlasu. Format: 2014-06-30 15:43:12
     * @param agreement2 datum a cas souhlasu. Format: 2014-06-30 15:43:12
     * @param agreement3 datum a cas souhlasu. Format: 2014-06-30 15:43:12
     * @return <li>status</li>
     */
	@POST
	@Path("/gameStart")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIResponseBase gameStart(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword, @FormParam("ticket") String sTicket) throws CustomException {
		return null;
	}

    /**
     * Dohral jsem
     *
     * @return <li>status</li>
     */
	@POST
	@Path("/gameStop")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIResponseBase gameStop(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword, @FormParam("ticket") String sTicket) throws CustomException {
		return null;
	}

    /**
     * Seznam mojich her
     *
     * @return <li>status</li>
     */
	@POST
	@Path("/gameList")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Login", notes = "Pres heslo nebo pres ticket", response = APIGetInfoResponse.class)
	public APIResponseBase gameList(@Context HttpServletRequest request, @FormParam("username") String sUsername,
			@FormParam("password") String sPassword, @FormParam("ticket") String sTicket) throws CustomException {
		return null;
	}
	
	
}
