package agenda;

import java.time.LocalDate;
import java.time.LocalTime;

import agenda.exceptions.DatumVerledenException;

import java.time.LocalDateTime;

/**
 * Eenmalige afspraak item. Beheert datum en tijden van een afspraak
 * @author carlo
 *
 */
public class Afspraak extends AgendaItem implements Cloneable {
  
  private LocalDate datum;
  private LocalTime begintijd;
  private LocalTime eindtijd;

  /**
   * Maakt een afspraak item
   * Deze wordt gekenmerkt met een begin datum en tijd en een eind datum en tijd
   * @param id unieke identifier
   * @param titel de titel van de afspraak
   * @param datum de datum waarop de afspraak plaatsvindt
   * @param begintijd de tijd waarop de afspraak begint
   * @param eindtijd de tijd waarop de afspraak eindigt 
   * @throws NullPointerException       null waarde meegegeven
   * @throws IllegalArgumentException   Ongeldige waarde meegegeven
   * @throws IllegalStateException      Ongeldige state meegegeven
   * @throws DatumVerledenException     Gekozen datum ligt in het verleden
   */
   /*@
   @  @contract happy {
   @   @requires id > 0
   @   @requires titel is not null of lege string
   @   @requires een  begin datum / tijd combinatie die nu of in de toekomst ligt
   @   @requires een eind tijd die na de begin datum en tijd ligt
   @   @ensures alle attributen zijn set 
   @   @ensures eind datum en tijd ligt na het begin
   @   @assignable id, titel, datum, begintijd, eindtijd
   @   @signals IllegalArgumentException bij lege String titel
   @   @signals NullPointerException bij titel of een datum of tijden = null
   @   @signals IllegalStateException bij datum / tijd combinatie in het verleden
   @   @signals IllegalStateException bij eindtijd die voor het begin ligt
   @   @signals DatumVerledenException bij begintijd in het verleden
   @ }*/
  public Afspraak(int id, String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd) 
    throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException 
  {
    super(id,titel, datum); 

    Afspraak.checkGeldigeDateTime(datum,begintijd,eindtijd);    
    this.begintijd = begintijd;
    this.datum = datum;
    this.eindtijd = eindtijd;
  }
  
  /**
   * Controleert of er een ongeldige datum of tijd combinatie is gebruikt en gooit dan een exceptie op
   * @param begindatum                  de datum waarop de eerste afspraak plaatsvindt
   * @param eindDatum                   na deze datum mogen geen nieuwe afspraken gemaakt worden
   * @param begintijd                   de tijd waarop de afspraak begint
   * @param eindtijd                    de tijd waarop de afspraak eindigt
   * @throws IllegalStateException      Ongeldige begin en eindtijd
   * @throws DatumVerledenException     Datum is in het verledn
   */
  private static void checkGeldigeDateTime(LocalDate datum, LocalTime begintijd, LocalTime eindtijd) 
      throws DatumVerledenException, IllegalStateException, DatumVerledenException 
  {
    
    //Bugfix: Er leek een heel klein verschil (enkele nanoseconden) te zitten tussen begin en LocalDateTime.now() 
    //terwijl die in de testklasse ook door LocalDateTime.now() wordt gemaakt 
    //Dit rond de tijden af op hele minuten. Dat is goed voor deze
    LocalDateTime nu = maakAfgerondeDateTime(LocalDateTime.now());
    LocalDateTime begin = Afspraak.maakAfgerondeDateTime(datum, begintijd);
    LocalDateTime einde = Afspraak.maakAfgerondeDateTime(datum,eindtijd);
    
    //begin in het verleden
    if(begin.isBefore(nu)) {
      throw new DatumVerledenException("Je kunt geen afspraak in het verleden maken");
    }
    
    //einddatum < begindatum
    if(einde.isBefore(begin)) {
      throw new IllegalStateException("De einddatum mag niet voor de begindatum liggen");
    }
    
    //zelfde dag en zelfde tijd
    if(einde.isEqual(begin)) {
      throw new IllegalStateException("De begin en einddatum en tijd mogen niet op hetzelfde moment liggen");
    }
    
  }
  
  /**
   * Maakt LocalDateTime element met hele minuten ipv duizenste van een seconde
   * Rond de tijd af naar beneden op de hele minuut 
   * Dit lost een bug door verschillen in verwerkingstijd met LocalDateTime.now() op
   * @param date    datum object
   * @param time    tijd object
   * @return LocalDateTime object afgerond naar beneden met hele minuten
   */
  public static LocalDateTime maakAfgerondeDateTime(LocalDate date, LocalTime time) {
    int uren = time.getHour();
    int min = time.getMinute();
    LocalTime aangepastetijd = LocalTime.of(uren, min);
    return LocalDateTime.of(date, aangepastetijd); 
  }
  
  /**
   * Maakt LocalDateTime element met hele minuten ipv duizenste van een seconde 
   * Rond de tijd af naar beneden op de hele minuut 
   * Dit lost een bug door verschillen in verwerkingstijd met LocalDateTime.now() op
   * @param datetime    datetime object
   * @return LocalDateTime object afgerond naar beneden op hele minuten
   */
  public static LocalDateTime maakAfgerondeDateTime(LocalDateTime datetime) {
    LocalDate date = datetime.toLocalDate();
    int uren = datetime.toLocalTime().getHour();
    int min = datetime.toLocalTime().getMinute();
    LocalTime time = LocalTime.of(uren, min);
    return LocalDateTime.of(date, time); 
  }
  
  /**
   * Maakt een diepe kloon van dit object
   * Begin en eindtijden worden tot duizenste van een seconden nauwkeurig gekopieerds
   */
  @Override
  public Afspraak clone() {
    Afspraak item = (Afspraak) super.clone();
    
    //Diepe kloon datum en tijd 
    LocalDate begind = LocalDate.of(datum.getYear(), datum.getMonth(), datum.getDayOfMonth());
    LocalTime begint = LocalTime.of(begintijd.getHour(), begintijd.getMinute(), begintijd.getSecond(), begintijd.getNano());
    LocalTime eindt = LocalTime.of(eindtijd.getHour(), eindtijd.getMinute(), eindtijd.getSecond(), eindtijd.getNano());
   
    item.begintijd = begint;
    item.datum = begind;
    item.eindtijd = eindt;
    return item;
  }
  
  /**
   * Geeft string representie van begin en eind datum en tijd
   * @return de inhoud van dit object
   */
  @Override
  public String toString() {
    return "\n" + 
        "\n" + 
        "ID: " + getId() + "\n" +
        "Titel: " + getTitel() + "\n" +
        "Begindatum: " + datum.toString() + "\n" +
        "Begintijd: " + begintijd.toString() + "\n" +
        "Eindtijd: " + eindtijd.toString();
  }
  
  /**
   * Geeft begintijd
   * @return LocaTime begintijdstip
   */
  public LocalTime getBeginTijd() {
    return begintijd;
  }
  
  /**
   * Geeft eindtijd
   * @return LocaTime eindtijdstip
   */
  public LocalTime getEindtijd() {
    return eindtijd;
  }
  
  
  
  
  
  
  
  

}
