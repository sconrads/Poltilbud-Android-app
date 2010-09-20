package no.poltilbud.feed;

import java.io.InputStream;

public abstract class FeedParserFactory {
	static String feedUrl = "http://www.poltilbud.com/thisMonthOffersXML.xml";
	
	public static Parser getParser(InputStream localInputStream){
		return new SaxParser(feedUrl, localInputStream);
	}
	
	public static Parser getParser(){
		return new SaxParser(feedUrl, null);
	}
	
}
