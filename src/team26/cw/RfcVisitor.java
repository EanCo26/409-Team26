package team26.cw;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RfcVisitor extends VoidVisitorAdapter {

    String returnString = "";
    private int methodSize = 0;
    boolean isFirstClass = true;
    String currentClassName = "";
    HashSet<String> methodCalls = new HashSet<>(); //Use a hashset so that repeat method calls are not counted

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        recordOutput(); //Need to call recordOutput once more to get the final class details added into the string
        return returnString;
    }

    //Record the output here adding the info gathered to a string which we will return at the end of the file
    private void recordOutput(){
        //Records the Class name, number of methods, number of methods called and will then calculate and record the Rfc value
        returnString += "Class Name: " + currentClassName
                + " - Number of Methods: " + methodSize  +
                " - Number of Methods Called: " + methodCalls.size() + " - RFC Value: " + (methodCalls.size() + methodSize) +  "\n";
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg){

        //Check if it is the first class as this is handled differently since the method calls have not yet been visited
        if(!isFirstClass){
            recordOutput();
        }
        currentClassName = n.getNameAsString();
        methodSize = n.getMethods().size();
        methodCalls.clear();
        isFirstClass = false;


        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Object arg)
    {
        methodCalls.add(n.getNameAsString()); //Add all the method calls into the hashset
        super.visit(n, arg);
    }

}
