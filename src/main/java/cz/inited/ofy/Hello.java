package cz.inited.ofy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"pet"})
@Path("/jerseyws")
public class Hello {

    @GET
    @Path("/test")    
    public String testMethod() {
        return "this is a test";
    }
    
    
    @GET
    @Path("/blesk")
    @ApiOperation(value = "Find pet by ID", 
    notes = "Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
    response = String.class)
    public String blesk() {
        return "this is a blesk";
    }
	
}
