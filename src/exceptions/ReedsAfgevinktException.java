
package exceptions;

/**
 * Deze exeptie opgooien als een todo reeds is afgevinkt
 * @author carlo
 *
 */
public class ReedsAfgevinktException extends Exception{

   /**
   * Geen idee waarom dit nodig is
   */
  private static final long serialVersionUID = 1L;

    public ReedsAfgevinktException(){
        super();
    }

    public ReedsAfgevinktException(String s){
        super(s);
    }
}
