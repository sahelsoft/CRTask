package utils.shared_class;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.*;

import static java.util.Arrays.asList;

public class CoreNLPSentenceSpliting {
	static Properties propsEN = null;
	static StanfordCoreNLP corenlpEN = null;
	
	static Properties propsDE = null;
	static StanfordCoreNLP corenlpDE = null;

	static Properties propsES = null;
	static StanfordCoreNLP corenlpES = null;
	
	static CoreDocument document = null;
	
	static ArrayList<String> selectedEnglishTag=null;
	static ArrayList<String> selectedGermanTag=null;
	static ArrayList<String> selectedSpanishTag=null;

	static List<String> tokensList=null;
	static String word;

	public static List<String> tokenizeEnglish(String text) {
		List<String> tokensList=new ArrayList<String>();
		String word;
		document = new CoreDocument(text);
		corenlpEN.annotate(document);
		for (CoreSentence coreSen: document.sentences()) {
			for (CoreLabel token :coreSen.tokens()) {
				word=token.lemma().toLowerCase();
				if(!CoreStopWordDictionary.isEnglishStopWord(word) && word.length()>3 && word.length()<40) {
					tokensList.add(word.toLowerCase());
				}
			}
		}
		return tokensList;
	}


	public static List<String> tokenizeGerman(String text) {
		tokensList=new ArrayList<String>();
		document = new CoreDocument(text);
		corenlpDE.annotate(document);
		for (CoreSentence coreSen: document.sentences()) {
			for (CoreLabel token :coreSen.tokens()) {
				word=token.lemma().toLowerCase();
				if(!CoreStopWordDictionary.isGermanStopWord(word) &&  word.length()>3 && word.length()<40) {
					tokensList.add(word.toLowerCase());
				}
			}
		}
		return tokensList;
	}


	public static List<String> tokenizeSpanish(String text) {
		tokensList=new ArrayList<String>();
		document = new CoreDocument(text);
		corenlpES.annotate(document);
		for (CoreSentence coreSen: document.sentences()) {
			for (CoreLabel token :coreSen.tokens()) {
				word=token.lemma().toLowerCase();
				if(!CoreStopWordDictionary.isSpanishStopWord(word) &&  word.length()>3 && word.length()<40) {
					tokensList.add(word.toLowerCase());
				}
			}
		}
		return tokensList;
	}


	public static ArrayList<Map<String,Float>> getEnglishSentWithItsTokens(String content){
		Map<String, Float> tempMap=new HashMap<String,Float>();
		ArrayList<Map<String,Float>> tempList= new ArrayList<Map<String,Float>>();

		String word;
		document = new CoreDocument(content);
		corenlpEN.annotate(document);
		for (CoreSentence coreSen: document.sentences()) {
			tempMap.clear();
			for (CoreLabel token :coreSen.tokens()) {
				word=token.lemma().toLowerCase();
				if(!CoreStopWordDictionary.isEnglishStopWord(word) && selectedEnglishTag.contains(token.tag()) && word.length()>3 && word.length()<40) {
					tempMap.put(word.toLowerCase(), (float) 0);
				}
			}
			if(tempMap.size()!=0)
				tempList.add(new HashMap<String, Float>(tempMap));
		}
		return tempList;
	}


	public static ArrayList<Map<String,Float>> getGermanSentWithItsTokens(String content){
		Map<String, Float> tempMap=new HashMap<String,Float>();
		ArrayList<Map<String,Float>> tempList= new ArrayList<Map<String,Float>>();

		String word;
		document = new CoreDocument(content);
		corenlpDE.annotate(document);
		for (CoreSentence coreSen: document.sentences()) {
			tempMap.clear();
			for (CoreLabel token :coreSen.tokens()) {
				word=token.lemma().toLowerCase();
				if(!CoreStopWordDictionary.isGermanStopWord(word) && selectedGermanTag.contains(token.tag()) && word.length()>3 && word.length()<40) {
					tempMap.put(word.toLowerCase(), (float) 0);
				}
			}
			if(tempMap.size()!=0)
				tempList.add(new HashMap<String, Float>(tempMap));
		}
		return tempList;
	}



	public static ArrayList<Map<String,Float>> getSpanishSentWithItsTokens(String content){
		Map<String, Float> tempMap=new HashMap<String,Float>();
		ArrayList<Map<String,Float>> tempList= new ArrayList<Map<String,Float>>();

		String word;
		document = new CoreDocument(content);
		corenlpES.annotate(document);
		for (CoreSentence coreSen: document.sentences()) {
			tempMap.clear();
			for (CoreLabel token :coreSen.tokens()) {
				word=token.lemma().toLowerCase();
				if(!CoreStopWordDictionary.isSpanishStopWord(word) && selectedSpanishTag.contains(token.tag()) && word.length()>3 && word.length()<40) {
					tempMap.put(word.toLowerCase(), (float) 0);
				}
			}
			if(tempMap.size()!=0)
				tempList.add(new HashMap<String, Float>(tempMap));
		}
		return tempList;
	}



	public static void loadingCoreNLPModel() {
		try {
			propsEN = new Properties();
			propsEN.setProperty("annotators", "tokenize, ssplit, pos, lemma");
			propsEN.setProperty("tokenize.language", "en");
			corenlpEN = new StanfordCoreNLP(propsEN);
			selectedEnglishTag=new ArrayList<String>(asList("NN","NNP","NNS","NNPS","VB","VBD","VBG","VBN","JJ","RB","FW","JJR","JJS","RBR","RBS","VBP","VBZ","CD"));


			propsDE = new Properties();
			propsDE.setProperty("annotators", "tokenize, ssplit, pos, lemma");
			propsDE.setProperty("tokenize.language", "de");
			propsDE.setProperty("pos.model","edu/stanford/nlp/models/pos-tagger/german/german-fast.tagger");
			corenlpDE = new StanfordCoreNLP(propsDE);
			selectedGermanTag=new ArrayList<String>(asList("VVPP","NN","ADJD","ADJA","VVINF","VVFIN","VVIZU","XY","NE","ADV","FM","VVIMP","VVPP","VAFIN","VAIMP","VAINF","VAPP","VMPP","NA","CARD"));


			propsES = new Properties();
			propsES.setProperty("annotators", "tokenize, ssplit,pos, lemma");
			propsES.setProperty("tokenize.language", "es");
			propsES.setProperty("pos.model","edu/stanford/nlp/models/pos-tagger/spanish/spanish.tagger");
			corenlpES = new StanfordCoreNLP(propsES);
			selectedSpanishTag=new ArrayList<String>(asList("ao0000","aq0000","dn0000","do0000","nc00000","nc0n000","nc0p000","nc0s000","np00000","rg","rn","va00000","vag0000","vaic000","vaif000","vaii000",
														"vaip000","vais000","vam0000","van0000", "vap0000","vasi000","vasp000","vmg0000","vmic000","vmif000","vmii000","vmip000", "vmis000","vmm0000","vmn0000",
														"vmp0000","vmsi000", "vmsp000", "vsg0000", "vsic000", "vsif000", "vsii000", "vsip000", "vsis000", "vsm0000", "vsn0000", "vsp0000", "vssf000", "vssi000", "vssp000" ,"w","z0","zm", "zu"));

					
			System.out.println("English CoreNLP Model Loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
