package Main;
import ICFileReaders.*;

public class InCharacterMain {

    public static void main (String []args) {

        InCharacter inst = new InCharacter();
        inst.Launch();

        ICFileParser parser = new ICFileParser();
        ICFile icfile = parser.parse("C:\\Users\\jwk\\Desktop\\InCharacter\\InCharacter\\Config\\Rule Sets\\WadeRules\\Base.txt");
        //icfile.print();



        CharacterGen charGen = new CharacterGen();
        charGen.genCharacter(icfile);

        //icfile.print();
    }

}
