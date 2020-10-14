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

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        //recordMethodsInClass();
        returnString += " - Number of Methods Called: " + num + "\n";
        return returnString;
    }

   /* public int numberOfMethods()
    {
        if(!lMD.isEmpty()){
            for (MethodDetails details: lMD) {
                returnString += "      Method Name: " + details.getMethodName()
                        + " - Simple Complexity: " + details.getMethodDecisions() + "\n";
            }
            lMD.clear();
        }
    }*/

    public void visit(ClassOrInterfaceDeclaration n, Object arg){
        returnString += "Class Name: " + n.getName()
                + " - Number of Methods: " + n.getMethods().size();
        /*System.out.println("Class Implements: ");
        // check for nothing implemented
        if(n.getImplementedTypes().isNonEmpty()) {
            for (ClassOrInterfaceType coi : n.getImplementedTypes()) {
                System.out.println(coi.getName());
            }
        }
        System.out.println("Class Extends: ");
        // check for nothing inherited
        if(n.getExtendedTypes().isNonEmpty()) {
            for (ClassOrInterfaceType coi : n.getExtendedTypes()) {
                System.out.println(coi.getName());
            }
        }*/
        super.visit(n, arg);
    }

    public void visit(MethodCallExpr n, Object arg)
    {
        //returnString += "\n - Method Called: "  + n.getNameAsString(); //Need to get number of method calls here its going to each individually
        num++;
        super.visit(n, arg);
    }

    /*public void visit(MethodDeclaration n, Object arg) {
        System.out.println("Method name: " + n.getName());
        System.out.println("Method type: " + n.getType());
        num++;
        returnString = String.valueOf(num);
        //       	System.out.println("Method modifier: " + n.getModifiers() + " " + decodeModifiers(n.getModifiers()));

        // Find local variables in method body
        //n.getBody().ifPresent(l -> l.accept(new LocalVarVisitor(), arg));
    }*/
}
