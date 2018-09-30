package Main;
import ICFileReaders.*;

public class CharacterGen {

    public Character genCharacter(ICFile base) {

        Attribute baseData = base.getData();
        Character character = new Character(baseData);

        //fulfill character
        character.interpret();
        //character.print();
        return character;
    }

}
