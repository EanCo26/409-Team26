package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.*;


public class LcomVisitor extends VoidVisitorAdapter {

    String returnString = "";

    String statementI = "";
    String statementJ = "";

    // starts with accept to pass to visits and then returns the final output
    public String returnOutput(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);
        return returnString;
    }

    // for every class LCOM is calculated
    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
        returnString+= "Class: ------> " + cid.getName();

        // list of methods within class
        List<MethodDeclaration> mdList = cid.getMethods();
        // gets list of all field names within class
        List<String> fieldVariables = new ArrayList<>();
        for (FieldDeclaration fieldDec:cid.getFields()) {
            for (VariableDeclarator varDec : fieldDec.getVariables()) {
                fieldVariables.add(varDec.getNameAsString());
            }
        }

        // list of pairs of methods
        HashSet<String> Pair = new HashSet<>();
        // loop through every method in class
        for(int i = 0; i < mdList.size(); i++){
            statementI = "";

            // get every statement within this method body and add to string
            MethodDeclaration methodDeclarationI = mdList.get(i);
            methodDeclarationI.getBody().ifPresent(blockStmt -> {
                BlockStmt bStmt = methodDeclarationI.getBody().get();
                for (Statement stmt : bStmt.getStatements()) {
                        statementI += stmt.toString();
                }
            });
            // loop through every method in class that comes after method in previous loop
            for(int j = i; j < mdList.size(); j++) {
                statementJ = "";
                if(i==j){
                    continue;
                }

                // get every statement within this method body and add to string
                MethodDeclaration methodDeclarationJ = mdList.get(j);
                methodDeclarationJ.getBody().ifPresent(blockStmt -> {
                    BlockStmt bStmt = methodDeclarationJ.getBody().get();
                    for (Statement stmt : bStmt.getStatements()) {
                        statementJ += stmt.toString();
                    }
                });

                // if a field name shows up in first method body and second method body then add to list of method pairs
                // since list is a HashSet then there is no duplicates
                for (String fieldVar:fieldVariables) {
                    if (statementI.contains(fieldVar)) {
                        if(statementJ.contains(fieldVar)) {
                            String currentPair = i + ":" + j;
                            Pair.add(currentPair);
                        }
                    }
                }
            }
        }

        // for every possible pair of methods minus the number of method pairs then LCOM is calculated
        // return has the name of class and LCOM value
        int possiblePairs = (mdList.size() * (mdList.size() - 1))/2;
        int accPairs = Pair.size();
        int lcom = possiblePairs - accPairs;
        returnString += " - LCOM1 - " + lcom + "\n";

        super.visit(cid, arg);
    }
}

