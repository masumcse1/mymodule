package org.meveo.mymodule.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.meveo.admin.exception.BusinessException;
import org.meveo.script.CreateMyProduct;

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



	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveProduct(Mydatasmappper mydatasmappper) throws ServletException, IOException {
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Status status = null;
		parameterMap.put("product", mydatasmappper.getProduct());
		parameterMap.put("delayUnit", "SECONDS");
		parameterMap.put("type", mydatasmappper.getType());

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