package codesmell.smellRules;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
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
    public void runAnalysis(CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
        ClassVisitor classVisitor;
        classVisitor = new ClassVisitor();
        classVisitor.visit(productionFileCompilationUnit, null);
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

                TestMethod  testMethod = new TestMethod("knalid");
                smellyElementList.add( testMethod);
                testMethod.setHasSmell(true);
            }




        }


    }
}
