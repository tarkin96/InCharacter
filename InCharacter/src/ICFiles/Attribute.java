package ICFiles;
//import javafx.util.Pair;

import java.util.*;

public class Attribute {
    private ArrayList<Attribute> subAttrs;
    private Map<String, String> descriptions;
    private Map<String, Float> values;
    private Map<String, String> expressions;
    private Map<String, String> functions;
    private ArrayList<String> mappings;
    private Attribute parentAttr;
    private String name;
    private int itemNumber;

    public Attribute() {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new HashMap<String, String>();
        expressions = new HashMap<String, String>();
        functions = new HashMap<String, String>();
        values = new HashMap<String, Float>();
        mappings = new ArrayList<String>();
        name = "";
        itemNumber = 0;
    }

    public Attribute(String text) {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new HashMap<String, String>();
        expressions = new HashMap<String, String>();
        functions = new HashMap<String, String>();
        values = new HashMap<String, Float>();
        mappings = new ArrayList<String>();
        name = text;
        itemNumber = 0;
    }

    //even better deep copy
    protected Attribute(Attribute other) {

        descriptions = new HashMap<String, String>();
        expressions = new HashMap<String, String>();
        functions = new HashMap<String, String>();
        values = new HashMap<String, Float>();
        subAttrs = new ArrayList<Attribute>();
        mappings = new ArrayList<String>();
        name = other.getName();

        for (int i = 0; i < other.getMappings().size(); i++) {
            if (other.getValues().containsKey(other.getMappings().get(i))) {
                addValue(other.getMappings().get(i), other.getValues().get(other.getMappings().get(i)));
                //System.out.println(mappings.get(i) + " = " + values.get(mappings.get(i)).getValue() + ";");
            }
            else if (other.getDescriptions().containsKey(other.getMappings().get(i))) {
                addDescription(other.getMappings().get(i), other.getDescriptions().get(other.getMappings().get(i)));
                //System.out.println(mappings.get(i) + " = " + descriptions.get(mappings.get(i)).getValue() + ";");
            }
            else if (other.getExpressions().containsKey(other.getMappings().get(i))) {
                addExpression(other.getMappings().get(i), other.getExpressions().get(other.getMappings().get(i)));
                //System.out.println(mappings.get(i) + " = " + functions.get(mappings.get(i)).getValue() + ";");
            }
            else if (other.getFunctions().containsKey(other.getMappings().get(i))) {
                addFunction(other.getMappings().get(i), other.getFunctions().get(other.getMappings().get(i)));
                //System.out.println(mappings.get(i) + " = " + functions.get(mappings.get(i)).getValue() + ";");
            }
            else if (other.subAttrsContains(other.getMappings().get(i))) {
                addSubAttr(other.findSubAttr(other.getMappings().get(i)).copy());
                //System.out.println(findSubAttr(mappings.get(i)).getName() + " =");
                //findSubAttr(mappings.get(i)).print();
                //System.out.println(";");
            }

        }
    }


    public void addSubAttr(Attribute attr) {
        //itemNumber++;
        attr.setParentAttr(this);
        subAttrs.add(attr);
        addMapping(attr.getName());
        //System.out.println(attr.name);
    }

    public void addDescription(String identifier, String desc) {
        if (!mappings.contains(identifier)) {
            //itemNumber++;
            descriptions.put(identifier, desc);
            addMapping(identifier);
            //System.out.println(identifier + " = " + desc);
        }
        else {
            //System.out.println("This description already exists");
            descriptions.put(identifier, desc);
        }
    }

    public void addExpression(String identifier, String expr) {
        if (!mappings.contains(identifier)) {
            //itemNumber++;
            expressions.put(identifier, expr);
            addMapping(identifier);
            //System.out.println("hmph " + functions.size());
            //System.out.println(identifier + " = " + desc);
        }
        else {
            //System.out.println("This equation already exists");
            expressions.put(identifier, expr);
        }
    }

    public void addFunction(String identifier, String fun) {
        if (!mappings.contains(identifier)) {
            //itemNumber++;
            functions.put(identifier, fun);
            addMapping(identifier);
            //System.out.println("hmph " + functions.size());
            //System.out.println(identifier + " = " + desc);
        }
        else {
            //System.out.println("This function already exists");
            functions.put(identifier, fun);
        }
    }

    public void addValue(String identifier, Float val) {
        if (!mappings.contains(identifier)) {
            //itemNumber++;
            values.put(identifier, val);
            addMapping(identifier);
            //System.out.println(identifier + " = " + val.toString());
        }
        else {
            //System.out.println("This value already exists");
            values.put(identifier, val);
        }
    }

    public void addMapping(String identifier) {
        mappings.add(identifier);
    }

    public void setParentAttr(Attribute attr) {
        parentAttr = attr;
    }

    public Attribute getParentAttr() {
        return parentAttr;
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getSubAttrs() {
        return subAttrs;
    }

    public Map<String, String> getDescriptions() {
        return descriptions;
    }

    public Map<String, String> getExpressions() {
        return expressions;
    }

    public Map<String, String> getFunctions() {
        return functions;
    }

    public Map<String, Float> getValues() {
        return values;
    }

    public ArrayList<String> getMappings() {
        return mappings;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    private boolean subAttrsContains(String find) {
        for (int i = 0; i < subAttrs.size(); i++) {
            if (subAttrs.get(i).getName().equals(find)) {
                return true;
            }
        }
        return false;
    }

    public Attribute findSubAttr(String find) {
        for (int i = 0; i < subAttrs.size(); i++) {
            if (subAttrs.get(i).getName().equals(find)) {
                return subAttrs.get(i);
            }
        }
        return null;
    }


    //start a deep copy for attribute
    public Attribute copy() {

        return new Attribute(this);
    }

    //new and improved print for attributes
    public void print() {
        for (int i = 0; i < mappings.size(); i++) {
            if (values.containsKey(mappings.get(i))) {
                System.out.println(mappings.get(i) + " = " + values.get(mappings.get(i)) + ";");
            }
            else if (descriptions.containsKey(mappings.get(i))) {
                System.out.println(mappings.get(i) + " = " + descriptions.get(mappings.get(i)) + ";");
            }
            else if (expressions.containsKey(mappings.get(i))) {
                System.out.println(mappings.get(i) + " = " + expressions.get(mappings.get(i)) + ";");
            }
            else if (functions.containsKey(mappings.get(i))) {
                System.out.println(mappings.get(i) + " = " + functions.get(mappings.get(i)) + ";");
            }
            else if (subAttrsContains(mappings.get(i))) {

                System.out.println(findSubAttr(mappings.get(i)).getName() + " =");
                findSubAttr(mappings.get(i)).print();
                System.out.println(";");
            }

        }
    }


    //remove methods
    public void removeValue(String key) {
        values.remove(key);
    }

    public void removeDescription(String key) {
        descriptions.remove(key);
    }

    public void removeExpression(String key) {
        expressions.remove(key);
    }
    public void removeFunction(String key) {
        functions.remove(key);
    }
    public void removeSubAttr(String key) {
        subAttrs.remove(mappings.indexOf(key));
    }

}
