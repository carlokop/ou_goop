package agenda;

import java.time.LocalDate;
import java.time.LocalTime;

import agenda.exceptions.DatumVerledenException;

/**
 * Maakt een PeriodiekeAfspraak instantie
 * Deze klasse is een subklasse van de Afspraakklasse met als extra attribuut een periodiekItemId
 * @author carlo
 *
 */
public class PeriodiekeAfspraak extends Afspraak implements Cloneable {
  
  private int periodiekItemId;
  

  /**
   * Maakt een afspraak item
   * Deze wordt gekenmerkt met een begin datum en tijd en een eind datum en tijd
   * De periodiekeId verwijst naar meerdere periodieke items die bij elkaar horen
   * 
   * @param id                          unieke identifier
   * @param titel                       de titel van de afspraak
   * @param datum                       de datum waarop de afspraak plaatsvindt
   * @param begintijd                   de tijd waarop de afspraak begint
   * @param eindtijd                    de tijd waarop de afspraak eindigt
   * @param periodiekItemId             verschillende instanties voor dezelfde periodieke afspraak hebben dezelfde periodiekeId
   * @throws NullPointerException       null waarde meegegeven
   * @throws IllegalArgumentException   Ongeldige waarde meegegeven
   * @throws IllegalStateException      Ongeldige state meegegeven
   * @throws DatumVerledenException     Gekozen datum ligt in het verleden
   */
  /*
   @ @contract happy {
   @  @requires id > 0
   @  @requires periodiekItemId > 0
   @  @assignable periodiekItemId
   @  @signal IllegalArgumentException bij lege String titel
   @  @signal IllegalArgumentException bij id of periodiek id <= 0
   @  @signal NullPointerException bij titel of een datum of tijden = null
   @  @signal IllegalStateException bij datum / tijd combinatie in het verleden
   @  @signal IllegalStateException bij eindtijd die voor het begin ligt
   @  @signal DatumVerledenException bij begintijd in het verleden
   @  }
   @ */
  public PeriodiekeAfspraak(int id, String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd, int periodiekItemId) 
      throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException 
  {
     super(id, titel, datum, begintijd, eindtijd);
     
     if(periodiekItemId <= 0) {
       throw new IllegalArgumentException("Periodiek ID moet een geheel getal groter dan 0 zijn maar was " + id);
     }
     
     this.periodiekItemId = periodiekItemId;
  }

 /**
  * Geeft de identifier om alle periodieke items uit een serie te identificeren
  * @return het periodiekeItemId
  */
  public int getPeriodiekItemId() {
    return periodiekItemId;
  }
  
  /**
   * Geeft string representie van begin en eind datum en tijd en de periodiekeitemid
   * @return de inhoud van dit object
   */
  @Override
  public String toString() {
    return super.toString() + "\n" +
          "PeriodiekItemId : " + periodiekItemId;
  }
  
  /**
   * Maakt een diepe kloon van dit object
   * Begin en eindtijden worden tot duizenste van een seconden nauwkeurig gekopieerds
   */
  @Override
  public PeriodiekeAfspraak clone() {
    PeriodiekeAfspraak item = (PeriodiekeAfspraak) super.clone();
    return item;    
  }
  
  



} //class
