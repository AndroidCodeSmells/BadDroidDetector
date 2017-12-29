package codesmell.smellRules;

import codesmell.AbstractSmell;
import codesmell.XmlParser;
import codesmell.entity.Method;
import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class NestedLayoutRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;

    public NestedLayoutRule() {
        smellyElementList = new ArrayList<>();
    }

    @Override
    public String getSmellName() {
        return "NestedLayoutRule";
    }

    @Override
    public boolean getHasSmell() {
        return smellyElementList.stream().filter(x -> x.getHasSmell()).count() >= 1;
    }

    @Override
    public void runAnalysis(CompilationUnit compilationUnit, XmlParser xmlParser) throws FileNotFoundException, DocumentException {

        XmlParser.ElementCollection nestedLayoutRule = xmlParser.FindNode("LinearLayout");

        // LinearLayout
        // contain android:layout_weight
        for (Element element :nestedLayoutRule.getElementsWithAttribute()) {

            for (Attribute att : element.attributes()){

                if (att.getName().equalsIgnoreCase("layout_weight")){
                    Method xmlelement = new Method(element.getName());
                     xmlelement.setHasSmell(true);
                     smellyElementList.add(xmlelement);


                }
            }


        }







    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }
}