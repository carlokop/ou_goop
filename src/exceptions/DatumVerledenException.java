
package exceptions;

/**
 * Deze exeptie opgooien als er een datum of tijd wordt gekozen die in het verleden ligt
 * @author carlo
 *
 */
public class DatumVerledenException extends Exception{

    /**
   * 
   */
  private static final long serialVersionUID = 1L;

    public DatumVerledenException(){
        super();
    }

    public DatumVerledenException(String s){
        super(s);
    }
}
