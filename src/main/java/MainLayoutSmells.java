import codesmell.AbstractSmell;
import codesmell.CodeSmellDetector;
import codesmell.ResultsWriter;
import codesmell.entity.File;
import org.dom4j.DocumentException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLayoutSmells {
    public static void main(String[] args) throws IOException, DocumentException {

//XML Parser
//XmlParser.ElementCollection i = XmlParser.FindAttribute("C:\\Projects\\milk\\Apps\\accessible\\DemoApp03\\app\\src\\main\\res\\layout\\activity_main.xml", "contentDescription");



        CodeSmellDetector codeSmellDetector = CodeSmellDetector.createXmlSmellDetector();

        /*
          Read the the folder list subfolder and build the File objects
         */

        //BufferedReader in = new BufferedReader(new FileReader("G:\\Khalid\\output\\crashFile.txt"));
        BufferedReader input = new BufferedReader(new FileReader("G:\\Khalid\\output\\tags\\tags_join_class_xml.csv"));

        String s;

        String[] LineItem;
        File testFile;
        List<File> files = new ArrayList<>();
        while ((s = input.readLine()) != null) {//
            // use comma as separator
            LineItem = s.split(",");

            //check if the test file has an associated production file
            if (LineItem.length == 3) {
                testFile = new File(LineItem[0], LineItem[1],LineItem[2]);
            } else {
                testFile = new File(LineItem[0], LineItem[1],LineItem[2]);
            }
            files.add(testFile);
        }


        /*
          Initialize the output file - Create the output file and add the column names
         */
        ResultsWriter resultsWriter = ResultsWriter.createResultsWriter();
        List<String> columnNames;
        List<String> columnValues;

        columnNames = codeSmellDetector.getXmlSmellNames();
        columnNames.add(0, "App");
        columnNames.add(1, "Tag");
        columnNames.add(2, "XmlFilePath");

        resultsWriter.writeColumnName(columnNames);

        /*
          Iterate through all test files to detect smells and then write the output
        */
        File tempFile;
        for (File file : files) {
           System.out.println("Processing: " + file.getFilePath());

            //detect smells
            tempFile = codeSmellDetector.detectSmellsXmlFile(file);

            //write output
            columnValues = new ArrayList<>();
            columnValues.add(file.getApp());
            columnValues.add(file.getTag());
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
