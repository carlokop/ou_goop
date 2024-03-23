package agenda;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import agenda.exceptions.DatumVerledenException;
import agenda.exceptions.ReedsAfgevinktException;


/**
 * ToDo item 
 * Deze klasse maakt een ToDo object 
 * @author carlo
 *
 */
public class ToDo extends Item implements Cloneable  {
  
  private boolean afgevinkt = false;


  /**
   * Maakt ToDo item
   * @param id                          identificerende element mag negatief zijn
   * @param titel                       de titel
   * @param datum                       de datum
   * @throws AgendaException            Er is ongeldige actie uitgevoerd of onjuiste invoer
   * @throws DateTimeException          Er is een datum in het verleden ingevoerd
   */
  /*
   @ @Contract Geldige invoer {
   @  @requires id > 0
   @  @requires titel not null of lege string
   @  @requires einddatum in de toekomst 
   @  @ensures id, titel, einddatum en afgevinkt zijn set
   @  @ensures afgevinkt is false bij initialisatie
   @  @assignable id, titel, einddatum
   @  @signal IllegalArgumentException bij lege String titel
   @  @signal NullPointerException bij tittel of LocalDateTime = null
   @  @signal DatumVerledenException bij einddatum in het verleden
   @}
   */
  public ToDo(int id, String titel, LocalDate datum) throws DateTimeException, AgendaException {
    super(id,titel, datum);      
  }
  
  /**
   * Todo is afgevinkt
   * @return ToDo is afgevinkt
   */
  public boolean getAfgevinkt() {
    return afgevinkt;
  }
  
  /**
   * Vinkt todo af als deze nog niet afgevinkt is
   * @throws AgendaException    Todo instance is al afgevinkt
   */
  /*
   @ @Contract happy {
   @  @requires afgevinkt == false
   @  @ensures afgevinkt = true
   @  @assignable afgevinkt
   @ }
   @  @Contract reeds afgevinkt
   @   @requires afgevinkt == true
   @   @assignable geen
   @   @signals (AgendaException e) e.getMessage().equals("ToDo is reeds afgevinkt");
   @ } */
  public void vinkToDoAf() throws AgendaException {
    if(afgevinkt) {
      throw new AgendaException("ToDo is reeds afgevinkt");
    }
    afgevinkt = true;
  }
  
  /**
   * String representatie van dit object
   * @return de inhoud van dit object
   */
  @Override
  public String toString() {
    return "\n" + 
        "ID: " + getId() + "\n" +
        "Titel: " + getTitel() + "\n" +
        "Einddatum: " + getDatum() + "\n" +
        "Afgevinkt: " + afgevinkt + "\n";
  }
  
  /**
   * Kopieert de instantie met diepe kloon
   * @return een kopie van de instantie
   */
  @Override
  public ToDo clone() {
    ToDo todo = (ToDo) super.clone();  
    if(todo != null) {
      todo.afgevinkt = afgevinkt; 
    }
    return todo;
  }
  

  
  
  

}
