
package utils.shared_class;

import java.io.*;
import java.sql.*;
import java.util.*;

public class TFIDFWithPOSTAG
{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/benchmark";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "asdf";

	static Connection conn = null;
	static PreparedStatement selectPreparedStatement=null;
	static ResultSet selectResultSet=null;

	static String selectSQL;


	static String curSrcFileName = null;

	static int totalNumberOfDoc;

	static HashMap<String, Float> wordsIDF = new HashMap<String, Float>();

	public static HashMap<String, Float> getTF(String fileContent, String language)
	{    
		List<String> terms=new ArrayList<String>();
		ArrayList<String> words = new ArrayList<String>();

		if (language.equals("en")) {
			terms=CoreNLPSentenceSpliting.tokenizeEnglishWithPOSTAG(fileContent);
		} else if (language.equals("de")) {
			terms=CoreNLPSentenceSpliting.tokenizeGermanWithPOSTAG(fileContent);
		} else if (language.equals("es")) {
			terms=CoreNLPSentenceSpliting.tokenizeSpanishWithPOSTAG(fileContent);
		}
		
		for(String word:terms)
		{
			words.add(word);
		}

		// get TF values
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
		HashMap<String, Float> TFValues = new HashMap<String, Float>();
		for(String word : words)
		{
			if(wordCount.get(word) == null)	{
				wordCount.put(word, 1);
			} else {
				wordCount.put(word, wordCount.get(word) + 1);
			}
		}

		int wordLen = words.size();
		//traverse the HashMap
		Iterator<Map.Entry<String, Integer>> iter = wordCount.entrySet().iterator(); 
		while(iter.hasNext())
		{
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
			TFValues.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()) / wordLen);
		}
		return TFValues;
	} 


	public static void loadIDFList(String language) throws IOException {
		if (language.equals("en")) {
			System.out.print("Loading EN IDF: ");
			wordsIDF = loadIDFfromFile("EnglishBigCorpusIDF.properties");
			totalNumberOfDoc=610099;
			System.out.println("Done");
		} else if(language.equals("de")) {
			System.out.print("Loading DE IDF: ");
			wordsIDF=loadIDFfromFile("GermanyBigCorpusIDF.properties");
			totalNumberOfDoc=651497;
			System.out.println("Done");
		} else if(language.equals("es")) {
			System.out.print("Loading ES IDF: ");
			wordsIDF=loadIDFfromFile("SpanishBigCorpusIDF.properties");
			totalNumberOfDoc=610099;
			System.out.println("Done");
		}

	}

	public static HashMap<String,Float> getAllWordsScores(String content,String language) throws  SQLException, ClassNotFoundException, FileNotFoundException, IOException
	{
		HashMap<String,Float> passageWordsTF= new HashMap<String,Float>();
		
		if (language.equals("en")) {
			passageWordsTF = getTF(content,"en");
		} else if(language.equals("de")) {
			passageWordsTF = getTF(content,"de");
		} else if(language.equals("es")) {
			passageWordsTF = getTF(content,"es");
		}

		Iterator<Map.Entry<String, Float>> it = passageWordsTF.entrySet().iterator();
		HashMap<String,Float> passageTFIDF= new HashMap<String,Float>();
		try {
			while(it.hasNext()) {
				Map.Entry<String, Float> entry = it.next();
				String word = entry.getKey();
				Float TF=entry.getValue();
				Float IDF;

				if(wordsIDF.containsKey(word)) {
					IDF=wordsIDF.get(word);
				} else {
					IDF=(float)Math.log(totalNumberOfDoc / (0.1));
				}

				Float TFIDF = TF*IDF;
				passageTFIDF.put(word, TFIDF);
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}

		return passageTFIDF;
	}	


	private static HashMap<String, Float> loadIDFfromFile(String fileName) {
		wordsIDF = new HashMap<String, Float>();
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("C:\\Users\\Sahelsoft\\Desktop\\Project\\Project\\Other\\IDF\\"+fileName));

			for (String key : properties.stringPropertyNames()) {
				wordsIDF.put(key, Float.parseFloat(properties.get(key).toString()));
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
		return wordsIDF;
	}
}
