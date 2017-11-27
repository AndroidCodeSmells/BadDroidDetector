import codesmell.AbstractSmell;
import codesmell.CodeSmellDetector;
import codesmell.ResultsWriter;
import codesmell.XmlParser;
import codesmell.entity.File;
import org.dom4j.DocumentException;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, DocumentException {

//XML Parser
//XmlParser.ElementCollection i = XmlParser.FindAttribute("C:\\Projects\\milk\\Apps\\accessible\\DemoApp03\\app\\src\\main\\res\\layout\\activity_main.xml", "contentDescription");


        CodeSmellDetector codeSmellDetector = CodeSmellDetector.createTestSmellDetector();

        /*
          Read the the folder list subfolder and build the File objects
         */

        BufferedReader in = new BufferedReader(new FileReader("/Users/khalidsalmalki/Desktop/cidesmell.csv"));
        String str;

        String[] lineItem;
        File testFile;
        List<File> files = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            // use comma as separator
            lineItem = str.split(",");

            //check if the test file has an associated production file
            if (lineItem.length == 2) {
                testFile = new File(lineItem[0], lineItem[1]);
            } else {
                testFile = new File(lineItem[0], lineItem[1]);
            }

            files.add(testFile);
        }


        /*
          Initialize the output file - Create the output file and add the column names
         */
        ResultsWriter resultsWriter = ResultsWriter.createResultsWriter();
        List<String> columnNames;
        List<String> columnValues;

        columnNames = codeSmellDetector.getTestSmellNames();
        columnNames.add(0, "App");
        columnNames.add(1, "FilePath");
        resultsWriter.writeColumnName(columnNames);

        /*
          Iterate through all test files to detect smells and then write the output
        */
        File tempFile;
        for (File file : files) {
            System.out.println("Processing: " + file.getFilePath());

            //detect smells
            tempFile = codeSmellDetector.detectSmells(file);

            //write output
            columnValues = new ArrayList<>();
            columnValues.add(file.getApp());
            columnValues.add(file.getFilePath());
            for (AbstractSmell smell : tempFile.getCodeSmells()) {
                try {
                    columnValues.add(String.valueOf(smell.getHasSmell()));
                } catch (NullPointerException e) {
                    columnValues.add("");
                }
            }
            resultsWriter.writeLine(columnValues);
        }

        System.out.println("end");
    }


}
