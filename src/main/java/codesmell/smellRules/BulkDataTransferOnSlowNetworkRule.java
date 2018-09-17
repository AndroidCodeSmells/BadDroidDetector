package codesmell.smellRules;
// need java
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class  variableDeclaratorInitializer{

    public String varName;
    public MethodCallExpr initializer;

    public variableDeclaratorInitializer( String varName,MethodCallExpr initializer){
        this.varName = varName;
        this.initializer = initializer;
    }
    public variableDeclaratorInitializer(MethodCallExpr initializer){
        this.varName = null;
        this.initializer = initializer;
    }

    String getInitializersScop(){

        if ( initializer.getScope().isPresent() && initializer.getScope().get().isNameExpr()){
            return initializer.getScope().get().asNameExpr().getName().asString();

        }
        return "";
    }

    String getInitializersName(){

        return initializer.getNameAsString();
    }

    @Override
    public String toString() {
        return initializer.getNameAsString();
    }
}
public class BulkDataTransferOnSlowNetworkRule extends codesmell.AbstractSmell {



    private List<SmellyElement> smellyElementList;

    private boolean isClassUsingInternet;
    private VariableDeclarator networkInfo;
    private String netType;
    private String netSubtype;
    private  boolean isCheckingWIFI = false;
    private  boolean isChecking3GAbove = false;

    private List<variableDeclaratorInitializer> methodCallExprList;
    private List<BinaryExpr> binaryExprList;


    public BulkDataTransferOnSlowNetworkRule(){
        smellyElementList = new ArrayList<>();
        methodCallExprList = new  ArrayList<>();
        binaryExprList = new  ArrayList<>();



    }
    @Override
    public String getSmellName() {
        return "BulkDataTransferOnSlowNetworkRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {



        // check if using internet 
        BulkDataTransferOnSlowNetworkRule.ClassCheckInternetConnectivity classCheckInternetConnectivity;
        classCheckInternetConnectivity = new  BulkDataTransferOnSlowNetworkRule.ClassCheckInternetConnectivity();
        classCheckInternetConnectivity.visit(compilationUnit, null);

        for (variableDeclaratorInitializer m: methodCallExprList) {
            if (networkInfo != null){

                if (networkInfo.getName().asString().equalsIgnoreCase(m.getInitializersScop())){

                    if (m.getInitializersName().equalsIgnoreCase("getType")){
                        netType = m.varName;
                    }
                    if (m.getInitializersName().equalsIgnoreCase("getSubtype")){
                        netSubtype = m.varName;
                    }

                }
            }


        }

        if (isClassUsingInternet){

            // check if the developer check the internet speed.


            for (BinaryExpr b: binaryExprList) {



                if (netType != null){  //check for the wifi

                    setCheckingWIFI(b);
                }

                if (netSubtype != null){ //check for high speed
                    setChecking3GAbove(b);
                }



            }

            // determine if the

            if (!isCheckingWIFI && !isChecking3GAbove){

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
        }

        System.out.println(getHasSmell());

    }

    void setCheckingWIFI(BinaryExpr b){



        if (b.getLeft() instanceof FieldAccessExpr){
            FieldAccessExpr bf = b.getLeft().asFieldAccessExpr();
            if (!isCheckingWIFI) {


                if (bf.getName().asString().equalsIgnoreCase("TYPE_WIFI")) {
                    isCheckingWIFI = true;

                }
            }
        }

        if (b.getRight() instanceof FieldAccessExpr){
            FieldAccessExpr bf = b.getRight().asFieldAccessExpr();

            if (!isCheckingWIFI) {
                if (bf.getName().asString().equalsIgnoreCase("TYPE_WIFI")) {
                    isCheckingWIFI = true;
                }
            }

        }
        }

    void setChecking3GAbove(BinaryExpr bb){

        if (bb.getLeft() instanceof FieldAccessExpr){
            FieldAccessExpr bf = bb.getLeft().asFieldAccessExpr();
            if (!isChecking3GAbove) {


                if (bf.getName().asString().equalsIgnoreCase("NETWORK_TYPE_LTE") || bf.getName().asString().equalsIgnoreCase("NETWORK_TYPE_UMTS")) {
                    isChecking3GAbove = true;
                }
            }
        }

        if (bb.getRight() instanceof FieldAccessExpr){
            FieldAccessExpr bf = bb.getRight().asFieldAccessExpr();

            if (!isChecking3GAbove) {
                if (bf.getName().asString().equalsIgnoreCase("NETWORK_TYPE_LTE") || bf.getName().asString().equalsIgnoreCase("NETWORK_TYPE_UMTS")) {
                    isChecking3GAbove = true;
                }
            }

        }
    }


    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    // check if the class deall with internet
    private class ClassCheckInternetConnectivity extends VoidVisitorAdapter<Void> {


        @Override
        public void visit(VariableDeclarator n, Void arg) {
            if (n.getType().asString().equalsIgnoreCase("HttpURLConnection") || n.getType().asString().equalsIgnoreCase("HttpsURLConnection")
                    || n.getType().asString().equalsIgnoreCase("HttpClient")){

                isClassUsingInternet = true;

            }
            if (n.getInitializer().isPresent()){

                if (n.getInitializer().get() instanceof  MethodCallExpr){
                    MethodCallExpr methodCallExpr = n.getInitializer().get().asMethodCallExpr();

                    methodCallExprList.add(new variableDeclaratorInitializer(n.getName().asString(),methodCallExpr));

                }


            }
            if (n.getType().asString().equalsIgnoreCase("NetworkInfo")){ // get the name of NetworkInfo type

                networkInfo = n;
            }

            super.visit(n, arg);
        }

        @Override
        public void visit(AssignExpr n, Void arg) {
            super.visit(n, arg);

            if (n.getTarget() instanceof  MethodCallExpr){
                MethodCallExpr methodCallExpr = n.getTarget().asMethodCallExpr();

                methodCallExprList.add(new variableDeclaratorInitializer(n.getValue().asNameExpr().getName().asString(),methodCallExpr));

            }

        }

        @Override
        public void visit(IfStmt n, Void arg) {
            // trying to check if the developer checking the internet speed before uploading date
            super.visit(n, arg);


                getChildCondition(n);

        }



    }


    private  void extractCondition(BinaryExpr binaryExpr){

        if (!(binaryExpr.getLeft() instanceof BinaryExpr) && !(binaryExpr.getRight() instanceof BinaryExpr) ) {
            binaryExprList.add(binaryExpr);
        }

    }

    private void getChildCondition(IfStmt n){


        if (n.getCondition() instanceof BinaryExpr){

            BinaryExpr binaryExpr = (BinaryExpr) n.getCondition();
            if (binaryExpr.getLeft() instanceof BinaryExpr){
                extractCondition(binaryExpr.getLeft().asBinaryExpr());
            }
            if ((binaryExpr.getRight() instanceof BinaryExpr) && (binaryExpr.getLeft() instanceof BinaryExpr)){
            }
            if  (binaryExpr.getRight() instanceof BinaryExpr){
                extractCondition(binaryExpr.getRight().asBinaryExpr());
            }

            if (!(binaryExpr.getRight() instanceof BinaryExpr) && !(binaryExpr.getLeft() instanceof BinaryExpr)){
                extractCondition(binaryExpr);
            }

        }

    }
}
