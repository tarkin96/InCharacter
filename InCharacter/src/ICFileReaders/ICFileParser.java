package ICFileReaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ICFileParser {
    //creates an ICFile from a file path
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
                    /*
                    IMPORTANT!!!!!
                    THIS IS HOW THE FILE ENDS UP POINTING TO THE TOP OF THE ATTRIBUTE STRUCTURE!!!!
                     */
                    //check if line isj ust a semicolon
                    if(line.equals(";")) {
                        icfile.setParentAsData();
                    }
                    //check if line has no sub attrs
                    else if (line.substring(line.length() - 1).equals(";")) {
                        //check if line has a value
                        if (isNumeric(line.substring(line.indexOf("=") + 1, line.length() - 1))) {
                            String nameOfAttr = line.substring(0, line.lastIndexOf("="));
                            addValueToICFile(icfile, nameOfAttr, Integer.parseInt(line.substring(line.lastIndexOf("=") + 1, line.length() - 1)));
                        }
                        // check if line has a description
                        else if(line.substring(line.lastIndexOf("=") + 1).contains("\"")) {
                            String nameOfAttr = line.substring(0, line.lastIndexOf("="));
                            addDescToICFile(icfile, nameOfAttr, line.substring(line.lastIndexOf("=") + 1, line.length() - 1));
                        }
                        //check if line has an equation
                        else if(!line.equals(";")) {
                            String nameOfAttr = line.substring(0, line.lastIndexOf("="));
                            addDescToICFile(icfile, nameOfAttr, line.substring(line.lastIndexOf("=") + 1, line.length() - 1));
                        }
                        else {System.out.println("something is wrong");}

                    }
                    //if line has sub attributes
                    else if (line.substring(line.length() - 1).equals("=")){
                        String nameOfAttr = line.substring(0, line.length() - 1);
                        addSubAttrToICFile(icfile, nameOfAttr);
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

    //finds data in ICFile
    public Float findVal(ICFile icfile, String name) {
        return 1.0f;
    }
    public String findEquation(ICFile icfile, String name) {
        return "";
    }
    public String findDescription(ICFile icfile, String name) {
        return "";
    }
    public Attribute findAttribute(ICFile icfile, String name) {
        return new Attribute("");
    }
    public Object findData(ICFile icfile, String name) {
        return new Object();
    }


    //method to add a sub attribute
    public void addSubAttrToICFile(ICFile file, String name) {
        file.addSubAttr(name);
    }

    //method to add a value
    public void addValueToICFile(ICFile file, String name, float value) {
        file.addVal(name, value);
        //System.out.println("added stuff");
    }

    //method to add a decription
    public void addDescToICFile(ICFile file, String name, String desc) {
        file.addDesc(name, desc);
        //System.out.println("added stuff");
    }

    public void addEqToICFile(ICFile file, String name, String eq) {
        file.addEq(name, eq);
        //System.out.println("added stuff");
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
