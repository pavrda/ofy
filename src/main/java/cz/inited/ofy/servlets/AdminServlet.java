package cz.inited.ofy.servlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cz.inited.ofy.models.APIResponseBase;
import cz.inited.ofy.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/admin")
@Api(tags = {"Admin"})
public class AdminServlet {

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find users in system",
    	notes = "",
    	response = User.class)
	public User getUsers() {
		return null;
	}

	@GET
	@Path("/user/{username}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find user by username", 
    	notes = "Returns a full user structure",
    	response = User.class)
	public User getUser(@ApiParam(value = "Username", required = true) @PathParam("username") String username) {
		User u = new User();
		u.setUsername(username);
		u.setFirstName("Jara");
		u.setLastName("Pavrda");
		return u;
	}

	@POST
	@Path("/user/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update user information", 
    	notes = "Returns a full user structure",
    	response = User.class)
	public User getUser(
			@ApiParam(value = "Username", required = true) @PathParam("username") String username,
			@ApiParam(value = "User", required = true) User user			
		) {
		User u = user;
		u.setUsername(username);
		u.setFirstName("Jara");
		u.setLastName("Pavrda");
		return u;
	}

	@GET
	@Path("/user/{username}/games")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Zobrazi hry daneho uzivatele", 
    	notes = "",
    	response = APIResponseBase.class)
	public APIResponseBase getUserGames(@ApiParam(value = "Username", required = true) @PathParam("username") String username) {
		return null;
	}

	@GET
	@Path("/user/{username}/bets")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Zobrazi sazky daneho uzivatele", 
    	notes = "",
    	response = APIResponseBase.class)
	public APIResponseBase getUserBets(@ApiParam(value = "Username", required = true) @PathParam("username") String username) {
		return null;
	}

	@GET
	@Path("/user/{username}/transactions")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Zobrazi financni transakce uzivatele", 
		notes = "",
		response = APIResponseBase.class)
	public APIResponseBase getUserTransactions(@ApiParam(value = "Username", required = true) @PathParam("username") String username) {
		return null;
	}

	@GET
	@Path("/account")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Zobrazi seznam penenich uctu", 
    	notes = "",
    	response = APIResponseBase.class)
	public APIResponseBase getAccounts() {
		return null;
	}

	@GET
	@Path("/account/{title}/transactions")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Zobrazi pohyby na ucte", 
    	notes = "",
    	response = APIResponseBase.class)
	public User getFinTransactions(@ApiParam(value = "Title", required = true) @PathParam("title") String title) {
		return null;
	}
	
	
}
