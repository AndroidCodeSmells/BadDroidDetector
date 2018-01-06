package codesmell.smellRules;

import codesmell.entity.Method;
import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import codesmell.*;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.*;


public class TerminateOpenInternetConnectionRule extends AbstractSmell {

    private List<SmellyElement> smellyElementList;

    private boolean isClassUsingTheInternet;
    private boolean hasDisconnectMethod;

    public TerminateOpenInternetConnectionRule() {

        isClassUsingTheInternet = false;

        hasDisconnectMethod = false;
        smellyElementList = new ArrayList<>();
    }

    /**
     * Checks of 'ProhibitedDataTransferRule' smellRules
     */
    @Override
    public String getSmellName() {
        return "TerminateOpenInternetConnectionRule";
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
        TerminateOpenInternetConnectionRule.ClassVisitor classVisitor;
        classVisitor = new TerminateOpenInternetConnectionRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);

        if (isClassUsingTheInternet && !hasDisconnectMethod){
            // consider the class smelly

            Method smell = new Method("disconnect");
            smell.setHasSmell(true);
            smellyElementList.add(smell);

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
        public void visit(VariableDeclarator n, Void arg) {

            if (n.getType().asString().equalsIgnoreCase("HttpURLConnection")
                    || n.getType().asString().equalsIgnoreCase("HttpClient")){

                isClassUsingTheInternet = true;

            }


            super.visit(n, arg);


        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {


                String methodName = n.getName().asString();

                if (methodName.equalsIgnoreCase("disconnect")){
                    hasDisconnectMethod = true;

                }


            super.visit(n, arg);

        }






    }




}
