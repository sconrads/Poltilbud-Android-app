package no.poltilbud.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser implements FeedParser {

	// names of the XML tags
	static final String CHANNEL = "channel";
	static final String PUB_DATE = "pubDate";
	static final  String DESCRIPTION = "description";
	static final  String LINK = "link";
	static final  String TITLE = "title";
	static final  String ITEM = "item";
	
	private final URL feedUrl;
	private InputStream localInputStream;

	protected BaseFeedParser(String feedUrl, InputStream localInputStream){
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