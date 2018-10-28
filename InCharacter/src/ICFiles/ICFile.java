package ICFiles;

public class ICFile {
    protected Attribute data;
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

    public Attribute getData() {
        return data;
    }

    //add a sub attribute
    public void addSubAttr(String name) {
        Attribute newAttr = new Attribute(name);
        data.addSubAttr(newAttr);
        //newAttr.setParentAttr(data);
        data = newAttr;
    }

    //add a value
    public void addVal(String name, Float val) {
        data.addValue(name, val);
    }

    //add a description
    public void addDesc(String name, String desc) {
        data.addDescription(name, desc);
    }

    //add an expression
    public void addExpr(String name, String expr) {
        data.addExpression(name, expr);
    }

    //add a function
    public void addFun(String name, String fun) {
        data.addExpression(name, fun);
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
