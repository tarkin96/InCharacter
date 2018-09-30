package Main;
import ICFileReaders.*;

public class CharacterGen {

    public Character genCharacter(ICFile base) {

        Attribute baseData = base.getData();
        Character character = new Character(baseData);
        //character.print();
        return character;
    }

}
