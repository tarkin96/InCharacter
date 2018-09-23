package ICFileReaders;
import javafx.util.Pair;

import java.util.*;

public class Attribute {
    private ArrayList<Attribute> subAttrs;
    private Map<String, Pair<Integer, String>> descriptions;
    private Map<String, Pair<Integer, Integer>> values;
    private Attribute parentAttr;
    private String name;
    private int itemNumber;

    public Attribute(String text) {
        subAttrs = new ArrayList<Attribute>();
        descriptions = new LinkedHashMap<String, Pair<Integer, String>>();
        values = new LinkedHashMap<String, Pair<Integer, Integer>>();
        name = text;
        itemNumber = 0;
    }

    public void addSubAttr(Attribute attr) {
        itemNumber++;
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
    public void addValue(String identifier, Integer val) {
        if (!values.containsKey(identifier)) {
            itemNumber++;
            values.put(identifier, new Pair<Integer, Integer>(itemNumber, val));
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



    public void print() {
        int count = 1, subCount=0, valCount = 0, descCount = 0;

        Iterator<Map.Entry<String, Pair<Integer, Integer>>> valit = values.entrySet().iterator();
        Map.Entry<String, Pair<Integer, Integer>> value = null;
        if (values.size() != 0) {
            value = valit.next();
        }

        Iterator<Map.Entry<String, Pair<Integer, String>>> descit = descriptions.entrySet().iterator();
        Map.Entry<String, Pair<Integer, String>> desc = null;
        if (descriptions.size() != 0) {
            desc = descit.next();
        }

        //System.out.println(subAttrs.size());
        //System.out.println(values.size());
        //System.out.println(descriptions.size());
        while (count <= values.size() + descriptions.size() + subAttrs.size()) {
            if (values.size() != 0 && value.getValue().getKey() == count) {
                //System.out.println("found val");
                count++;
                valCount++;
                System.out.println(value.getKey() + " = " + value.getValue().getValue());
                if (valit.hasNext()) {
                    value = valit.next();
                }

            }
            else if (descriptions.size() != 0 && desc.getValue().getKey() == count) {
                //System.out.println("found desc");
                count++;
                descCount++;
                System.out.println(desc.getKey() + " = " + desc.getValue().getValue());
                if (descit.hasNext()) {
                    desc = descit.next();
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
