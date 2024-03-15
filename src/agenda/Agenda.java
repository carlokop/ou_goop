package agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import exceptions.AgendaException;

/**
 * Klasse die een agenda representeert. Een agenda beheert verschillenden
 * agenda-items (zoals todo's en afspraken).
 *
 * @author OUNL
 */

public class Agenda {
	
    private int nextId = 0;

	

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
     * Maakt een nieuwe afspraak en voegt deze toe aan de agenda.
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
    public int maakEenmaligeAfspraak(String titel, LocalDate datum, LocalTime begintijd, LocalTime eindtijd)  {
 
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
                                                Frequentie frequentie)  {
    }


    /**
     * Maakt een todo en voegt deze toe aan de agenda.
     *
     * @param titel de titel van de todo
     * @param datum de datum waarop de todo moet worden uitgevoerd
     * @return de id van de todo
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
    public int maakToDo(String titel, LocalDate datum) {
    }


    /**
     * Vinkt een todo af.
     *
     * @param id de id van de todo
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
     @*/
    public boolean vinkToDoAf(int id) {
    }

    /**
     * Geeft een kopie van alle afspraken of todo's in een bepaalde periode (begin- en einddatum)
     *
     * @param begindatum de eerste datum van de periode
     * @param einddatum  de laatste datum van de periode (inclusief)
     * @return lijst met een kopie van alle items(afspraken of todo's) die vallen in de periode
     * van begindatum tot en met einddatum
     */
     /*@ @contract happy path {
     @     @requires begindatum ligt voor of op einddatum;
     @     @ensures \result = lijst met een kopie van alle items(afspraken of todo's) die vallen in de periode
     @                        van begindatum tot en met einddatum
     @   }
     @*/
    public List<Item> /*@ pure */ getItems(LocalDate begindatum, LocalDate einddatum) {
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
    public List<ToDo> /*@ pure */ getToDos(LocalDate datum, boolean afgevinkt) {
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
    public Item /*@ pure */ getItem(int id) {
    }

}
