package agenda;

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
public class ToDo extends AgendaItem implements Cloneable  {
  
  private Boolean afgevinkt = false;


  /**
   * Maakt ToDo item
   * @param id                          identificerende element mag negatief zijn
   * @param titel                       de titel
   * @param eindDatum                   de datum
   * @throws NullPointerException       null waarde meegegeven
   * @throws IllegalArgumentException   Ongeldige waarde meegegeven
   * @throws DatumVerledenException     Gekozen datum ligt in het verleden
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
   @} */
  public ToDo(int id, String titel, LocalDate eindDatum) throws IllegalArgumentException, NullPointerException, DatumVerledenException {
    super(id,titel, eindDatum);      
  }
  
  /**
   * Todo is afgevinkt
   * @return ToDo is afgevinkt
   */
  public Boolean getAfgevinkt() {
    return afgevinkt;
  }
  
  /**
   * Vinkt todo af als deze nog niet afgevinkt is
   * @throws ReedsAfgevinktException    Todo instance is al afgevinkt
   */
  /*
   @ @Contract vinkaf {
   @  @requires afgevinkt == false
   @  @ensures afgevinkt == true
   @*  @assignable afgevinkt
   @  @signals ReedsAfgevinktException   Todo instance is al afgevinkt
   @ } */
  public void vinkToDoAf() throws ReedsAfgevinktException {
    if(afgevinkt) {
      throw new ReedsAfgevinktException("ToDo is reeds afgevinkt");
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
        "\n" +
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
    todo.afgevinkt = afgevinkt;
    return todo;
  }
  
  
  

}
