package elearning.wiacekp.pl.jarvis.rssreader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RSSParser extends DefaultHandler {

	private ArrayList<RSSItem> rssItems;
	private RSSItem parsingItem;
	private StringBuilder stringBuilder;

	public RSSParser() {
		rssItems = new ArrayList<RSSItem>();
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		stringBuilder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) {

		if (parsingItem != null) {
			try {
				if (qName.equals("content:encoded")) {
					qName = "content";
				}

				String functionName = "set"
						+ qName.substring(0, 1).toUpperCase()
						+ qName.substring(1);		

				Method function = RSSItem.class.getMethod(functionName,
						String.class);

				if (qName.equals("content")) {
					Method tempImageFunction = RSSItem.class.getMethod("setImage", String.class);
					tempImageFunction.invoke(parsingItem, stringBuilder.toString());	
				}
				
				function.invoke(parsingItem, stringBuilder.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		stringBuilder = new StringBuilder();

		if (qName.equals("item") && rssItems != null) {
			parsingItem = new RSSItem();
			rssItems.add(parsingItem);
		}

	}
	
	public ArrayList<RSSItem> getResult(){
		return rssItems;
	}

}