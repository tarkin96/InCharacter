package ICFileReaders;

public class ICFile {
    private Attribute data;
    private String path;
    private String name;
    private String ictype;

    public void setPath(String filepath) {
        path = filepath;
    }

    public void setName(String filename) {
        name = filename;
    }

    public void setICType(String filetype) {
        ictype = filetype;
    }


}
