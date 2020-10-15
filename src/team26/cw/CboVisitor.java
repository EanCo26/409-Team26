package team26.cw;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CboVisitor extends VoidVisitorAdapter {


    String returnString = "";
    private int num = 0;
    FieldDeclaration fieldDec;
    ArrayList<String> classList = new ArrayList<>();
    ArrayList<ArrayList> listOfVarTypes = new ArrayList<>();

    public String returnOutput(CompilationUnit cu, Object arg){
        cu.accept(this, arg);
        //returnString += " - Number of Classes Coupled: " + num + "\n";
       /* for (String c: classList) {

            //returnString+= "CL = " + (c);
            for (String t: typeList)
            {
                returnString += c + " ?= " + t + "\n";
                if(c.equals(t))
                {
                    num++;
                }

            }
            //returnString+= "Class: " + c.toString() + " Score: " + num + "\n";
            num = 0;
        }*/

        for (int i = 0; i <=classList.size(); i++)
        {
            /*for (int j = 0; j <= listOfVarTypes.size(); j++)
            {
                if(listOfVarTypes.get(j).get(j).equals(i))
                {
                    num++;
                    returnString += "j: " + listOfVarTypes.get(j).get(j).toString() + "\n";
                    returnString += "j: " + classList.get(i) + "\n";
                    returnString += "Score: " + num + "\n";
                }
            }*/
            num =0;
        }

        return returnString;
    }



      public void visit(ClassOrInterfaceDeclaration n, Object arg){
        classList.add(n.getNameAsString());
        ArrayList<String> typeList = new ArrayList<>();
        //returnString += "Class Name: " + n.getNameAsString();
          for(FieldDeclaration fieldDec:n.getFields())
          {
              typeList.add(fieldDec.getVariable(0).getTypeAsString());
              //returnString +="VarTypes: " + fieldDec.getVariable(0).getTypeAsString() + "\n";
          }
          listOfVarTypes.add(typeList);
        //returnString +="TP: " + n.get;

        super.visit(n, arg);
    }

    public void visit(MethodCallExpr n, Object arg)
    {
        //num++;
        super.visit(n, arg);
    }

}
