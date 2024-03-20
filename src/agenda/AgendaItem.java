package agenda;

import java.time.LocalDate;

import agenda.exceptions.DatumVerledenException;
import agenda.interfaces.ItemInterface;

/**
 * Abstracte klasse die informatie over agenda items bevat
 * @author carlo
 *
 */
public abstract class AgendaItem implements ItemInterface, Cloneable {
  
  private int id;
  private String titel;
  private LocalDate datum;

  /**
   * Inits class 
   * @param id
   * @param titel
   * @param eindDatum 
   * @throws NullPointerException
   * @throws IllegalArgumentException
   * @throws DatumVerledenException 
   * 
   * @Contract happy
   *  @requires titel niet null
   *  @requires titel geen lege string
   *  @required id > 0
   *  @requires dat de einddatum bij creatie nog niet versteken is
   *  @ensures dat id, titel en datum zijn ingesteld met niet lege of null waarden
   *  @signal NullPointerException als titel == null
   *  @signal IllegalArgumentException als titel is lege string
   *  @signal IllegalArgumentException als id <= 0
   *  @signal einddatumNogNietVerstreken als datum eerdfer dan vandaag was
   *  
   */
  public AgendaItem(int id, String titel, LocalDate datum) 
      throws NullPointerException, IllegalArgumentException, DatumVerledenException 
  {
    if(titel == null) {
      throw new NullPointerException("Titel mag niet null zijn");
    } 
    if(titel == "") {
      throw new IllegalArgumentException("Titel mag niet leeg zijn");
    } 
    if(id <= 0) {
      throw new IllegalArgumentException("ID moet een geheel getal groter dan 0 zijn maar was " + id);
    }

    if(!einddatumNogNietVerstreken(datum)) {
      throw new DatumVerledenException("Einddatum is reeds verstreken");
    } 
    
    this.datum = datum;
    this.id = id;
    this.titel = titel;
  }
  
  /**
   * String representatie van dit object
   * deze moet geherfineerd worden in de subclasse
   * @return de inhoud van dit object
   */
  public abstract String toString();
  
  /**
   * Maakt een shallow kloon van dit object
   * @throws CloneNotSupportedException 
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch(CloneNotSupportedException e) {
      System.out.println(e.getMessage()); 
      return null;
    }
    
  }
  
  /**
   * Gekozen einddatum is nog niet verstreken
   * @param  einddatum
   * @return true als de gekozen datum of vandaag of in de toekomst ligt
   */
  public Boolean einddatumNogNietVerstreken(LocalDate datum) {
    LocalDate nu = LocalDate.now();
    return datum.isAfter(nu) || datum.isEqual(nu);
  }
  
  /**
   * Geeft identifier
   * @return de ID
   */
  public int getId() {
    return id;
  }
  
  /**
   * Geeft titel
   * @return de titel
   */
  public String getTitel() {
    return titel;
  }
  
  
  /**
   * Geeft einddatum
   * @return LocalDate met de einddatum
   */
  public LocalDate getDatum() {
    return datum;
  }
  
  
  
  
  
  
  

}
