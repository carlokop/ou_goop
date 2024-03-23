package test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import agenda.Agenda;
import agenda.Item;
import agenda.Frequentie;
import agenda.Afspraak;
import agenda.ToDo;

/**
 * Tests de Agende klasse
 * @author carlo
 *
 */
public class TestAgenda {
  
  private Agenda agenda;
  private LocalDate d1;
  private LocalDate d2;
  private LocalDate d3;
  private LocalTime t1;
  private LocalTime t2;

  
  @Before
  public void setUp() {
    agenda = new Agenda();
    d1 = LocalDate.of(2025, Month.JANUARY, 1);
    d2 = LocalDate.of(2025, Month.JANUARY, 2);
    d3 = LocalDate.of(2023, Month.JANUARY, 2);
    t1 = LocalTime.of(6, 0);
    t2 = LocalTime.of(7, 0);
  }
  
  @Test
  public void getItemsTest() {
    
    //happy test 
    
    //items met id 1 t/5 worden aan lijst toegevoegd
    //voor datum 1 jan 2025
    for(int i=0; i<5; i++) {
     agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    } 
    List<Item> items = agenda.getItems(d1, d2);
    assertEquals(5, items.size());
    
    //en voeg nog 2 toe van 2 jan 2025
    agenda.maakEenmaligeAfspraak("Titel",d2,t1,t2);
    agenda.maakEenmaligeAfspraak("Titel",d2,t1,t2);
    
    items = agenda.getItems(d1, d2);
    assertEquals(7, items.size());
    
    //test ids in items
    assertEquals(1, items.get(0).getId());
    assertEquals(2, items.get(1).getId());
    assertEquals(3, items.get(2).getId());
    assertEquals(4, items.get(3).getId());
    assertEquals(5, items.get(4).getId());
    assertEquals(6, items.get(5).getId());
    assertEquals(7, items.get(6).getId());
    
    //alleen de items van 1 jan 2025
    items = agenda.getItems(d1, d1);
    assertEquals(5, items.size());
    
    //alleen de items van 2 jan 2025
    items = agenda.getItems(d2, d2);
    assertEquals(2, items.size());
    
    //Zouden niet meer dan 2 items mogen zijn
    assertThrows(IndexOutOfBoundsException.class, () -> { agenda.getItems(d2, d2).get(2); });
    
    //test ids in items
    assertEquals(6, items.get(0).getId());
    assertEquals(7, items.get(1).getId());
    
    //test er mag geen referentie zijn door de clone
    Item item1 = items.get(0);
    Item item2 = agenda.getItem(6);
    assertEquals(item1.getId(), item2.getId()); //zou zelfde item moeten zijn
    assertNotEquals(item1, item2); //niet zelfde referentie
    
    //datum waar geen items van zijn
    LocalDate outrangedate = LocalDate.of(2026, Month.JANUARY, 1);
    items = agenda.getItems(outrangedate, outrangedate);
    assertEquals(0, items.size());
     
    //test einddatum komt na de begindatum moet resulteren in lege list
    items = agenda.getItems(d2, d1);
    assertEquals(0, items.size());

    
  }
  
  
  @Test 
  public void maakEenmaligeAfspraakTest() {
    //happy
    int id = agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    assertEquals(1,id);
    id = agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    assertEquals(2,id);
    
    //bevat attributen
    Afspraak afspraak = (Afspraak) agenda.getItem(1);
    assertEquals("Titel",afspraak.getTitel());
    assertEquals(1,afspraak.getId());
    assertEquals("2025-01-01",afspraak.getDatum().toString());
    //ook de attributen die niet in de abstracte klasse staan
    assertEquals("06:00",afspraak.getBeginTijd().toString());
    assertEquals("07:00",afspraak.getEindtijd().toString());
    
    //maak kloon en test attributen
    Afspraak kloon = afspraak.clone();
    assertEquals("Titel",kloon.getTitel());
    assertEquals(1,kloon.getId());
    assertEquals("2025-01-01",kloon.getDatum().toString());
    //ook de attributen die niet in de abstracte klasse staan
    assertEquals("06:00",kloon.getBeginTijd().toString());
    assertEquals("07:00",kloon.getEindtijd().toString());
    
    //toegevoegd aan de lijst
    List<Item> items = agenda.getItems(d1, d1);
    assertEquals(1, items.get(0).getId());
    assertEquals(2, items.get(1).getId());
    assertEquals(2, items.size());
    
    //out range elements 
    assertThrows(IndexOutOfBoundsException.class, () -> { agenda.getItems(d1, d1).get(-1); });
    assertThrows(IndexOutOfBoundsException.class, () -> { agenda.getItems(d1, d1).get(2); });
    
    /*
     * Fout condities
     * Deze moeten id = -1 opleveren en geen excepties
     * Item mogen niet aan lijst worden toegevoegd
     */
    //Titel is null
    id = agenda.maakEenmaligeAfspraak(null,d1,t1,t2);
    assertEquals(-1,id);
    
    //Titel is lege string
    id = agenda.maakEenmaligeAfspraak("",d1,t1,t2);
    assertEquals(-1,id);
    
    //datum in het verleden en begintijd < eindtijd
    id = agenda.maakEenmaligeAfspraak("Titel",d3,t1,t2);
    assertEquals(-1,id);
    
    //datum in de toekomst begintijd == eindtijd
    id = agenda.maakEenmaligeAfspraak("Titel",d1,t1,t1);
    assertEquals(-1,id);
    
    //datum in de toekomst eindtijd < begintijd
    id = agenda.maakEenmaligeAfspraak("Titel",d1,t2,t1);
    assertEquals(-1,id);
    
    //Geen van bovenstaande foutcondities resulteert in een item die wordt toegevoegd aan de lijst
    //dus we zouden dezelfde items in de lijst moeten hebben asl hiervoor
    items = agenda.getItems(d1, d1);
    assertEquals(1, items.get(0).getId());
    assertEquals(2, items.get(1).getId());
    assertEquals(2, items.size());
    
    /*
     * Er zijn een aantal foutcondities gedaan
     * Agenda.nextid zou niet opgehoogd mogen zijn
     * Bij de eerst volgende goede afspraak moeten we de volgende id hebben
     */
    id = agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    assertEquals(3,id);
    
  }
  
