package codesmell.smellRules;

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

public class OverdrawnPixelRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;
    private  String  currentMethod ;
    private boolean hasClipRect;

    public OverdrawnPixelRule() {
        smellyElementList = new ArrayList<>();

    }

    @Override
    public String getSmellName() {
        return "OverdrawnPixelRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        OverdrawnPixelRule.ClassVisitor classVisitor;
        classVisitor = new OverdrawnPixelRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);


        if (currentMethod != null){
            if (currentMethod.equalsIgnoreCase("onDraw")) {
                if (!hasClipRect){
                    Method smell = new Method(currentMethod);
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
        @Override
        public void visit(MethodDeclaration n, Void arg) {

            if (n.getNameAsString().equalsIgnoreCase("onDraw")){
                currentMethod = n.getNameAsString();

                System.out.println(currentMethod);
                hasClipRect = false;

                super.visit(n, arg);

            }



        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {

            if (n.getName().asString().equalsIgnoreCase("clipRect")){

                hasClipRect = true;
                System.out.println(n.getName().asString());

            }
            super.visit(n, arg);


        }


    }

}