package cz.inited.ofy.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import cz.inited.ofy.models.APIResponseBase;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<CustomException> {

	@Override
	public Response toResponse(CustomException exception) {
		APIResponseBase res = new APIResponseBase();
		res.setStatus("error");
		res.setCode(exception.getCode());
		res.setMsg(exception.getMsg());
		return Response
			.status(200)
			.entity(res)
			.type("application/json")
			.build();
	}

}
