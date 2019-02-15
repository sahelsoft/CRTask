package utils.initializing_src_doc;

import org.apache.tika.language.LanguageIdentifier;

/**
 * Created by Sahelsoft on 4/10/2018.
 */
public class LanguageDetector {
    public static  String languageDetector(String inputText) {
        LanguageIdentifier identifier = new LanguageIdentifier(inputText);
        return  identifier.getLanguage();
    }
}