  @Test
  public void getItemTest() {
    //happy path
    
    //maak items id 1 t/m 5
    for(int i=0; i<5; i++) {
     agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    }    
    
    //item in range bevat alle data
    Afspraak afspraak = (Afspraak) agenda.getItem(1);
    assertEquals("Titel",afspraak.getTitel());
    assertEquals(1,afspraak.getId());
    assertEquals("2025-01-01",afspraak.getDatum().toString());
    assertEquals("06:00",afspraak.getBeginTijd().toString());
    assertEquals("07:00",afspraak.getEindtijd().toString());
    
    //attributen in range bevatten data
    afspraak = (Afspraak) agenda.getItem(1);
    assertNotNull(afspraak);
    afspraak = (Afspraak) agenda.getItem(3);
    assertNotNull(afspraak);
    afspraak = (Afspraak) agenda.getItem(5);
    assertNotNull(afspraak);
    
    //afspraak buiten range zouden null moeten zijn
    afspraak = (Afspraak) agenda.getItem(0);
    assertNull(afspraak);
    afspraak = (Afspraak) agenda.getItem(-1);
    assertNull(afspraak);
    afspraak = (Afspraak) agenda.getItem(6);
    assertNull(afspraak);
  }
  
  /**
   * Test alleen happy path maakPeriodiekeAfspraa()
   */
  @Test
  public void maakPeriodiekeAfspraakHappyTest() {
    //happy
    //maak een periodiek item die 5x gemaakt moet worden
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    LocalDate einddatum = LocalDate.of(2025, Month.FEBRUARY, 1);
    agenda.maakPeriodiekeAfspraak("Titel",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    
    //test hoeveel items er zijn gemaakt
    List<Item> items = agenda.getItems(begindatum, einddatum);
    assertEquals(5,items.size());//zou een array met 5 items moeten zijn
    
    //in range id 1 tm 5
    assertEquals(1,items.get(0).getId());
    assertEquals(3,items.get(2).getId());
    assertEquals(5,items.get(4).getId());
    
    //out range elements
    assertThrows(IndexOutOfBoundsException.class, () -> { items.get(-1); });
    assertThrows(IndexOutOfBoundsException.class, () -> { items.get(5); });
    
    //datum zou moeten zijn 1 jan, 8 jan, 15 jan, 22 jan en 29 jan 2025
    assertEquals("2025-01-01",items.get(0).getDatum().toString());
    assertEquals("2025-01-08",items.get(1).getDatum().toString());
    assertEquals("2025-01-15",items.get(2).getDatum().toString());
    assertEquals("2025-01-22",items.get(3).getDatum().toString());
    assertEquals("2025-01-29",items.get(4).getDatum().toString());
    
    /*
     * Buiten de datum en id moet titel begin en eindtijd gelijk
     * Test items in range 1 en 4
     */
    Afspraak afspraak1 = (Afspraak) items.get(1);
    Afspraak afspraak4 = (Afspraak) items.get(4);
    assertEquals(afspraak1.getTitel(),afspraak4.getTitel());
    assertEquals(afspraak1.getBeginTijd(),afspraak4.getBeginTijd());
    assertEquals(afspraak1.getEindtijd(),afspraak4.getEindtijd());
    

        
  }
  
  /**
   * Test fout condities maakPeriodiekeAfspraa()
   */
  @Test
  public void maakPeriodiekeAfspraakFoutConditieTest() {
    
    /*
     * Test foutcondities
     * Dit zou moeten resulteren in lege lijst
     * Items moeten niet aan de agenda worden toegevoegd 
     * agenda.nextid zou niet verhoogd moeten zijn
     */
    
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    LocalDate einddatum = LocalDate.of(2025, Month.FEBRUARY, 1);
    
    //Krijgen we een lege lijst al we een lege string als titel opgeven
    agenda.maakPeriodiekeAfspraak("",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    List<Item> items = agenda.getItems(begindatum, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst al we een startdatum in het verleden opgeven
    LocalDate datumverleden = LocalDate.of(2023, Month.JANUARY, 1);
    agenda.maakPeriodiekeAfspraak("Titel",datumverleden, einddatum, t1, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(datumverleden, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst al we een startdatum in het verleden opgeven
    LocalDate datumvandaag = LocalDate.now();
    LocalTime tweeminutengeleden = LocalTime.now().minusMinutes(2);
    agenda.maakPeriodiekeAfspraak("Titel",datumvandaag, einddatum, tweeminutengeleden, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(datumvandaag, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst met einddatum voor de bedgindatum
    agenda.maakPeriodiekeAfspraak("Titel",einddatum, begindatum, t1, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(datumverleden, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst met begintijd == eindtijd
    agenda.maakPeriodiekeAfspraak("Titel",begindatum, begindatum, t1, t1,Frequentie.WEKELIJKS);
    items = agenda.getItems(begindatum, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst met eindtijd < begintijd
    LocalTime t1 = LocalTime.of(6, 0);
    LocalTime t2 = LocalTime.of(7, 0);
    agenda.maakPeriodiekeAfspraak("Titel",begindatum, begindatum, t2, t1,Frequentie.WEKELIJKS);
    items = agenda.getItems(begindatum, begindatum);
    assertEquals(0,items.size());
    
    /*
     * Er zijn een aantal foutcondities geweest. 
     * Dit moet niet geresulteerd hebben in het verhogen van Agenda.nextId 
     */
    
    int id = agenda.maakEenmaligeAfspraak("Titel",begindatum,t1,t2);
    assertEquals(1,id);
    //voeg 5 items toe
    agenda.maakPeriodiekeAfspraak("Titel",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(begindatum, einddatum);
    assertEquals(6,items.size());
    
  }
  
  @Test
  public void maakToDoTest() {
    
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    LocalDate einddatum = LocalDate.of(2025, Month.JANUARY, 2);
    int id = agenda.maakToDo("Titel", begindatum);
    assertEquals(1,id);
    
    id = agenda.maakToDo("Titel", begindatum);
    assertEquals(2,id);
    
    //Get alle attributes
    ToDo todo = (ToDo) agenda.getItem(1);
    assertEquals("Titel",todo.getTitel());
    assertEquals("2025-01-01",todo.getDatum().toString());
    assertFalse(todo.getAfgevinkt());
        
    //er zijn twee items toegevoegd test size
    List<Item> items = agenda.getItems(begindatum, einddatum);
    assertEquals(2,items.size());
    
    //inhoud ToDo 1 en 2
    ToDo todo1 = (ToDo) items.get(0);
    ToDo todo2 = (ToDo) items.get(1);
  
    //in range id 1 en 2
    assertEquals(1,todo1.getId());
    assertEquals(2,todo2.getId());
    
    //out range elements
    assertThrows(IndexOutOfBoundsException.class, () -> { items.get(-1); });
    assertThrows(IndexOutOfBoundsException.class, () -> { items.get(2); });
    
    /*
     * Foutcondities 
     */
    
    //titel is een lege string
    id = agenda.maakToDo("", begindatum);
    assertEquals(-1,id);
    
    //titel is een null
    id = agenda.maakToDo(null, begindatum);
    assertEquals(-1,id);
    
    //datum is reeds verstreken
    LocalDate datumverleden = LocalDate.of(2023, Month.JANUARY, 1);
    id = agenda.maakToDo("Titel", datumverleden);
    assertEquals(-1,id);
    

    /**
     * Geen van deze foutcondities zou moeten resulteren in een verhoging van Agenda.nextId 
     */
    id = agenda.maakToDo("Titel", begindatum);
    assertEquals(3,id);
    
  }
  
  @Test
  public void vinkToDoAfTest() {
    
    //maak twee todos
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    agenda.maakToDo("ToDo 1", begindatum);
    agenda.maakToDo("ToDo 2", begindatum);
    
    //afgevinkt moet false zijn
    ToDo todo1 = (ToDo) agenda.getItem(1); //geeft kopie
    assertFalse(todo1.getAfgevinkt());
    agenda.vinkToDoAf(1);
    todo1 = (ToDo) agenda.getItem(1); //geeft kopie
    assertTrue(todo1.getAfgevinkt());
    
    //todo 2 
    ToDo todo2 = (ToDo) agenda.getItem(2); //geeft kopie
    assertFalse(todo2.getAfgevinkt());
    agenda.vinkToDoAf(2);
    todo2 = (ToDo) agenda.getItem(2); //geeft kopie
    assertTrue(todo2.getAfgevinkt());
    
    //return van vinkToDoAf()
    agenda.maakToDo("ToDo 3", begindatum);
    assertTrue(agenda.vinkToDoAf(3));
    
    
    /*
     * Foutcondities
     */
    
    //reeds afgevinkt
    assertFalse(agenda.vinkToDoAf(3));
    
    //id ongeldig dit moet null zijn
    todo2 = (ToDo) agenda.getItem(5); //niet bestaande todo
    assertNull(todo2);
    //vinkaf op null
    assertFalse(agenda.vinkToDoAf(5)); 
   
    
  }
  
  @Test
  public void getTodosTest() {
    //happy
    
    //voeg 4 items toe met verschillende datums
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    agenda.maakToDo("ToDo 1", begindatum);
       
    begindatum = LocalDate.of(2025, Month.MARCH, 1);
    for(int i=0; i<3; i++) {
      agenda.maakToDo("ToDo "+i, begindatum);
    }
    
    //vinkaf twee van de todo op 1 maart af
    agenda.vinkToDoAf(2);
    agenda.vinkToDoAf(3);
    
    //todo van 1 jan
    List<ToDo> todos = agenda.getToDos(LocalDate.of(2025, Month.JANUARY, 1), false);
    assertEquals(1,todos.size());
    
    //todos op 1 maart
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), false);
    assertEquals(1,todos.size());
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), true);
    assertEquals(2,todos.size());
    
    
    //vink laatste op 1 maart af
    agenda.vinkToDoAf(4);
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), false);
    assertEquals(0,todos.size());
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), true);
    assertEquals(3,todos.size());
    
    
    //datum zonder todos
    todos = agenda.getToDos(LocalDate.of(2025, Month.JUNE, 1), false);
    assertEquals(0,todos.size());
    todos = agenda.getToDos(LocalDate.of(2025, Month.JUNE, 1), true);
    assertEquals(0,todos.size());
    
  }

  



} 
