package codesmell;

import codesmell.entity.File;
import codesmell.entity.XML;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

/**
 * This class is utilized to write output to a CSV file
 */
public class ResultsWriter {

    private String outputFile;
    private FileWriter writer;

    /**
     * Creates the file into which output it to be written into. Results from each file will be stored in a new file
     * @throws IOException
     */
    private ResultsWriter(File.FileType type) throws IOException {
        String time =  String.valueOf(Calendar.getInstance().getTimeInMillis());
        String fileLable = "";
        if (type == File.FileType.XML){
            fileLable = "Output_XmlFile";
        }else {
            fileLable = "Output_JavaFile";

        }
        outputFile = MessageFormat.format("{0}_{1}_{2}.{3}", fileLable,"AndroidCodeSmellDetection",time, "csv");
        writer = new FileWriter(outputFile,false);
    }

    /**
     * Factory Method that provides a new instance of the ResultsWriter
     * @return new ResultsWriter instance
     * @throws IOException
     */
    public static ResultsWriter createJavaResultsWriter() throws IOException {
        return new ResultsWriter(File.FileType.JAVA);
    }
    public static ResultsWriter createXmlResultsWriter() throws IOException {
        return new ResultsWriter(File.FileType.XML);
    }
    /**
     * Writes column names into the CSV file
     * @param columnNames the column names
     * @throws IOException
     */
    public void writeColumnName(List<String> columnNames) throws IOException {
        writeOutput(columnNames);
    }

    /**
     * Writes column values into the CSV file
     * @param columnValues the column values
     * @throws IOException
     */
    public void writeLine(List<String> columnValues) throws IOException {
        writeOutput(columnValues);
    }

    /**
     * Appends the input values into the CSV file
     * @param dataValues the data that needs to be written into the file
     * @throws IOException
     */
    private void writeOutput(List<String> dataValues)throws IOException {
        writer = new FileWriter(outputFile,true);

        for (int i=0; i<dataValues.size(); i++) {
            writer.append(String.valueOf(dataValues.get(i)));

            if(i!=dataValues.size()-1)
                writer.append(",");
            else
                writer.append(System.lineSeparator());

        }
        writer.flush();
        writer.close();
    }
}
