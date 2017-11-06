package codesmell;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.lang3.StringUtils;
import codesmell.smellRules.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestSmellDetector {

    private List<AbstractSmell> testSmells;

    /**
     * Instantiates the various test smellRules analyzer classes and loads the objects into an List
     */
    public TestSmellDetector() {
        initializeSmells();
    }

    private void initializeSmells(){
        testSmells = new ArrayList<>();
//        testSmells.add(new BulkDataTransferOnSlowNetworkRule());
//        testSmells.add(new DroppedDataRule());
//        testSmells.add(new EarlyResourceBindingRule());
//        testSmells.add(new NotDescriptiveUIRule());
//        testSmells.add(new UncontrolledFocusOrderRule());
//        testSmells.add(new InterruptingFromBackgroundRule());
//        testSmells.add(new SetConfigChangesRule());
        testSmells.add(new ProhibitedDataTransferRule());
//        testSmells.add(new UnnecessaryPermissionRule());
//        testSmells.add(new UntouchableRule());
//        testSmells.add(new TrackingHardwareIdRule());
//        testSmells.add(new UncachedViewsRule());
//        testSmells.add(new OverdrawnPixelRule());
    }

    /**
     * Factory method that provides a new instance of the TestSmellDetector
     *
     * @return new TestSmellDetector instance
     */
    public static TestSmellDetector createTestSmellDetector() {
        return new TestSmellDetector();
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
     * Loads the java source code file into an AST and then analyzes it for the existence of the different types of test smells
     */
    public TestFile detectSmells(TestFile testFile) throws IOException {
        CompilationUnit productionFileCompilationUnit=null;
        FileInputStream testFileInputStream, productionFileInputStream;



        if(!StringUtils.isEmpty(testFile.getProductionFilePath())){
            productionFileInputStream = new FileInputStream(testFile.getProductionFilePath());
            productionFileCompilationUnit = JavaParser.parse(productionFileInputStream);
        }

        initializeSmells();
        for (AbstractSmell smell : testSmells) {
            try {
                smell.runAnalysis(productionFileCompilationUnit);
            } catch (FileNotFoundException e) {
                testFile.addSmell(null);
                continue;
            }
            testFile.addSmell(smell);
        }

        return testFile;

    }


}
