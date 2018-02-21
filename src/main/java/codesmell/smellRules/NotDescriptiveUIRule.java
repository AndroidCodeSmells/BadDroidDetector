package codesmell.smellRules;
// xml
import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class NotDescriptiveUIRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;

    public NotDescriptiveUIRule() {
        smellyElementList = new ArrayList<>();
    }

    @Override
    public String getSmellName() {
        return "NotDescriptiveUIRule";
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

                if (element.attributeValue("contentDescription")==null){

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