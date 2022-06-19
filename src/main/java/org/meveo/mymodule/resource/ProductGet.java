package org.meveo.mymodule.resource;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.meveo.admin.exception.BusinessException;
import org.meveo.model.technicalservice.endpoint.EndpointHttpMethod;
import org.meveo.script.GetMyProduct;

import util.CustomEndpointResource;


@Path("myproduct")
@RequestScoped
public class ProductGet extends CustomEndpointResource {

	@Inject
	private GetMyProduct myProduct;

	
	@GET
	@Path("/{uuids}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("uuids") String productid) throws ServletException {
		 Map<String, Object> parameterMap = new HashMap<String, Object>();
		
		 parameterMap.put("productid", productid);
		 parameterMap.put("delayUnit", "SECONDS");
		Status status = null;
		try {
			myProduct.init(parameterMap);
			myProduct.execute(parameterMap);
			myProduct.finalize(parameterMap);
			String result= myProduct.getResult();
			System.out.println("#######ProductGet result####" + result);
			status = Status.valueOf(result);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return Response.ok(status, MediaType.APPLICATION_JSON).build();
		//return Response.status(status).type(MediaType.APPLICATION_JSON).build();

	}

}

//http://localhost:8080/mymodule/api/myproduct