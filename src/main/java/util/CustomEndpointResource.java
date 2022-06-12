package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.meveo.api.rest.technicalservice.EndpointExecution;
import org.meveo.api.rest.technicalservice.EndpointExecutionFactory;
import org.meveo.model.technicalservice.endpoint.Endpoint;
import org.meveo.model.technicalservice.endpoint.EndpointPathParameter;
import org.meveo.model.technicalservice.endpoint.EndpointVariables;

@RequestScoped
public class CustomEndpointResource {
	
	@Inject
	protected EndpointExecutionFactory endpointExecutionFactory;
	
	protected EndpointExecution execution = null;
	protected Map<String, Object> parameterMap = null;
	

	protected void test() {
		parameterMap = new HashMap<>(execution.getParameters());
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
	}
	
}
