package agenda;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import agenda.exceptions.DatumVerledenException;
import agenda.exceptions.ReedsAfgevinktException;

/**
 * Klasse die een agenda representeert. Een agenda beheert verschillenden
 * agenda-items (zoals todo's en afspraken).
 *
 * @author OUNL
 */

public class Agenda {
	
    private int nextId = 0;
    private ArrayList<Item> items;
    
    /**
     * Maakt nieuwe instantie en initisliseerd een lege lijst met agende items
     */
    public Agenda() {
      items = new ArrayList<>();
    }

    /**
     * De waarde van nextID is met 1 opgehoogd
     *
     * @return de volgende id
     */
    /*@ @contract happy path{
      @   @requires true
      @   @ensures nextID is met 1 opgehoogd
      @   @ensures \result = nextId
      @   @assignable nextId
     @*/
    private int getNextId() {
        return ++nextId;
    }


    /**
     * Maakt een nieuwe eenmalige afspraak en voegt deze toe aan de lijst met items.
     *
     * @param titel     de titel van de afspraak
     * @param datum     de datum waarop de afspraak plaatsvindt
     * @param begintijd de tijd waarop de afspraak begint
     * @param eindtijd  de tijd waarop de afspraak eindigt
     * @return de id van de afspraak
     */

    /*@ @contract happy path {
     @   @requires Titel mag geen lege string zijn, begintijd is voor eindtijd,
     @     datum is vandaag of in de toekomst;
     @   @ensures De waarde van nextID is met 1 opgehoogd
     @   @ensures Eenmalige afspraak is gemaakt met gegeven attribuutwaarden en nextId en toegevoegd aan de agenda.
     @   @ensures \result = nextId
     @   @assignable nextId
     @   @assignable items
     @ }
     @ @Contract titel leeg {
     @  @requires titel == "" 
     @  @ensures \result = -1
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId
     @ } 
     @ @Contract datum verstreken {
     @  @requires datum in het verleden 
     @  @ensures \result = -1
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId  
     @ }
     @ @Contract datum of tijd verstreken {
     @  @requires datum == vandaag
     @  @requires begintijd < huidige tijd in hele minuten is reeds verstreken
     @  @ensures \result = -1 
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId
     @ } 
     @ @Contract einde vaar begin {
     @  @requires begintijd > eindtijd
     @  @ensures \result = -1
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId 
     @ } 
     @ @Contract begin en eindtijd gelijk {
     @  @requires begintijd == eindtijd
     @  @ensures \result = -1
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId    
     @ } 
     @*/
    public int maakEenmaligeAfspraak(String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd)   {
      int id = -1;
      try {
        Afspraak afspraak = new Afspraak(getNextId(), titel, datum, begintijd, eindtijd);
        id = afspraak.getId();
        if(id > 0) {
          items.add(afspraak);
        }
      } catch(AgendaException e) {
        nextId--;
        //doen hier niets?
      } catch(DateTimeException e) {
        nextId--;
        //doen hier niets?
      }
      return id;
    }

    /**
     * Genereert afspraken tussen begin- en einddatum (inclusief) op
     * basis van frequentie en voegt deze oe aan de agenda.
     *
     * @param titel             de titel van de afspraak
     * @param begindatum        de eerste datum waarop de afspraak plaatsvindt
     * @param einddatum         de laatste datum waarop de afspraak plaatsvindt
     * @param begintijd         de tijd waarop de afspraak begint
     * @param eindtijd          de tijd waarop de afspraak eindigt
     * @param frequentie        de periode tussen twee afspraken (Zie enum Frequentie)
     * @return een lijst met id's van de gegenereerde afspraken
     */
     /*@ @contract happy path {
     @     @requires titel mag geen lege string zijn
     @     @requires begindatum is voor einddatum,
     @     @requires begintijd is voor eindtijd
     @     @requires begindatum is vandaag of in de toekomst
     @     @requires einddatum is in de toekomst;
     @     @ensures afspraken zijn gemaakt en toegevoegd aan de agenda,
     @     @ensures de waarde van nextID is met 1 opgehoogd voor iedere gegenereerde afspraak
     @     @ensures \result = lijst van id’s die  bij gegenereerde afspraken horen
     @     @assignable items
     @ }
     @ @Contract titel leeg {
     @  @requires titel == "" 
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId
     @ } 
     @ @Contract begindatum verstreken {
     @  @requires begindatum in het verleden 
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId  
     @ }
     @ @Contract begindatum is vandaag maar begintijd verstreken {
     @  @requires datum == vandaag
     @  @requires begintijd < huidige tijd in hele minuten is reeds verstreken
     @  @ensures \result = lege lijst 
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId
     @ } 
     @ @Contract einddatum voor begindatum {
     @  @requires einddatum < begindatum
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId 
     @ } 
     @ @Contract begin en eindtijd gelijk {
     @  @requires begintijd == eindtijd
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId    
     @ } 
     @ @Contract eindtijd ligt voor de begintijd {
     @  @requires eindtijd < begintijd
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId    
     @ } 
    @*/
    public List<Integer> maakPeriodiekeAfspraak(String titel,
                                                LocalDate begindatum, LocalDate einddatum,
                                                LocalTime begintijd, LocalTime eindtijd,
                                                Frequentie frequentie)   {

      ArrayList<Integer> ids = new ArrayList<>();
      LocalDate datum = LocalDate.of(begindatum.getYear(), begindatum.getMonth(), begindatum.getDayOfMonth());
      
      while(!datum.isAfter(einddatum)) {
        
        //maakEenmaligeAfspraak voor deze datum
        //voeg id toe aan lijst
        int id = maakEenmaligeAfspraak(titel, datum, begintijd, eindtijd);
        if(id > 0) {
          ids.add(id);
        } else {
          break;
        }
        datum = frequentie.volgendeDatum(datum);
        
      }

      return ids;
      
    }
       

