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
    // private List<MethodDetails> lMD = new ArrayList<MethodDetails>();
    private int num = 0;
    private int methodSize = 0;
    private String tempNode = "";
    private ArrayList<Integer> nums = new ArrayList<Integer>();

    boolean isFirstClass = true;
    String currentClassName = "";
    HashSet<String> methodCalls = new HashSet<>();

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        recordOutput();
        return returnString;
    }

    private void recordOutput(){
        returnString += "Class Name: " + currentClassName
                + " - Number of Methods: " + methodSize  +
                " - Number of Methods Called: " + methodCalls.size() + " - RFC Value: " + (methodCalls.size() + methodSize) +  "\n";
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg){

        if(!isFirstClass){
            recordOutput();
        }
        currentClassName = n.getNameAsString();
        methodSize = n.getMethods().size();
        methodCalls.clear();
        isFirstClass = false;

//        num = 0;
//        if(num != 0)
//        {
//            returnString += "Class Name: " + n.getName()
//                    + " - Number of Methods: " + methodSize;// +
//                    //" - Number of Methods Called: " + num + " - RFC Value: " + (num + methodSize) +  "\n";
//            num = 0;
//        }
//        else {
//            returnString +=  "Class Name: " + n.getName()
//                    + " - Number of Methods: " + methodSize;// +
//                    //" - RFC Value: " + (num + methodSize) + "\n";
//        }


        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Object arg)
    {
        methodCalls.add(n.getNameAsString());
//        if(tempNode != n.getScope().toString())
//        {
//           returnString += " - Number of Methods Called: " + num + " - RFC Value: " + (num + methodSize) +  "\n";
//        }
//
//        tempNode = n.getScope().toString();
//        System.out.println(n.getScope().toString());
//        num++;
        super.visit(n, arg);
    }

}
