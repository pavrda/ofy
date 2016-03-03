package cz.inited.ofy.servlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cz.inited.ofy.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/adminuser")
@Api(tags = {"adminuser"})
public class AdminUserServlet {

	@GET
	@Path("/{username}")
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
	@Path("/{username}")
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
	@Path("/{username}/hry")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find user by username", 
    	notes = "Returns a full user structure",
    	response = User.class)
	public User getUserHry(@ApiParam(value = "Username", required = true) @PathParam("username") String username) {
		User u = new User();
		u.setUsername(username);
		u.setFirstName("HRY");
		u.setLastName("Pavrda");
		return u;
	}
	
	
}
