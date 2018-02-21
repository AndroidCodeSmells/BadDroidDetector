package codesmell.smellRules;
// java
import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.MethodChild;
import codesmell.entity.SmellyElement;
import codesmell.entity.Variable;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.dom4j.DocumentException;


import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class DroppedDataRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;
    private List<Variable> variableList;
    private List<String> listOfUserControls;

    private MethodDeclaration CompilationUnitForonSaveInstanceState;

    public DroppedDataRule() {
        smellyElementList = new ArrayList<>();
        variableList = new ArrayList<>();
        listOfUserControls  = new ArrayList<String>();
        listOfUserControls.add("TextInputEditText");
        listOfUserControls.add("EditText");
        listOfUserControls.add("NumberPicker");
        listOfUserControls.add("Button");
        listOfUserControls.add("ToggleButton");
        listOfUserControls.add("CheckBox");
        listOfUserControls.add("RadioButton");
        listOfUserControls.add("Spinner");
        listOfUserControls.add("SeekBar");
        listOfUserControls.add("DatePicker");
        listOfUserControls.add("TimePicker");

    }

    @Override
    public String getSmellName() {
        return "DroppedDataRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        DroppedDataRule.ClassVisitor classVisitor;
        classVisitor = new DroppedDataRule.ClassVisitor();
        classVisitor.visit(compilationUnit, null);

        if (variableList.size()>0){

            if (CompilationUnitForonSaveInstanceState != null){
                DroppedDataRule.onSaveInstanceState onSaveInstanceState;
                onSaveInstanceState = new DroppedDataRule.onSaveInstanceState();
                onSaveInstanceState.visit(CompilationUnitForonSaveInstanceState, null);
            }
        }

        for (Variable var : variableList) {

            if (!var.isUsedInOnSaveInstanceState()){
                Method smell = new Method(var.getVariableName());
                smell.setHasSmell(true);
                smellyElementList.add(smell);
            }
        }

    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }

    // check only inside onSaveInstanceState method to check if the variable name was used inside this method
    private class onSaveInstanceState extends VoidVisitorAdapter<Void> {


        @Override
        public void visit(NameExpr n, Void arg) {

           OptionalInt index = IntStream.range(0, variableList.size())
                    .filter(i -> n.getName().asString().equals(variableList.get(i).getVariableName()))
                    .findFirst();

           if(index.isPresent()){
               variableList.get(index.getAsInt()).setUsedInOnSaveInstanceState(true);
           }


            super.visit(n, arg);
        }
    }
    private class ClassVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            if(n.getNameAsString().equalsIgnoreCase("onSaveInstanceState")){
                CompilationUnitForonSaveInstanceState = n;
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(VariableDeclarator n, Void arg) {
            if(listOfUserControls.contains(n.getType().asString())){
                variableList.add( new Variable(n.getNameAsString(),n.getType().asString()));
            }
            super.visit(n, arg);
        }
    }

}