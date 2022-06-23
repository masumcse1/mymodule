package org.meveo.mymodule.dto;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import org.meveo.model.technicalservice.endpoint.EndpointVariables;


@RequestScoped
public class CustomEndpointResource {	
	
	@Context 
	HttpHeaders headers;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;
	
	protected Map<String, Object> parameterMap = null;
	

	protected void setRequestResponse() {
		
		MultivaluedMap<String, String> httpHeaders = headers.getRequestHeaders();
		
		parameterMap.put("httpRequest",request);
		parameterMap.put("httpResponse",response);
		parameterMap.put(EndpointVariables.MAX_BUDGET,  httpHeaders.getFirst("maxDelay"));
		parameterMap.put(EndpointVariables.BUDGET_UNIT, httpHeaders.getFirst("budgetUnit"));
		parameterMap.put(EndpointVariables.MAX_DELAY,   httpHeaders.getFirst("maxBudget"));
		parameterMap.put(EndpointVariables.DELAY_UNIT,  httpHeaders.getFirst("delayUnit"));


	}
	
}
