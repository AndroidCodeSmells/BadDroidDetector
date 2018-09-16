package codesmell.smellRules;
//java
import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;


import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class EarlyResourceBindingRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;
    private  String  currentMethod, foundSmellMethod ;
    private List<MethodChild> methodChildList ;
    public EarlyResourceBindingRule() {
        smellyElementList = new ArrayList<>();
        methodChildList = new ArrayList<>();

    }

    @Override
    public String getSmellName() {
        return "EarlyResourceBindingRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        EarlyResourceBindingRule.ClassVisitor classVisitor;
        classVisitor = new EarlyResourceBindingRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);

        if (foundSmellMethod!=null){
            for (MethodChild m : methodChildList) {

                if (m.has("requestLocationUpdates") && m.getMethodName().equalsIgnoreCase("onCreate")){
                    Method smell = new Method(foundSmellMethod);
                    smell.setHasSmell(true);
                    smellyElementList.add(smell);
                }
            }


        }

    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    private class ClassVisitor extends VoidVisitorAdapter<Void> {
        MethodChild methodChild;
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            currentMethod = n.getNameAsString();
            methodChild = new MethodChild(n.getNameAsString());

            super.visit(n, arg);

            methodChildList.add(methodChild);

        }
        @Override
        public void visit(MethodCallExpr n, Void arg) {


            if (currentMethod!= null){
                String methodName = n.getName().asString();

                methodChild.addCallMethod(methodName);
                if (methodName.equalsIgnoreCase("requestLocationUpdates")){

                    foundSmellMethod = currentMethod;


                }
            }

            super.visit(n, arg);

        }


    }

}