package ICInterpreter;

import ICFiles.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Interpreter {

    private ArrayList<String> reserved_symbols;
    private String reserved_file;
    private static Interpreter instance = null;


    /*


    These methods are used to get the interpretation going.


     */

    public static Interpreter getInstance() {
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

    //goes through all expressions and tries to resolve them
    private void recInterpret(Attribute attr) {
        for (int i = 0; i < attr.getMappings().size(); i++) {
            if (attr.getExpressions().containsKey(attr.getMappings().get(i))) {
                String resStr = resolve(attr, attr.getMappings().get(i), attr.getExpressions().get(attr.getMappings().get(i)));
                addResolve(attr, attr.getMappings().get(i), resStr);
            }
        }
        //recursively interpret sub attributes
        for (int i = 0; i < attr.getSubAttrs().size(); i++) {
            recInterpret(attr.getSubAttrs().get(i));
        }
    }


    /*



    This is the main bulk and main methods used for interpretation. They
    do most of the work.



    */



    //resolves an entire expression and returns resolved value
    private String resolve(Attribute attr, String key, String expr) {
        ArrayList<String> parts = getStringParts(expr);

        //scans for variables
        for (int i = 0; i < parts.size(); i++) {
            //if it's a method, do something for that method
            if (isReserved(parts.get(i))) {

                if (parts.get(i).equals(":")) {System.out.println("Found reserved item!");
                    //if right side of expression is another expression
                    if (isReserved(parts.get(i+1))) {
                        String newExpr = makeExpression(parts, i + 1, findNextExpression(parts, i + 1));
                        parts.subList(i+1, findNextExpression(parts, i+ 1) + 1).clear();
                        parts.add(i + 1, resolve(attr, key, newExpr));
                        String result = do_range(parts.get(i - 1), parts.get(i+1));
                        parts.subList(i, i + 1).clear();
                        parts.set(i - 1, result);
                    }
                    else {
                        String result = do_range(parts.get(i-1), parts.get(i+1));
                        parts.remove(i+1); parts.remove(i);
                        parts.set(i - 1, result);
                    }
                }
            }
            //if it's a varirable
            else if (!isNumeric((parts.get(i))) && !isDescription(parts.get(i))) {
                parts.set(i, interpret_var(attr, parts.get(i)));
            }
            //moves to next token if it's a value or description


        }

        //begin interpretting whole line


        //placeholder for actual resolve of item
        String retStr = new String();
        for (int j = 0; j < parts.size(); j++) {
            retStr+=parts.get(j);
        }

        System.out.println("Expression resulted as: " + retStr);
        //resolve parts as a whole

        //place new object into appropriate map
        /*if (isNumeric(retStr)) {
            attr.addValue(key, Float.parseFloat(retStr));
        }
        else if (isDescription(retStr)) {
            attr.addDescription(key, retStr);
        }
        else {
            System.out.println("Something went wrong with resolving function!");
        }
        //remove function from function map
        attr.removeExpression(key);*/
        return retStr;
    }


    //how to interpret variables
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
            String check_expr = null;
            if (check_val == null) {
                check_desc = parser.findDescription(attr, variable);
            }
            else {
                return check_val.toString();
            }
            if (check_desc == null) {
                check_expr = parser.findExpression(attr, variable);

            }
            else {
                return check_desc;
            }
            if (check_expr == null) {
                return null;
            }
            else {
                //resolve the new function in the attribute
                check_expr = resolve(attr, variable, check_expr);
                return check_expr;
            }

        }
        //return "";
    }

    //interpret if statement
    private String do_if(ArrayList<String> statement) {
        return "true";
    }

    //interpret range (ex. 4:6)
    private String do_range(String first, String second) {
        if (!isInteger(first) && !isInteger(second)) {
            return "null";
        }
        Random rand = new Random();
        int first_val = Integer.parseInt(first);
        int second_val = Integer.parseInt(second);

        if (first_val <= second_val) {
            return Integer.toString(rand.nextInt(second_val - first_val) + first_val) ;
        }
        else {
            return Integer.toString(rand.nextInt(first_val - second_val) + second_val) ;
        }
    }






    /*


    These are some helper methods.


     */




    //parses line into tokens (words and symbols)
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


    //methods to help determine if a string is of a certain type
    private boolean isReserved(String str) {
        return reserved_symbols.contains(str);
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean isDescription(String str) {
        if (str == null || str.equals("") ) {
            return false;
        }
        if (str.substring(0, 1).equals("\"") && str.substring(str.length() - 1, str.length()).equals("\"")) {
            return true;
        }
        else {return false;}

    }

    private boolean isInteger(String str) { return str.matches("-?\\d+"); }

    private int findNextExpression(ArrayList<String> tokens, int start_index) {
        //search inside set of parenthesis
        if (tokens.get(start_index).equals("(")) {
            int levels = 1;
            int i = start_index + 1;
            while (levels != 0 && i < tokens.size()) {
                if (tokens.get(i).equals(")")) {
                    levels--;
                }
                else if (tokens.get(i).equals("(")) {
                    levels++;
                }
                i++;
            }
            if (levels != 0) {
                return -1; //ERROR!!!!!!
            }
            return i;

        }
        //search for end of if statement
        else if (tokens.get(start_index).equals("if")) {

        }
        return -1; //ERROR!!!!

    }

    private String makeExpression(ArrayList<String> tokens, int begin, int end) {
        if (end >= tokens.size()) {
            return "null";
        }
        String retStr = new String();
        for (int i = begin; i <= end; i++) {
            retStr = retStr + " " + tokens.get(i);
        }
        return retStr;
    }

    private void addResolve(Attribute attr, String key, String retStr) {
        //place new object into appropriate map
        if (isNumeric(retStr)) {
            attr.addValue(key, Float.parseFloat(retStr));
        }
        else if (isDescription(retStr)) {
            attr.addDescription(key, retStr);
        }
        else {
            System.out.println("Something went wrong with resolving function!");
        }
        //remove function from function map
        attr.removeExpression(key);
    }

}
