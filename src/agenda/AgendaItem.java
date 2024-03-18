package agenda;

import java.time.LocalDate;

import exceptions.DatumVerledenException;

public abstract class AgendaItem implements Cloneable {
  
  private int id;
  private String titel;
  private LocalDate einddatum;

  /**
   * Inits class 
   * @param id
   * @param titel
   * @param eindDatum 
   * @throws NullPointerException
   * @throws IllegalArgumentException
   * @throws DatumVerledenException 
   */
  public AgendaItem(int id, String titel, LocalDate einddatum) 
      throws NullPointerException, IllegalArgumentException, DatumVerledenException 
  {
    if(titel == null) {
      throw new NullPointerException("ToDo titel mag niet null zijn");
    } 
    if(titel == "") {
      throw new IllegalArgumentException("ToDo titel mag niet leeg zijn");
    } 
    if(id < 0) {
      throw new IllegalArgumentException("ID moet een geheel getal groter dan of gelijk aan 0 zijn maar was " + id);
    }

    if(!einddatumNogNietVerstreken(einddatum)) {
      throw new DatumVerledenException("Einddatum is reeds verstreken");
    } 
    
    this.einddatum = einddatum;
    this.id = id;
    this.titel = titel;
  }
  
  /**
   * String representatie van dit object
   * @return de inhoud van dit object
   */
  public abstract String toString();
  
  /**
   * Maakt een shallow kloon van dit object
   * @throws CloneNotSupportedException 
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  
  /**
   * Gekozen einddatum is nog niet verstreken
   * @param  einddatum
   * @return true als de gekozen datum of vandaag of in de toekomst ligt
   */
  public Boolean einddatumNogNietVerstreken(LocalDate einddatum) {
    LocalDate nu = LocalDate.now();
    return einddatum.isAfter(nu) || einddatum.isEqual(nu);
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
  public LocalDate getEindDatum() {
    return einddatum;
  }
  
  
  
  
  
  
  

}
