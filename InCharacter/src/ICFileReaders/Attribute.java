package ICFileReaders;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Attribute {
    private List<Attribute> subAttrs;
    private Map<String, Pair<Integer, String>> descriptions;
    private Map<String, Pair<Integer, Integer>> values;
    private Attribute parentAttr;

    public void addSubAttr(Attribute attr) {
        subAttrs.add(attr);
    }
    public void addDescription(String identifier, Pair<Integer, String> desc) {
        if (!descriptions.containsKey(identifier)) {
            descriptions.put(identifier, desc);
        }
        else { System.out.println("This description already exists");}
    }
    public void addValue(String identifier, Pair<Integer, Integer> val) {
        if (!values.containsKey(identifier)) {
            values.put(identifier, val);
        }
        else { System.out.println("This value already exists");}
    }
    public void setParentAttr(Attribute attr) {
        parentAttr = attr;
    }
}
