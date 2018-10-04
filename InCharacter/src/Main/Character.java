package Main;

import ICFileReaders.*;
import ICInterpreter.*;

public class Character {
    private Attribute base_data;
    private Attribute data;


    public Character(Attribute read_data) {
        base_data = read_data.copy();
    }

    public void print() {
        base_data.print();
        data.print();
    }

    public void interpret() {
        Interpreter inter = new Interpreter();
        inter.interpret(base_data);
        //data = base_data.interpret();

        //data.print();
    }
}
