package codesmell.smellRules;

import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;


import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class TrackingHardwareIdRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;
    private  String  currentMethods;
    public TrackingHardwareIdRule() {
        smellyElementList = new ArrayList<>();

    }

    @Override
    public String getSmellName() {
        return "TrackingHardwareIdRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

           TrackingHardwareIdRule.ClassVisitor classVisitor;
           classVisitor = new TrackingHardwareIdRule.ClassVisitor();
           classVisitor.visit(compilationUnit, null);


    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    private class ClassVisitor extends VoidVisitorAdapter<Void> {


        @Override
        public void visit(MethodCallExpr n, Void arg) {
            String methodName = n.getName().asString();
            if (methodName.equalsIgnoreCase("getImei") ||
                    methodName.equalsIgnoreCase("getMeid") ||
                    methodName.equalsIgnoreCase("getDeviceId") ){

                Method smell = new Method(n.getNameAsString());
                smell.setHasSmell(true);
                smellyElementList.add(smell);

            }
            super.visit(n, arg);

        }


    }

}