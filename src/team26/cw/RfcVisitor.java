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
import java.util.List;

public class RfcVisitor extends VoidVisitorAdapter {

    String returnString = "";
   // private List<MethodDetails> lMD = new ArrayList<MethodDetails>();
    private int num = 0;
    private int methodSize = 0;
    private String tempNode = "";
    private ArrayList<Integer> nums = new ArrayList<Integer>();

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        return returnString;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg){
        methodSize = n.getMethods().size();

        if(num != 0)
        {
            returnString += "Class Name: " + n.getName()
                    + " - Number of Methods: " + methodSize;// +
                    //" - Number of Methods Called: " + num + " - RFC Value: " + (num + methodSize) +  "\n";
            num = 0;
        }
        else {
            returnString +=  "Class Name: " + n.getName()
                    + " - Number of Methods: " + methodSize;// +
                    //" - RFC Value: " + (num + methodSize) + "\n";
        }


        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Object arg)
    {
        if(tempNode != n.getScope().toString())
        {
           returnString += " - Number of Methods Called: " + num + " - RFC Value: " + (num + methodSize) +  "\n";
        }

        tempNode = n.getScope().toString();
        System.out.println(n.getScope().toString());
        num++;
        super.visit(n, arg);
    }

}
