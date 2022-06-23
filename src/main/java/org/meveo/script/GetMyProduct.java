package org.meveo.script;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.meveo.admin.exception.BusinessException;
import org.meveo.api.exception.EntityDoesNotExistsException;
import org.meveo.api.persistence.CrossStorageApi;
import org.meveo.model.customEntities.Product;
import org.meveo.model.storage.Repository;
import org.meveo.service.script.Script;
import org.meveo.service.storage.RepositoryService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class GetMyProduct extends Script {

	private static final Logger LOG = LoggerFactory.getLogger(GetMyProduct.class);

	@Inject
	private CrossStorageApi crossStorageApi;

	@Inject
	private RepositoryService repositoryService;

	private String uuid;

	private String result;

	public String getResult() {
		return this.result;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public void execute(Map<String, Object> parameters) throws BusinessException {
		uuid = (String) parameters.get("uuid");
		LOG.info("Get Product: {}----------", uuid);
		Repository defaultRepo = repositoryService.findDefaultRepository();
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Product product = crossStorageApi.find(defaultRepo, uuid, Product.class);
			resultMap.put("status", "success");
			resultMap.put("result", product);
		} catch (EntityDoesNotExistsException e) {
			String errorMessage = String.format("uuid : %s does not exist.", uuid);
			LOG.error(errorMessage, e);
			resultMap.put("status", "fail");
			resultMap.put("result", errorMessage);
		}
		result = new Gson().toJson(resultMap);
		super.execute(parameters);
	}
}
