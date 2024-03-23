package agenda;

/**
 * Exceptie voor een algemene fout in de Agenda
 * @author carlo
 *
 */
@SuppressWarnings("serial")
public class AgendaException extends Exception{

    public AgendaException(){
        super();
    }

    public AgendaException(String s){
        super(s);
    }
}
