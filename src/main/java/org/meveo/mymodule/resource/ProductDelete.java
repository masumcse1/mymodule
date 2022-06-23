package org.meveo.mymodule.resource;

import java.util.HashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.meveo.admin.exception.BusinessException;
import org.meveo.mymodule.dto.CustomEndpointResource;
import org.meveo.script.DeleteMyProduct;

@Path("myproduct")
@RequestScoped
public class ProductDelete extends CustomEndpointResource {

	@Inject
	private DeleteMyProduct myProduct;

	@DELETE
	@Path("/{uuids}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("uuids") String productid) throws ServletException {

		parameterMap = new HashMap<String, Object>();
		parameterMap.put("productid", productid);
		setRequestResponse();
		String result = null;
		try {
			myProduct.setProductId(productid);
			myProduct.init(parameterMap);
			myProduct.execute(parameterMap);
			myProduct.finalize(parameterMap);
			result = myProduct.getResult();
		} catch (BusinessException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
		}
		
		return Response.status(Response.Status.OK).entity(result).build();

	}

}