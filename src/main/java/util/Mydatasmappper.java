package util;

import org.meveo.model.customEntities.Product;

public class Mydatasmappper {
	
	String type;
	Product product;
	
	
	public String getType() {
		return type;
	}
	public Mydatasmappper(String type, Product product) {
		super();
		this.type = type;
		this.product = product;
	}
	
	
	public Mydatasmappper() {
		super();
	}
	public void setType(String type) {
		this.type = type;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	
	
	
	
	

}
