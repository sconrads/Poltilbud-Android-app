package no.poltilbud.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseParser implements Parser {

	// names of the XML tags
	static final  String PUB_DATE = "pubDate";
	static final  String PRODUCT = "product";
	static final  String NAME = "name";
	static final  String TYPE = "type";
	static final  String DIFFERANCE = "differance";
	static final  String DIFFERANCEPERC = "differancePerc";
	static final  String NEWPRICE = "newPrice";
	static final  String OLDPRICE = "oldPrice";
	static final  String URL = "url";
	
	private final URL feedUrl;
	private InputStream localInputStream;

	protected BaseParser(String feedUrl, InputStream localInputStream){
		try {
			this.feedUrl = new URL(feedUrl);
			this.localInputStream = localInputStream;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			if (this.localInputStream != null){
				return this.localInputStream;
			}
			else {
				return feedUrl.openConnection().getInputStream();
			}						
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}