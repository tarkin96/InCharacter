package ICInterpreter;
import ICFileReaders.*;

public class Interpreter {

    public void interpret (Attribute attr) {
        //for each subattr in list
        for (int eqNum = 0; eqNum < attr.getEquations().size(); eqNum++) {
            //resolve equation

        }
        //for each sub attribute in the attribute
        for (int subAttrNum = 0; subAttrNum < attr.getSubAttrs().size(); subAttrNum++) {
            //interpret that attribute
            interpret(attr.getSubAttrs().get(subAttrNum));
        }
        /*

                read the equations map
                for each equation
                    got an attribute in it?
                    find attribute (search equations and values)




         */
    }

}
