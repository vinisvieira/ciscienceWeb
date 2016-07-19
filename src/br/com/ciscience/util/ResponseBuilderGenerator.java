package br.com.ciscience.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

public class ResponseBuilderGenerator {

	/**
	 * Este método retorna um {@link ResponseBuilder} de sucesso do tipo
	 * APPLICATION_JSON
	 * 
	 * @param responseBuilder
	 * @param jsonElement
	 * @return {@link ResponseBuilder}
	 */
	public static ResponseBuilder createOKResponseJSON(ResponseBuilder responseBuilder, String jsonElement) {
		responseBuilder = responseBuilder.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN,
				Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
		responseBuilder = responseBuilder.type(MediaType.APPLICATION_JSON);
		responseBuilder = responseBuilder.status(Status.OK);
		responseBuilder = responseBuilder.entity(jsonElement);
		return responseBuilder;
	}

	/**
	 * Este método retorna um {@link ResponseBuilder} de sucesso do tipo
	 * APPLICATION_JSON
	 * 
	 * @param responseBuilder
	 * @return {@link ResponseBuilder}
	 */
	public static ResponseBuilder createOKResponseTextPlain(ResponseBuilder responseBuilder) {
		responseBuilder = responseBuilder.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN,
				Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
		responseBuilder = responseBuilder.type(MediaType.TEXT_PLAIN);
		responseBuilder = responseBuilder.status(Status.OK);
		return responseBuilder;
	}

	/**
	 * Este método retorna um {@link ResponseBuilder} de erro do tipo TEXT_PLAIN
	 * 
	 * @param responseBuilder
	 * @return {@link ResponseBuilder}
	 */
	public static ResponseBuilder createErrorResponse(ResponseBuilder responseBuilder) {
		responseBuilder = responseBuilder.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN,
				Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
		responseBuilder = responseBuilder.type(MediaType.TEXT_PLAIN);
		responseBuilder = responseBuilder.status(Status.INTERNAL_SERVER_ERROR);
		return responseBuilder;
	}
	
	/**
	 * Este método retorna um {@link ResponseBuilder} de unauthorized do tipo TEXT_PLAIN
	 * 
	 * @param responseBuilder
	 * @return {@link ResponseBuilder}
	 */
	public static ResponseBuilder createUnauthorizedResponse(ResponseBuilder responseBuilder) {
		responseBuilder = responseBuilder.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN,
				Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
		responseBuilder = responseBuilder.type(MediaType.TEXT_PLAIN);
		responseBuilder = responseBuilder.status(Status.UNAUTHORIZED);
		return responseBuilder;
	}

	public static ResponseBuilder createErrorResponseJSON(ResponseBuilder responseBuilder, String jsonElement) {
		responseBuilder = responseBuilder.header(Constants.ACCESS_CONTROL_ALLOW_ORIGIN,
				Constants.ACCESS_CONTROL_ALLOW_ORIGIN_EXTRA);
		responseBuilder = responseBuilder.type(MediaType.APPLICATION_JSON);
		responseBuilder = responseBuilder.status(Status.INTERNAL_SERVER_ERROR);
		responseBuilder = responseBuilder.entity(jsonElement);
		return responseBuilder;
	}

}
