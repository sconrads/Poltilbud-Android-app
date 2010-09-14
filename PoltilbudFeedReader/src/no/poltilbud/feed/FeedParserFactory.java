package no.poltilbud.feed;

public abstract class FeedParserFactory {
	static String feedUrl = "http://www.poltilbud.com/thisMonthOffers.xml";
	
	public static FeedParser getParser(){
		return new AndroidSaxFeedParser(feedUrl);
	}
	
}
