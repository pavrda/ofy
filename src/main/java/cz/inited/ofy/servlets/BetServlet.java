package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import cz.inited.ofy.models.APIBetListResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/bet")
@Api(tags = { "Bets" })
public class BetServlet {

	/**
	 * Zrusi sazku
	 * 
	 * @param request
	 * @param betId
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/betCancel")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Zrusi sazku", notes = "Rusi moji sazku nebo odmita vyzvu", response = APIResponseBase.class)
	public APIResponseBase betCancel(
			@Context HttpServletRequest request,
			@FormParam("betId") String betId
		) throws CustomException {
		return null;
	}

	/**
	 * Zobrazi seznam sazek
	 * 
	 * @param request
	 * @param filter
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/betList")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Zobrazi seznam sazek", notes = "Zobrazi seznam sazek", response = APIBetListResponse.class)
	public APIBetListResponse betList(
			@Context HttpServletRequest request,
			@FormParam("filter") String filter
		) throws CustomException {
		return null;
	}

	/**
	 * Smaze sazky ze seznamu sazek
	 * Oznaci je jako smazane
	 * 
	 * @param request
	 * @param betIds
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/betListDelete")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Smaze sazky ze seznamu sazek", notes = "Takto smazane sazky nebudou videt v aplikaci", response = APIResponseBase.class)
	public APIResponseBase gameListDelete(
			@Context HttpServletRequest request,
			@ApiParam(value="Seznam gameId oddeleny carkou")	@FormParam("gameIds") String gameIds
		) throws CustomException {
		return null;
	}

	@POST
	@Path("/betCheckUser")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Hleda uzivatele pro uzavreni sazky", notes = "Pri zadavani vyzvy hledam protihrace, vola se tato funkce", response = APIResponseBase.class)
	public APIResponseBase betCheckUser(
			@Context HttpServletRequest request,
			@ApiParam(value="Username, mail, telefonni cislo")	@FormParam("username") String username
		) throws CustomException {
		return null;
	}

	
}
