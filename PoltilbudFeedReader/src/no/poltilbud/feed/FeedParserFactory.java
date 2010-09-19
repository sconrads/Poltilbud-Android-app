package no.poltilbud.feed;

import java.io.InputStream;

public abstract class FeedParserFactory {
	static String feedUrl = "http://www.poltilbud.com/thisMonthOffers.xml";
	
	public static FeedParser getParser(InputStream localInputStream){
		return new AndroidSaxFeedParser(feedUrl, localInputStream);
	}
	
	public static FeedParser getParser(){
		return new AndroidSaxFeedParser(feedUrl, null);
	}
	
}
