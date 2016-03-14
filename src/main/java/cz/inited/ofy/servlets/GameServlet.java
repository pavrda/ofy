package cz.inited.ofy.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import cz.inited.ofy.models.APIGameListResponse;
import cz.inited.ofy.models.APIGameStartResponse;
import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.utils.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/game")
@Api(tags = { "Games" })
public class GameServlet {

	/**
	 * Vola se tesne pred zacatkem hry
	 * 
	 * @param request
	 * @param gameMode
	 *            trial, game, bet1, bet2
	 * @param betId
	 *            ID sazky, kterou timto prijimam
	 * @param betAmount
	 *            o kolik se vsazim
	 * @param betUser
	 *            s kym se sazim
	 * @param localTime
	 *            aktualni cas na klientovi
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/gameStart")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Začátek hry", notes = "Začátek hry, založení sázky, přijetí sázky", response = APIGameStartResponse.class)
	public APIGameStartResponse gameStart(
			@Context HttpServletRequest request,
			@ApiParam(value="trial | game | bet1 | bet2")	@FormParam("gameMode") String gameMode,
			@ApiParam(value="ID sazky, kterou timto prijimam")	@FormParam("betId") String betId,
			@ApiParam(value="o kolik se vsazim")			@FormParam("betAmount") String betAmount,
			@ApiParam(value="s kym se vsazim")				@FormParam("betUser") String betUser,
			@ApiParam(value="aktualni cas na klientovi")	@FormParam("localTime") long localTime
		) throws CustomException {
		return null;
	}

	/**
	 * Konec hry
	 * 
	 * @param request
	 *            HTTP session
	 * @param gameId
	 *            ID hry
	 * @param results
	 *            vysledky, co hrac naklikal
	 * @param resultTime
	 *            vysledny cas hry v milisekundach
	 * @param resultGood
	 *            pocet spravnych odpovedi
	 * @param resultBad
	 *            pocet spatnych odpovedi
	 * @param localTime
	 *            aktualni cas na klientovi
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/gameStop")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Konec hry", notes = "Vola se ihned po ukonceni hry", response = APIResponseBase.class)
	public APIResponseBase gameStop(
			@Context HttpServletRequest request,
			@FormParam("gameId") String gameId,
			@FormParam("results") String results,
			@FormParam("resultTime") int resultTime,
			@FormParam("resultGood") int resultGood,
			@FormParam("resultBad") int resultBad,
			@FormParam("localTime") long localTime
		) throws CustomException {
		return null;
	}

	/**
	 * Zobrazi seznam mojich her
	 * 
	 * @param request
	 * @param filter
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/gameList")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Seznam her", notes = "Zobrazi seznam mojich her", response = APIGameListResponse.class)
	public APIGameListResponse gameList(
			@Context HttpServletRequest request,
			@FormParam("filter") String filter
		) throws CustomException {
		return null;
	}

	/**
	 * Smaze hry ze seznamu her
	 * Oznaci je jako smazane
	 * 
	 * @param request
	 * @param gameIds
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/gameListDelete")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Smaze hry ze seznamu her", notes = "Takto smazane hry nebudou videt v aplikaci", response = APIResponseBase.class)
	public APIResponseBase gameListDelete(
			@Context HttpServletRequest request,
			@ApiParam(value="Seznam gameId oddeleny carkou")	@FormParam("gameIds") String gameIds
		) throws CustomException {
		return null;
	}

}
