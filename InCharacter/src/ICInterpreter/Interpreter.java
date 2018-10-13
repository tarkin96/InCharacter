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



    public Attribute interpret (Attribute attr) {
        Attribute newAttr = attr.copy();
        recInterpret(newAttr);
        return newAttr;
    }

    private void recInterpret(Attribute attr) {

    }

    //gives value of line
    private String resolve(Attribute attr, String key, String funct) {
        //System.out.println("made it!");
        ArrayList<String> parts = getStringParts(funct);
        for (int i = 0; i < parts.size(); i++) {

            //System.out.println(parts.get(i));
        }
        //scan for equations or values
        for (int i = 0; i < parts.size(); i++) {
            //check if element is reserved
            if (false) {
                //do something
            }
            //if not numeric
            else if (!isNumeric(parts.get(i))) {
                //resolve recursively
                //if it references sub attribute
                parts.set(i, interpret_var(attr, parts.get(i)));
            }
            //if numeric
            else if(false) {
                //do nothing
            }

        }


        //placeholder for actual resolve of item
        String retStr = new String();
        for (int j = 0; j < parts.size(); j++) {
            retStr+=parts.get(j);
        }
        return retStr;
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

    private String interpret_var(Attribute attr, String variable) {
        ICParser parser = new ICParser();
        //String whatsleft = variable;
        if (variable.contains(".")) {
            interpret_var(parser.findAttribute(attr, variable.substring(0, variable.indexOf("."))),
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
                //check_func = resolve(attr, variable, check_func);
                return check_func;
            }

        }
        return "";
    }






    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
