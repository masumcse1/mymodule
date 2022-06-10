package org.meveo.script;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import com.google.gson.Gson;
import org.meveo.admin.exception.BusinessException;
import org.meveo.api.exception.EntityDoesNotExistsException;
import org.meveo.api.persistence.CrossStorageApi;
import org.meveo.model.customEntities.Product;
import org.meveo.model.storage.Repository;
import org.meveo.service.script.Script;
import org.meveo.service.storage.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateMyProduct extends Script {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateMyProduct.class);

    @Inject
    private CrossStorageApi crossStorageApi;

    @Inject
    private RepositoryService repositoryService;

    private String productId;

    private Product product;

    private String result;

    public String getResult() {
        return this.result;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void execute(Map<String, Object> parameters) throws BusinessException {
        LOG.info("Update Product: {}, {}", productId, product);
        Repository defaultRepo = repositoryService.findDefaultRepository();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            crossStorageApi.find(defaultRepo, productId, Product.class);
        } catch (EntityDoesNotExistsException e) {
            String errorMessage = String.format("Product with id: %s does not exist.", productId);
            LOG.error(errorMessage, e);
            resultMap.put("status", "fail");
            resultMap.put("result", errorMessage);
        }
        // make sure updated cart uses specified cartId
        product.setUuid(productId);
        try {
            crossStorageApi.createOrUpdate(defaultRepo, product);
        } catch (Exception e) {
            String errorMessage = String.format("Failed to update product: %s", productId);
            LOG.error(errorMessage, e);
            resultMap.put("status", "fail");
            resultMap.put("result", errorMessage);
        }
        try {
            Product updatedProduct = crossStorageApi.find(defaultRepo, productId, Product.class);
            resultMap.put("status", "success");
            resultMap.put("result", updatedProduct);
        } catch (EntityDoesNotExistsException e) {
            String errorMessage = "Failed to retrieve updated cart.";
            LOG.error(errorMessage, e);
            resultMap.put("status", "fail");
            resultMap.put("result", errorMessage);
        }
        result = new Gson().toJson(resultMap);
        super.execute(parameters);
    }
}
