package ICFiles;

import ICInterpreter.*;

public class Character {
    private Attribute base_data;
    private Attribute data;


    public Character(Attribute read_data) {
        base_data = read_data.copy();
        data = new Attribute();
    }

    public void print() {
        System.out.println("Character's Base Data");
        base_data.print();
        System.out.println("\nCharacter's Data");
        data.print();
    }

    public void interpret() {
        Interpreter inter = new Interpreter();
        inter.interpret(base_data);
        //data = base_data.interpret();

        //print();
    }
}
