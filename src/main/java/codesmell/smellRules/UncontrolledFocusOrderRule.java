package codesmell.smellRules;

import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UncontrolledFocusOrderRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;

    public UncontrolledFocusOrderRule() {
        smellyElementList = new ArrayList<>();
    }

    @Override
    public String getSmellName() {
        return "UncontrolledFocusOrderRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        if(xmlParser == null){
            return;
        }
        XmlParser.ElementsCollection  notDescriptive = xmlParser.FindAttribute();

        for (Element element :notDescriptive.getElementsWithAttribute()) {
            int counter = 0;
            if (element.attributeValue("nextFocusUp")==null){
                counter ++;
            }
            if (element.attributeValue("nextFocusDown")==null){
                counter ++;

            }
            if (element.attributeValue("nextFocusLeft")==null){

                counter ++;

            }
            if (element.attributeValue("nextFocusRight")==null){
                counter ++;

            }
            if (element.attributeValue("nextFocusForward")==null){
                counter ++;

            }

            if (counter==5){
                Method xmlelement = new Method(element.getName());
               xmlelement.setHasSmell(true);
                smellyElementList.add(xmlelement);
            }


        }







    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }
}