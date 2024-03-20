package agenda.interfaces;

import java.time.LocalDate;

/**
 * De interface beschrijft items voor in de agenda
 * @author carlo
 *
 */
public interface ItemInterface {
 
  /**
   * String representatie van dit object
   * @return print de inhoud van dit object
   */
  public String toString();
  
  /**
   * Maakt een kloon van dit object
   * @return een kopie van de instantie
   */
  public Object clone();
  
  /**
   * Gekozen einddatum is nog niet verstreken
   * @param  datum  deze datum wordt getest of die nog niet is verstreken
   * @return true als de gekozen datum of vandaag of in de toekomst ligt
   */
  public Boolean einddatumNogNietVerstreken(LocalDate datum);
  
  /**
   * Geeft identifier
   * @return de ID
   */
  public int getId();
  
  /**
   * Geeft titel
   * @return de titel
   */
  public String getTitel();
  
  
  /**
   * Geeft einddatum
   * @return LocalDate met de datum
   */
  public LocalDate getDatum();

}
