import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String input_folder = "src/file_input";
    private static final String output_folder = "src/file_output/";

    public static void main(String[] args) {
        File input_file = listf(input_folder, new ArrayList<>()).get(0);
        List<String> lines = null;
        try {
            lines = fileToList(input_file);
            lines = translator(lines);
            listoToFile(lines, removeExtension(input_file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Hello World! ");
    }

    private static List<String> translator(List<String> lines) {
        if (lines.get(0).startsWith("<?php")) {
            lines.remove(0);
        }
        if (lines.get(6).startsWith("namespace ")) {
            String import_namespace = lines.get(6).replace("namespace", "import");
            import_namespace = import_namespace.replace("\\", ".");
            import_namespace = import_namespace.replace("yii.","inq.inquid.framework.");
            lines.set(6, import_namespace);
        }
        //TODO Change the "use" in the dependencies to "import" and also adjust the namespace with this> replace("yii.","inq.inquid.framework.");

        //TODO Let comments pass

        //TODO add the public to the class File

        //TODO Change "const" to "public static final TYPE"

        //TODO Change the ' ' to " " in the String variables

        //TODO Add the TYPE to the variables using the PHPDocs

        //TODO Change the "function" to "void" or "TYPE"

        //TODO Remove the function contains and add just the return null when apply
        
        return lines;
    }

    private static ArrayList<File> listf(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
        }
        return files;
    }

    private static List<String> fileToList(File file) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
            //br returns as stream and convert it into a List
            list = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void listoToFile(List<String> list, String file_name) throws IOException {
        FileWriter writer = new FileWriter(output_folder + file_name + ".java");
        for (String str : list) {
            writer.write(str + "\n");
        }
        writer.close();
    }

    private static String removeExtension(File file) {
        String fname = file.getName();
        int pos = fname.lastIndexOf(".");
        if (pos > 0) {
            fname = fname.substring(0, pos);
        }
        return fname;
    }
}
