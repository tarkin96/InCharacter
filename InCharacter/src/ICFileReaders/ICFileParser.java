package ICFileReaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ICFileParser {
    public ICFile parse(String file_path) {

        ICFile icfile = new ICFile();
        File readFile = new File(file_path);

        icfile.setName(readFile.getName());
        icfile.setPath(readFile.getPath());

        //read from file
        try {
            Scanner scan = new Scanner(readFile);
            //this is where the magic happens
            while (scan.hasNextLine()) {
                String line = scan.nextLine().replaceAll("\\s","");
                //check if line has sub attrs

                //check if line has a value

                //check if line has a description

                //check if line isj ust a semicolon
            }
        } catch(IOException ex) {
            System.out.println("File can't be read!");
            System.exit(0);
        }

        return icfile;
    }
}
