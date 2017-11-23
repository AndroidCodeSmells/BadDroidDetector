//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.*;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * This class checks if a test Method is empty (i.e. the Method does not contain statements in its body)
// * If the the number of statements in the body is 0, then the Method is smelly
// */
//public class UncontrolledFocusOrderRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//
//    public UncontrolledFocusOrderRule() {
//        smellyElementList = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Empty Test' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "UncontrolledFocusOrderRule";
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
//     * Analyze the test file for test methods that are empty (i.e. no Method body)
//     */
//    @Override
//    public void runAnalysis(CompilationUnit testFileCompilationUnit,CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//        UncontrolledFocusOrderRule.ClassVisitor classVisitor;
//        classVisitor = new UncontrolledFocusOrderRule.ClassVisitor();
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
//    /**
//     * Visitor class
//     */
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        Method testMethod;
//
//        /**
//         * The purpose of this Method is to 'visit' all test methods in the test file
//         */
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            //only analyze methods that either have a @test annotation (Junit 4) or the Method name starts with 'test'
//            if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
//                testMethod = new Method(n.getNameAsString());
//                testMethod.setHasSmell(false); //default value is false (i.e. no smellRules)
//                //Method should not be abstract
//                if (!n.isAbstract()) {
//                    if (n.getBody().isPresent()) {
//                        //get the total number of statements contained in the Method
//                        if (n.getBody().get().getStatements().size() == 0) {
//                            testMethod.setHasSmell(true); //the Method has no statements (i.e no body)
//                        }
//                    }
//                }
//                smellyElementList.add(testMethod);
//            }
//        }
//    }
//}
