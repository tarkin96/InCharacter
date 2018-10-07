package ICFiles;
import javafx.util.Pair;

import java.util.*;

public class Attribute {
    private ArrayList<Attribute> subAttrs;
    private Map<String, Pair<Integer, String>> descriptions;
    private Map<String, Pair<Integer, Float>> values;
    private Map<String, Pair<Integer, String>> equations;
    private Attribute parentAttr;
    private String name;
    private int itemNumber;

    public Attribute() {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        equations = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        name = "";
        itemNumber = 0;
    }

    public Attribute(String text) {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        equations = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        name = text;
        itemNumber = 0;
    }


    //perform deep copy of attribute
    protected Attribute(Attribute other) {
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        equations = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Float>>();
        subAttrs = new ArrayList<Attribute>();
        name = other.getName();

        int count = 1, subCount=0, valCount = 0, eqCount = 0, descCount = 0;

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

        Iterator<Map.Entry<String, Pair<Integer, String>>> eqit = other.getEquations().entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> eq = null;
        if (other.getEquations().size() != 0) {
            eq = eqit.next();
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
            else if (other.getEquations().size() != 0 && eq.getValue().getKey() == count) {
                count++;
                eqCount++;
                //copy equation element
                addEquation(eq.getKey(), eq.getValue().getValue());

                if (eqit.hasNext()) {
                    eq = eqit.next();
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
    }

    public void addSubAttr(Attribute attr) {
        itemNumber++;
        attr.setParentAttr(this);
        subAttrs.add(attr);
        //System.out.println(attr.name);
    }

    public void addDescription(String identifier, String desc) {
        if (!descriptions.containsKey(identifier)) {
            itemNumber++;
            descriptions.put(identifier, new Pair<Integer, String>(itemNumber, desc));
            //System.out.println(identifier + " = " + desc);
        }
        else { System.out.println("This description already exists");}
    }

    public void addEquation(String identifier, String eq) {
        if (!equations.containsKey(identifier)) {
            itemNumber++;
            equations.put(identifier, new Pair<Integer, String>(itemNumber, eq));
            //System.out.println("hmph " + equations.size());
            //System.out.println(identifier + " = " + desc);
        }
        else { System.out.println("This equation already exists");}
    }

    public void addValue(String identifier, Float val) {
        if (!values.containsKey(identifier)) {
            itemNumber++;
            values.put(identifier, new Pair<Integer, Float>(itemNumber, val));
            //System.out.println(identifier + " = " + val.toString());
        }
        else { System.out.println("This value already exists");}
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

    public Map<String, Pair<Integer, String>> getEquations() {
        return equations;
    }

    public Map<String, Pair<Integer, Float>> getValues() {
        return values;
    }

    public int getItemNumber() {
        return itemNumber;
    }


    //start a deep copy for attribute
    public Attribute copy() {

        return new Attribute(this);
    }


    public Attribute interpret() {

        //System.out.println("Equations: " + equations.size());
        Attribute new_Attr = new Attribute();
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

        Iterator<Map.Entry<String, Pair<Integer, String>>> eqit = equations.entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> eq = null;
        if (equations.size() != 0) {
            eq = eqit.next();
        }

        while (count <= itemNumber) {
            if (values.size() != 0 && value.getValue().getKey() == count) {
                //System.out.println("found val");
                count++;
                valCount++;
                new_Attr.addValue(value.getKey(), value.getValue().getValue());
                //System.out.println(value.getKey() + " = " + value.getValue().getValue() + ";");
                if (valit.hasNext()) {
                    value = valit.next();
                }

            }
            else if (descriptions.size() != 0 && desc.getValue().getKey() == count) {
                //System.out.println("found desc");
                count++;
                descCount++;
                new_Attr.addDescription(desc.getKey(), desc.getValue().getValue());
                //System.out.println(desc.getKey() + " = " + desc.getValue().getValue() + ";");
                if (descit.hasNext()) {
                    desc = descit.next();
                }

            }
            else if (equations.size() != 0 && eq.getValue().getKey() == count) {
                //System.out.println("found desc");
                count++;
                eqCount++;
                //this is where the hard part is
                //System.out.println(eq.getKey() + " = " + eq.getValue().getValue() + ";");
                if (eqit.hasNext()) {
                    eq = eqit.next();
                }

            }
            else if (subAttrs.size() != 0 && subAttrs.size() > subCount) {

                new_Attr.addSubAttr(subAttrs.get(subCount).interpret());
                //System.out.println("found sub");
                //System.out.println(subAttrs.get(subCount).name + " =");
                //subAttrs.get(subCount).print();
                //System.out.println(";");
                count++;
                subCount++;

            }
            else {System.out.println("Something bad happened!"); count++;}
        }

        return new_Attr;
    }

    public void print() {

        //System.out.println("Equations: " + equations.size());

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

        Iterator<Map.Entry<String, Pair<Integer, String>>> eqit = equations.entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> eq = null;
        if (equations.size() != 0) {
            eq = eqit.next();
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
            else if (equations.size() != 0 && eq.getValue().getKey() == count) {
                //System.out.println("found desc");
                count++;
                eqCount++;
                System.out.println(eq.getKey() + " = " + eq.getValue().getValue() + ";");
                if (eqit.hasNext()) {
                    eq = eqit.next();
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
    }
}
