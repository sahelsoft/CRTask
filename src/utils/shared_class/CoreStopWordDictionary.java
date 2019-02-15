package utils.shared_class;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CoreStopWordDictionary {
	static ArrayList<String> englishStopwordList=null;
	static ArrayList<String> germanStopwordList=null;
	static ArrayList<String> spanishStopwordList=null;
	
	public static boolean isEnglishStopWord(String term) {
		if (englishStopwordList.contains(term))
			return true;
		else 
			return false;
	}

	public static boolean isGermanStopWord(String term) {
		if (germanStopwordList.contains(term))
			return true;
		else 
			return false;
	}
	
	public static boolean isSpanishStopWord(String term) {
		if (spanishStopwordList.contains(term))
			return true;
		else 
			return false;
	}
	
	public static void loadStopWordList() throws IOException {
		loadEnglishStopWordList();
		loadGermanStopWordList();
		loadSpanishStopWordList();
	}
	
	public static void loadEnglishStopWordList() throws IOException {
		System.out.println("Loading Stopword list...");
		englishStopwordList=new ArrayList<String>();
		File stopwordFile = new File("C:\\Users\\Sahelsoft\\Desktop\\Project\\Project\\Other\\Stopwords\\English_stopwords.txt");
		BufferedReader br = new BufferedReader(new FileReader(stopwordFile)); 
		String line;
		while ((line = br.readLine())!= null) {
			if (!englishStopwordList.contains(line)) {
				englishStopwordList.add(line.toLowerCase());
			}
		}
		br.close();
	}
	
	public static void loadGermanStopWordList() throws IOException {
		System.out.println("Loading Stopword list...");
		germanStopwordList=new ArrayList<String>();
		File stopwordFile = new File("C:\\Users\\Sahelsoft\\Desktop\\Project\\Project\\Other\\Stopwords\\German_stopwords.txt");
		BufferedReader br = new BufferedReader(new FileReader(stopwordFile)); 
		String line;
		while ((line = br.readLine())!= null) {
			if (!germanStopwordList.contains(line)) {
				germanStopwordList.add(line.toLowerCase());
			}
		}
		br.close();
	}
	
	public static void loadSpanishStopWordList() throws IOException {
		System.out.println("Loading Stopword list...");
		spanishStopwordList=new ArrayList<String>();
		File stopwordFile = new File("C:\\Users\\Sahelsoft\\Desktop\\Project\\Project\\Other\\Stopwords\\Spanish_stopwords.txt");
		BufferedReader br = new BufferedReader(new FileReader(stopwordFile)); 
		String line;
		while ((line = br.readLine())!= null) {
			if (!spanishStopwordList.contains(line)) {
				spanishStopwordList.add(line.toLowerCase());
			}
		}
		br.close();
	}
}
