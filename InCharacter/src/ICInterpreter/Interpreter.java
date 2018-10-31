package ICInterpreter;

import ICFiles.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Interpreter {

    private ArrayList<String> reserved_symbols;
    private ArrayList<String> reserved_words;
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
        reserved_symbols = new ArrayList<String>();
        reserved_words = new ArrayList<String>();
    }

    public void init(String filepath) {
        String reserved_words_path = filepath + "reserved_words.txt";
        String reserved_symbols_path = filepath + "reserved_symbols.txt";

        //get all reserved words from configuration
        File symbols_file = new File(reserved_symbols_path);
        File words_file = new File(reserved_words_path);
        try {
            Scanner scan = new Scanner(symbols_file);
            while (scan.hasNextLine()) {
                reserved_symbols.add(scan.nextLine());
            }
            scan = new Scanner(words_file);
            while (scan.hasNextLine()) {
                reserved_words.add(scan.nextLine());
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
                String resStr = resolve(attr, attr.getExpressions().get(attr.getMappings().get(i)));
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
    private String resolve(Attribute attr, String expr) {
        ArrayList<String> tokens = getStringParts(expr);

        //scans for variables
        for (int i = 0; i < tokens.size(); i++) {
            //if it's a method, do something for that method
            if (isReserved(tokens.get(i))) {
                if (tokens.get(i).equals(":")) {System.out.println("Found reserved item!");
                    resolve_Range(attr, tokens, i);
                }
                else if (false) {

                }
                else {System.out.println("Reserved token not implemented yet!");}
            }
            //if it's a varirable
            else if (!isNumeric((tokens.get(i))) && !isDescription(tokens.get(i))) {
                tokens.set(i, interpret_var(attr, tokens.get(i)));
            }
            //moves to next token if it's a value or description


        }

        //begin interpretting whole line


        //placeholder for actual resolve of item
        String retStr = new String();
        for (int j = 0; j < tokens.size(); j++) {
            retStr+=tokens.get(j);
        }

        System.out.println("Expression resulted as: " + retStr);
        //resolve parts as a whole

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
                check_expr = resolve(attr, check_expr);
                return check_expr;
            }

        }
        //return "";
    }

    //interpret if statement
    private String do_if(ArrayList<String> statement) {
        return "true";
    }

    //resolves the range method and changes the tokens arrayList
    private void resolve_Range(Attribute attr, ArrayList<String> tokens, int rangeIndex) {
        //if right side of expression is another expression
        if (isReserved(tokens.get(rangeIndex+1))) {
            String newExpr = makeExpression(tokens, rangeIndex + 1, findNextExpression(tokens, rangeIndex + 1));
            tokens.subList(rangeIndex+1, findNextExpression(tokens, rangeIndex + 1) + 1).clear();
            tokens.add(rangeIndex + 1, resolve(attr, newExpr));
            String result = do_range(tokens.get(rangeIndex - 1), tokens.get(rangeIndex+1));
            tokens.subList(rangeIndex, rangeIndex + 1).clear();
            tokens.set(rangeIndex - 1, result);
        }
        else {
            String result = do_range(tokens.get(rangeIndex-1), tokens.get(rangeIndex+1));
            tokens.remove(rangeIndex+1); tokens.remove(rangeIndex);
            tokens.set(rangeIndex - 1, result);
        }

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
        ArrayList<String> tokens = new ArrayList<String>();
        if (funct.length() == 0) {return tokens;}
        int count = 0;
        int lookback = 0;

        while (count <= funct.length()) {
            //if at end of string
            if (count == funct.length()) {
               //if lookback points to a character
                if (funct.substring(lookback, lookback + 1).trim().length() != 0) {
                    //add rest of the token
                    tokens.add(funct.substring(lookback, count));
                    return tokens;
                }
            }
            else {
                //if window is empty space
                if(funct.substring(lookback, count+1).trim().length() == 0) {
                    //System.out.println("Emtpy Space");
                    count++;
                }
                //window contains characters
                else {
                    //if start of window is white space (found new set of words or symbols)
                    if (funct.substring(lookback, lookback+1).trim().length() == 0) {
                        //set window to current character
                        lookback = count;
                    }
                    //check if current window is a reserved symbol
                    if (isReservedSymbol(funct.substring(lookback, count + 1))) {
                        //check if it is unambiguous
                        if (!isAmbiguous(funct.substring(lookback, count + 1))) {
                            //add it to tokens
                            tokens.add(funct.substring(lookback, count + 1));
                            //move to next character
                            count++;
                            lookback = count;
                        }
                        //if ambiguous
                        else {
                            String newStr = lookAhead(funct, count, lookback);
                            tokens.add(newStr);
                            lookback = lookback + newStr.length();
                            count = lookback;
                        }
                    }
                    //if new character is whitespace (found end of word or symbol)
                    else if (funct.substring(count, count + 1).trim().length() == 0) {
                        //add word to tokens
                        tokens.add(funct.substring(lookback, count));
                        //set to scan for new words
                        lookback = count;
                        count++;
                    }
                }
            }

        }

        return tokens;
    }

    private String lookAhead(String str, int count, int lookback) {
        int addon = 0;
        String possible = new String();
        //possible = str.substring(lookback, count + 1);
        //look ahead until we reach the end, become unambiguous (have 1 or 0 options), or reach whitespace
        while ((count + addon  <= str.length() - 1) && hasPossibleMatch(str.substring(lookback, count + 1 + addon))
                && (str.substring(count + addon, count + addon + 1 ).trim().length() != 0)) {
            if (isReservedSymbol(str.substring(lookback, count + addon + 1))) {
                possible = str.substring(lookback, count + addon + 1 );
            }
            addon++;
        }

        //if our window is a reserved symbol
        return possible;
    }


    //methods to help determine if a string is of a certain type
    private boolean isReserved(String str) {
        return (reserved_symbols.contains(str) || reserved_words.contains(str));
    }

    private boolean isReservedSymbol(String str) {
        return reserved_symbols.contains(str);
    }

    private boolean isReservedWord(String str) {
        return reserved_words.contains(str);
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    //checks if there are more than 1 possible symbols left
    private boolean isAmbiguous(String str) {
        int match_count = 0;
        for (int i = 0; i < reserved_symbols.size(); i++) {
            //if symbol is possible
            if (reserved_symbols.get(i).substring(0, str.length()).equals(str)) {
                match_count++;
            }
            if (match_count > 1) {
                return true;
            }
        }
        return false;
    }

    //checks to see if a symbol starts with the input string
    private boolean hasPossibleMatch(String str) {
        for (int i = 0; i < reserved_symbols.size(); i++) {
            //if there is a possible match
            if (reserved_symbols.get(i).substring(0, str.length()).equals(str)) {
                return true;
            }
        }
        return false;
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
