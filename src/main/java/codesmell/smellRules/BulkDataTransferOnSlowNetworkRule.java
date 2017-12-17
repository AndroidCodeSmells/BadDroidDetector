package codesmell.smellRules;

import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BulkDataTransferOnSlowNetworkRule extends codesmell.AbstractSmell {
    private List<SmellyElement> smellyElementList;

    private boolean isClassUsingInternet;
    private VariableDeclarator networkInfo;

    public BulkDataTransferOnSlowNetworkRule(){
        smellyElementList = new ArrayList<>();

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

        if (isClassUsingInternet){

            BulkDataTransferOnSlowNetworkRule.ClassCheckInternetConnectivityType classCheckInternetConnectivityType;
            classCheckInternetConnectivityType = new  BulkDataTransferOnSlowNetworkRule.ClassCheckInternetConnectivityType();
            classCheckInternetConnectivityType.visit(compilationUnit, null);
        }


    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    private class ClassCheckInternetConnectivityType extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(IfStmt n, Void arg) {

            if (networkInfo != null){ //  obtain the instance info for  networkInfo of reading the connectivity


                if (!n.getCondition().getChildNodes().toString().contains("ConnectivityManager.TYPE_WIFI")){

                    Method smell = new Method("TYPE_WIFI");
                    smell.setHasSmell(true);
                    smellyElementList.add(smell);
                }

                if (!n.getCondition().getChildNodes().toString().contains("ConnectivityManager.TYPE_MOBILE")){
                    Method smell = new Method("TYPE_MOBILE");
                    smell.setHasSmell(true);
                    smellyElementList.add(smell);

                }

                if (!n.getCondition().getChildNodes().toString().contains("isNetworkRoaming()")){
                    Method smell = new Method("isNetworkRoaming");
                    smell.setHasSmell(true);
                    smellyElementList.add(smell);

                }



            }

            super.visit(n, arg);
        }



    }
    private class ClassCheckInternetConnectivity extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(VariableDeclarator n, Void arg) {
            if (n.getType().asString().equalsIgnoreCase("HttpURLConnection")
                    || n.getType().asString().equalsIgnoreCase("HttpClient")){

                isClassUsingInternet = true;

            }

            if (n.getType().asString().equalsIgnoreCase("NetworkInfo")){ // get the name of NetworkInfo type

                networkInfo = n;
            }
            super.visit(n, arg);
        }


    }
}
