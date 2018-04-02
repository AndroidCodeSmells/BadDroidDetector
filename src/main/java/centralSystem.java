import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class centralSystem {
  // static Process p ;
    public static void main(String arg[]){

        // loop throhg folder get the path of file


        File folder = new File("E:\\Khalid\\appTagFilePath Files\\appTagFilePathClass");
        File[] listOfFiles = folder.listFiles();
        runJarFile("E:\\Khalid\\appTagFilePathFiles\\appTagFilePathClass\\AppTagFilePathClass_1.csv");

//        for (File file: listOfFiles) {
//            if (file.isFile()) {
//                if (!file.getName().equalsIgnoreCase("AppTagFilePathClass.csv")){
//                    String path = file.getPath().toString();
//
//
//                        runJarFile(path);
//                        System.out.println(path);
//
//
//
//
//                }
//
//
//            }
//        }




    }
    private static void runJarFile(String pathUrl) {
        String jarURl = "E:\\Khalid\\GitHub\\AndroidCodeSmellDetector\\classes\\artifacts\\TestSmellDetector_jar\\TestSmellDetector.jar";

        String command = "java -jar "+jarURl+" "+pathUrl+" ";
        System.out.println(command);
            try {
                Process p = Runtime.getRuntime().exec(command);
                System.out.println(p);

                BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;

                while ((line = is.readLine()) != null)
                    System.out.println(line);

            } catch (Exception e) {
               // p.destroy();
                e.printStackTrace();
            }
    }
}
