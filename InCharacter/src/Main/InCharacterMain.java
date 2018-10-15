package Main;
import ICFiles.*;

public class InCharacterMain {

    public static void main (String []args) {

        InCharacter inst = new InCharacter();
        inst.Launch();

        ICParser parser = new ICParser();
        ICFile icfile = parser.parse("C:\\Users\\jwk\\Desktop\\InCharacter\\InCharacter\\Data\\Rule Sets\\WadeRules\\Base.txt");
        //icfile.print();



        CharacterGen charGen = new CharacterGen();
        charGen.genCharacter(icfile);

        //icfile.print();
    }

}
