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


    //Class names

    // Class dec
    // Method dec

    //if class dec  or method dec contains a class name +1
    //hash set
    //count appearance in hash set

    String classList = "";
    String methodList = "";
    List<String> typeList = new ArrayList<>();

    private  List<FieldDeclaration> fieldDec = new ArrayList<FieldDeclaration>();
    private  List<MethodDeclaration> methodDec = new ArrayList<MethodDeclaration>();

    int score = 0;
    private int num = 0;


    public String returnClassList(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);


        return classList;
    }
    public String returnMethodList(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);
        return methodList;
    }

    public void visit(MethodCallExpr n, Object arg)
    {

        String s = n.getParentNodeForChildren().toString();

        if (s.contains(".")) {

            String[] temp = new String[]{};
            temp = s.split("\\.");
            s=temp[0];
        }
        methodList += s + ".";
        super.visit(n, arg);
    }

    public int returnScore(CompilationUnit cu, Object arg) {
        cu.accept(this, arg);

        return score;
    }

    public void visit(ClassOrInterfaceDeclaration n, Object arg){
        classList +=  n.getNameAsString();
        super.visit(n, arg);
    }

}
