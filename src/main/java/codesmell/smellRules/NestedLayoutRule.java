package codesmell.smellRules;
// xml
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
        // LinearLayout
        // contain android:layout_weight

        if (xmlParser != null){
            XmlParser.ElementsCollection nestedLayoutRule = xmlParser.FindAttribute();

            for (Element element :nestedLayoutRule.getElementsWithAttribute()) {

                        isLayout_weightExsit(element); // check if contain android:layout_weight

                    for (Element elm :element.elements() ){
                        isLayout_weightExsit(elm);
                    }
                }


            }

            }










     private void checkIfelementHasSmell(Element element){
        for (Attribute att : element.attributes()){

            if (att.getName().equalsIgnoreCase("layout_weight")){
                Method xmlelement = new Method(element.getName());
                xmlelement.setHasSmell(true);
                smellyElementList.add(xmlelement);


            }
        }
    }

    private void isLayout_weightExsit(Element element) {

        if (element.getName().equalsIgnoreCase("LinearLayout")){

            for (Element elm : element.elements()){

                checkIfelementHasSmell(elm);
            }

        }

    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }
}