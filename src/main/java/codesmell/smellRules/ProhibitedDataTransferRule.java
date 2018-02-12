package codesmell.smellRules;

import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import codesmell.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.BinaryOperator;


public class ProhibitedDataTransferRule extends AbstractSmell {

    private List<SmellyElement> smellyElementList;

    private boolean isClassUsingTheInternet;
    private VariableDeclarator networkInfo;
    private  String methodName;

    public ProhibitedDataTransferRule() {

        isClassUsingTheInternet = false;
        smellyElementList = new ArrayList<>();
    }

    /**
     * Checks of 'ProhibitedDataTransferRule' smellRules
     */
    @Override
    public String getSmellName() {
        return "ProhibitedDataTransferRule";
    }

    /**
     * Returns true if any of the elements has a smellRules
     */
    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    /**
     * Analyze the test file for test methods that use external resources
     */
    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {
        ProhibitedDataTransferRule.ClassVisitor classVisitor;
        classVisitor = new ProhibitedDataTransferRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);

        if (isClassUsingTheInternet){
            System.out.println("isClassUsingTheInternet class deal with internet");
        }
    }




    /**
     * Returns the set of analyzed elements (i.e. test methods)
     */
    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    private class ClassVisitor extends VoidVisitorAdapter<Void> {



        MethodChild methodChild;

        @Override
        public void visit(MethodCallExpr n, Void arg) {

            if (n.getName().asString().equalsIgnoreCase("getBackgroundDataSetting")){

                methodName = n.getName().asString();
            }else {
                methodName = null;
            }
            super.visit(n, arg);
        }

            @Override
            public void visit(VariableDeclarator n, Void arg) {

                if (n.getType().asString().equalsIgnoreCase("HttpURLConnection")
                        || n.getType().asString().equalsIgnoreCase("HttpClient")){

                    isClassUsingTheInternet = true;

                }

                if (n.getType().asString().equalsIgnoreCase("NetworkInfo")){ // get the name of NetworkInfo type
                    System.out.println(n.getType().asString() +"->"+ n.getNameAsString());

                    networkInfo = n;

                }

                if (methodName != null){
                    System.out.println(n.getType().asString() +"->"+ n.getNameAsString());

                }
                super.visit(n, arg);


            }





        @Override
        public void visit(IfStmt n, Void arg) {


            for (Node chNode:n.getCondition().getChildNodes()) {

                if (chNode instanceof BinaryExpr){

                    BinaryExpr condition = (BinaryExpr) chNode;

                    System.out.println( condition.getOperator());

                }

            }






            if (networkInfo != null){

            }
            super.visit(n, arg);
        }


    }





}
