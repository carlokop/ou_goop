
package exceptions;

/**
 * Deze exeptie opgooien als er een nieut unieke ID wordt gebruikt
 * @author carlo
 *
 */
public class NietUniekeIdException extends Exception{

    public NietUniekeIdException(){
        super();
    }

    public NietUniekeIdException(String s){
        super(s);
    }
}
