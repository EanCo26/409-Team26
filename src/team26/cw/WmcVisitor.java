package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

class WmcVisitor extends VoidVisitorAdapter {
    String returnString = "";
    public String returnOutput(CompilationUnit cu, Object arg){
        visit(cu, arg);
        return returnString;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
        returnString += "Class: " + cid.getName() + " Number of Methods: " + cid.getMethods().size() + "\n";
        super.visit(cid, arg);
    }

    private int cc = 0;
    private String curMethodName = "";
    @Override
    public void visit(MethodDeclaration md, Object arg) {
        if(curMethodName != "") {
            returnString += "   Method Name: " + curMethodName + " has Complexity of: " + cc + "\n";
        }
        super.visit(md, arg);
    }

    @Override
    public void visit(BlockStmt bStmt, Object arg) {
        for(Statement s : bStmt.getStatements()){
            if(s.isForStmt()){
            }
            if(s.isForEachStmt()){
            }
            if(s.isWhileStmt()){
            }

            //Needs work to count different branches
            if(s.isIfStmt()){
                if(s.asIfStmt().getElseStmt().isPresent()) {
                }
            }
        }

        super.visit(bStmt, arg);
    }
}
