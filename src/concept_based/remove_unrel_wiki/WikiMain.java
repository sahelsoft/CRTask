package concept_based.remove_unrel_wiki;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;

public class WikiMain {
    public static XMLStreamWriter xmlStreamWriter;
    public static void main(String[] args) throws SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            final String dir = System.getProperty("user.dir");

            //Preparing Sax Parser
            System.out.println("Initializing Sax Parser");
            InputStream xmlInput = new FileInputStream(dir+"\\Other\\Wikipedia\\enawiki-20180120-pages-articles-xml\\enwiki-20180120-pages-articles.xml");
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new SaxHandler();

            //Preparing Stax Writer
            System.out.println("Initializing Stax Writer");
            String filePath = dir+"\\Other\\Wikipedia\\en-with-de-Out.xml";
            Writer fileWriter = new FileWriter(filePath);
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(fileWriter);

            //Preparing Wiki Database connection
            System.out.println("Initializing Wikipedia Database Connection");
            DBWiki.startWikiConnection();

            //Start Sax Parser. The Stax Writer and DBWiki will use in the middle of Sax Parsing
            saxParser.parse(xmlInput, handler);

        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}
