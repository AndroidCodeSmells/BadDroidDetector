import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class centralSystem {
   static Process p ;
    static int index = 0;

    public static void main(String arg[]){

        // loop throhg folder get the path of file


        File folder = new File("E:\\Khalid\\appTagFilePathFiles\\appTagFilePathClass");
        File[] listOfFiles = folder.listFiles();

        try {
            p =  Runtime.getRuntime().exec("java --version ");
            p.destroyForcibly();
        } catch (IOException e) {
            e.printStackTrace();
            p.destroyForcibly();

        }
        while (index < listOfFiles.length)
        {
            File file = listOfFiles[index];
            if (file.isFile()){
                String path = listOfFiles[index].getPath();

                if (!p.isAlive()){
                        runJarFile(path);
                        System.out.println(index+"-----"+path);

                    index++;
                }
            }else {
                index++;

            }



        }
//        for (File file: listOfFiles) {
//            if (file.isFile()) {
//                if (!file.getName().equalsIgnoreCase("AppTagFilePathClass.csv")){
//
//                    while (p.onExit().isDone()){
//                        String path = file.getPath().toString();
//                        runJarFile(path);
//                        System.out.println(p);
//                    }
//
//                }
//
//
//            }
//        }




    }
    private static void runJarFile(String pathUrl) {
       // String jarURl = "E:\\Khalid\\GitHub\\AndroidCodeSmellDetector\\src\\jarfile\\TestSmellDetector-0.1-jar-with-dependencies.jar";
        String jarURl = "E:\\Khalid\\GitHub\\AndroidCodeSmellDetector\\src\\jarfile\\xmlMain-0.1-jar-with-dependencies.jar";

        String command = "java -Xms512m -Xmx10024m -jar "+jarURl+" "+pathUrl+" ";
            try {
                 p = Runtime.getRuntime().exec(command);
                BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((is.readLine()) != null)
                    System.out.println(index+"  waiting - "+p.pid());

                // Get the jvm heap size.


            } catch (Exception e) {
                e.printStackTrace();
                p.destroy();

            }
    }
}
