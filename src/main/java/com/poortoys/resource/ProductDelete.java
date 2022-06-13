package com.poortoys.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.meveo.admin.exception.BusinessException;
import org.meveo.api.rest.technicalservice.EndpointExecution;
import org.meveo.api.rest.technicalservice.EndpointExecutionFactory;
import org.meveo.model.technicalservice.endpoint.Endpoint;
import org.meveo.model.technicalservice.endpoint.EndpointHttpMethod;
import org.meveo.model.technicalservice.endpoint.EndpointPathParameter;
import org.meveo.model.technicalservice.endpoint.EndpointVariables;
import org.meveo.script.DeleteMyProduct;

import util.CustomEndpointResource;

/**
 * Sample JAX-RS resources.
 *
 */
@Path("myproduct")
@RequestScoped
public class ProductDelete extends CustomEndpointResource {

	@Inject
	private DeleteMyProduct myProduct;

	@Inject
	private EndpointExecutionFactory endpointExecutionFactory;

	@Context
	private HttpServletRequest req;

	@Context
	private HttpServletResponse res;

	@DELETE
	@Path("/{uuids}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("uuids") String productid) throws ServletException {

	
		execution = endpointExecutionFactory.getExecutionBuilder(req, res)
				.setParameters(new HashMap<>(req.getParameterMap())).setMethod(EndpointHttpMethod.DELETE)
				.createEndpointExecution();

		setRequestResponse();
		Status status = null;
		try {
			myProduct.setProductId(productid);
			myProduct.init(parameterMap);
			myProduct.execute(parameterMap);
			myProduct.finalize(parameterMap);
			String result = myProduct.getResult();
			System.out.println("#######ProductDelete result####" + result);
			status = Status.valueOf(result);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		
		return Response.status(status).type(MediaType.APPLICATION_JSON).build();

	}

}

//http://localhost:8080/mymodule/api/myproduct