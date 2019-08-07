package Step1_SourceRetrieval.revised3.DatasetCreation;

import com.google.gson.JsonArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.*;

/**
 * Created by Sahelsoft on 7/27/2019.
 */
public class chatNoirRequest {
    public static List<String> relDoc=new ArrayList<>();
    public static String fullDocPath="C:\\Users\\Sahelsoft\\Desktop\\revised3_dataset\\";

    public static boolean CWDocExtraction(String curSuspFileName, String query) {
        JSONObject json = new JSONObject();
        json.put("apikey", "APIkey");
        List<String> datasource=new ArrayList<>();
        datasource.add("cw09");
        json.put("index", datasource);
        json.put("pretty", true);
        json.put("size", 10);
        json.put("query",query);

        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("https://www.chatnoir.eu/api/v1/_search");
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response= httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray=null;
            if (jsonObject.has("results"))
                jsonArray= jsonObject.getJSONArray("results");
     
            Map<String,String> curQueryRes=new HashMap<>();
            for(int x = 0; x < jsonArray.length(); x++){
                String uuid=jsonArray.getJSONObject(x).getString("uuid");
                if (!relDoc.contains(uuid)) {
                    relDoc.add(uuid);
                    getRelatedDoc(uuid);
		}
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%");
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private static void getRelatedDoc(String uuid) throws Exception {
        String urlAdd="https://www.chatnoir.eu/cache?";
        urlAdd=urlAdd+"uuid="+uuid +"&index="+"cw09&raw&plain";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlAdd);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String result = "";
        String line = "";
        while ((line = rd.readLine()) != null) {
            result+=line;
        }
        String plainText=Jsoup.parse(result).text();
        writeToFile(uuid, plainText);
    }

    public static boolean writeToFile(String fileName, String plainText) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullDocPath+fileName));
            writer.write(plainText);
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
