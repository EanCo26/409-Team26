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

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        recordMethodsInClass();
        return returnString;
    }

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

    Boolean isWithinMethod(Statement statement){
        int line = statement.getEnd().get().line;
        for (MethodDetails details: lMD) {
            if (line >= details.getBeginLine() && line <= details.getEndLine()) {
                return true;
            }
        }
        return false;
    }

    public void recordMethodsInClass(){
        int sumComplex = 0;
        if(!lMD.isEmpty()){
            for (MethodDetails details: lMD) {
                returnString += "      Method Name: " + details.getMethodName()
                        + " - Simple Complexity: " + details.getMethodDecisions() + "\n";
                sumComplex += details.getMethodDecisions();
            }
            returnString += "   Sum of Class Complexity: " + sumComplex + "\n";
            lMD.clear();
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {

        recordMethodsInClass();
        returnString += "   Class: " + cid.getName()
                + " - Number of Methods: " + cid.getMethods().size() + "\n";

        super.visit(cid, arg);
    }
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
