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
    private CharacterGen generator;
    private ICFile ruleSet;

    public InCharacter() {
        ICFilePath = "";
        configPath = "Data/Config/";
        interpreter = Interpreter.getInstance();
        parser = ICParser.getInstance();
        generator = new CharacterGen();
        ruleSet = new ICFile();
    }

    public void Launch () {

        if(!Config()) {
            return;
        }
        interpreter.init(ICFilePath + configPath);

        ruleSet = parser.parse("C:\\Users\\jwk\\Desktop\\InCharacter\\InCharacter\\Data\\Rule Sets\\WadeRules\\Base.txt");

        //generator.genCharacter(icfile);

        //everything above is performed before main loop begins

        MainLoop();

        //everything below is performed after leaving the main loop


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
                System.out.println("Getting config info!");
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

    //this is where main functionality of IC begins, occuring after pre-processing
    private void MainLoop() {
        Scanner userInput = new Scanner(System.in);
        String input;

        while(true) {
            //ask user what they want to do
            System.out.println("Type \"new\" to create a new character!");
            input = userInput.nextLine();
            if (input.equals("new")) {
                //generate a character from the current base
                generator.genCharacter(ruleSet);
            }
        }
    }

}
