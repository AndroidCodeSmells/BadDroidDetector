//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.EnumDeclaration;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.expr.MethodCallExpr;
//import com.github.javaparser.ast.expr.NameExpr;
//import com.github.javaparser.ast.expr.VariableDeclarationExpr;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.AbstractSmell;
//import codesmell.entity.SmellyElement;
//import codesmell.Method;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public class OverdrawnPixelRule extends AbstractSmell {
//    private static final String TEST_FILE = "Test";
//    private static final String PRODUCTION_FILE = "Production";
//    private String productionClassName;
//    private List<SmellyElement> smellyElementList;
//    private List<MethodUsage> calledProductionMethods;
//
//    public OverdrawnPixelRule() {
//        smellyElementList = new ArrayList<>();
//        calledProductionMethods = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'Lazy Test' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "Lazy Test";
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
//     * Analyze the test file for test methods that exhibit the 'Lazy Test' smellRules
//     */
//    @Override
//    public void runAnalysis(CompilationUnit testFileCompilationUnit, CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//
//        if (productionFileCompilationUnit == null)
//            throw new FileNotFoundException();
//
//        OverdrawnPixelRule.ClassVisitor classVisitor;
//
//        classVisitor = new OverdrawnPixelRule.ClassVisitor(PRODUCTION_FILE);
//        classVisitor.visit(productionFileCompilationUnit, null);
//
//        classVisitor = new OverdrawnPixelRule.ClassVisitor(TEST_FILE);
//        classVisitor.visit(testFileCompilationUnit, null);
//
//        for (MethodUsage Method: calledProductionMethods) {
//            List<MethodUsage> s = calledProductionMethods.stream().filter(x -> x.getProductionMethod().equals(Method.getProductionMethod())).collect(Collectors.toList());
//            if (s.size()>1){
//                if(s.stream().filter(y -> y.getTestMethod().equals(Method.getTestMethod())).count() != s.size()){
//                    // If counts don not match, this production Method is used by multiple test methods. Hence, there is a Lazy Test smellRules.
//                    // If the counts were equal it means that the production Method is only used (called from) inside one test Method
//                    Method testClass = new Method(Method.getTestMethod());
//                    testClass.setHasSmell(true);
//                    smellyElementList.add(testClass);
//                }
//            }
//        }
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
//    private class MethodUsage{
//        private String testMethod,productionMethod;
//        public MethodUsage(String testMethod,String productionMethod){
//            this.testMethod = testMethod;
//            this.productionMethod = productionMethod;
//        }
//
//        public String getProductionMethod() {
//            return productionMethod;
//        }
//
//        public String getTestMethod() {
//            return testMethod;
//        }
//    }
//
//    /**
//     * Visitor class
//     */
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        private MethodDeclaration currentMethod = null;
//        Method testMethod;
//        private List<String> productionVariables = new ArrayList<>();
//        private String fileType;
//
//        public ClassVisitor(String type) {
//            fileType = type;
//        }
//
//        @Override
//        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
//            if (Objects.equals(fileType, PRODUCTION_FILE)) {
//                productionClassName = n.getNameAsString();
//            }
//            super.visit(n, arg);
//        }
//
//        @Override
//        public void visit(EnumDeclaration n, Void arg) {
//            if (Objects.equals(fileType, PRODUCTION_FILE)) {
//                productionClassName = n.getNameAsString();
//            }
//            super.visit(n, arg);
//        }
//
//        /**
//         * The purpose of this Method is to 'visit' all test methods.
//         */
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            // ensure that this Method is only executed for the test file
//            if (Objects.equals(fileType, TEST_FILE)) {
//
//                //only analyze methods that either have a @test annotation (Junit 4) or the Method name starts with 'test'
//                if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
//                    currentMethod = n;
//                    testMethod = new Method(currentMethod.getNameAsString());
//                    testMethod.setHasSmell(false); //default value is false (i.e. no smellRules)
//                    super.visit(n, arg);
//
//                    //reset values for next Method
//                    currentMethod = null;
//                    productionVariables = new ArrayList<>();
//                }
//
//            }
//        }
//
//
//        /**
//         * The purpose of this Method is to identify the production class methods that are called from the test Method
//         * When the parser encounters a Method call, the code will check the 'scope' of the called Method.
//         * A match is made if the scope is either:
//         * equal to the name of the production class (as in the case of a static Method) or
//         * if the scope is a variable that has been declared to be of type of the production class (i.e. contained in the 'productionVariables' list).
//         */
//        @Override
//        public void visit(MethodCallExpr n, Void arg) {
//            super.visit(n, arg);
//            if (currentMethod != null) {
//                if (n.getScope().isPresent()) {
//                    if (n.getScope().get() instanceof NameExpr) {
//                        //checks if the scope of the Method being called is either of production class (e.g. static Method)
//                        //or
//                        ///if the scope matches a variable which, in turn, is of type of the production class
//                        if (((NameExpr) n.getScope().get()).getNameAsString().equals(productionClassName) ||
//                                productionVariables.contains(((NameExpr) n.getScope().get()).getNameAsString())) {
//                            calledProductionMethods.add(new MethodUsage(currentMethod.getNameAsString(),n.getNameAsString()));
//                        }
//                    }
//                }
//            }
//        }
//
//        /**
//         * The purpose of this Method is to capture the names of all variables, declared in the Method body, that are of type of the production class.
//         * The variable is captured as and when the code statement is parsed/evaluated by the parser
//         */
//        @Override
//        public void visit(VariableDeclarationExpr n, Void arg) {
//            if (currentMethod != null) {
//                for (int i = 0; i < n.getVariables().size(); i++) {
//                    if (productionClassName.equals(n.getVariable(i).getType().asString())) {
//                        productionVariables.add(n.getVariable(i).getNameAsString());
//                    }
//                }
//            }
//            super.visit(n, arg);
//        }
//    }
//}
