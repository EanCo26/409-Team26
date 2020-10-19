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
        List<String> cboClassLists = new ArrayList<>(); //Stores a list of every class in the system


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

            //This will check if it is currently the first file being run
            //It will then add every class in the system to a list by going through each file
            //This is used so that we can compare coupling
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
            metric =  "" + CalcCbo(cu, counter, cboClassLists);
            MetricOutput[2] += metric.isEmpty() ? "": fileOut + "CBO Metric: " + metric ;

            metric = new LcomVisitor().returnOutput(cu, null);
            MetricOutput[3] += metric.isEmpty() ? "": fileOut + metric;


            
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
            //System.out.println(MetricOutput[i]);

            // Find the path for output then write a new file with metrics for each returned metric
            Path file = Paths.get(outputFilePath);
            Files.write(file, MetricOutput[i].getBytes());
        }



    }

    public static int CalcCbo(CompilationUnit cu, int counter, List<String> cboClassList)
    {

        String cboMethodString = new String();
        String[] cboMethodList = new String[]{};

        //This hashset is used to store the couples
        HashSet<String> couples = new HashSet<String>();

        cboMethodString = (new CboVisitor().returnMethodList(cu, null));
        //This will split the method string based on the dot that is placed between each method call in CboVisitor
        cboMethodList = cboMethodString.split("\\.");


        int cboValue =0;
        //Here we are checking every class in the system against the method calls made by the current class to see if we get any matches
        for (int i = 0; i < cboClassList.size(); i++) {
            for (int j = 0; j < cboMethodList.length; j++) {
                String cboClassString = cboClassList.get(i);
                String cboMethodStr = cboMethodList[j];
                if (cboClassString.equalsIgnoreCase(cboMethodStr)) {

                    //cboClassList.get(counter) is used as this will be the current class that we are examining
                    String cboTemp = cboClassList.get(counter) + ":" + cboClassList.get(i);

                    //When we get a match we add it into the hashset
                    couples.add(cboTemp);
                }
            }
        }


        //Now we will check to see How many times a class shows up in the couples hashset, from this we get the cbo metric
        for (int i = 0; i < cboClassList.size(); i++) {
            for (int j = 0; j < couples.size(); j++) {

                if (couples.toArray()[j].toString().equals(cboClassList.get(counter) + ":" + cboClassList.get(i)))
                {
                    cboValue++;
                }
                else if (couples.toArray()[j].toString().equals(cboClassList.get(i) + ":" + cboClassList.get(counter)))
                {
                    cboValue++;
                }
            }
        }


        return cboValue;
    }

}
