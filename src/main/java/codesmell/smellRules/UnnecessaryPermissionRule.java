//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.expr.FieldAccessExpr;
//import com.github.javaparser.ast.expr.MethodCallExpr;
//import com.github.javaparser.ast.expr.NameExpr;
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
//Test methods should not contain print statements as execution of unit tests is an automated process with little to no human intervention. Hence, print statements are redundant.
//This code checks the body of each test Method if System.out. print(), println(), printf() and write() methods are called
// */
//public class UnnecessaryPermissionRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//
//    public UnnecessaryPermissionRule() {
//        smellyElementList = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Print Statement' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "Print Statement";
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
//     * Analyze the test file for test methods that print output to the console
//     */
//    @Override
//    public void runAnalysis(CompilationUnit testFileCompilationUnit,CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//        UnnecessaryPermissionRule.ClassVisitor classVisitor;
//        classVisitor = new UnnecessaryPermissionRule.ClassVisitor();
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
//        private MethodDeclaration currentMethod = null;
//        private int printCount = 0;
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
//                super.visit(n, arg);
//
//                testMethod.setHasSmell(printCount >= 1);
//                testMethod.addDataItem("PrintCount", String.valueOf(printCount));
//
//                smellyElementList.add(testMethod);
//
//                //reset values for next Method
//                currentMethod = null;
//                printCount = 0;
//            }
//        }
//
//        // examine the methods being called within the test Method
//        @Override
//        public void visit(MethodCallExpr n, Void arg) {
//            super.visit(n, arg);
//            if (currentMethod != null) {
//                // if the name of a Method being called is 'print' or 'println' or 'printf' or 'write'
//                if (n.getNameAsString().equals("print") || n.getNameAsString().equals("println") || n.getNameAsString().equals("printf") || n.getNameAsString().equals("write")) {
//                    //check the scope of the Method & proceed only if the scope is "out"
//                    if ((n.getScope().isPresent() &&
//                            n.getScope().get() instanceof FieldAccessExpr &&
//                            (((FieldAccessExpr) n.getScope().get())).getNameAsString().equals("out"))) {
//
//                        FieldAccessExpr f1 = (((FieldAccessExpr) n.getScope().get()));
//
//                        //check the scope of the field & proceed only if the scope is "System"
//                        if ((f1.getScope() != null &&
//                                f1.getScope() instanceof NameExpr &&
//                                ((NameExpr) f1.getScope()).getNameAsString().equals("System"))) {
//                            //a print statement exists in the Method body
//                            printCount++;
//                        }
//                    }
//
//                }
//            }
//        }
//
//    }
//}
