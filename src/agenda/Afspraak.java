package agenda;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

import agenda.exceptions.DatumVerledenException;

import java.time.LocalDateTime;

/**
 * Eenmalige afspraak item. Beheert datum en tijden van een afspraak
 * @author carlo
 *
 */
public class Afspraak extends Item {
  
  private LocalDate datum;
  private LocalTime begintijd;
  private LocalTime eindtijd;

  /**
   * Maakt een afspraak 
   * Deze wordt gekenmerkt met een begin datum en tijd en een eind datum en tijd
   * @param id unieke identifier
   * @param titel de titel van de afspraak
   * @param datum de datum waarop de afspraak plaatsvindt
   * @param begintijd de tijd waarop de afspraak begint
   * @param eindtijd de tijd waarop de afspraak eindigt 
   * @throws AgendaException            Er is een foutive invoerwaarde gegeven
   * @throws DateTimeException          Er is een ongeldige datum of tijd ingevoerd
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
   @ }
   @ @Contract datum of tijd verstreken {
   @  @requires datum == vandaag & begintijd < huidige tijd in hele minuten is reeds verstreken 
   @  @assignable niets
   @  @signals (DateTimeException e) e.getMessage().equals("Datum en of tijd is reeds verstreken");    
   @ } 
   @ @Contract einde voor begin {
   @  @requires begintijd > eindtijd
   @  @assignable niets
   @  @signals (DateTimeException e) e.getMessage().equals("De eindtijd mag niet voor het begin liggen");    
   @ } 
   @ @Contract begin en eindtijd gelijk {
   @  @requires begintijd == eindtijd
   @  @assignable niets
   @  @signals (DateTimeException e) e.getMessage().equals("De begin en einddatum en tijd mogen niet op hetzelfde moment liggen");    
   @ } 
   */
  public Afspraak(int id, String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd) throws AgendaException, DateTimeException {
    super(id,titel, datum); 

    Afspraak.checkGeldigeDateTime(datum,begintijd,eindtijd);    
    this.begintijd = begintijd;
    this.datum = datum;
    this.eindtijd = eindtijd;
  }
  
  /**
   * Copy constructor maakt een kopie van het meegegeven object
   * @param item    object wat gekopieerd moet worden
   */
  public Afspraak(Afspraak object) {
    super(object);
    this.datum = object.datum;
    this.begintijd = object.begintijd;
    this.eindtijd = object.eindtijd;
  }
  
  /**
   * Controleert of er een ongeldige datum of tijd combinatie is gebruikt en gooit dan een exceptie op
   * @param begindatum                  de datum waarop de eerste afspraak plaatsvindt
   * @param eindDatum                   na deze datum mogen geen nieuwe afspraken gemaakt worden
   * @param begintijd                   de tijd waarop de afspraak begint
   * @param eindtijd                    de tijd waarop de afspraak eindigt
   * @throws DateTimeException          Ongeldige datum en of tijden ingevoerd
   */
  private static void checkGeldigeDateTime(LocalDate datum, LocalTime begintijd, LocalTime eindtijd) 
      throws DateTimeException 
  {
    
    //Bugfix: Er leek een heel klein verschil (enkele nanoseconden) te zitten tussen begin en LocalDateTime.now() 
    //terwijl die in de testklasse ook door LocalDateTime.now() wordt gemaakt 
    //Dit rond de tijden af op hele minuten. 
    LocalDateTime nu;
    LocalDateTime begin = Afspraak.maakAfgerondeDateTime(datum,begintijd);
    LocalDateTime einde = Afspraak.maakAfgerondeDateTime(datum,eindtijd);
    
    nu = maakAfgerondeDateTime(LocalDateTime.now());
    if(begin.isBefore(nu)) {
      throw new DateTimeException("Datum en of tijd is reeds verstreken");
    }
    
    //einddatum < begindatum
    if(einde.isBefore(begin)) {
      throw new DateTimeException("De eindtijd mag niet voor het begin liggen");
    }
    
    //zelfde dag en zelfde tijd
    if(einde.isEqual(begin)) {
      throw new DateTimeException("De begin en einddatum en tijd mogen niet op hetzelfde moment liggen");
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
  private static LocalDateTime maakAfgerondeDateTime(LocalDate date, LocalTime time) {
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
  private static LocalDateTime maakAfgerondeDateTime(LocalDateTime datetime) {
    LocalDate date = datetime.toLocalDate();
    int uren = datetime.toLocalTime().getHour();
    int min = datetime.toLocalTime().getMinute();
    LocalTime time = LocalTime.of(uren, min);
    return LocalDateTime.of(date, time); 
  }
  
  /**
   * Maakt een diepe kloon van dit object
   * Begin en eindtijden worden tot duizenste van een seconden nauwkeurig gekopieerds
   * @return een diepe kloon van dit object
   * /*@ @contract happy path {
     @     @requires true;
     @     @ensures \result = een kopie van het object zonder referentie
     @ }
   */
//  @Override
//  public Afspraak clone() {
//    Afspraak item = (Afspraak) super.clone();
//    
//    if(item != null) {
//      //Diepe kloon datum en tijd 
//      LocalDate begind = LocalDate.of(datum.getYear(), datum.getMonth(), datum.getDayOfMonth());
//      LocalTime begint = LocalTime.of(begintijd.getHour(), begintijd.getMinute(), begintijd.getSecond(), begintijd.getNano());
//      LocalTime eindt = LocalTime.of(eindtijd.getHour(), eindtijd.getMinute(), eindtijd.getSecond(), eindtijd.getNano());
//     
//      item.begintijd = begint;
//      item.datum = begind;
//      item.eindtijd = eindt;
//    }
//    
//    return item;
//  }
  
  
  /**
   * Geeft string representie van begin en eind datum en tijd
   * @return de inhoud van dit object
   */
  @Override
  public String toString() {
    return "" + getId() + ", " + datum.toString() + ", " + getTitel() + ", van " + begintijd.toString() + " " + eindtijd.toString();
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
