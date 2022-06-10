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

import org.meveo.admin.exception.BusinessException;
import org.meveo.api.rest.technicalservice.EndpointExecution;
import org.meveo.api.rest.technicalservice.EndpointExecutionFactory;
import org.meveo.model.technicalservice.endpoint.Endpoint;
import org.meveo.model.technicalservice.endpoint.EndpointHttpMethod;
import org.meveo.model.technicalservice.endpoint.EndpointPathParameter;
import org.meveo.model.technicalservice.endpoint.EndpointVariables;
import org.meveo.script.DeleteMyProduct;


/**
 * Sample JAX-RS resources.
 *
 */
@Path("myproduct")
@RequestScoped
public class ProductDelete {
  
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
	public String getMessage(@PathParam("uuids") String productid) throws ServletException {

		System.out.println("----------111-->>"+req.getPathInfo());
		
		final EndpointExecution execution = endpointExecutionFactory.getExecutionBuilder(req, res)
				.setParameters(new HashMap<>(req.getParameterMap())).setMethod(EndpointHttpMethod.GET)
				.createEndpointExecution();

		Map<String, Object> parameterMap = new HashMap<>(execution.getParameters());
		System.out.println("----------222-->>");
		//////////////////////////////////////////////////////////////////// final
		Endpoint endpoint = execution.getEndpoint();

		Matcher matcher = endpoint.getPathRegex().matcher(execution.getPathInfo());
		matcher.find();
		for (EndpointPathParameter pathParameter : endpoint.getPathParametersNullSafe()) {
			try {
				String val = matcher.group(pathParameter.toString());
				parameterMap.put(pathParameter.toString(), val);
				System.out.println("------------>>"+val);
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"cannot find param " + pathParameter + " in " + execution.getPathInfo());
			}
		}
		System.out.println("----------333-->>");

		// Set budget variables
		parameterMap.put(EndpointVariables.MAX_BUDGET, execution.getBudgetMax());
		parameterMap.put(EndpointVariables.BUDGET_UNIT, execution.getBudgetUnit());
		parameterMap.put(EndpointVariables.MAX_DELAY, execution.getDelayMax());
		parameterMap.put(EndpointVariables.DELAY_UNIT, execution.getDelayUnit());
		parameterMap.put("request", execution.getRequest());

		if (endpoint.isSynchronous()) {
			parameterMap.put("response", execution.getResponse());

		}

		/////////////////////////////////////////////////////////////////
		System.out.println("----------444-->>"+req.getPathInfo());
		try {
			myProduct.setProductId(productid);
			myProduct.init(parameterMap);
			myProduct.execute(parameterMap);
			myProduct.finalize(parameterMap);
			String result1=myProduct.getResult();
			System.out.println("#######ProductDelete result####"+result1);
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		System.out.println("----------555-->>"+req.getPathInfo());
		return "Hello, world";

	}

}



//http://localhost:8080/mymodule/api/myproduct