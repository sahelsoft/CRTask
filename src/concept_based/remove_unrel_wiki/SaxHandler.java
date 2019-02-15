package concept_based.remove_unrel_wiki;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static concept_based.remove_unrel_wiki.StaxWriter.StartWriteToXML;
import static concept_based.remove_unrel_wiki.StaxWriter.closingOutputFile;
import static concept_based.remove_unrel_wiki.StaxWriter.writeToXML;

/**
 * Created by Sahelsoft on 2/24/2018.
 */
public class SaxHandler extends DefaultHandler {
    private boolean id_Flag=false;
    private boolean b_title=false;
    private boolean b_id=false;
    private boolean b_text=false;
    private static String textValue;
    public static MyWikiTemplate myWiki=null;
    private static StringBuffer textBuffer = null;
    private boolean isLang=false;
    public static int val=0;

    public void startDocument() throws SAXException {
        System.out.println("Start Parsing: ");
        StartWriteToXML();
        myWiki=new MyWikiTemplate();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("page")) {
            myWiki.setId(null);
            myWiki.setTitle(null);
            myWiki.setText(null);
            b_title=false;
            b_id=false;
            id_Flag=false;
            b_text=false;
            textBuffer=null;
            textValue=null;
            isLang=false;
        } else if(qName.equalsIgnoreCase("title")) {
            b_title=true;
        } else if (qName.equalsIgnoreCase("id") && id_Flag==false) {
            b_id=true;
            id_Flag=true;
        } else if (qName.equalsIgnoreCase("text")) {
            b_text=true;
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (b_title) {
            myWiki.setTitle(new String(ch, start, length));
            b_title=false;
        } else if (b_id) {
            myWiki.setId(new String(ch, start, length));
            isLang=DBWiki.execQueryOnWiki();
            b_id=false;
        } else if (b_text && isLang) {
            try {
                textValue = new String(ch, start, length);
                if (!textValue.equals("")) {
                    if (textBuffer == null)
                        textBuffer = new StringBuffer(textValue);
                    else
                        textBuffer.append(textValue);
                }
            } catch (Exception e) {
                System.out.println("error buffer=id=" + myWiki.getId());
                System.out.println(e.toString());
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (qName.equalsIgnoreCase("page")) {
                if (isLang) {
                    writeToXML();
                }
            } else if (qName.equalsIgnoreCase("text") && isLang) {
                try {
                    myWiki.setText(textBuffer.toString());
                } catch (Exception e) {
                    System.out.println("Error- Text buffer- " + myWiki.getId());
                }
            }
        } catch (Exception e) {
            System.out.println("element schema error ID=" + myWiki.getId());
        }
    }

    public void endDocument() throws SAXException {
        closingOutputFile();

        System.out.println("End Parsing: ");
    }
}
