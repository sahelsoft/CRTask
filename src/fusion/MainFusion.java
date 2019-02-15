package fusion;

import org.apache.commons.collections4.map.HashedMap;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by SahelSoft on 6/23/2018.
 */
public class MainFusion {

    public static void main(String[] args) throws Exception {

        fusionApproach();
    }


    public static void fusionApproach() throws Exception {
        System.out.println("===CL-BOC result loaded");
        Map<String,Map<String,Float>> CLBOC= readCandidateRetrievalOutput("CLBOC.txt");

        System.out.println("===TM+BOW result loaded");
        Map<String,Map<String,Float>> TMABOW= readCandidateRetrievalOutput("TMABOW.txt");


        System.out.println("\n===Interpolated:");
        Map<String,Map<String,Float>> fused_result = interpolatingTwoResult(CLBOC, TMABOW);
        System.out.println("\n===cuttingByPeakAndPlateau:");
        Map<String,Map<String,Float>> cutResult = cuttingByPeakAndPlateau(fused_result);
        displayResult(cutResult);
    }

    public static Map<String, Map<String,Float>> interpolatingTwoResult(Map<String,Map<String,Float>> CLBOC, Map<String,Map<String,Float>> TMABOW) throws Exception {

        Map<String, Map<String,Float>> result=new HashMap<>();

        String key;
        Map<String,Float> currentBOWordValue=new HashedMap<>();
        Map<String,Float> currentBOConceptValue=new HashedMap<>();

        for (Entry<String, Map<String, Float>> entryBOW : TMABOW.entrySet()) {
            key = entryBOW.getKey();
            currentBOWordValue= entryBOW.getValue();
            float max=0.0f;
            float min=100000f;
            for (Entry<String,Float> entry : currentBOWordValue.entrySet()) {
                if (entry.getValue()>max) {
                    max=entry.getValue();
                }
                if (entry.getValue()<min) {
                    min=entry.getValue();
                }
            }
            float normScore=0.0f;
            float denominator=max-min;
            for (Entry<String,Float> entry : currentBOWordValue.entrySet()) {
                if (denominator==0) {
                    normScore=0;
                } else {
                    normScore=(entry.getValue()-min)/denominator;
                }
                currentBOWordValue.replace(entry.getKey(), normScore);
            }

            currentBOConceptValue=CLBOC.get(key);
            max=0.0f;
            min=600000f;
            for (Entry<String,Float> entry : currentBOConceptValue.entrySet()) {
                if (entry.getValue()>max) {
                    max=entry.getValue();
                }
                if (entry.getValue()<min) {
                    min=entry.getValue();
                }
            }

            float sumC=0.0f;
            int count=0;
            denominator=max-min;
            for (Entry<String,Float> entry : currentBOConceptValue.entrySet()) {
                if (denominator==0) {
                    normScore=0;
                } else {
                    normScore=(entry.getValue()-min)/denominator;
                }
                currentBOConceptValue.replace(entry.getKey(), normScore);
                sumC=sumC+normScore;
                count++;
            }
            float interpolater= (float) (sumC/(count+sumC));
            Map<String, Float> currResult=new HashMap<>();
            String tmpKey;
            float tmpBOWValue;
            float tmpBOCValue;
            float newScore;
            for (Entry<String,Float> entry : currentBOWordValue.entrySet()) {
                tmpKey=entry.getKey();
                tmpBOWValue=entry.getValue();
                tmpBOCValue=0.0f;
                if (currentBOConceptValue.containsKey(tmpKey)) {
                    tmpBOCValue=currentBOConceptValue.get(tmpKey);
                }
                newScore=(interpolater*(tmpBOCValue)) + ((1-interpolater)*(tmpBOWValue));
                currResult.put(tmpKey,newScore);
            }
            for (Entry<String,Float> entry : currentBOConceptValue.entrySet()) {
                tmpKey=entry.getKey();
                tmpBOCValue=entry.getValue();
                if (currResult.containsKey(tmpKey)) continue;
                tmpBOWValue=0.0f;
                newScore=(interpolater*(tmpBOCValue)) + ((1-interpolater)*(tmpBOWValue));
                currResult.put(tmpKey,newScore);
            }
            currResult=extractingTopDocs(currResult,10);
            result.put(key,currResult);
        }
        return result;
    }


    public static Map<String, Map<String,Float>> cuttingByPeakAndPlateau(Map<String, Map<String,Float>> fusedResult) throws IOException {
        Map<String, Map<String,Float>> cut_result=new HashMap<>();
        Map<String,Float> current_cut_result=null;
        for (Entry<String, Map<String, Float>> entry : fusedResult.entrySet()) {
            String suspFileName = entry.getKey();
            Map<String, Float> foundDocs = entry.getValue();
            int diffStep=0;
            float diff=0.0f;
            int i=0;
            Iterator<Entry<String, Float>> entriesSrc = foundDocs.entrySet().iterator();
            Entry<String, Float> entryCurr;
            float preVal=0.0f;
            float currVal=0.0f;
            current_cut_result=new HashMap<>();
            if (entriesSrc.hasNext()) {
                entryCurr = entriesSrc.next();
                currVal=entryCurr.getValue();
            }
            while (entriesSrc.hasNext()) {
                preVal=currVal;
                entryCurr= entriesSrc.next();
                currVal=entryCurr.getValue();
                if (diff<(preVal-currVal)) {
                    diff=preVal-currVal;
                    diffStep=i;
                }
                i++;
            }
            i=0;
            for (Entry<String, Float> curEntry : foundDocs.entrySet()) {
                if (diffStep>=i) {
                    current_cut_result.put(curEntry.getKey(),curEntry.getValue());
                    i++;
                } else {
                    break;
                }

            }
            cut_result.put(suspFileName,current_cut_result);
        }
        return cut_result;
    }


   public static Map<String,Map<String,Float>> readCandidateRetrievalOutput(String fileName) throws IOException {
        //read the true detection file
        File candidateOutputFile = new File("C:\\Users\\Sahelsoft\\Desktop\\Project\\Project\\Other\\"+fileName);
        BufferedReader br = new BufferedReader(new FileReader(candidateOutputFile));
        String line = null;
        Map<String,Map<String,Float>> candApproachOutput=new HashMap<>();
        int counter=0;
        String tempKey="";
        Map<String,Float> tempValue=new HashedMap<>();
        while ((line = br.readLine()) != null) {
            if (line.equals("======================================================================")) {
                candApproachOutput.put(tempKey,tempValue);
                tempKey="";
                tempValue=new HashedMap<>();
                counter++;
                continue;
            }
            String[] stFile = line.split(":");
            if(stFile[0].equals("Cheking")) {
                tempKey= stFile[1];
            } else {
                tempValue.put(stFile[0],Float.parseFloat(stFile[1]));
            }
        }

        br.close();
        return candApproachOutput;
    }

    public static void displayResult(Map<String,Map<String,Float>> fusedResult) throws IOException {
        for (Entry<String, Map<String, Float>> foundDocs : fusedResult.entrySet()) {
            System.out.println("Cheking:"+ foundDocs.getKey());
            for (Entry<String, Float> entry : foundDocs.getValue().entrySet()) {
                System.out.println(String.format(entry.getKey() + ":" + entry.getValue()));
            }
            System.out.println("======================================================================");
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> extractingTopDocs(Map<K, V> unsortMap, int numberOfTop) {
        List<Entry<K, V>> list =
                new LinkedList<Entry<K, V>>(unsortMap.entrySet());
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Entry<K, V>>() {
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        int counter=0;
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
            if (++counter>=numberOfTop) break;
        }
        return result;
    }

}
