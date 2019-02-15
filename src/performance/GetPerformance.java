package performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sahelsoft on 6/21/2017.
 */
public class GetPerformance {
    public static void main(String[] args) throws Exception {
            evaluationMethod("fusion.txt");

    }
    public static void evaluationMethod(String fileName) throws Exception {
        //read true detection from pair file
        Map<String,String> true_detection=readTrueDetection();

        //read candidate retrieval output
        Map<String,String> cand_approach_output=readCandidateRetrievalOutput(fileName,10);

        // Evaluate the detection performance
        List<Double> finalScore=getPerformance(cand_approach_output,true_detection);
        Double macroAvgPrecision=finalScore.get(0);
        Double macroAvgRecall=finalScore.get(1);
        Double F1Measure=finalScore.get(3);
        Double F2Measure=finalScore.get(4);

        System.out.println("=====================================================");
        System.out.println("macro average recall="+macroAvgRecall);
        System.out.println("macro average precision="+macroAvgPrecision);
        System.out.println("F1-Measure="+F1Measure);
        System.out.println("F2-Measure="+F2Measure);
        System.out.println("=====================================================");

    }

    public static Map<String,String> readCandidateRetrievalOutput(String fileName,int bestN) throws IOException {
        //read the true detection file
        File candidateOutputFile = new File("C:\\Users\\Sahelsoft\\Desktop\\Project\\Project\\Other\\revised\\output\\"+fileName);
        BufferedReader br = new BufferedReader(new FileReader(candidateOutputFile));
        String line = null;
        Map<String,String> candApproachOutput=new HashMap<>();
        int counter=0;
        String tempKey="";
        String tempValue="";
        int incBest=0;
        while ((line = br.readLine()) != null) {
            if (line.equals("======================================================================")) {
                candApproachOutput.put(tempKey,tempValue);
                tempKey="";
                tempValue="";
                counter++;
                incBest=0;
                continue;
            }
            String[] stFile = line.split(":");
            if(stFile[0].equals("Cheking")) {
                tempKey= stFile[1];
            } else {
                if (incBest<bestN) {
                    incBest++;
                    if (tempValue.isEmpty())
                        tempValue = stFile[0];
                    else
                        tempValue = tempValue + ";" + stFile[0];
                }
            }
        }
        br.close();
        return candApproachOutput;
    }

    public static Map<String,String> readTrueDetection() throws IOException {
        //read the true detection file
        File pairFile = new File("E:\\PhD\\Thesis\\Plagdet\\CLPD\\Project\\Other\\CandidateRetrievalFiles\\CLPD-Dataset\\pairs.txt");
        BufferedReader br = new BufferedReader(new FileReader(pairFile));
        String line = null;
        Map<String,String> TrueDetection=new HashMap<>();

        while ((line = br.readLine()) != null) {
            String[] stFile = line.split(" ");
            String tempKey=stFile[0];
            String tempValue=stFile[1];
            if (TrueDetection.containsKey(tempKey)) {
                String curValue=TrueDetection.get(tempKey);
                TrueDetection.replace(tempKey,curValue,curValue+";"+tempValue);
            } else {
                TrueDetection.put(tempKey,tempValue);
            }
        }
        br.close();
        return TrueDetection;
    }

    public static  List<Double> getPerformance(Map<String,String> candApproachOutput, Map<String,String> TrueDetection) throws IOException {
        /*
        Precision= |{Relevant} ∩ {Retrieved}| /  |{Retrieved}|
        Recall = |{Relevant} ∩ {Retrieved}| /  |{Relevant}|
        F-score = recall x precision / (recall + precision) / 2
        Macro Average Precision
        Macro Average Recall
        */
        List<Double> Perci=new ArrayList<>();
        List<Double> Reci=new ArrayList<>();
        List<Double> FMi=new ArrayList<>();

        //Calculate precision,recall and F-Measure
        candApproachOutput.forEach((key, value) -> {
            String[] retrievedDoc=value.split(";");
            double numOfRetrieved=retrievedDoc.length;

            String[] relevantDoc=TrueDetection.get(key).split(";");
            double numOfRelevant=relevantDoc.length;

            double relRet=0;
            for(int i=0;i<retrievedDoc.length;i++) {
                for(int j=0;j<relevantDoc.length;j++) {
                    if (retrievedDoc[i].contentEquals(relevantDoc[j]))
                        relRet++;
                }
            }
            double Pi=relRet/numOfRetrieved;
            double Ri=relRet/numOfRelevant;
            double FScore=((2*Pi*Ri)/(Pi+Ri));
            Perci.add(Pi);
            Reci.add(Ri);
            if (!Double.isNaN(FScore))
                FMi.add(FScore);
            else
                FMi.add(0.0);
        });

        //===================================================
        List<Double> finalScore=new ArrayList();

        //calculate macro average precision
        Double sum=0.0;
        for (int i=0;i<Perci.size();i++){
            sum+=Perci.get(i);
        }
        double macroPi=sum/Perci.size();
        finalScore.add(macroPi);

        //calculate macro average recall
        sum=0.0;
        for (int i=0;i<Reci.size();i++){
            sum+=Reci.get(i);
        }
        double macroRi=sum/Reci.size();
        finalScore.add(macroRi);


        //Calculate macro average F-Measure based on each sample result
        sum=0.0;
        for (int i=0;i<FMi.size();i++){
            sum+=FMi.get(i);
        }
        double macroFMeasure=sum/FMi.size();
        finalScore.add(macroFMeasure);

        //calculate macro average F-Measure
        finalScore.add((2*macroPi*macroRi)/(macroPi+macroRi));
        finalScore.add((5*macroPi*macroRi)/((4*macroPi)+macroRi));

        return  finalScore;
    }

}
