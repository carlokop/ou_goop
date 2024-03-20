package agenda;

import java.time.LocalDate;
import java.time.LocalTime;

import agenda.exceptions.AgendaException;
import agenda.exceptions.DatumVerledenException;
import agenda.exceptions.ReedsAfgevinktException;
import agenda.interfaces.ItemInterface;

/**
 * Generieke klasse Item
 * Beheert alleen het generieke type 
 * @author carlo
 *
 * @param <T>   Generiek type
 */
public class Item<T extends AgendaItem> implements ItemInterface, Cloneable {
  
  private T elm = null;

  public Item(T t) {
    this.elm = t;
  }
  
  /**
   * Maakt ToDo en en slaat deze op in dit element
   * @param id                          unieke identifier
   * @param titel                       de titel van de todo
   * @param datum                       de datum waarop de todo plaatsvindt
   * @throws IllegalArgumentException   Ongeldige waarde meegegeven
   * @throws NullPointerException       null waarde meegegeven
   * @throws DatumVerledenException     Gekozen datum ligt in het verleden
   */
  @SuppressWarnings("unchecked")
  public Item(int id, String titel, LocalDate datum) throws IllegalArgumentException, NullPointerException, DatumVerledenException {
    ToDo todo = new ToDo(id,titel,datum);
    this.elm = (T) todo;
  }
  
  /**
   * Maakt eenmalige afspraak en slaat deze op in dit element
   * @param id                          unieke identifier
   * @param titel                       de titel van de afspraak
   * @param datum                       de datum waarop de afspraak plaatsvindt
   * @param begintijd                   de tijd waarop de afspraak begint
   * @param eindtijd                    de tijd waarop de afspraak eindigt
   * @throws NullPointerException       null waarde meegegeven
   * @throws IllegalArgumentException   Ongeldige waarde meegegeven
   * @throws IllegalStateException      Ongeldige state meegegeven
   * @throws DatumVerledenException     Gekozen datum ligt in het verleden
   */
  @SuppressWarnings("unchecked")
  public Item(int id, String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd) throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException  {
    Afspraak afspraak = new Afspraak(id,titel,datum,begintijd,eindtijd);
    this.elm = (T) afspraak;
  }
  
  /**
   * Maakt een periodieke afspraak instantie en en slaat deze op in dit element
   * Periodieke afspraken in dezelfde serie moeten los van elkaar worden gemaakt
   * @param id                          unieke identifier
   * @param titel                       de titel van de todo
   * @param datum                       de datum waarop de todo plaatsvindt
   * @param begintijd                   de tijd waarop de afspraak begint
   * @param eindtijd                    de tijd waarop de afspraak eindigt
   * @param periodiekId                 identificeert alle bij elkaar horende periodieke afspraken
   * @throws NullPointerException       null waarde meegegeven
   * @throws IllegalArgumentException   ongeldige waarde meegegeven
   * @throws IllegalStateException      ongeldige state meegegeven
   * @throws DatumVerledenException     Gekozen datum ligt in het verleden
   */
  @SuppressWarnings("unchecked")
  public Item(int id, String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd, int periodiekId) throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException  {
    PeriodiekeAfspraak periodiekeafspraak = new PeriodiekeAfspraak(id,titel,datum,begintijd,eindtijd, periodiekId);
    this.elm = (T) periodiekeafspraak;
  }
  
  /**
   * Geeft het elment
   * @return het element
   */
  public T getElm() {
    return elm;
  }
  
  /**
   * Prints item inhoud
   * @return inhoud item
   */
  @Override
  public String toString() {
    return elm.toString();
  }

  /**
   * Geeft aan of de einddatum reeds in het verleden ligt
   * @return true is de einddatum reeds verstreken is
   */
  @Override
  public Boolean einddatumNogNietVerstreken(LocalDate datum) {
    return elm.einddatumNogNietVerstreken(datum);
  }

  /**
   * Geeft de id
   * @return de id
   */
  @Override
  public int getId() {
    return elm.getId();
  }

  /**
   * Geeft de titel
   * @return de titel
   */
  @Override
  public String getTitel() {
    return elm.getTitel();
  }

  /**
   * Geeft de datum
   * @return de datum
   */
  @Override
  public LocalDate getDatum() {
    return elm.getDatum();
  }
  
  /**
   * Maakt een klaan van het object
   * @return kopie van this
   */
  @SuppressWarnings("unchecked")
  @Override
  public Item<T> clone() {
      // Maak een nieuw object aan en kopieer de gegevens van het huidige object
    try {
      return (Item<T>) super.clone();
    } catch(CloneNotSupportedException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
 

  /**
   * Vinkt ToDo af als instance type todo is
   * @throws AgendaException                als een andere klasse dan Todo de methode aanroept
   * @throws ReedsAfgevinktException        als afgevinkt reeds true was
   */
  /*
   @ @Contract VinkToDoAf {
   @  @requires instantie van ToDo
   @  @requres afgevinkt = false
   @  @ensures afgevinkt = true
   @  @assignable afgervinkt
   @  @signals ReedsAfgevinktException      als afgevinkt reeds true was
   @  @signals AgendaException              als een andere klasse dan Todo de methode aanroept
   @  
   @} */
  public void vinkToDoAf() throws ReedsAfgevinktException, AgendaException {
    if(elm instanceof ToDo) {
      ((ToDo) elm).vinkToDoAf();
    } else {
      throw new AgendaException("VinkToDoAf kan alleen worden aangeroepen op een ToDo maar was " + elm.getClass().getName()); 
    } 
  }
  
  /**
   * Geeft afgevinkt als instantie type ToDo is
   * @return afgevinkt
   */
  public Boolean getAfgevinkt() {
    if(elm instanceof ToDo) {
      return ((ToDo) elm).getAfgevinkt();
    }
    return false;
  }
  

  

  
  
  
  
  
}//class
