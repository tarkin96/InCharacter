package Main;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InCharacter {

    private String ICFilePath;
    private File configFile;
    private List<String> defaultConfig;
    private List<RuleSet> ruleSets;

    public void Launch () {
        if(Config()) {
            LoadRuleSets();
        }
    }

    private void LoadRuleSets() {


    }

    private boolean Config() {
        try {
            File configFolder = new File("Config");

            if (configFolder.isDirectory()) {
                configFile = new File("Config/config.txt");
                if (!configFile.isFile()) {
                    //create config file
                    System.out.println("Creating config file!");

                    new File("Config/config.txt").createNewFile();
                    configFile = new File("Config/config.txt");

                    //write default content
                    defaultConfig = new ArrayList<String>();
                    defaultConfig.add("Path = " + System.getProperty("user.dir") + ";");
                    Files.write(Paths.get(configFile.getPath()), defaultConfig, Charset.forName("UTF-8"));
                }
                //read from file
                Scanner scan = new Scanner(configFile);
                List<String> ConfigContent = new ArrayList<String>();
                while (scan.hasNextLine()) {
                    ConfigContent.add(scan.nextLine());
                }

                /*for (String s : ConfigContent ) {
                    System.out.println(s);
                }*/
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


}
