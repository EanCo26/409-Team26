package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.*;


public class LconVisitor extends VoidVisitorAdapter {

    String returnString = "";
    private List<FieldDeclaration> fdList = new ArrayList<FieldDeclaration>();
    private List<MethodDeclaration> mdList = new ArrayList<MethodDeclaration>();
    int possiblePairs = 0;
    String statementStringI = "";
    String statementStringJ = "";
    int newCounter = 0;



    public String returnOutput(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);
        System.out.println("  " + newCounter);
        return returnString;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
        int counter = 0;
        System.out.println(cid.getName());

        fdList = cid.getFields();
        mdList = cid.getMethods();
        newCounter = 0;

        //System.out.println(mdList.size());
//        for (MethodDeclaration methodDec : mdList) {
//            statementString = "";
//            possiblePairs += counter;
//            counter++;
//
//            methodDec.getBody().ifPresent(blockStmt -> {
//                BlockStmt bStmt = methodDec.getBody().get();
//                for (Statement stmt:bStmt.getStatements()) {
//                    statementString += stmt.toString();
//                    //System.out.println(tempString);
//                }
//            });
//            for (FieldDeclaration fieldDec : fdList) {
//                for (VariableDeclarator varDec : fieldDec.getVariables()) {
//                    System.out.println((varDec.getNameAsString()));
//                    if (statementString.contains(varDec.getNameAsString())) {
//                        newCounter++;
//                    }
//                }
////                    bStmt.getStatement().
//            }
//            //System.out.println(counter);
//        }

        for (FieldDeclaration fieldDec:fdList) {
            for (VariableDeclarator varDec : fieldDec.getVariables()) {
//                for (MethodDeclaration methodDec : mdList) {
                for(int i = 0; i < mdList.size(); i++){
                    for(int j = i; j < mdList.size(); j++) {
                        if(i==j){
                            continue;
                        }
                        statementStringI = "";
                        statementStringJ = "";
//                        possiblePairs += counter;
//                        counter++;
                        MethodDeclaration methodDeclarationI = mdList.get(i);
                        methodDeclarationI.getBody().ifPresent(blockStmt -> {
                            BlockStmt bStmt = methodDeclarationI.getBody().get();
                            for (Statement stmt : bStmt.getStatements()) {
                                statementStringI += stmt.toString();
                                //System.out.println(tempString);
                            }
                        });
                        MethodDeclaration methodDeclarationJ = mdList.get(j);
                        methodDeclarationJ.getBody().ifPresent(blockStmt -> {
                            BlockStmt bStmt = methodDeclarationJ.getBody().get();
                            for (Statement stmt : bStmt.getStatements()) {
                                statementStringJ += stmt.toString();
                                //System.out.println(tempString);
                            }
                        });
                        //System.out.println(statementString);
                        if (statementStringI.contains(varDec.getNameAsString())) {
//                            System.out.println((varDec.getNameAsString()));
                            if(statementStringJ.contains(varDec.getNameAsString())) {
                                newCounter++;
                            }
                        }
                    }
                }
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

