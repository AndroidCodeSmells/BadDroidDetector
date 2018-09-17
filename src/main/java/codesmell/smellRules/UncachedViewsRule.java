package codesmell.smellRules;

import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class UncachedViewsRule extends AbstractSmell {
    private List<SmellyElement> smellyElementList;
    private  String  currentMethod ;
    private Parameter parameterView;
    private boolean ischeckingParameterView = false;
    public UncachedViewsRule() {
        smellyElementList = new ArrayList<>();

    }

    @Override
    public String getSmellName() {
        return "UncachedViewsRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        UncachedViewsRule.ClassVisitor classVisitor;
        classVisitor = new UncachedViewsRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);


        System.out.println("ischeckingParameterView  "+ischeckingParameterView);

        if (!ischeckingParameterView){

        }


    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    private class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {

            if (n.getNameAsString().equalsIgnoreCase("getView")){
                currentMethod = n.getNameAsString();


                for (Parameter p: n.getParameters()) {

                    if (p.getType().asString().equalsIgnoreCase("View")){

                        System.out.println(p.getType());
                        parameterView = p;
                        super.visit(n, arg);


                    }

                }


            }



        }

        @Override
        public void visit(IfStmt n, Void arg) {
            super.visit(n, arg);

            if (!ischeckingParameterView && parameterView != null){


                if (n.getCondition() instanceof BinaryExpr){

                    BinaryExpr binaryExpr = n.getCondition().asBinaryExpr();

                    if (binaryExpr.getLeft() instanceof NameExpr){

                        if (binaryExpr.getLeft().asNameExpr().getName().asString().equalsIgnoreCase(parameterView.getName().asString())) {


                            if (binaryExpr.getRight() instanceof NullLiteralExpr){

                                if (binaryExpr.getOperator().asString().equalsIgnoreCase("==")){

                                    ischeckingParameterView = true;
                                }
                            }
                    }


                        }else   {

                        if (binaryExpr.getRight() instanceof NameExpr){

                            if ((binaryExpr.getRight().asNameExpr().getName().asString().equalsIgnoreCase(parameterView.getName().asString()))){
                                if (binaryExpr.getLeft() instanceof NullLiteralExpr){
                                    if (binaryExpr.getOperator().asString().equalsIgnoreCase("==")){
                                        ischeckingParameterView = true;

                                    }
                                }

                            }
                        }




                        }






                }

            }

        }
    }

}