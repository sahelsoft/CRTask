package concept_based.align_cross_wiki;

import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Sahelsoft on 3/8/2018.
 */
public class WikiConvertorMain {
    public static void main(String[] args) {
        step1();
        step2();
//        step3();
//        step4();
    }

    public static void step1() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            final String dir = System.getProperty("user.dir");

            //Preparing Sax Parser
            System.out.println("Initializing Sax Parser");
            InputStream xmlInput = new FileInputStream(dir + "\\Other\\Wikipedia\\eswiki-20180120-pages-articles-xml\\eswiki-20180120-pages-articles.xml");
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new FirstXMLParser();

            System.out.println("Initializing Wikipedia Database Connection");
            DBClass.startWikiConnection();

            //Start Sax Parser. The Stax Writer and DBWiki will use in the middle of Sax Parsing
            saxParser.parse(xmlInput, handler);

            DBClass.stopWikiConnection();

        } catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public static void step2() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            final String dir = System.getProperty("user.dir");

            //Preparing Sax Parser
            System.out.println("Initializing Sax Parser");
            InputStream xmlInput = new FileInputStream(dir + "\\Other\\Wikipedia\\dewiki-20180120-pages-articles-xml\\dewiki-20180120-pages-articles.xml");
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new CrossXMLParser();

            System.out.println("Initializing Wikipedia Database Connection");
            DBClass.startWikiConnection();

            //Start Sax Parser. The Stax Writer and DBWiki will use in the middle of Sax Parsing
            saxParser.parse(xmlInput, handler);

            DBClass.stopWikiConnection();

        } catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public static void step3() {
        try {
            System.out.println("Initializing Wikipedia Database Connection");
            DBClass.startWikiConnection();

            DBClass.retrieveUnCrossedElement();

            DBClass.stopWikiConnection();

        } catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public static void step4() {
        try {
            System.out.println("Initializing Wikipedia Database Connection");
            DBClass.startWikiConnection();

            DBClass.checkWikiRecords();

            DBClass.stopWikiConnection();

        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}

