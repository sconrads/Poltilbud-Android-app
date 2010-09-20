package no.poltilbud.feed;

import java.util.List;

public class ParsedProducts {
	
	private List<Product> products;
	private String date;
	
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
