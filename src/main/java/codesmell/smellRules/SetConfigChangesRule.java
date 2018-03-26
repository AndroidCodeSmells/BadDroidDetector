package codesmell.smellRules;
//xml
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

public class SetConfigChangesRule extends AbstractSmell{
    private List<SmellyElement> smellyElementList;

    public SetConfigChangesRule() {
        smellyElementList = new ArrayList<>();
    }

    @Override
    public String getSmellName() {
        return "SetConfigChangesRule";
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
        XmlParser.ElementCollection setConfigChangesRule = xmlParser.FindAttribute("configChanges");

        // androidManifest
        // contain android:configChanges

        if (setConfigChangesRule.getElementsWithAttribute().size()>0){
            Method xmlelement = new Method(setConfigChangesRule.getElementsWithAttribute().get(0).getName());
            xmlelement.setHasSmell(true);
            smellyElementList.add(xmlelement);
        }






    }

    @Override
    public List<SmellyElement> getSmellyElements() {
        return smellyElementList;
    }
}