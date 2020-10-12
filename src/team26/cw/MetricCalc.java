package team26.cw;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetricCalc {
    public static void main(String[] args) throws Exception {
        String outputPath = "Test Outputs/";

        File f = new File("rsrc/Test.java");
//        File f = new File("rsrc/VisitorDemo.java");

        FileInputStream fis = new FileInputStream(f);
        CompilationUnit cu;
        try{
            cu = StaticJavaParser.parse(f);
        }finally {
            fis.close();
        }

        //Output of test that is stored in strings to be written to files
        // commented Wmc Visitor cos it runs but needs tweaking
        String output = "";
        output += new WmcVisitor().returnOutput(cu, null);
        System.out.println(output);

        //Sets format for output file names using Date and Time
        SimpleDateFormat  dateFormat = new SimpleDateFormat("'Date 'dd-MM-yyyy ' at ' HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());
        outputPath += dateFormat.format(date) +".txt";
        //Creates and writes to those files- Commented out cos was sick of deleting new files after each run
//        Path file = Paths.get(outputPath);
//        Files.write(file, output.getBytes());
    }
}
