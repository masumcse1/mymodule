package org.meveo.mymodule.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.meveo.admin.exception.BusinessException;
import org.meveo.api.rest.technicalservice.EndpointExecutionFactory;
import org.meveo.model.persistence.JacksonUtil;
import org.meveo.model.technicalservice.endpoint.EndpointHttpMethod;
import org.meveo.script.CreateMyProduct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import util.CustomEndpointResource;
import util.Mydatasmappper;

/**
 * Sample JAX-RS resources.
 *
 */
@Path("myproduct")
@RequestScoped
public class ProductCreate extends CustomEndpointResource {

	@Inject
	private CreateMyProduct myProduct;

	@Inject
	private EndpointExecutionFactory endpointExecutionFactory;

	@Context
	private HttpServletRequest req;

	@Context
	private HttpServletResponse res;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveProduct(Mydatasmappper mydatasmappper) throws ServletException, IOException {
		Map<String, Object> parameters = new HashMap<>();
		String requestBody = new Gson().toJson(mydatasmappper);
		parameters.put("REQUEST_BODY", requestBody);
		parameters = JacksonUtil.fromString(requestBody, new TypeReference<Map<String, Object>>() {
		});

		execution = endpointExecutionFactory.getExecutionBuilder(req, res).setParameters(parameters)
				.setMethod(EndpointHttpMethod.POST).createEndpointExecution();
		setRequestResponse();
		Status status = null;
		try {
			myProduct.setProduct(mydatasmappper.getProduct());
			myProduct.init(parameterMap);
			myProduct.execute(parameterMap);
			myProduct.finalize(parameterMap);
			String result = myProduct.getResult();
			System.out.println("ProductCreate restult:  : " + result);
			status = Status.valueOf(result);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return Response.status(status).type(MediaType.APPLICATION_JSON).build();

	}

}