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
    String statementI = "";
    String statementJ = "";
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
        List<String> fieldVariables = new ArrayList<>();
        for (FieldDeclaration fieldDec:fdList) {
            for (VariableDeclarator varDec : fieldDec.getVariables()) {
                fieldVariables.add(varDec.getNameAsString());
            }
        }

        HashSet<String> Pair = new HashSet<>();
        for (String fieldVar:fieldVariables) {
            for(int i = 0; i < mdList.size(); i++){
                for(int j = i; j < mdList.size(); j++) {
                    if(i==j){
                        continue;
                    }

                    statementI = "";
                    statementJ = "";

                    MethodDeclaration methodDeclarationI = mdList.get(i);
                    methodDeclarationI.getBody().ifPresent(blockStmt -> {
                        BlockStmt bStmt = methodDeclarationI.getBody().get();
                        for (Statement stmt : bStmt.getStatements()) {
                            statementI += stmt.toString();
                        }
                    });

                    MethodDeclaration methodDeclarationJ = mdList.get(j);
                    methodDeclarationJ.getBody().ifPresent(blockStmt -> {
                        BlockStmt bStmt = methodDeclarationJ.getBody().get();
                        for (Statement stmt : bStmt.getStatements()) {
                            statementJ += stmt.toString();
                        }
                    });

                    if (statementJ.contains(fieldVar)) {
                        if(statementJ.contains(fieldVar)) {
                            String currentPair = i + "" + j;
                            Pair.add(currentPair);
                        }
                    }
                }
            }
        }
        newCounter += Pair.size();
        super.visit(cid, arg);
    }
}

