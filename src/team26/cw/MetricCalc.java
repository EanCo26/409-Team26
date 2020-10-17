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
import java.util.List;

public class MetricCalc {

    public static void main(String[] args) throws Exception {

        List<String> filesInDir = new ArrayList<String>();
        File rootDir = new File("rsrc/");
        Files.walk(Paths.get(rootDir.getName())).forEach(path -> {
            if(path.toString().endsWith(".java")){
                filesInDir.add(path.toString());
            }
        });

        String MetricOutput[] = {"", "", "", ""};
        for (String filePath: filesInDir){
            File f = new File(filePath);

            FileInputStream fis = new FileInputStream(f);
            CompilationUnit cu;
            try{
                cu = StaticJavaParser.parse(f);
            }finally {
                fis.close();
            }

            //Output of test that is stored in strings to be written to files
            // commented Wmc Visitor cos it runs but needs tweaking
            String fileOut = "\nFile: " + filePath + "\n";
            String metric = "";

            /*metric = new WmcVisitor().returnOutput(cu, null);
            MetricOutput[0] += metric.isEmpty() ? "": fileOut + metric;
            metric = new RfcVisitor().returnOutput(cu, null);
            MetricOutput[1] += metric.isEmpty() ? "": fileOut + metric;*/
//            metric = new CboVisitor().returnOutput(cu, null);
//            MetricOutput[2] += metric.isEmpty() ? "": fileOut + metric;
            metric = new LconVisitor().returnOutput(cu, null);
            MetricOutput[3] += metric.isEmpty() ? "": fileOut + metric;

//            ArrayList output = new CboVisitor().returnOutput(cu, null);
//            //System.out.println("Andy = " + new CboVisitor().getCboClassList().get(0));
//            for (Object s: output) {
//                System.out.println(s);
//            }

            
        }
//Sets format for output file names using Date and Time
        String outputFilePath = "";
        SimpleDateFormat  dateFormat = new SimpleDateFormat("'Date 'dd-MM-yyyy ' at ' HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());

        for (int i = 0; i < 4; i ++){
            outputFilePath = "Test Outputs/";
            if(MetricOutput[i].isEmpty()){
                continue;
            }
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

            Path file = Paths.get(outputFilePath);
            Files.write(file, MetricOutput[i].getBytes());
        }
    }
}
