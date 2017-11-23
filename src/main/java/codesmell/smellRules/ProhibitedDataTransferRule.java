package codesmell.smellRules;

import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import codesmell.*;

import java.io.FileNotFoundException;
import java.util.*;

public class ProhibitedDataTransferRule extends AbstractSmell {

    private List<SmellyElement> smellyElementList;

    public ProhibitedDataTransferRule() {
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
    public void runAnalysis(CompilationUnit compilationUnit) throws FileNotFoundException {
        ClassVisitor classVisitor;
        classVisitor = new ClassVisitor();
        classVisitor.visit(compilationUnit, null);
    }

    /**
     * Returns the set of analyzed elements (i.e. test methods)
     */
    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }
    private MethodDeclaration currentMethod = null;

    private class ClassVisitor extends VoidVisitorAdapter<Void> {





        // examine all methods in the test class
        @Override
        public void visit(MethodDeclaration n, Void arg) {

            if (n.getNameAsString().equalsIgnoreCase("onClick")) {

                Method Method = new Method("knalid");
                smellyElementList.add(Method);
                Method.setHasSmell(true);
            }




        }


    }
}
