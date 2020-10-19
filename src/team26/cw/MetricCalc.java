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
        boolean first = true;
        List<String> cboClassLists = new ArrayList<>();


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
            //metric = new WmcVisitor().returnOutput(cu, null);
            //MetricOutput[0] += metric.isEmpty() ? "": fileOut + metric;

            //metric = new RfcVisitor().returnOutput(cu, null);
            //MetricOutput[1] += metric.isEmpty() ? "": fileOut + metric;


            //metric = cboClassList.get(counter) + "\n" + "Score: " + cboScore.get(counter) + "\n"
            if (first)
            {

                for (String filePaths: filesInDir) {
                    File fs = new File(filePaths);

                    FileInputStream fiss = new FileInputStream(fs);
                    CompilationUnit cus;
                    try {
                        cus = StaticJavaParser.parse(fs);
                    } finally {
                        fiss.close();
                    }
                    cboClassLists.add(new CboVisitor().returnClassList(cus, null));
                }
                first = false;
            }
            //CalcCbo(cu, counter, cboClassLists);
            System.out.println(CalcCbo(cu, counter, cboClassLists));
            MetricOutput[2] += metric.isEmpty() ? "": fileOut + metric;

            //metric = new LcomVisitor().returnOutput(cu, null);
            //MetricOutput[3] += metric.isEmpty() ? "": fileOut + metric;

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

    public static int CalcCbo(CompilationUnit cu, int counter, List<String> cboClassList)
    {

        String cboMethodString = new String();
        String[] cboMethodList = new String[]{};

        HashSet<String> couples = new HashSet<String>();

        List<Integer> cboScore = new ArrayList<Integer>();

        cboMethodString = (new CboVisitor().returnMethodList(cu, null));
        cboMethodList = cboMethodString.split("\\.");

        System.out.println(cboClassList);
        System.out.println(cboMethodList[0]);


        int tempCount =0;
        for (int i = 0; i < cboClassList.size(); i++) {
            for (int j = 0; j  < cboMethodList.length ; j++) {
                if(cboClassList.get(i).equals(cboMethodList[j]));
                {

                    //System.out.println(cboClassList.get(i).toString() + ":" + cboMethodList.get(j).toString());

                    //String cboTemp = cboClassList.get(i) + ":" + cboMethodList[j];

                    //couples.add(cboTemp);
                    tempCount++;
                }
            }
        }



        /*for (int i = 0; i < cboClassList.size(); i++) {
            for(int j =0; j<cboMethodList.length; j++) {
                for (int k = 0; k < couples.size(); k++) {

                    if (couples.toArray()[k].toString().equals(cboClassList.get(i) + ":" + cboMethodList[j])) ;
                    {
                        tempCount++;
                    }
                }
            }
        }*/
//        cboScore.add(tempCount);
        couples.clear();
        return tempCount;
    }

}
