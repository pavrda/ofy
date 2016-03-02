package cz.inited.ofy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cz.inited.ofy.model.User;

@Path("/user")
public class UserServlet {

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("username") String username) {
		User u = new User();
		u.setUsername(username);
		u.setFirstName("Jara");
		u.setLastName("Pavrda");
		return u;
	}

}
