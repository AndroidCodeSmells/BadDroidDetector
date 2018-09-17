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

    private List<AbstractSmell> codeSmellsJava;
    private List<AbstractSmell> codeSmellsXmel;

    /**
     * Instantiates the various test smellRules analyzer classes and loads the objects into an List
     */
    public CodeSmellDetector() {
        initializeJavaSmells();
    }
    public CodeSmellDetector(String s) {
        initializeXmlSmells();
    }

    private void initializeJavaSmells(){
          codeSmellsJava = new ArrayList<>();
          codeSmellsJava.add(new OverdrawnPixelRule());
          codeSmellsJava.add(new InterruptingFromBackgroundRule());
          codeSmellsJava.add(new BulkDataTransferOnSlowNetworkRule());
          codeSmellsJava.add(new DroppedDataRule());
          codeSmellsJava.add(new EarlyResourceBindingRule());
          codeSmellsJava.add(new ProhibitedDataTransferRule());
          codeSmellsJava.add(new TrackingHardwareIdRule());
          codeSmellsJava.add(new TerminateOpenInternetConnectionRule());
          codeSmellsJava.add(new UncachedViewsRule());

    }

    private void initializeXmlSmells(){
            codeSmellsXmel = new ArrayList<>();

            codeSmellsXmel.add(new NestedLayoutRule());
            codeSmellsXmel.add(new NotDescriptiveUIRule());
            codeSmellsXmel.add(new UncontrolledFocusOrderRule());
            codeSmellsXmel.add(new UntouchableRule());
            codeSmellsXmel.add(new SetConfigChangesRule());


    }

    /**
     * Factory Method that provides a new instance of the CodeSmellDetector
     *
     * @return new CodeSmellDetector instance
     */
    public static CodeSmellDetector createJavaSmellDetector() {
        return new CodeSmellDetector();
    }

    public static CodeSmellDetector createXmlSmellDetector() {
        return new CodeSmellDetector("xml");
    }


    /**
     * Provides the names of the smells that are being checked for in the code
     *
     * @return list of smellRules names
     */
    public List<String> getJavaSmellNames() {
        return codeSmellsJava.stream().map(AbstractSmell::getSmellName).collect(Collectors.toList());
    }


    /**
     * Provides the names of the smells that are being checked for in the code
     *
     * @return list of smellRules names
     */
    public List<String> getXmlSmellNames() {
        return codeSmellsXmel.stream().map(AbstractSmell::getSmellName).collect(Collectors.toList());
    }


    /**
     * Loads the java source code file into an AST and then analyzes it for the existence of the different types of android code smells smells
     */
    public File detectSmellsJavaFile(File file) throws Exception {
        CompilationUnit compilationUnit=null;
        XmlParser xmlParser=null;

        FileInputStream  fileInputStream;

        if(file.getFileType()== File.FileType.JAVA) {
            if (!StringUtils.isEmpty(file.getFilePath())) {
                fileInputStream = new FileInputStream(file.getFilePath());
                try {
                    compilationUnit = JavaParser.parse(fileInputStream);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (compilationUnit != null) {


                initializeJavaSmells();
                for (AbstractSmell smell : codeSmellsJava) {
                    try {
                        smell.runAnalysis(compilationUnit, null);
                    } catch (FileNotFoundException e) {
                        file.addSmell(null);
                        continue;
                    }
                    file.addSmell(smell);
                }
            } else {

            }
        }

        return file;
    }

    public File detectSmellsXmlFile(File file) throws  DocumentException {
        XmlParser xmlParser=null;

        if(file.getFileType()== File.FileType.XML) {

            if (!StringUtils.isEmpty(file.getFilePath())) {
                xmlParser = new XmlParser(file.getFilePath());
            }
            if (xmlParser != null) {


                initializeXmlSmells();


                for (AbstractSmell XmlSmells : codeSmellsXmel) {
                    try { // sen
                        XmlSmells.runAnalysis(null, xmlParser);

                    } catch (FileNotFoundException e) {
                        file.addSmell(null);
                        continue;
                    }
                    file.addSmell(XmlSmells);
                }
            } else {

            }
        }

        return file;
    }
}
