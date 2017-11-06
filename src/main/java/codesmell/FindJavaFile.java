package codesmell;

import java.io.*;
import java.io.FileWriter;


class FindJavaFile
{
    public void FindJavaFile(String name,File file)
    {
        //Delimiter used in CSV file
        final String COMMA_DELIMITER = ",";
       final String NEW_LINE_SEPARATOR = "\n";
      //  private static final String FILE_HEADER = "id,firstName,lastName,gender,age";
        BufferedWriter fileWriter = null;

        try {
             fileWriter= new BufferedWriter(new FileWriter("/Users/khalidsalmalki/Desktop/cidesmell.csv", true));
        } catch (IOException e) {
            e.printStackTrace();
        }


        File[] list = file.listFiles();
        try {
        if(list!=null)
            for (File fil : list)
            {
                String folderName = fil.getName();
                if (fil.isDirectory())
                {
                    FindJavaFile(name,fil);
                }
                else if (extension(fil.getName()).equalsIgnoreCase(name))
                {

                        fileWriter.append(fil.getParentFile().getName());
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(fil.getAbsolutePath());
                        fileWriter.append(NEW_LINE_SEPARATOR);


                    System.out.println(fil.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String extension(String fileName) {
        int dot = fileName.lastIndexOf(".");
        return fileName.substring(dot + 1);
    }
    public static void main(String[] args)
    {
        FindJavaFile ff = new FindJavaFile();
        ff.FindJavaFile("java",new File("/Users/khalidsalmalki/Desktop/SampleTesting/a2dp.Vol"));
    }
}