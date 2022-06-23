package org.meveo.mymodule.resource;

import java.io.IOException;
import java.util.HashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.meveo.admin.exception.BusinessException;
import org.meveo.mymodule.dto.CustomEndpointResource;
import org.meveo.mymodule.dto.ProductDto;
import org.meveo.script.CreateMyProduct;


@Path("myproduct")
@RequestScoped
public class ProductCreate extends CustomEndpointResource {

	@Inject
	private CreateMyProduct myProduct;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveProduct(ProductDto productDto) throws ServletException, IOException {

		parameterMap = new HashMap<String, Object>();
		parameterMap.put("product", productDto.getProduct());
		parameterMap.put("type", productDto.getType());
		setRequestResponse();
		String result = null;
		try {
			myProduct.setProduct(productDto.getProduct());
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