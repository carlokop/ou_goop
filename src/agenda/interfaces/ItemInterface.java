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
   * @return de inhoud van dit object
   */
  public String toString();
  
  /**
   * Maakt een shallow kloon van dit object
   * @throws CloneNotSupportedException 
   */
  public Object clone();
  
  /**
   * Gekozen einddatum is nog niet verstreken
   * @param  einddatum
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
   * @return LocalDate met de einddatum
   */
  public LocalDate getDatum();

}
