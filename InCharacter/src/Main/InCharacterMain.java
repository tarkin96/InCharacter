package Main;
import ICFileReaders.*;

public class InCharacterMain {

    public static void main (String []args) {

        InCharacter inst = new InCharacter();
        inst.Launch();

        ICFileParser parser = new ICFileParser();
        parser.parse("C:\\Users\\jwk\\Desktop\\InCharacter\\InCharacter\\Config\\Rule Sets\\WadeRules\\Base.txt");
    }

}
