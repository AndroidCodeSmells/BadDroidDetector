package codesmell.smellRules;

import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import codesmell.entity.Variable;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;


import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class InterruptingFromBackgroundRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;
    private boolean isClassHasBroadcastRecieversOrServices;

    public InterruptingFromBackgroundRule() {
        smellyElementList = new ArrayList<>();
        isClassHasBroadcastRecieversOrServices = false;

    }

    @Override
    public String getSmellName() {
        return "InterruptingFromBackgroundRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        InterruptingFromBackgroundRule.ClassVisitor classVisitor;
        classVisitor = new InterruptingFromBackgroundRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);



    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }


    private class ClassVisitor extends VoidVisitorAdapter<Void> {



        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {



            for (Object implementedType : n.getExtendedTypes().parallelStream().toArray()){
                if (implementedType.toString().equalsIgnoreCase("BroadcastReceiver") ||
                        implementedType.toString().equalsIgnoreCase("Service")){

                    isClassHasBroadcastRecieversOrServices =true;
                    super.visit(n, arg);

                }

            }
        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {


            if (isClassHasBroadcastRecieversOrServices){

                if (n.getName().asString().equalsIgnoreCase("startActivities")
                        || n.getName().asString().equalsIgnoreCase("startActivityFromChild")
                        || n.getName().asString().equalsIgnoreCase("startActivity")
                        || n.getName().asString().equalsIgnoreCase("startActivityFromFragment")){

                    Method smell = new Method(n.getName().asString());
                    smell.setHasSmell(true);
                    smellyElementList.add(smell);

                }
            }

            super.visit(n, arg);
        }
    }

}