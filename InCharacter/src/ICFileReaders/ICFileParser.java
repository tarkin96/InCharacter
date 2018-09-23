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
                if (!line.equals("")) {
                    //check if line isj ust a semicolon
                    if(line.equals(";")) {
                        icfile.setParentAsData();
                    }
                    //check if line has no sub attrs
                    else if (line.substring(line.length() - 1).equals(";")) {
                        //check if line has a value
                        if (isNumeric(line.substring(line.lastIndexOf("=") + 1, line.length() - 1))) {
                            String nameOfAttr = line.substring(0, line.lastIndexOf("="));
                            addtoICFile(icfile, nameOfAttr, Integer.parseInt(line.substring(line.lastIndexOf("=") + 1, line.length() - 1)));
                        }
                        // check if line has a description
                        else if(line.substring(line.lastIndexOf("=") + 1).contains("\"")) {
                            String nameOfAttr = line.substring(0, line.lastIndexOf("="));
                            addtoICFile(icfile, nameOfAttr, line.substring(line.lastIndexOf("=") + 1, line.length() - 1));
                        }
                        else {System.out.println("something is wrong");}

                    }
                    //if line has sub attributes
                    else if (line.substring(line.length() - 1).equals("=")){
                        String nameOfAttr = line.substring(0, line.length() - 1);
                        addtoICFile(icfile, nameOfAttr);
                        //System.out.println("added stuff");
                    }
                    else {System.out.println("Could not read line properly.");}
                }
            }
        } catch(IOException ex) {
            System.out.println("File can't be read!");
            System.exit(0);
        }

        return icfile;
    }

    //method to add a sub attribute
    public void addtoICFile(ICFile file, String name) {
        file.addData(name);
    }

    //method to add a value
    public void addtoICFile(ICFile file, String name, int value) {
        file.addData(name, value);
        //System.out.println("added stuff");
    }

    //method to add a decription
    public void addtoICFile(ICFile file, String name, String desc) {
        file.addData(name, desc);
        //System.out.println("added stuff");
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
