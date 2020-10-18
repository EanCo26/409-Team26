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
    private List<FieldDeclaration> fdList = new ArrayList<FieldDeclaration>();
    private List<MethodDeclaration> mdList = new ArrayList<MethodDeclaration>();

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

        fdList = cid.getFields();
        mdList = cid.getMethods();

        // get list of all variables names in fields
        List<String> fieldVariables = new ArrayList<>();
        for (FieldDeclaration fieldDec:fdList) {
            for (VariableDeclarator varDec : fieldDec.getVariables()) {
                fieldVariables.add(varDec.getNameAsString());
            }
        }

        // list of pairs of methods
        HashSet<String> Pair = new HashSet<>();
        // loop through every method
        for(int i = 0; i < mdList.size(); i++){
            statementI = "";

            // get every statement within method body and add to string
            MethodDeclaration methodDeclarationI = mdList.get(i);
            methodDeclarationI.getBody().ifPresent(blockStmt -> {
                BlockStmt bStmt = methodDeclarationI.getBody().get();
                for (Statement stmt : bStmt.getStatements()) {
                        statementI += stmt.toString();
                }
            });
            // loop through every method that comes after method in previous loop
            for(int j = i; j < mdList.size(); j++) {
                statementJ = "";
                if(i==j){
                    continue;
                }

                // get every statement within method body and add to string
                MethodDeclaration methodDeclarationJ = mdList.get(j);
                methodDeclarationJ.getBody().ifPresent(blockStmt -> {
                    BlockStmt bStmt = methodDeclarationJ.getBody().get();
                    for (Statement stmt : bStmt.getStatements()) {
                        statementJ += stmt.toString();
                    }
                });

                // if a field shows up in first method body and second method body then add to list of method pairs
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

