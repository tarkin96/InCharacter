package ICInterpreter;

import ICFiles.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {

    private ArrayList<String> reserved_symbols;
    private String reserved_file;
    private static Interpreter instance = null;


    public static Interpreter getInstance()
    {
        if (instance == null)
            instance = new Interpreter();

        return instance;
    }


    private Interpreter() {
        reserved_file = "";
        reserved_symbols = new ArrayList<String>();
    }

    public void init(String filename) {
        reserved_file = filename;

        //get all reserved words from configuration
        File file = new File(filename);
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                reserved_symbols.add(scan.nextLine());
            }
        }
        catch (IOException e) {

        }

    }

    //begin interpretation of lines
    public Attribute interpret (Attribute attr) {
        Attribute newAttr = attr.copy();
        recInterpret(newAttr);
        return newAttr;
    }

    //goes through all functions and tries to resolve them
    private void recInterpret(Attribute attr) {
        for (int i = 0; i < attr.getMappings().size(); i++) {
            if (attr.getFunctions().containsKey(attr.getMappings().get(i))) {
                resolve(attr, attr.getMappings().get(i), attr.getFunctions().get(attr.getMappings().get(i)));
            }
        }
        for (int i = 0; i < attr.getSubAttrs().size(); i++) {
            recInterpret(attr.getSubAttrs().get(i));
        }
    }

    //interprets an entire line and returns resolved value
    private String resolve(Attribute attr, String key, String funct) {
        //System.out.println("made it!");
        ArrayList<String> parts = getStringParts(funct);
        //scan for equations or values
        for (int i = 0; i < parts.size(); i++) {
            //interpret all the variables in the line first
            if (!isNumeric(parts.get(i)) && !isReserved(parts.get(i))) {
                //resolve recursively
                //if it references sub attribute
                parts.set(i, interpret_var(attr, parts.get(i)));
            }
            //begin interpretting whole line

        }

        //placeholder for actual resolve of item
        String retStr = new String();
        for (int j = 0; j < parts.size(); j++) {
            retStr+=parts.get(j);
        }

        //resolve parts as a whole

        //place new object into appropriate map
        if (isNumeric(retStr)) {
            attr.addValue(key, Float.parseFloat(retStr));
        }
        else if (retStr.contains("\"")) {
            attr.addDescription(key, retStr);
        }
        else {
            System.out.println("Something went wrong with resolving function!");
        }
        //remove function from function map
        attr.removeFunction(key);
        return retStr;
    }



    private String interpret_var(Attribute attr, String variable) {
        ICParser parser = ICParser.getInstance();
        //String whatsleft = variable;
        if (variable.contains(".")) {
            System.out.println("moved to next level of variable!");
            return interpret_var(parser.findAttribute(attr, variable.substring(0, variable.indexOf("."))),
                    variable.substring(variable.indexOf(".") + 1, variable.length()));
        }
        else {
            Float check_val = parser.findVal(attr, variable);

            String check_desc = null;
            String check_func = null;
            if (check_val == null) {
                check_desc = parser.findDescription(attr, variable);
            }
            else {
                return check_val.toString();
            }
            if (check_desc == null) {
                check_func = parser.findFunct(attr, variable);

            }
            else {
                return check_desc;
            }
            if (check_func == null) {
                return null;
            }
            else {
                //resolve the new function in the attribute
                check_func = resolve(attr, variable, check_func);
                return check_func;
            }

        }
        //return "";
    }

    private ArrayList<String> getStringParts(String funct) {
        ArrayList<String> parts = new ArrayList<String>();
        if (funct.equals("")) {return parts;}
        int count = 0;
        int look_back = 0;

        while (count < funct.length()) {
            //if window is empty space
            if(funct.substring(look_back, count+1).trim().length() == 0) {
                //System.out.println("Emtpy Space");
                count++;
            }
            //window contains other characters
            else {
                //if current character is reserved
                if(isReserved(funct.substring(count, count + 1))) {
                    //System.out.println("Reserved Character");
                    //check to add previous characters to parts list
                    if (!(funct.substring(look_back, look_back+1).trim().length() == 0)) {
                        parts.add(funct.substring(look_back, count));
                    }
                    //add reserved character to list
                    parts.add(funct.substring(count, count+1));
                    //move look back to count
                    count++;
                    look_back = count;
                }
                else {
                    //if look back is white space (found a new word) or a reserved character
                    if (funct.substring(look_back, look_back + 1).trim().length() == 0 || isReserved(funct.substring(look_back, look_back + 1))) {
                        //System.out.println("Found word at " + count);
                        look_back = count;
                    }
                    //if look back is a character
                    else {
                        //if count is whitespace (found end of word)
                        if (funct.substring(count, count+ 1).trim().length() == 0) {
                            parts.add(funct.substring(look_back, count));
                            //System.out.println("End of word: lookback = " + look_back + " count = " + count);
                            look_back = count;
                        }

                    }
                    count++;
                }
            }
        }
        //add last word of line
        if (!(funct.substring(look_back, look_back + 1).trim().length() == 0)) {
            parts.add(funct.substring(look_back, count));
            //System.out.println("End of word: lookback = " + look_back + " count = " + count);
            look_back = count;
        }

        return parts;
    }

    private boolean isReserved(String str) {
        return reserved_symbols.contains(str);
    }




    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
