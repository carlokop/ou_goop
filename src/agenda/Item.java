package agenda;

import java.time.DateTimeException;
import java.time.LocalDate;

import agenda.exceptions.DatumVerledenException;
import agenda.interfaces.ItemInterface;

/**
 * Abstracte klasse die informatie over agenda items bevat
 * @author carlo
 *
 */
public abstract class Item {
  
  private int id;
  private String titel;
  private LocalDate datum;

  /**
   * Inits class 
   * @param id                          unieke identifier
   * @param titel                       de titel van de afspraak
   * @param datum                       de datum waarop de afspraak plaatsvindt
   * @throws AgendaException            Er is een foutive invoerwaarde gegeven
   */ 
   /*@
   @ @Contract happy {
   @  @requires titel niet null
   @  @requires titel geen lege string
   @  @required id > 0
   @  @requires dat de datum bij creatie vandfaag of in de toekomst is
   @  @ensures dat id, titel en datum zijn ingesteld met niet lege of null waarden
   @ }
   @ @Contract titel null {
   @  @requires titel == null 
   @  @assignable niets
   @  @signals (AgendaException e) e.getMessage().equals("Titel mag niet null zijn");    
   @ }
   @ @Contract titel leeg {
   @  @requires titel == "" 
   @  @assignable niets
   @  @signals (AgendaException e) e.getMessage().equals("Titel mag niet leeg zijn");    
   @ } 
   @ @Contract ongeldige id {
   @  @requires id <= 0 
   @  @assignable niets
   @  @signals (AgendaException e) e.getMessage("ID moet een geheel getal groter dan 0 zijn maar was " + id);    
   @ } 
   @ @Contract datum verstreken {
   @  @requires datum in het verleden 
   @  @assignable niets
   @  @signals (DateTimeException e) e.getMessage().equals("Datum is reeds verstreken");    
   @ } 
   @ */
  public Item(int id, String titel, LocalDate datum) throws AgendaException, DateTimeException {
    if(titel == null) {
      throw new AgendaException("Titel mag niet null zijn");
    } 
    //titel is lege string of alleen spaties
    if(titel.trim().isEmpty()) {
      throw new AgendaException("Titel mag niet leeg zijn");
    } 
    if(id <= 0) {
      throw new AgendaException("ID moet een geheel getal groter dan 0 zijn maar was " + id);
    }

    if(!einddatumNogNietVerstreken(datum)) {
      throw new DateTimeException("Einddatum is reeds verstreken");
    } 
    
    this.datum = datum;
    this.id = id;
    this.titel = titel;
  }
  
  /**
   * Copy constructor maakt een kopie van het meegegeven object
   * @param item    object wat gekopieerd moet worden
   */
  public Item(Item item) {
    this.id = item.id;
    this.titel = item.titel;
    this.datum = item.datum;
  }
  
  /**
   * String representatie van dit object
   * deze moet geherfineerd worden in de subclasse
   * @return de inhoud van dit object
   */
  @Override
  public abstract String toString();
  
  /**
   * Maakt een shallow kloon van dit object
   * @return kopie van het object of null
   */
  //@Override
//  public Object clone() {
//    try {
//      return super.clone();
//    } catch(CloneNotSupportedException e) { 
//      return null;
//    }
//  }
  
  
  /**
   * Gekozen einddatum is nog niet verstreken
   * @param  datum      deze datum wordt getest of die nog niet is verstreken
   * @return true als de gekozen datum of vandaag of in de toekomst ligt
   */
  public boolean einddatumNogNietVerstreken(LocalDate datum) {
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
