//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.ConstructorDeclaration;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.*;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
//
///*
//This class checks if the code file contains a Constructor. Ideally, the test suite should not have a constructor. Initialization of fields should be in the setUP() Method
//If this code detects the existence of a constructor, it sets the class as smelly
// */
//public class EarlyResourceBindingRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//
//    public EarlyResourceBindingRule() {
//        smellyElementList = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Constructor Initialization' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "Constructor Initialization";
//    }
//
//    /**
//     * Returns true if any of the elements has a smellRules
//     */
//    @Override
//    public boolean getHasSmell() {
//        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
//    }
//
//    /**
//     * Analyze the test file for Constructor Initialization smellRules
//     */
//    @Override
//    public void runAnalysis(CompilationUnit testFileCompilationUnit,CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//        EarlyResourceBindingRule.ClassVisitor classVisitor;
//        classVisitor = new EarlyResourceBindingRule.ClassVisitor();
//        classVisitor.visit(testFileCompilationUnit, null);
//    }
//
//    /**
//     * Returns the set of analyzed elements (i.e. test methods)
//     */
//    @Override
//    public List<SmellyElement> getSmellyElements() {
//        return smellyElementList;
//    }
//
//
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        Class testClass;
//
//        @Override
//        public void visit(ConstructorDeclaration n, Void arg) {
//            testClass = new Class(n.getNameAsString());
//            testClass.setHasSmell(true);
//            smellyElementList.add(testClass);
//        }
//    }
//}
