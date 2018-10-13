package ICFiles;
import javafx.util.Pair;

import java.util.*;

public class Attribute {
    private ArrayList<Attribute> subAttrs;
    private Map<String, Pair<Integer, String>> descriptions;
    private Map<String, Pair<Integer, Float>> values;
    private Map<String, Pair<Integer, String>> functions;
    private ArrayList<String> mappings;
    private Attribute parentAttr;
    private String name;
    private int itemNumber;

    public Attribute() {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        functions = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        mappings = new ArrayList<String>();
        name = "";
        itemNumber = 0;
    }

    public Attribute(String text) {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        functions = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        mappings = new ArrayList<String>();
        name = text;
        itemNumber = 0;
    }

    //even better deep copy
    protected Attribute(Attribute other) {

        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        functions = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        subAttrs = new ArrayList<Attribute>();
        mappings = new ArrayList<String>();
        name = other.getName();

        for (int i = 0; i < other.getMappings().size(); i++) {
            if (other.getValues().containsKey(other.getMappings().get(i))) {
                addValue(other.getMappings().get(i), other.getValues().get(other.getMappings().get(i)).getValue());
                //System.out.println(mappings.get(i) + " = " + values.get(mappings.get(i)).getValue() + ";");
            }
            else if (other.getDescriptions().containsKey(other.getMappings().get(i))) {
                addDescription(other.getMappings().get(i), other.getDescriptions().get(other.getMappings().get(i)).getValue());
                //System.out.println(mappings.get(i) + " = " + descriptions.get(mappings.get(i)).getValue() + ";");
            }
            else if (other.getFunctions().containsKey(other.getMappings().get(i))) {
                addFunction(other.getMappings().get(i), other.getFunctions().get(other.getMappings().get(i)).getValue());
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




    //perform deep copy of attribute
    /*protected Attribute(Attribute other) {
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        functions = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        subAttrs = new ArrayList<Attribute>();
        mappings = new ArrayList<String>();
        name = other.getName();

        int count = 1, subCount=0, valCount = 0, funCount = 0, descCount = 0;

        Iterator<Map.Entry<String, Pair<Integer, Float>>> valit = other.getValues().entrySet().iterator();
        Map.Entry<String, Pair<Integer, Float>> value = null;
        if (other.getValues().size() != 0) {
            value = valit.next();
        }

        Iterator<Map.Entry<String, Pair<Integer, String>>> descit = other.getDescriptions().entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> desc = null;
        if (other.getDescriptions().size() != 0) {
            desc = descit.next();
        }

        Iterator<Map.Entry<String, Pair<Integer, String>>> funit = other.getFunctions().entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> fun = null;
        if (other.getFunctions().size() != 0) {
            fun = funit.next();
        }

        while (count <= other.getItemNumber()) {
            if (other.getValues().size() != 0 && value.getValue().getKey() == count) {
                count++;
                valCount++;
                //copy value element
                addValue(value.getKey(), value.getValue().getValue());
                if (valit.hasNext()) {
                    value = valit.next();
                }

            }
            else if (other.getDescriptions().size() != 0 && desc.getValue().getKey() == count) {
                count++;
                descCount++;
                //copy description element
                addDescription(desc.getKey(), desc.getValue().getValue());
                if (descit.hasNext()) {
                    desc = descit.next();
                }

            }
            else if (other.getFunctions().size() != 0 && fun.getValue().getKey() == count) {
                count++;
                funCount++;
                //copy function element
                addFunction(fun.getKey(), fun.getValue().getValue());

                if (funit.hasNext()) {
                    fun = funit.next();
                }

            }
            else if (other.getSubAttrs().size() != 0 && other.getSubAttrs().size() > subCount) {
                //copy subattr element
                addSubAttr(other.getSubAttrs().get(subCount).copy());
                count++;
                subCount++;

            }
            else {System.out.println("Something bad happened!"); count++;}
        }
    }*/

    public void addSubAttr(Attribute attr) {
        itemNumber++;
        attr.setParentAttr(this);
        subAttrs.add(attr);
        addMapping(attr.getName());
        //System.out.println(attr.name);
    }

    public void addDescription(String identifier, String desc) {
        if (!descriptions.containsKey(identifier)) {
            itemNumber++;
            descriptions.put(identifier, new Pair<Integer, String>(itemNumber, desc));
            addMapping(identifier);
            //System.out.println(identifier + " = " + desc);
        }
        else { System.out.println("This description already exists");}
    }

    public void addFunction(String identifier, String fun) {
        if (!functions.containsKey(identifier)) {
            itemNumber++;
            functions.put(identifier, new Pair<Integer, String>(itemNumber, fun));
            addMapping(identifier);
            //System.out.println("hmph " + functions.size());
            //System.out.println(identifier + " = " + desc);
        }
        else { System.out.println("This equation already exists");}
    }

    public void addValue(String identifier, Float val) {
        if (!values.containsKey(identifier)) {
            itemNumber++;
            values.put(identifier, new Pair<Integer, Float>(itemNumber, val));
            addMapping(identifier);
            //System.out.println(identifier + " = " + val.toString());
        }
        else { System.out.println("This value already exists");}
    }

    private void addMapping(String identifier) {
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

    public Map<String, Pair<Integer, String>> getDescriptions() {
        return descriptions;
    }

    public Map<String, Pair<Integer, String>> getFunctions() {
        return functions;
    }

    public Map<String, Pair<Integer, Float>> getValues() {
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
                System.out.println(mappings.get(i) + " = " + values.get(mappings.get(i)).getValue() + ";");
            }
            else if (descriptions.containsKey(mappings.get(i))) {
                System.out.println(mappings.get(i) + " = " + descriptions.get(mappings.get(i)).getValue() + ";");
            }
            else if (functions.containsKey(mappings.get(i))) {
                System.out.println(mappings.get(i) + " = " + functions.get(mappings.get(i)).getValue() + ";");
            }
            else if (subAttrsContains(mappings.get(i))) {

                System.out.println(findSubAttr(mappings.get(i)).getName() + " =");
                findSubAttr(mappings.get(i)).print();
                System.out.println(";");
            }

        }
    }


    /*public void print() {

        //System.out.println("functions: " + functions.size());

        int count = 1, subCount=0, valCount = 0, eqCount = 0, descCount = 0;

        Iterator<Map.Entry<String, Pair<Integer, Float>>> valit = values.entrySet().iterator();
        Map.Entry<String, Pair<Integer, Float>> value = null;
        if (values.size() != 0) {
            value = valit.next();
        }

        Iterator<Map.Entry<String, Pair<Integer, String>>> descit = descriptions.entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> desc = null;
        if (descriptions.size() != 0) {
            desc = descit.next();
        }

        Iterator<Map.Entry<String, Pair<Integer, String>>> funit = functions.entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> fun = null;
        if (functions.size() != 0) {
            fun = funit.next();
        }

        while (count <= itemNumber) {
            //System.out.println("Size: " + values.size() + " itemnum = " + desc.getValue().getKey() + " count = " + count);
            if (values.size() != 0 && value.getValue().getKey() == count) {
                //System.out.println("found val");
                count++;
                valCount++;
                System.out.println(value.getKey() + " = " + value.getValue().getValue() + ";");
                if (valit.hasNext()) {
                    value = valit.next();
                }

            }
            else if (descriptions.size() != 0 && desc.getValue().getKey() == count) {
                //System.out.println("found desc");
                count++;
                descCount++;
                System.out.println(desc.getKey() + " = " + desc.getValue().getValue() + ";");
                if (descit.hasNext()) {
                    desc = descit.next();
                }

            }
            else if (functions.size() != 0 && fun.getValue().getKey() == count) {
                //System.out.println("found desc");
                count++;
                eqCount++;
                System.out.println(fun.getKey() + " = " + fun.getValue().getValue() + ";");
                if (funit.hasNext()) {
                    fun = funit.next();
                }

            }
            else if (subAttrs.size() != 0 && subAttrs.size() > subCount) {
                //System.out.println("found sub");
                System.out.println(subAttrs.get(subCount).name + " =");
                subAttrs.get(subCount).print();
                System.out.println(";");
                count++;
                subCount++;

            }
            else {System.out.println("Something bad happened!"); count++;}
        }
    }*/
}
