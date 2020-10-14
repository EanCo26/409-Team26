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

        List<String> outputsList = new ArrayList<String>();
        for (String filePath: filesInDir){

//            String filePath =  "rsrc/Test.java";
//            String filePath =  "rsrc/VisitorDemo.java";
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
            String output = "File: " + filePath + "\n";
            output += new RfcVisitor().returnOutput(cu, null);
            outputsList.add(output);
        }

        //Sets format for output file names using Date and Time
        String outputFilePath = "Test Outputs/";
        SimpleDateFormat  dateFormat = new SimpleDateFormat("'Date 'dd-MM-yyyy ' at ' HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());
        outputFilePath += dateFormat.format(date) +".txt";

        //Creates and writes to those files- Commented out cos was sick of deleting new files after each run
        Path file = Paths.get(outputFilePath);

        //Either add it all to one string for writing to one file OR
        //Different strings that output to separate files
        String output = "";
        for (String out: outputsList){
            output+=out;
            System.out.println(out);
        }
        Files.write(file, output.getBytes());
    }
}
