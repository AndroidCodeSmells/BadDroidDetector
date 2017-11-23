//package codesmell.smellRules;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.NodeList;
//import com.github.javaparser.ast.body.BodyDeclaration;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.FieldDeclaration;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.expr.AssignExpr;
//import com.github.javaparser.ast.expr.NameExpr;
//import com.github.javaparser.ast.stmt.BlockStmt;
//import com.github.javaparser.ast.stmt.ExpressionStmt;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import codesmell.*;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class SetConfigChangesRule extends AbstractSmell {
//
//    private List<SmellyElement> smellyElementList;
//    List<MethodDeclaration> methodList;
//    MethodDeclaration setupMethod;
//    List<FieldDeclaration> fieldList;
//    List<String> setupFields;
//
//    public SetConfigChangesRule() {
//        smellyElementList = new ArrayList<>();
//        methodList = new ArrayList<>();
//        fieldList = new ArrayList<>();
//        setupFields = new ArrayList<>();
//    }
//
//    /**
//     * Checks of 'General Fixture' smellRules
//     */
//    @Override
//    public String getSmellName() {
//        return "General Fixture";
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
//    @Override
//    public void runAnalysis(CompilationUnit testFileCompilationUnit,CompilationUnit productionFileCompilationUnit) throws FileNotFoundException {
//        SetConfigChangesRule.ClassVisitor classVisitor;
//        classVisitor = new SetConfigChangesRule.ClassVisitor();
//        classVisitor.visit(testFileCompilationUnit, null); //This call will populate the list of test methods and identify the setup Method [visit(ClassOrInterfaceDeclaration n)]
//
//        //Proceed with general fixture analysis if setup Method exists
//        if (setupMethod != null) {
//            //Get all fields that are initialized in the setup Method
//            //The following code block will identify the class level variables (i.e. fields) that are initialized in the setup Method
//            // TODO: There has to be a better way to do this identification/check!
//            Optional<BlockStmt> blockStmt = setupMethod.getBody();
//            NodeList nodeList = blockStmt.get().getStatements();
//            for (int i = 0; i < nodeList.size(); i++) {
//                for (int j = 0; j < fieldList.size(); j++) {
//                    for (int k = 0; k < fieldList.get(j).getVariables().size(); k++) {
//                        if (nodeList.get(i) instanceof ExpressionStmt) {
//                            ExpressionStmt expressionStmt = (ExpressionStmt) nodeList.get(i);
//                            if (expressionStmt.getExpression() instanceof AssignExpr) {
//                                AssignExpr assignExpr = (AssignExpr) expressionStmt.getExpression();
//                                if (fieldList.get(j).getVariable(k).getNameAsString().equals(assignExpr.getTarget().toString())) {
//                                    setupFields.add(assignExpr.getTarget().toString());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        for (MethodDeclaration Method : methodList) {
//            //This call will visit each test Method to identify the list of variables the Method contains [visit(MethodDeclaration n)]
//            classVisitor.visit(Method, null);
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
//
//    private class ClassVisitor extends VoidVisitorAdapter<Void> {
//        private MethodDeclaration methodDeclaration = null;
//        private MethodDeclaration currentMethod = null;
//        Method testMethod;
//        private int fixtureCount = 0;
//
//        @Override
//        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
//            NodeList<BodyDeclaration<?>> members = n.getMembers();
//            for (int i = 0; i < members.size(); i++) {
//                if (members.get(i) instanceof MethodDeclaration) {
//                    methodDeclaration = (MethodDeclaration) members.get(i);
//
//                    //Get a list of all test methods
//                    if (methodDeclaration.getAnnotationByName("Test").isPresent() || methodDeclaration.getNameAsString().toLowerCase().startsWith("test")) {
//                        methodList.add(methodDeclaration);
//                    }
//
//                    //Get the setup Method
//                    if (methodDeclaration.getNameAsString().toLowerCase().equals("setup")) {
//                        setupMethod = methodDeclaration;
//                    }
//                }
//
//                //Get all fields in the class
//                if (members.get(i) instanceof FieldDeclaration) {
//                    fieldList.add((FieldDeclaration) members.get(i));
//                }
//            }
//        }
//
//        // examine all methods in the test class
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            //only analyze methods that either have a @test annotation (Junit 4) or the Method name starts with 'test'
//            if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
//                currentMethod = n;
//
//                //call visit(NameExpr) for current Method
//                super.visit(n, arg);
//
//                testMethod = new Method(n.getNameAsString());
//                testMethod.setHasSmell(fixtureCount != setupFields.size());
//                smellyElementList.add(testMethod);
//
//                fixtureCount = 0;
//                currentMethod = null;
//            }
//        }
//
//        @Override
//        public void visit(NameExpr n, Void arg) {
//            if (currentMethod != null) {
//                //check if the variable contained in the current test Method is also contained in the setup Method
//                if (setupFields.contains(n.getNameAsString())) {
//                    fixtureCount++;
//                    //System.out.println(currentMethod.getNameAsString() + " : " + n.getName().toString());
//                }
//            }
//
//            super.visit(n, arg);
//        }
//
//
//    }
//}