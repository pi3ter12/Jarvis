package elearning.wiacekp.pl.jarvis.rssreader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class RSSReader {

	public static ArrayList<RSSItem> startReader(URL url) throws SAXException, IOException {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			RSSParser rssParser = new RSSParser();
			InputSource inputSource = new InputSource(url.openStream());
			
			reader.setContentHandler(rssParser);
			reader.parse(inputSource);

			return rssParser.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException();
		}

	}

}
