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

public class Main {
    public static void main(String[] args) throws Exception{

//XML Parser
//XmlParser.ElementCollection i = XmlParser.FindAttribute("C:\\Projects\\milk\\Apps\\accessible\\DemoApp03\\app\\src\\main\\res\\layout\\activity_main.xml", "contentDescription");



        CodeSmellDetector codeSmellDetector = CodeSmellDetector.createJavaSmellDetector();

        /*
          Read the the folder list subfolder and build the File objects
         */

       // BufferedReader in = new BufferedReader(new FileReader("E:\\Khalid\\appTagFilePathFiles\\appTagFilePathClass\\AppTagFilePathClass_0.csv"));
        BufferedReader in = new BufferedReader(new FileReader(args[0].toString()));


        // the data structure will be app,tag, JavaFilePath
        String str;

        String[] lineItem;
        File testFile;
        List<File> files = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            // use comma as separator
            lineItem = str.split(",");

            //check if the test file has an associated production file
            if (lineItem.length == 3) {
                testFile = new File(lineItem[0], lineItem[1],lineItem[2]);
            } else {
                testFile = new File(lineItem[0], lineItem[1],lineItem[2]);
            }

            files.add(testFile);
        }


        /*
          Initialize the output file - Create the output file and add the column names
         */
        ResultsWriter resultsWriter = ResultsWriter.createJavaResultsWriter();
        List<String> columnNames;
        List<String> columnValues;

        columnNames = codeSmellDetector.getJavaSmellNames();
        columnNames.add(0, "App");
        columnNames.add(1, "Tag");
        columnNames.add(2, "JavaFilePath");

        resultsWriter.writeColumnName(columnNames);

        /*
          Iterate through all test files to detect smells and then write the output
        */
        File tempFile;
        for (File file : files) {
           System.out.println("Processing: " + file.getFilePath());

            //detect smells
            tempFile = codeSmellDetector.detectSmellsJavaFile(file);

            //write output
            columnValues = new ArrayList<>();
            columnValues.add(file.getApp());
            columnValues.add(file.getTag());
            columnValues.add(file.getFilePath());
                int counter = 0;
            for (AbstractSmell smell : tempFile.getCodeSmells()) {
                try {
                    columnValues.add(String.valueOf(smell.getHasSmell()));
                    counter++;
                    if (counter == 50){
                        new Exception();
                    }
                } catch (NullPointerException e) {
                    columnValues.add("");
                }
            }
            resultsWriter.writeLine(columnValues);
        }

        System.out.println("end");
    }


}
