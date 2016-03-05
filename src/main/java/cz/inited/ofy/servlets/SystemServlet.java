package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.googlecode.objectify.ObjectifyService;

import cz.inited.ofy.controllers.MoneyController;
import cz.inited.ofy.models.APIGetInfoResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.models.MoneyAccount;
import cz.inited.ofy.models.User;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/system")
@Api(tags = {"system"})
public class SystemServlet {

	private MoneyController moneyController;

	// zaregistruji si vsechny entity, ktere budu pouzivat
	static {
		ObjectifyService.register(User.class);
		ObjectifyService.register(MoneyAccount.class);
	}

	public SystemServlet() {
		moneyController = MoneyController.getInstance();
	}

	
	@GET
	@Path("/init")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Inicializace", notes = "Zalozi ucty, nastavi default parametry")
	public APIResponseBase init() throws CustomException {
		APIResponseBase res = new APIResponseBase();
		moneyController.createMoneyAccount("_game");
		moneyController.createMoneyAccount("_credit");
		moneyController.createMoneyAccount("_fee");
		
		return res;
	}

}
