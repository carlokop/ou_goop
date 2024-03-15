package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import exceptions.DatumVerledenException;
import exceptions.NietUniekeIdException;

public class PeriodiekItem extends Item {
  
  private int periodiekItemId;

  /**
   * Maakt een afspraak item
   * Deze wordt gekenmerkt met een begin datum en tijd en een eind datum en tijd
   * @param id
   * @param titel
   * @param eindDatum
   * @param begindatum
   * @param begintijd
   * @param eindtijd
   * @param periodiekItemId
   * @throws IllegalArgumentException 
   * @throws NullPointerException 
   * @throws DatumVerledenException 
   * @throws IllegalStateException
   * @throws NietUniekeIdException 
   * 
   * @contract happy
   *  @requires periodiekItemId != id
   *  @assignable periodiekItemId
   *  @signal IllegalArgumentException bij lege String titel
   *  @signal NullPointerException bij titel of een datum of tijden = null
   *  @signal IllegalStateException bij begindatum / tijd combinatie in het verleden
   *  @signal IllegalStateException bij einddatum / tijd die voor het begin ligt
   *  @signal DatumVerledenException bij begintijd in het verleden
   */
  public PeriodiekItem(int id, String titel, LocalDate begindatum, LocalDate eindDatum, LocalTime begintijd, LocalTime eindtijd, int periodiekItemId) 
      throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException, NietUniekeIdException 
  {
     super(id, titel, begindatum, eindDatum, begintijd, eindtijd);
     
     if(id == periodiekItemId) {
       throw new NietUniekeIdException("periodiekItemId moet anders zijn dan id");
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
   * @throws CloneNotSupportedException 
   */
  @Override
  public PeriodiekItem clone() throws CloneNotSupportedException {
    PeriodiekItem item = (PeriodiekItem) super.clone();
    
    item.periodiekItemId = periodiekItemId;
    return item;
  }



} //class
