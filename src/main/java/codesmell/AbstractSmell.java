package codesmell;

import codesmell.entity.SmellyElement;
import com.github.javaparser.ast.CompilationUnit;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public abstract class AbstractSmell {
    public abstract String getSmellName();

    public abstract boolean getHasSmell();


    public abstract void runAnalysis(CompilationUnit compilationUnit,XmlParser xmlParser) throws FileNotFoundException, DocumentException;

    public abstract List<SmellyElement> getSmellyElements();
}
