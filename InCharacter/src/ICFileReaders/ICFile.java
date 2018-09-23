package ICFileReaders;

public class ICFile {
    private Attribute data;
    //private Attribute currAttr;
    private String path;
    private String name;
    private String ictype;

    public ICFile() {
        data = new Attribute("");
        path = "";
        name = "";
        ictype = "";
    }

    public void setPath(String filepath) {
        path = filepath;
    }

    public void setName(String filename) {
        name = filename;
    }

    public void setICType(String filetype) {
        ictype = filetype;
    }

    //add a sub attribute
    public void addData(String name) {
        Attribute newAttr = new Attribute(name);
        data.addSubAttr(newAttr);
        newAttr.setParentAttr(data);
        data = newAttr;
    }

    //add a value
    public void addData(String name, Integer val) {
        data.addValue(name, val);
    }

    //add a description
    public void addData(String name, String desc) {
        data.addDescription(name, desc);
    }

    //sets current Attribute to the parent attribute
    public void setParentAsData () {
        data = data.getParentAttr();
    }


    public void print() {

        data.print();
        System.out.println("life is great!");
    }


}
