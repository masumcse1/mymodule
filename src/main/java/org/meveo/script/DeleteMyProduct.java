package org.meveo.script;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.meveo.admin.exception.BusinessException;
import org.meveo.api.persistence.CrossStorageApi;
import org.meveo.model.customEntities.Product;
import org.meveo.model.storage.Repository;
import org.meveo.service.script.Script;
import org.meveo.service.storage.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class DeleteMyProduct extends Script {
	private static final Logger LOG = LoggerFactory.getLogger(DeleteMyProduct.class);

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
		LOG.info("Delete Product: {}", uuid);
		Repository defaultRepo = repositoryService.findDefaultRepository();
		Map<String, Object> resultMap = new HashMap<>();
		try {
			crossStorageApi.remove(defaultRepo, uuid, Product.class);
			resultMap.put("status", "success");
			resultMap.put("result", "Successfully deleted Product: " + uuid);
		} catch (Exception e) {
			String errorMessage = String.format("Failed to delete Product: %s", uuid);
			LOG.error(errorMessage, e);
			resultMap.put("status", "fail");
			resultMap.put("result", errorMessage);
		}
		result = new Gson().toJson(resultMap);
		super.execute(parameters);
	}
}
