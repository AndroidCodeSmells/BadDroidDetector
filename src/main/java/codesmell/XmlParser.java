package codesmell;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public static ElementCollection FindAttribute(String filePath, String attributeName) throws DocumentException {
        List<Element> elementsWithAttribute = new ArrayList<>();
        List<Element> elementsWithoutAttribute = new ArrayList<>();
        boolean hasAttribute;

        SAXReader reader = new SAXReader();
        Document document = reader.read(filePath);
        List<Node> elements = document.selectNodes(".//*");

        for (Node element: elements) {
            hasAttribute = false;
            for(Attribute attribute: ((DefaultElement) element).attributes()){
                if(attribute.getName().equals(attributeName)){
                    if(attribute.getValue()!=null && !attribute.getValue().isEmpty()) {
                        hasAttribute = true;
                    }
                }
            }
            if (hasAttribute){
                elementsWithAttribute.add((DefaultElement) element);
            }
            else {
                elementsWithoutAttribute.add((DefaultElement) element);
            }
        }

        return new ElementCollection(elementsWithAttribute,elementsWithoutAttribute);
    }

    public static class ElementCollection{
        List<Element> elementsWithAttribute = new ArrayList<>();
        List<Element> elementsWithoutAttribute = new ArrayList<>();

        ElementCollection( List<Element> elementsWithAttribute,List<Element> elementsWithoutAttribute ){
            this.elementsWithAttribute = elementsWithAttribute;
            this.elementsWithoutAttribute = elementsWithoutAttribute;
        }

        public List<Element> getElementsWithAttribute(){
            return elementsWithAttribute;
        }

        public List<Element> getElementsWithoutAttribute(){
            return elementsWithoutAttribute;
        }
    }
}
