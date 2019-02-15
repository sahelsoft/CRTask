package concept_based.remove_unrel_wiki;

import javax.xml.stream.XMLStreamException;

/**
 * Created by Sahelsoft on 2/25/2018.
 */
public class StaxWriter {

    public static void writeToXML() {
        try {
            WikiMain.xmlStreamWriter.writeStartElement("Page");
            WikiMain.xmlStreamWriter.writeAttribute("Id", SaxHandler.myWiki.getId());
            WikiMain.xmlStreamWriter.writeAttribute("Title", SaxHandler.myWiki.getTitle());
            WikiMain.xmlStreamWriter.writeCharacters(SaxHandler.myWiki.getText());
            WikiMain.xmlStreamWriter.writeEndElement();
            WikiMain.xmlStreamWriter.writeCharacters("\n");
        } catch (XMLStreamException e1) {
            e1.printStackTrace();
        }
    }

    public static void StartWriteToXML() {
        try {
            WikiMain.xmlStreamWriter.writeStartDocument();
            WikiMain.xmlStreamWriter.writeCharacters("\n");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static void closingOutputFile() {
        try {
            WikiMain.xmlStreamWriter.writeEndDocument();
            WikiMain.xmlStreamWriter.flush();
            WikiMain.xmlStreamWriter.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }


}