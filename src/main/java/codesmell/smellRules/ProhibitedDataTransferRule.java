package codesmell.smellRules;
//java
import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import codesmell.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;


import java.io.FileNotFoundException;
import java.util.*;


public class ProhibitedDataTransferRule extends AbstractSmell {

    private List<SmellyElement> smellyElementList;

    private boolean isClassUsingTheInternet;
    private VariableDeclarator networkInfo;
    private List<BinaryExpr> binaryExprList;
    private  Boolean isnetConcativeychecked = false;

    public ProhibitedDataTransferRule() {

        isClassUsingTheInternet = false;
        smellyElementList = new ArrayList<>();
        binaryExprList = new ArrayList<>();

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

        if (isClassUsingTheInternet) {


            for (BinaryExpr bi:binaryExprList
                 ) {

                if (!isnetConcativeychecked){
                    if (bi.getLeft() instanceof NameExpr){

                        if (bi.getRight() instanceof NullLiteralExpr){

                            if (bi.getOperator().asString().equalsIgnoreCase("==")){
                                isnetConcativeychecked = true;
                            }
                        }
                    }
                }


            }
          // check if the


            if (!isnetConcativeychecked){

                smellyElementList.add(new SmellyElement() {
                    @Override
                    public String getElementName() {
                        return null;
                    }

                    @Override
                    public boolean getHasSmell() {
                        return true;
                    }

                    @Override
                    public Map<String, String> getData() {
                        return null;
                    }
                });
            }
            System.out.println(getHasSmell());


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

        private  void extractACondition(BinaryExpr binaryExpr){

            if (!(binaryExpr.getLeft() instanceof BinaryExpr) && !(binaryExpr.getRight() instanceof BinaryExpr) ) {
                binaryExprList.add(binaryExpr);
            }

        }

        private void getChildCondition(IfStmt n){


            if (n.getCondition() instanceof BinaryExpr){

                BinaryExpr binaryExpr = (BinaryExpr) n.getCondition();
                if (binaryExpr.getLeft() instanceof BinaryExpr){
                    extractACondition(binaryExpr.getLeft().asBinaryExpr());
                }
                if ((binaryExpr.getRight() instanceof BinaryExpr) && (binaryExpr.getLeft() instanceof BinaryExpr)){
                }
                if  (binaryExpr.getRight() instanceof BinaryExpr){
                    extractACondition(binaryExpr.getRight().asBinaryExpr());
                }

                if (!(binaryExpr.getRight() instanceof BinaryExpr) && !(binaryExpr.getLeft() instanceof BinaryExpr)){
                    extractACondition(binaryExpr);
                }

            }else{

                if (!isnetConcativeychecked){
                    if (n.getCondition() instanceof UnaryExpr){

                        UnaryExpr unaryExpr = n.getCondition().asUnaryExpr();
                        if (unaryExpr.getExpression() instanceof  MethodCallExpr){
                            MethodCallExpr methodCallExpr = unaryExpr.getExpression().asMethodCallExpr();

                            if (methodCallExpr.getName().asString().equalsIgnoreCase("getBackgroundDataSetting")){


                                isClassUsingTheInternet = true;

                            }
                        }


                    }

                }

            }

        }


        @Override
        public void visit(VariableDeclarator n, Void arg) {

            if (n.getType().asString().equalsIgnoreCase("HttpURLConnection") || n.getType().asString().equalsIgnoreCase("HttpsURLConnection")
                    || n.getType().asString().equalsIgnoreCase("HttpClient")){

                isClassUsingTheInternet = true;

            }

            if (n.getType().asString().equalsIgnoreCase("NetworkInfo")){ // get the name of NetworkInfo type

                networkInfo = n;
            }

            super.visit(n, arg);


        }


        @Override
        public void visit(IfStmt n, Void arg) {
                super.visit(n, arg);


                getChildCondition(n);


        }


    }
}
