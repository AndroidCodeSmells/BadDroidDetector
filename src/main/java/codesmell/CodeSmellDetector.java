package codesmell;

import codesmell.entity.File;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.lang3.StringUtils;
import codesmell.smellRules.*;
import org.dom4j.DocumentException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeSmellDetector {

    private List<AbstractSmell> testSmells;

    /**
     * Instantiates the various test smellRules analyzer classes and loads the objects into an List
     */
    public CodeSmellDetector() {
        initializeSmells();
    }

    private void initializeSmells(){
        testSmells = new ArrayList<>();
//        testSmells.add(new BulkDataTransferOnSlowNetworkRule());
//        testSmells.add(new DroppedDataRule());
          testSmells.add(new EarlyResourceBindingRule());
          testSmells.add(new UncontrolledFocusOrderRule());
          testSmells.add(new UncontrolledFocusOrderRule());
//        testSmells.add(new InterruptingFromBackgroundRule());
//        testSmells.add(new SetConfigChangesRule());
        //testSmells.add(new ProhibitedDataTransferRule());
//        testSmells.add(new UnnecessaryPermissionRule());

          testSmells.add(new UntouchableRule());
          testSmells.add(new TrackingHardwareIdRule());
//        testSmells.add(new UncachedViewsRule());
//        testSmells.add(new OverdrawnPixelRule());
    }

    /**
     * Factory Method that provides a new instance of the CodeSmellDetector
     *
     * @return new CodeSmellDetector instance
     */
    public static CodeSmellDetector createTestSmellDetector() {
        return new CodeSmellDetector();
    }

    /**
     * Provides the names of the smells that are being checked for in the code
     *
     * @return list of smellRules names
     */
    public List<String> getTestSmellNames() {
        return testSmells.stream().map(AbstractSmell::getSmellName).collect(Collectors.toList());
    }

    /**
     * Loads the java source code file into an AST and then analyzes it for the existence of the different types of android code smells smells
     */
    public File detectSmells(File file) throws IOException, DocumentException {
        CompilationUnit compilationUnit=null;
        XmlParser xmlParser=null;

        FileInputStream  fileInputStream;

        if(file.getFileType()== File.FileType.JAVA) {
            if (!StringUtils.isEmpty(file.getFilePath())) {
                fileInputStream = new FileInputStream(file.getFilePath());
                compilationUnit = JavaParser.parse(fileInputStream);
            }
            if (!StringUtils.isEmpty(file.getXmlFilePath())) {
                xmlParser = new XmlParser(file.getXmlFilePath());
            }


            initializeSmells();
            for (AbstractSmell smell : testSmells) {
                try {
                    smell.runAnalysis(compilationUnit,xmlParser);
                } catch (FileNotFoundException e) {
                    file.addSmell(null);
                    continue;
                }
                file.addSmell(smell);
            }
        }
        else{

        }

        return file;
    }
}
