package no.poltilbud.feed;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class SaxParser extends BaseParser {

	static final String POLTILBUD = "poltilbud";
	public SaxParser(String feedUrl, InputStream localInputStream) {
		super(feedUrl, localInputStream);
	}

	public ParsedProducts parse() {
		final Product currentProduct = new Product();
		RootElement root = new RootElement(POLTILBUD);		
		final ParsedProducts parsedProducts = new ParsedProducts();

		root.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				parsedProducts.setDate(body);
			}
		});
		
		final List<Product> products = new ArrayList<Product>();
		Element product = root.getChild(PRODUCT);
		product.setEndElementListener(new EndElementListener(){
			public void end() {
				products.add(currentProduct.copy());
			}
		});
		product.getChild(NAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setName(body);
			}
		});
		product.getChild(TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setType(body);
			}
		});
		product.getChild(DIFFERANCE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setDifferance(body);
			}
		});
		product.getChild(DIFFERANCEPERC).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setDifferancePerc(body);
			}
		});
		product.getChild(NEWPRICE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setNewPrice(body);
			}
		});
		product.getChild(OLDPRICE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setOldPrice(body);
			}
		});
		product.getChild(URL).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentProduct.setLink(body);
			}
		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		parsedProducts.setProducts(products);
		return parsedProducts;
	}
}
