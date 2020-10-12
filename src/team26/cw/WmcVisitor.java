package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

class MethodDetails {
    private String methodName = "";
    private int methodDecisions = 0;

    public void setMethodName(String mName){
        this.methodName = mName;
    }
    public String getMethodName(){
        return this.methodName;
    }

    public void addMethodDecisions(int mDecisions){
        this.methodDecisions += mDecisions;
    }
    public int getMethodDecisions(){
        return this.methodDecisions;
    }
}

class WmcVisitor extends VoidVisitorAdapter {

    String returnString = "";
    private List<MethodDetails> lMD = new ArrayList<MethodDetails>();

    public String returnOutput(CompilationUnit cu, Object arg){
        visit(cu, arg);
        if(!lMD.isEmpty()){
            for (MethodDetails details: lMD) {
                returnString += "   Method Name: " + details.getMethodName()
                        + " has Complexity of: " + details.getMethodDecisions() + "\n";
            }
            lMD.clear();
        }
        //add method list attributes to string here?
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

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
        if(!lMD.isEmpty()){
            for (MethodDetails details: lMD) {
                returnString += "   Method Name: " + details.getMethodName()
                        + " number of decisions: " + details.getMethodDecisions() + "\n";
            }
            lMD.clear();
        }
        returnString += "Class: " + cid.getName() + " Number of Methods: " + cid.getMethods().size() + "\n";
        super.visit(cid, arg);
    }

    @Override
    public void visit(MethodDeclaration md, Object arg) {

        MethodDetails methodDetails = new MethodDetails();
        methodDetails.setMethodName(md.getNameAsString());
        methodDetails.addMethodDecisions(1);
        lMD.add(methodDetails);

        super.visit(md, arg);
    }

    @Override
    public void visit(IfStmt ifStmt, Object arg) {
        String conditionStmt = ifStmt.getCondition().toString();
        lMD.get(lMD.size()-1).addMethodDecisions(numOfConditions(conditionStmt));

        super.visit(ifStmt, arg);
    }
    @Override
    public void visit(SwitchStmt swStmt, Object arg) {
        NodeList<SwitchEntry> caseStmts = swStmt.getEntries();
        lMD.get(lMD.size()-1).addMethodDecisions(caseStmts.size());

        super.visit(swStmt, arg);
    }

    @Override
    public void visit(WhileStmt whStmt, Object arg) {
        String conditionStmt = whStmt.getCondition().toString();
        lMD.get(lMD.size()-1).addMethodDecisions(numOfConditions(conditionStmt));
        super.visit(whStmt, arg);
    }
    @Override
    public void visit(DoStmt doStmt, Object arg) {
        lMD.get(lMD.size()-1).addMethodDecisions(1);
        super.visit(doStmt, arg);
    }
    @Override
    public void visit(ForStmt forStmt, Object arg) {
        lMD.get(lMD.size()-1).addMethodDecisions(1);
        super.visit(forStmt, arg);
    }
    @Override
    public void visit(ForEachStmt forEachStmt, Object arg) {
        lMD.get(lMD.size()-1).addMethodDecisions(1);
        super.visit(forEachStmt, arg);
    }
//
//    @Override
//    public void visit(BlockStmt bStmt, Object arg) {
////        for(Statement s : bStmt.getStatements()){
////        }
//        super.visit(bStmt, arg);
//    }
}
