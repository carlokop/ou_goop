package agenda;

import java.time.LocalDate;

import exceptions.DatumVerledenException;
import exceptions.ReedsAfgevinktException;


/**
 * ToDo item 
 * @author carlo
 *
 */
public class ToDo extends AgendaItem  {
  
  private Boolean afgevinkt = false;


  /**
   * Maakt ToDo item
   * @param id identificerende element mag negatief zijn
   * @param titel
   * @param eindDatum
   * @throws IllegalArgumentException 
   * @throws NullPointerException 
   * @throws DatumVerledenException 
   * @throws ReedsAfgevinktException
   * 
   * @Contract Geldige invoer
   *  @requires id > 0
   *  @requires titel not null of lege string
   *  @requires einddatum in de toekomst 
   *  @ensures id, titel, einddatum en afgevinkt zijn set
   *  @ensures afgevinkt is false bij initialisatie
   *  @assignable id, titel, einddatum
   *  @signal IllegalArgumentException bij lege String titel
   *  @signal NullPointerException bij tittel of LocalDateTime = null
   *  @signal DatumVerledenException bij einddatum in het verleden
   */
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
   * @throws ReedsAfgevinktException
   * 
   * @Contract vinkaf
   *  @requires afgevinkt == false
   *  @ensures afgevinkt == true
   *  @assignable afgevinkt
   *  @signal ReedsAfgevinktException
   */
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
    return "ID: " + getId() + "\n" +
        "Titel: " + getTitel() + "\n" +
        "Einddatum: " + getEindDatum() + "\n" +
        "Afgevinkt: " + afgevinkt + "\n";
  }
  
  
  

}
