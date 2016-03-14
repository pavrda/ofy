package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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

@Path("/money")
@Api(tags = { "Money" })
public class MoneyServlet {

	/**
	 * Zobrazi moje financni operace
	 * 
	 * @param request
	 * @param filter
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/transactionList")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Zobrazi moje financni operace", notes = "Zobrazi moje financni operace", response = APIBetListResponse.class)
	public APIBetListResponse transactionList(
			@Context HttpServletRequest request,
			@FormParam("filter") String filter
		) throws CustomException {
		return null;
	}

	/**
	 * Zobrazi celkovy stav mych penez
	 * 
	 * @param request
	 * @param betIds
	 * @return
	 * @throws CustomException
	 */
	@GET
	@Path("/moneySummary")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Zobrazi celkovy stav mych penez", notes = "", response = APIResponseBase.class)
	public APIResponseBase moneySummary(
			@Context HttpServletRequest request
		) throws CustomException {
		return null;
	}

	@POST
	@Path("/paypalLink")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Vlozeni penez pres PayPal", notes = "Vytvori link pro redirect na PayPal", response = APIResponseBase.class)
	public APIResponseBase paypalLink(
			@Context HttpServletRequest request,
			@FormParam("amount") long amount
		) throws CustomException {
		return null;
	}

	@POST
	@Path("/paypalWithdraw")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Prevod penez z hry na PayPal", notes = "Prevede penize z hry na PayPal", response = APIResponseBase.class)
	public APIResponseBase paypalWithdraw(
			@Context HttpServletRequest request,
			@FormParam("amount") long amount
		) throws CustomException {
		return null;
	}
	
}
