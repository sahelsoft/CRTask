package utils.initializing_src_doc;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sahelsoft on 4/10/2018.
 */

public class Translator {
    public static String translate(String langFrom,String langTo, String inputText) throws Exception {
        Translator tr = new Translator();
        return tr.parseResult(langFrom, langTo, inputText);
    }

    private String parseResult(String langFrom, String langTo,
                                         String text) throws Exception
    {
        //use google translate API to translate the text
        StringBuffer response = new StringBuffer();


        return parseResult(response.toString());
    }

    private String parseResult(String inputJson) throws Exception
    {
        //convert JSON to String
        String str="";

        return str;
    }
}
