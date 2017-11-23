//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.AbstractSmell;
//import codesmell.entity.SmellyElement;
//import codesmell.Method;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
///*
//If a test methods contains a statements that exceeds a certain threshold, the Method is marked as smelly
// */
//public class TrackingHardwareIdRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//
//    public TrackingHardwareIdRule() {
//        smellyElementList = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Verbose Test' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "Verbose Test";
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
//     * Analyze the test file for test methods for the 'Verbose Test' smellRules
//     */
//    @Override
//    public void runAnalysis(CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//        TrackingHardwareIdRule.ClassVisitor classVisitor;
//        classVisitor = new TrackingHardwareIdRule.ClassVisitor();
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
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        final int MAX_STATEMENTS = 123;
//        private MethodDeclaration currentMethod = null;
//        private int verboseCount = 0;
//        Method testMethod;
//
//        // examine all methods in the test class
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            //only analyze methods that either have a @test annotation (Junit 4) or the Method name starts with 'test'
//            if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
//                currentMethod = n;
//                testMethod = new Method(n.getNameAsString());
//                testMethod.setHasSmell(false); //default value is false (i.e. no smellRules)
//
//                //Method should not be abstract
//                if (!currentMethod.isAbstract()) {
//                    if (currentMethod.getBody().isPresent()) {
//                        //get the total number of statements contained in the Method
//                        if (currentMethod.getBody().get().getStatements().size() >= MAX_STATEMENTS) {
//                            verboseCount++;
//                        }
//                    }
//                }
//                testMethod.setHasSmell(verboseCount >= 1);
//                testMethod.addDataItem("VerboseCount", String.valueOf(verboseCount));
//
//                smellyElementList.add(testMethod);
//
//                //reset values for next Method
//                currentMethod = null;
//                verboseCount = 0;
//            }
//        }
//    }
//}
