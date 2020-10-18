package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

public class WmcVisitor extends VoidVisitorAdapter {

    String returnString = "";
    private List<MethodDetails> lMD = new ArrayList<MethodDetails>();

    // Start with accept to pass to visits
    // records all methods in last class in file
    // returns string of outputs
    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        recordMethodsInClass();
        return returnString;
    }

    // if condition has &&/|| then add 1 condition every time either operator appears
    public int numOfConditions(String conditionExpr){
        int num = 1;
        if(conditionExpr.contains("||")||conditionExpr.contains("&&")){
            for(int i = 0; i < conditionExpr.length(); i++){
                if(conditionExpr.charAt(i) == '&' && conditionExpr.charAt(i-1) == '&'){
                    num++;
                }
                if(conditionExpr.charAt(i) == '|' && conditionExpr.charAt(i-1) == '|'){
                    num++;
                }
            }
        }
        return num;
    }

    // done at statement visitors to check that statements are not in constructor
    Boolean isWithinMethod(Statement statement){
        int line = statement.getEnd().get().line;
        for (MethodDetails details: lMD) {
            if (line >= details.getBeginLine() && line <= details.getEndLine()) {
                return true;
            }
        }
        return false;
    }

    // Adds all methods in class to be returned in returnOutput method as well as MCC value
    public void recordMethodsInClass(){
        int sumComplex = 0;
        if(!lMD.isEmpty()){
            for (MethodDetails details: lMD) {
                returnString += "      Method Name: " + details.getMethodName()
                        + " - Simple Complexity: " + details.getMethodDecisions() + "\n";
                sumComplex += details.getMethodDecisions();
            }
            // this is the WMC McCabe's Cyclomatic Complexity aggregated from all method complexities in class
            returnString += "   WMC MCC: " + sumComplex + "\n";
            lMD.clear();
        }
    }

    // for every class get the WMC Basic value
    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
        // records list of previous methods and then clears list - this done here to ensure that every class has it own recorded methods
        recordMethodsInClass();
        // This is where WMC basic is found - the number of methods with unity of 1
        returnString += "   Class: " + cid.getName()
                + " - WMC Basic: " + cid.getMethods().size() + "\n";

        super.visit(cid, arg);
    }
    // for every new method add to list of method in current class and set attributes
    @Override
    public void visit(MethodDeclaration md, Object arg) {
        lMD.add(new MethodDetails());
        lMD.get(lMD.size()-1).setMethodName(md.getNameAsString());
        lMD.get(lMD.size()-1).addMethodDecisions(1);
        md.getBody().ifPresent(blockStmt -> {
            BlockStmt block = md.getBody().get();
            lMD.get(lMD.size()-1).setMethodLines(block.getBegin().get().line,
                    block.getEnd().get().line);
        });

        super.visit(md, arg);
    }


    // Every statement is checked whether it is inside method scope - i.e. not in constructor
    // add to complexity of the current method
    // IfStmt, WhileStmt, DoStmt check for && and || operators - look at numOfConditions Method
    @Override
    public void visit(IfStmt ifStmt, Object arg) {

        String conditionStmt = ifStmt.getCondition().toString();
        if(isWithinMethod(ifStmt)) {
            lMD.get(lMD.size() - 1).addMethodDecisions(numOfConditions(conditionStmt));
        }
        super.visit(ifStmt, arg);
    }
    @Override
    public void visit(SwitchStmt swStmt, Object arg) {

        if(isWithinMethod(swStmt)) {
            NodeList<SwitchEntry> caseStmts = swStmt.getEntries();
            lMD.get(lMD.size() - 1).addMethodDecisions(caseStmts.size());
        }
        super.visit(swStmt, arg);
    }

    //loop statements
    @Override
    public void visit(WhileStmt whStmt, Object arg) {

        String conditionStmt = whStmt.getCondition().toString();
        if(isWithinMethod(whStmt)) {
            lMD.get(lMD.size() - 1).addMethodDecisions(numOfConditions(conditionStmt));
        }
        super.visit(whStmt, arg);
    }
    @Override
    public void visit(DoStmt doStmt, Object arg) {

        String conditionStmt = doStmt.getCondition().toString();
        if(isWithinMethod(doStmt)) {
            lMD.get(lMD.size() - 1).addMethodDecisions(numOfConditions(conditionStmt));
        }
        super.visit(doStmt, arg);
    }
    @Override
    public void visit(ForStmt forStmt, Object arg) {

        if(isWithinMethod(forStmt)) {
            lMD.get(lMD.size() - 1).addMethodDecisions(1);
        }
        super.visit(forStmt, arg);
    }
    @Override
    public void visit(ForEachStmt forEachStmt, Object arg) {

        if(isWithinMethod(forEachStmt)) {
            lMD.get(lMD.size() - 1).addMethodDecisions(1);
        }
        super.visit(forEachStmt, arg);
    }
}
