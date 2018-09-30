package Main;

import ICFileReaders.*;

public class Character {
    private Attribute data;


    public Character(Attribute read_data) {
        data = read_data.copy();
    }

    public void print() {
        data.print();
    }
}
