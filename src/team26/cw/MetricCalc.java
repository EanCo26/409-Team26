package team26.cw;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MetricCalc {


    public static void main(String[] args) throws Exception {

        // starts at rsrc directory and add every file that ends .java to a list of files
        List<String> filesInDir = new ArrayList<String>();
        File rootDir = new File("rsrc/");


        int counter= -1;


        Files.walk(Paths.get(rootDir.getName())).forEach(path -> {
            if(path.toString().endsWith(".java")){
                filesInDir.add(path.toString());
            }
        });

        // Each metric is defined in the array - WMC is at index 0, RFC is at index 1, CBO is at index 2, LCOM is at index 3
        String MetricOutput[] = {"", "", "", ""};
        // for every file found in rsrc that is a java file
        for (String filePath: filesInDir){
            counter++;
            File f = new File(filePath);

            FileInputStream fis = new FileInputStream(f);
            CompilationUnit cu;
            try{
                cu = StaticJavaParser.parse(f);
            }finally {
                fis.close();
            }

            // The file path that is being assessed is added to metric output later for readibility
            String fileOut = "\nFile: " + filePath + "\n";
            String metric = "";

            // For every metric it runs through the Visitors and checks if there was a return
            // if so add metric return to array
            // WMC Basic and WMC MCC is calculated in WmcVisitor
            metric = new WmcVisitor().returnOutput(cu, null);
            MetricOutput[0] += metric.isEmpty() ? "": fileOut + metric;

            metric = new RfcVisitor().returnOutput(cu, null);
            MetricOutput[1] += metric.isEmpty() ? "": fileOut + metric;


            //metric = cboClassList.get(counter) + "\n" + "Score: " + cboScore.get(counter) + "\n"
            MetricOutput[2] += metric.isEmpty() ? "": fileOut + metric;

            metric = new LcomVisitor().returnOutput(cu, null);
            MetricOutput[3] += metric.isEmpty() ? "": fileOut + metric;

//            ArrayList output = new CboVisitor().returnOutput(cu, null);
//            //System.out.println("Andy = " + new CboVisitor().getCboClassList().get(0));
//            for (Object s: output) {
//                System.out.println(s);
//            }

            
        }
        //Sets format for output file names using current Date and Time
        String outputFilePath = "";
        SimpleDateFormat  dateFormat = new SimpleDateFormat("'Date 'dd-MM-yyyy ' at ' HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());

        for (int i = 0; i < 4; i ++){
            outputFilePath = "Test Outputs/";
            // ignore metrics in array that have no return
            if(MetricOutput[i].isEmpty()){
                continue;
            }
            // Set file path output name to correspond with metric calculated
            switch (i){
                case 0:
                    outputFilePath += "WMC - " + dateFormat.format(date) +".txt";
                    break;
                case 1:
                    outputFilePath += "RFC - " + dateFormat.format(date) +".txt";
                    break;
                case 2:
                    outputFilePath += "CBO - " + dateFormat.format(date) +".txt";
                    break;
                case 3:
                    outputFilePath += "LCOM - " + dateFormat.format(date) +".txt";
                    break;
            }
            System.out.println(MetricOutput[i]);

            // Find the path for output then write a new file with metrics for each returned metric
            Path file = Paths.get(outputFilePath);
            Files.write(file, MetricOutput[i].getBytes());
        }



    }

    public static void CalcCbo(CompilationUnit cu, int counter)
    {
        List<String> cboClassList = new ArrayList<>();
        List<String> cboMethodList = new ArrayList<>();

        HashSet<String> couples = new HashSet<String>();

        List<Integer> cboScore = new ArrayList<Integer>();

        cboClassList.add(new CboVisitor().returnClassList(cu, null));
        cboMethodList.add(new CboVisitor().returnMethodList(cu, null));

        for (int i = 0; i < cboClassList.size(); i++) {
            for (int j = 0; j  < cboMethodList.size() ; j++) {
                if(cboClassList.get(i).equals(cboMethodList.get(j)));
                {

                    //System.out.println(cboClassList.get(i).toString() + ":" + cboMethodList.get(j).toString());

                    String cboTemp = cboClassList.get(i) + ":" + cboMethodList.get(j);

                    couples.add(cboTemp);
                }
            }
        }


        int tempCount =0;
        for (int i = 0; i < cboClassList.size(); i++) {

            for (int j = 0; j< couples.size(); j++) {

                if (couples.toArray()[j].toString().equals(cboClassList.get(i) + ":" + cboMethodList.get(i)));
                {
                    tempCount++;
                }
            }
            cboScore.add(tempCount);
            tempCount =0;
        }

           /* for (int i = 0; i < couples.size(); i++) {
                if (couples.contains(cboClassList.get(i)));
                {
                    cboScore[i]++;
                }
            }*/


    }

}
