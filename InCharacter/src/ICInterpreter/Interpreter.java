package ICInterpreter;

import ICFiles.*;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Interpreter {

    private ArrayList<String> reserved_symbols;

    public Interpreter() {
        reserved_symbols = new ArrayList<String>();
        reserved_symbols.add("+"); reserved_symbols.add("-"); reserved_symbols.add("*");
        reserved_symbols.add("/"); reserved_symbols.add("="); reserved_symbols.add("(");
        reserved_symbols.add(")"); reserved_symbols.add("||"); reserved_symbols.add("&&");
    }


    public void interpret (Attribute attr) {

        Iterator<Map.Entry<String, Pair<Integer, String>>> funit = attr.getFunctions().entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> fun = null;

        //for each function in the attribute
        while (funit.hasNext()) {

            fun = funit.next();
            //resolve equation
            resolve(attr, fun.getValue().getValue());
        }

        //for each sub attribute in the attribute
        for (int subAttrNum = 0; subAttrNum < attr.getSubAttrs().size(); subAttrNum++) {
            //interpret that attribute
            interpret(attr.getSubAttrs().get(subAttrNum));
        }
    }


    private void resolve(Attribute attr, String funct) {
        System.out.println("made it!");
        ArrayList<String> parts = getStringParts(funct);
        for (int i = 0; i < parts.size(); i++) {

            System.out.println(parts.get(i));
        }
        //scan for equations or values
        //resolve equations and values

        //split function into parts


        //resolve simplified equation
    }

    private ArrayList<String> getStringParts(String funct) {
        ArrayList<String> parts = new ArrayList<String>();
        if (funct.equals("")) {return parts;}
        int count = 0;
        int look_back = 0;

        while (count < funct.length()) {
            //if window is empty space
            if(funct.substring(look_back, count+1).trim().length() == 0) {
                System.out.println("Emtpy Space");
                count++;
            }
            //window contains other characters
            else {
                //if current character is reserved
                if(isReserved(funct.substring(count, count + 1))) {
                    System.out.println("Reserved Character");
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
                    //if look back is white space (found a new word)
                    if (funct.substring(look_back, look_back + 1).trim().length() == 0) {
                        System.out.println("Found word at " + count);
                        look_back = count;
                    }
                    //if look back is a character
                    else {
                        //if count is whitespace (found end of word)
                        if (funct.substring(count, count+ 1).trim().length() == 0) {
                            parts.add(funct.substring(look_back, count));
                            System.out.println("End of word: lookback = " + look_back + " count = " + count);
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
            System.out.println("End of word: lookback = " + look_back + " count = " + count);
            look_back = count;
        }

        return parts;
    }

    private boolean isReserved(String str) {
        return reserved_symbols.contains(str);
    }

}