    /**
     * Maakt een todo en voegt deze toe aan de agenda.
     *
     * @param titel     de titel van de todo
     * @param datum     de datum waarop de todo moet worden uitgevoerd
     * @return de id van de todo of -1 als er een fout is
     */
     /*@ @contract happy path {
     @     @requires titel mag geen lege string zijn
     @     @requires datum is vandaag of in de toekomst
     @     @ensures de waarde van nextID is met 1 opgehoogd 
     @     @ensures todo is gemaakt met deze nextId en afgevinkt is false
     @     @ensures todo is toegevoegd aan de agenda
     @     @ensures \result = nextId
     @     @assignable nextId
     @     @assignable items
     @ }
     @ @Contract titel leeg {
     @  @requires titel == "" 
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId
     @ } 
     @ @Contract datum verstreken {
     @  @requires datum in het verleden 
     @  @ensures \result = lege lijst
     @  @ensures nextId blijft onveranderd
     @  @assignable nextId  
     @ }

     @*/
    public int maakToDo(String titel, LocalDate datum) {
      int id = -1;
      try {
        ToDo item = new ToDo(getNextId(), titel, datum);
        id = item.getId();
        if(id > 0) {
          items.add(item);
        }
      } 
      catch(DateTimeException e) {
        nextId--;
        //doen hier niets?
      } 
      catch(AgendaException e) {
        nextId--;
        //doen hier niets?
      }
      return id;
    }


    /**
     * Vinkt een todo af.
     *
     * @param id                de id van de todo
     * @return true als status todo is gewijzigd van false naar true anders false
     */
     /*@ @contract happy path {
     @     @requires lijst met agenda-items bevat een todo met de gezochte id;
     @     @ensures de todo met deze id is afgevinkt
     @     @ensures \result = true als de oude waarde van afgevinkt ongelijk is
     @                        aan de nieuwe waarde van afgevinkt, anders false
     @     @assignable items
     @     @assignable item met deze id
     @ }
     @ @contract id onjuist {
     @     @requires er is geen todo met gegeven id
     @     @ensures \result = false
     @     @assignable geen
     @ }
     @ @contract toDo reeds afgevinkt {
     @     @requires afgevinkt == true
     @     @ensures \result = false
     @ }
     @*/
    public boolean vinkToDoAf(int id)   {
      boolean status = false;
      try {
        
        for(Item item: items) {
          if(item instanceof ToDo && item.getId() == id) {
            ((ToDo) item).vinkToDoAf();
            status = true;
          }
        }
      }   catch (AgendaException e) {
        //doen hier niets?
      }
      return status;
    }
    

    /**
     * Geeft een kopie van alle afspraken of todo's in een bepaalde periode (begin- en einddatum)
     * @param begindatum        De eerste datum van de periode
     * @param einddatum         De laatste datum van de periode (inclusief)
     * @return lijst met een kopie van alle items(afspraken of todo's) die vallen in de periode
     */
     /*@ @contract happy path {
     @     @requires begindatum ligt voor of op einddatum;
     @     @ensures \result = lijst met een kopie van alle items(afspraken of todo's) die vallen in de periode
     @                        van begindatum tot en met einddatum
     @   }
     @ @contract happy path {
     @     @requires einddatum ligt na de begindatum;
     @     @ensures \result = lege lijst
     @   }
     @*/
    public List<Item> /*@ pure */ getItems(LocalDate begindatum, LocalDate einddatum)  {
      
      
      List<Item> gefilterdeitems = new ArrayList<>();
      for (Item item : items) {
          
          if((!item.getDatum().isBefore(begindatum)) && (!item.getDatum().isAfter(einddatum))) {
            Item kopie = (Item) item.clone(); 
            gefilterdeitems.add(kopie);
          }
      }
      return gefilterdeitems;
    }
      

    /**
     * Geeft een kopie van alle todo's op een gegeven datum, wel/niet afgevinkt.
     *
     * @param datum     de datum van de dag
     * @param afgevinkt als true, dan selecteer afgevinkte todos, anders de
     *                  niet-afgevinkte
     * @return een lijst met kopieën van alle todo's op gegeven datum wel/niet afgevinkt
     */
    /*@ @contract happy path {
     @    @requires true;
     @    @ensures \result = een lijst met kopieen van alle todo's op bepaalde datum al dan niet  afgevinkt
     @ }
     @*/
    public List<ToDo> /*@ pure */ getToDos(LocalDate datum, boolean afgevinkt)  {
      
      List<ToDo> todos = new ArrayList<>();
      
      for( Item item: items) {
        if (item instanceof ToDo) {
          ToDo kopie = (ToDo) item.clone();
          if(datum.isEqual( kopie.getDatum()) && kopie.getAfgevinkt() == afgevinkt) {
            todos.add(kopie);
          }
        }
      }
    
      return todos;
    }
  

    /**
     * Levert een kopie van een item met item.id == id of null als id niet voorkomt in de id's van items
     *
     * @param id de id van het item
     * @return een kopie van het gevonden item of null
     */
     /*
     @  @ @contract happy path {
     @     @requires true
     @     @ensures \result is een kopie van item met item.id == id of null als id niet voorkomt in de id's van items
     @ }
     @  @ @contract id onbekend {
     @     @requires er is geen item met gegeven id in de lijst
     @     @ensures \result null
     @ }
     */
    public Item /*@ pure */ getItem(int id)  {
      for(Item item: items) {
        if(id == item.getId()) {

          return (Item) item.clone();
          
        }
      }
      return null;
    }

}
