package no.poltilbud.feed;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import no.poltilbud.feed.BaseFeedParser;

//import static BaseFeedParser.*;

public class RssHandler extends DefaultHandler{
	private List<Product> messages;
	private Product currentMessage;
	private StringBuilder builder;
	
	public List<Product> getMessages(){
		return this.messages;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (this.currentMessage != null){
			if (localName.equalsIgnoreCase(BaseFeedParser.TITLE)){
				currentMessage.setTitle(builder.toString());
			} else if (localName.equalsIgnoreCase(BaseFeedParser.LINK)){
				currentMessage.setLink(builder.toString());
			} else if (localName.equalsIgnoreCase(BaseFeedParser.DESCRIPTION)){
				currentMessage.setDescription(builder.toString());
			} else if (localName.equalsIgnoreCase(BaseFeedParser.PUB_DATE)){
				currentMessage.setDate(builder.toString());
			} else if (localName.equalsIgnoreCase(BaseFeedParser.ITEM)){
				messages.add(currentMessage);
			}
			builder.setLength(0);	
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		messages = new ArrayList<Product>();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(BaseFeedParser.ITEM)){
			this.currentMessage = new Product();
		}
	}
}