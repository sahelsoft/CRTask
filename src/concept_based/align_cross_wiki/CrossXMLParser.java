package concept_based.align_cross_wiki;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Sahelsoft on 3/11/2018.
 */

public class CrossXMLParser extends DefaultHandler {
    private boolean id_Flag=false;
    private boolean b_title=false;
    private boolean b_id=false;
    private boolean b_text=false;
    private static String textValue;
    public static WikiObjectTemplate secondWiki=null;
    private static StringBuffer textBuffer = null;
    private boolean hasENArticle=false;

    public void startDocument() throws SAXException {
        System.out.println("Start Parsing: ");
        secondWiki=new WikiObjectTemplate();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("page")) {
            secondWiki.setId(null);
            secondWiki.setTitle(null);
            secondWiki.setText(null);
            b_title=false;
            b_id=false;
            id_Flag=false;
            b_text=false;
            textBuffer=null;
            textValue=null;
            hasENArticle=false;
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
            secondWiki.setTitle(new String(ch, start, length));
            b_title=false;
        } else if (b_id) {
            secondWiki.setId(new String(ch, start, length));
            if (DBClass.CheckHasCross()) hasENArticle=true;
            b_id=false;
        } else if (b_text && hasENArticle) {
            try {
                textValue = new String(ch, start, length);
                if (!textValue.equals("")) {
                    if (textBuffer == null)
                        textBuffer = new StringBuffer(textValue);
                    else
                        textBuffer.append(textValue);
                }
            } catch (Exception e) {
                System.out.println("error buffer=id=" + secondWiki.getId());
                System.out.println(e.toString());
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (qName.equalsIgnoreCase("page")) {
                if (hasENArticle) {
                    DBClass.updEnWithSecondData();
                }
            } else if (qName.equalsIgnoreCase("text") && hasENArticle) {
                try {
                    secondWiki.setText(textBuffer.toString());
                } catch (Exception e) {
                    System.out.println("Error- Text buffer- " + secondWiki.getId());
                }
            }
        } catch (Exception e) {
            System.out.println("element schema error ID=" + secondWiki.getId());
        }
    }

    public void endDocument() throws SAXException {
        System.out.println("End Parsing: ");
    }
}
