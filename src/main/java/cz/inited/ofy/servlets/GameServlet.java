package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/game")
@Api(tags = {"game"})
public class GameServlet {

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

}
