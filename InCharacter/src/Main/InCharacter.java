package Main;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ICFiles.*;
import ICInterpreter.Interpreter;

public class InCharacter {

    private ICParser parser;
    private Interpreter interpreter;
    private String ICFilePath;
    private String configPath;
    private File configFile;
    private List<String> defaultConfig;

    public InCharacter() {
        ICFilePath = "";
        configPath = "Data/Config/";
        interpreter = Interpreter.getInstance();
        parser = ICParser.getInstance();
    }

    public void Launch () {
        if(!Config()) {
            return;
        }
        interpreter.init(ICFilePath + configPath);

        ICFile icfile = parser.parse("C:\\Users\\jwk\\Desktop\\InCharacter\\InCharacter\\Data\\Rule Sets\\WadeRules\\Base.txt");

        CharacterGen charGen = new CharacterGen();
        charGen.genCharacter(icfile);

        //icfile.print();

    }

    private boolean Config() {
        try {
            File configFolder = new File(ICFilePath + configPath);
            //if config folder exists
            if (configFolder.isDirectory()) {
                configFile = new File(ICFilePath + configPath + "config.txt");
                //if IC is missing a config file
                if (!configFile.isFile()) {
                    //create default config file
                    System.out.println("Creating config file!");
                    createDefaultConfig();
                }
                //read from file
                System.out.println("Getting confgi info!");
                Scanner scan = new Scanner(configFile);
                List<String> ConfigContent = new ArrayList<String>();
                while (scan.hasNextLine()) {
                    ConfigContent.add(scan.nextLine());
                }
                return true;
            } else {
                //exit program
                System.out.println("There is no config folder!");
                return false;
            }

            //new File("Config").mkdir();
        } catch (IOException ex) {
            System.out.println("Failed Configuration");
            return false;
        }

    }


    private void createDefaultConfig() {
        try {
            new File(ICFilePath + configPath + "config.txt").createNewFile();
            configFile = new File(ICFilePath + configPath + "config.txt");

            //write default content
            defaultConfig = new ArrayList<String>();
            defaultConfig.add("Path = " + ICFilePath + ";");
            Files.write(Paths.get(configFile.getPath()), defaultConfig, Charset.forName("UTF-8"));
        }
        catch(IOException e) {

        }
    }

}
