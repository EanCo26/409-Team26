package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.*;


public class LconVisitor extends VoidVisitorAdapter {

    String returnString = "";
    private List<FieldDeclaration> fdList = new ArrayList<FieldDeclaration>();
    private List<MethodDeclaration> mdList = new ArrayList<MethodDeclaration>();
    int possiblePairs = 0;

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        return returnString;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
        int counter =0;
        System.out.println("----------------------------------------");

        fdList = cid.getFields();
        mdList = cid.getMethods();

        System.out.println(mdList.size());
        for (MethodDeclaration methodDec:mdList) {
            possiblePairs += counter;
            counter++;
            System.out.println(possiblePairs);
        }

        for(FieldDeclaration fieldDec:fdList) {
            for(VariableDeclarator v : fieldDec.getVariables()){
                System.out.println("Field name: " + v.getName());
            }
        }

        super.visit(cid, arg);
    }

    @Override
    public void visit(MethodDeclaration md, Object arg)
    {

    }

    @Override
    public void visit(FieldDeclaration fd, Object arg)
    {

    }

}

