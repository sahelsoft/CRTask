package utils.shared_class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordsExtraction {

	public static ArrayList<String> getKeywords(String content, String language) throws Exception {
		HashMap<String, Float> allSegmWordsScores= TFIDFWithPOSTAG.getAllWordsScores(content,language);
		List<Map<String,Float>> sentList=null;
		ArrayList<String> key_list = new ArrayList<String>();
		if (language.equals("en")) {
			sentList=CoreNLPSentenceSpliting.getEnglishSentWithItsTokens(content);
		} else if (language.equals("de")) {
			sentList=CoreNLPSentenceSpliting.getGermanSentWithItsTokens(content);
		} else if (language.equals("es")) {
			sentList=CoreNLPSentenceSpliting.getSpanishSentWithItsTokens(content);
		}

		float totalWeight=0.0f;
		int totalNumberOfWords=0;
		for (Map<String, Float> mapSent: sentList) {
			for (Map.Entry<String, Float> entry : mapSent.entrySet()) {
				totalNumberOfWords++;
				totalWeight=totalWeight+allSegmWordsScores.get(entry.getKey());
			}
		}

		float beta=totalWeight/(totalNumberOfWords);

		for (Map<String, Float> mapSent: sentList) {
			for (Map.Entry<String, Float> entry : mapSent.entrySet()) {
				if (allSegmWordsScores.get(entry.getKey()) > beta && !key_list.contains(entry.getKey())) {
					key_list.add(entry.getKey());
				}
			}
		}

		return key_list;
	}


}
