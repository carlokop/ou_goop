package agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import agenda.exceptions.AgendaException;
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
    @SuppressWarnings("rawtypes")
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
     @*/
    @SuppressWarnings("finally")
    public int maakEenmaligeAfspraak(String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd)   {
      int id = -1;
      try {
        Item<Afspraak> item = new Item<Afspraak>(getNextId(), titel, datum, begintijd, eindtijd);
        items.add(item);
        id = item.getId();
      } catch(NullPointerException e) {
        System.out.println(e.getMessage());
      } catch(IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch(IllegalStateException e) {
        System.out.println(e.getMessage());
      } catch(DatumVerledenException e) {
        System.out.println(e.getMessage());
      } finally {
        return id;
      }
    }

    /**
     * Genereert afspraken tussen begin- en einddatum (inclusief) op
     * basis van frequentie en voegt deze oe aan de agenda.
     *
     * @param titel      de titel van de afspraak
     * @param begindatum de eerste datum waarop de afspraak plaatsvindt
     * @param einddatum  de laatste datum waarop de afspraak plaatsvindt
     * @param begintijd  de tijd waarop de afspraak begint
     * @param eindtijd   de tijd waarop de afspraak eindigt
     * @param frequentie de periode tussen twee afspraken (Zie enum
     *                   Frequentie)
     * @return een lijst met id's van de gegenereerde afspraken
     * @throws AgendaException
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
     @*/
    public List<Integer> maakPeriodiekeAfspraak(String titel,
                                                LocalDate begindatum, LocalDate einddatum,
                                                LocalTime begintijd, LocalTime eindtijd,
                                                Frequentie frequentie) throws AgendaException  {
      
      ArrayList<Integer> ids = new ArrayList<>();
      LocalDate datum = LocalDate.of(begindatum.getYear(), begindatum.getMonth(), begindatum.getDayOfMonth());
      
      int periodiekId = -1;
      while(!datum.isAfter(einddatum)) {
        
        if(!datum.isAfter(einddatum)) {
          
          if(periodiekId == -1) {
            //de eerste periodieke afspraak heeft een periodiekid == id
            //volgende periodieke afspraken hebben de periodiekeid die gelijk is aan id van de afspraak op de begindatum
            int id = getNextId();
            periodiekId = handleMaakPeriodiekeAfspraak(id, titel, datum, begintijd,  eindtijd, id);
            ids.add(periodiekId);
          } else {
            //volgende periodieke afspraken hebben periodiekid == de id van de 1e afspraak
            int id = handleMaakPeriodiekeAfspraak(getNextId(), titel, datum, begintijd,  eindtijd, periodiekId);
            if(id == -1) {
              //niet helemaal duidelijk waarom we in deze klasse fouten ofgooien
              //deze fout wordt niet afgehandeld in de main
              throw new AgendaException("Periodieke afspraak kon niet worden gemaakt");
            } else {
              ids.add(id);
            }
          }
          
        }
        datum = frequentie.volgendeDatum(datum);
      }

      return ids;
      
    }
    
    /**
     * Maakt een periodieke item en zorgt voor error handling en voegt die toe aan de lijst
     * Fouten worden afgehandeld door ze te printen naar het console
     * 
     * @param id
     * @param titel
     * @param datum
     * @param begintijd
     * @param eindtijd
     * @param periodiekId
     * @return de item id deze is -1 als er fouten waren
     * 
     * @Contract nieuw Periodiekitem happy path
     *  @requires string != null of gen lege string
     *  @requires id en periodiekId > 0
     *  @requires datum en tijden not null
     *  @requires dat allen gegeven datum en tijden in de toekomst liggen 
     *  @requires dat de eindtijd na de begintijd ligt
     *  @ensures dat een periodiek item instantie is creert en toegevoegd aan de items
     *  @assignable items
     */
    @SuppressWarnings("finally")
    private int handleMaakPeriodiekeAfspraak(int id, String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd, int periodiekId) 
    {
        int pid = -1;
        try {
          Item<PeriodiekeAfspraak> item = new Item<PeriodiekeAfspraak>(id, titel, datum, begintijd, eindtijd, periodiekId);
          pid = item.getId();
          items.add(item);
        } catch(NullPointerException e) {
          System.out.println(e.getMessage());
        } catch(IllegalArgumentException e) {
          System.out.println(e.getMessage());
        } catch(IllegalStateException e) {
          System.out.println(e.getMessage());
        } catch(DatumVerledenException e) {
          System.out.println(e.getMessage());
        } finally {
          return pid;
        }
    } 


    /**
     * Maakt een todo en voegt deze toe aan de agenda.
     *
     * @param titel de titel van de todo
     * @param datum de datum waarop de todo moet worden uitgevoerd
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
     @*/
    /**
     * Docent:
     * Ik heb hier staan @ensures afgevinkt is false
     * Dit wordt eigenlijk al in de todo klasse gedaan net als de meeste andere logica
     * Had dat hier gemoeten? Dit lijkt onlogisch
     */
    public int maakToDo(String titel, LocalDate datum) {
      int id = -1;
      try {
        Item<ToDo> item = new Item<ToDo>(getNextId(), titel, datum);
        id = item.getId();
        items.add(item);
      } 
      //er is geen GUI of iets waar we iets met fouten doen dus ik print ze maar naar het console
      catch(IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } 
      catch(NullPointerException e) {
        System.out.println(e.getMessage());
      }
      catch(DatumVerledenException e) {
        System.out.println(e.getMessage());
      }
      return id;
    }


    /**
     * Vinkt een todo af.
     *
     * @param id de id van de todo
     * @return true als status todo is gewijzigd van false naar true anders false
     * @throws AgendaException 
     */
     /*@ @contract happy path {
     @     @requires lijst met agenda-items bevat een todo met de gezochte id;
     @     @ensures de todo met deze id is afgevinkt
     @     @ensures \result = true als de oude waarde van afgevinkt ongelijk is
     @                        aan de nieuwe waarde van afgevinkt, anders false
     @     @assignable items
     @     @assignable item met deze id
     @ }
     @*/
    public boolean vinkToDoAf(int id) throws AgendaException  {
      try {
        @SuppressWarnings("unchecked")
        Item<ToDo> todo = (Item<ToDo>) getItem(id);    
        todo.vinkToDoAf();
        return true;
      }  catch(ReedsAfgevinktException e) {
        System.out.println(e.getMessage());
      } catch(NullPointerException e) {
        System.out.println("ToDo met id: " + id + " niet gevonden");
      }
      return false;
    }
    

    /**
     * Geeft een kopie van alle afspraken of todo's in een bepaalde periode (begin- en einddatum)
     * @param <T>
     *
     * @param begindatum de eerste datum van de periode
     * @param einddatum  de laatste datum van de periode (inclusief)
     * @return lijst met een kopie van alle items(afspraken of todo's) die vallen in de periode
     * @throws AgendaException
     * van begindatum tot en met einddatum
     */
     /*@ @contract happy path {
     @     @requires begindatum ligt voor of op einddatum;
     @     @ensures \result = lijst met een kopie van alle items(afspraken of todo's) die vallen in de periode
     @                        van begindatum tot en met einddatum
     @     @signal AgendaException als einddatum < begindatum
     @   }
     @*/
    @SuppressWarnings({
        "rawtypes"
    })
    public List<Item> /*@ pure */ getItems(LocalDate begindatum, LocalDate einddatum) throws AgendaException {
      
      if(einddatum.isBefore(begindatum)) {
        throw new AgendaException("Einddatum mag niet voor de begindatum liggen");
      }
      
      List<Item> gefilterdeitems = new ArrayList<>();
      for (Item item : items) {
          
          if((!item.getDatum().isBefore(begindatum)) && (!item.getDatum().isAfter(einddatum))) {
            Item kopie = item.clone(); 
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
      
      for(@SuppressWarnings("rawtypes") Item item: items) {
        if (item.getElm() instanceof ToDo) {
          ToDo kopie = (ToDo) item.getElm().clone();
          
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
     /*@ @contract happy path
     @     @requires true
     @     @ensures \result is een kopie van item met item.id == id of null als id niet voorkomt in de id's van items
     @*/
    public Item<?> /*@ pure */ getItem(int id)  {
      for(@SuppressWarnings("rawtypes") Item item: items) {
        if(id == item.getId()) {
          return (Item<?> )item.clone(); 
        }
      }
      return null;
    }

}
