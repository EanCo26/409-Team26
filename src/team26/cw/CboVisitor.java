package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.awt.desktop.SystemEventListener;
import java.util.ArrayList;
import java.util.List;

public class CboVisitor extends VoidVisitorAdapter {

    String classList = "";
    String methodList = "";
    List<String> typeList = new ArrayList<>();

    private  List<FieldDeclaration> fieldDec = new ArrayList<FieldDeclaration>();
    private  List<MethodDeclaration> methodDec = new ArrayList<MethodDeclaration>();

    //Passes classList to MetricCalc
    public String returnClassList(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);
        return classList;
    }

    //Passes methodList to MetricCalc
    public String returnMethodList(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);
        return methodList;
    }

    //Visits each class and returns the methods called within it
    public void visit(MethodCallExpr n, Object arg)
    {
        String s = n.getParentNodeForChildren().toString();

        //Splits the 's' string using at the '.' to remove
        //unnecessary information before methodList is returned
        if (s.contains(".")) {

            String[] temp = new String[]{};
            temp = s.split("\\.");
            s=temp[0];
        }
        methodList += s + "."; //Adds '.' to separate each method call

        super.visit(n, arg);
    }


    //Visits each class and records its name
    public void visit(ClassOrInterfaceDeclaration n, Object arg){
        classList +=  n.getNameAsString();
        super.visit(n, arg);
    }

}
