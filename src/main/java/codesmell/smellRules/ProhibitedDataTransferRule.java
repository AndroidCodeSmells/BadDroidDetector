package codesmell.smellRules;

import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import codesmell.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.*;


public class ProhibitedDataTransferRule extends AbstractSmell {

    private List<SmellyElement> smellyElementList;

    private boolean isClassUsingTheInternet;
    private VariableDeclarator networkInfo;

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
            public void visit(VariableDeclarator n, Void arg) {

                System.out.println(n.getType().asString() +"->"+ n.getNameAsString());
                if (n.getType().asString().equalsIgnoreCase("HttpURLConnection")
                        || n.getType().asString().equalsIgnoreCase("HttpClient")){

                    isClassUsingTheInternet = true;

                }

                if (n.getType().asString().equalsIgnoreCase("NetworkInfo")){ // get the name of NetworkInfo type

                    networkInfo = n;

                }

                super.visit(n, arg);


            }

        @Override
        public void visit(MethodCallExpr n, Void arg) {
            System.out.println("IfStmt   "+n.getScope().toString());
            super.visit(n, arg);
        }

        @Override
        public void visit(IfStmt n, Void arg) {
            System.out.println("IfStmt   "+n.getCondition().getChildNodes().toString());

            if (networkInfo != null){
                System.out.println("IfStmt   "+n.getCondition().getChildNodes().toString());

            }
            super.visit(n, arg);
        }
    }




}
