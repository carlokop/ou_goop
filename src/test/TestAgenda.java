package test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import agenda.Agenda;
import agenda.AgendaException;
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
  public void maakEenmaligeAfspraakTest() {
    /*
     * Happy path test
     */
    int id = agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    assertEquals(1,id);
    //waarde wordt met een opgehoogd
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
    Afspraak kloon = new Afspraak(afspraak);
    assertEquals("Titel",kloon.getTitel());
    assertEquals(1,kloon.getId());
    assertEquals("2025-01-01",kloon.getDatum().toString());
    //ook de attributen die niet in de abstracte klasse staan
    assertEquals("06:00",kloon.getBeginTijd().toString());
    assertEquals("07:00",kloon.getEindtijd().toString());
    
    Afspraak afspraak1 = (Afspraak) agenda.getItem(1);
    Afspraak afspraak2 = (Afspraak) agenda.getItem(2);
    
    //toegevoegd aan de lijst
    List<Item> items = agenda.getItems(d1, d1);
    assertTrue(arrayHasItemId(afspraak1,items));
    assertTrue(arrayHasItemId(afspraak2,items));
    assertTrue(arrayHasItemId(kloon,items));
    assertEquals(2, items.size());
    
    /*
     * Fout condities
     * Deze moeten id = -1 opleveren en geen excepties
     * Item mogen niet aan lijst worden toegevoegd
     */
    
    //Titel is lege string
    id = agenda.maakEenmaligeAfspraak("",d1,t1,t2);
    assertEquals(-1,id);
    
    //Titel is string met alleen spaties
    id = agenda.maakEenmaligeAfspraak("   ",d1,t1,t2);
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
    //dus we zouden dezelfde items in de lijst moeten hebben als hiervoor
    items = agenda.getItems(d1, d1);
    assertEquals(1, items.get(0).getId());
    assertEquals(2, items.get(1).getId());
    assertEquals(2, items.size());
    
    /*
     * Er zijn een aantal foutcondities gedaan
     * Agenda.nextid zou niet opgehoogd mogen zijn
     * Bij de eerst volgende goede afspraak moeten we de volgende nextid hebben
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
   * @throws AgendaException 
   */
  @Test
  public void maakPeriodiekeAfspraakTest() throws AgendaException {
    //happy
    //maak een periodiek item die 5x gemaakt moet worden
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    LocalDate einddatum = LocalDate.of(2025, Month.FEBRUARY, 1);
    agenda.maakPeriodiekeAfspraak("Titel",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    
    //test hoeveel items er zijn gemaakt
    //als we id's 1 t/m 5 hebben is waarde steeds met 1 opgehoogd
    List<Item> items = agenda.getItems(begindatum, einddatum);
    assertEquals(5,items.size());//zou een array met 5 items moeten zijn
    
    Afspraak afspraak0 = (Afspraak) agenda.getItem(0); //mag niet bestaan
    Afspraak afspraak1 = (Afspraak) agenda.getItem(1);
    Afspraak afspraak3 = (Afspraak) agenda.getItem(3);
    Afspraak afspraak4 = (Afspraak) agenda.getItem(4);
    Afspraak afspraak5 = (Afspraak) agenda.getItem(5);
    Afspraak afspraak6 = (Afspraak) agenda.getItem(6); //mag niet bestaan
        
    //in range id 1 tm 5
    assertEquals(1,afspraak1.getId());
    assertEquals(3,afspraak3.getId());
    assertEquals(5,afspraak5.getId());
    assertTrue(arrayHasItemId(afspraak1,items));
    assertTrue(arrayHasItemId(afspraak3,items));
    assertTrue(arrayHasItemId(afspraak5,items));
    
    //out range elements
    assertFalse(arrayHasItemId(afspraak0,items));
    assertFalse(arrayHasItemId(afspraak6,items));
    
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
    assertEquals(afspraak1.getTitel(),afspraak4.getTitel());
    assertEquals(afspraak1.getBeginTijd(),afspraak4.getBeginTijd());
    assertEquals(afspraak1.getEindtijd(),afspraak4.getEindtijd());
    
    
    /*
     * Test foutcondities
     * Dit zou moeten resulteren in lege lijst
     * Items moeten niet aan de agenda worden toegevoegd 
     * agenda.nextid zou niet verhoogd moeten zijn
     */
    
    //omdat er geen methode is om items te verwijderen uit de agenda maak ik een nieuwe agenda voor deze tests
    agenda = new Agenda();  
    
    //
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //Krijgen we een lege lijst al we een lege string als titel opgeven
    agenda.maakPeriodiekeAfspraak("",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(begindatum, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst al we een titel bestaande uit alleen spaties opgeven
    agenda.maakPeriodiekeAfspraak("   ",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(begindatum, einddatum);
    assertEquals(0,items.size());
    
    
    //Krijgen we een lege lijst als we een startdatum in het verleden opgeven
    LocalDate datumverleden = LocalDate.of(2023, Month.JANUARY, 1);
    agenda.maakPeriodiekeAfspraak("Titel",datumverleden, einddatum, t1, t2,Frequentie.WEKELIJKS);
    items = agenda.getItems(datumverleden, einddatum);
    assertEquals(0,items.size());
    
    //Krijgen we een lege lijst al we een vandaag starten maar tijd in het verleden
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
    
    //maak twee items met datum in de toekomst
    //ids opvolgend
    int id = agenda.maakToDo("Titel", begindatum);
    assertEquals(1,id);
    id = agenda.maakToDo("Titel", begindatum);
    assertEquals(2,id);
    
    //datum vandaag
    id = agenda.maakToDo("Titel", LocalDate.now());
    assertEquals(3,id);
    
    //Get alle attributes
    ToDo todo = (ToDo) agenda.getItem(1);
    assertEquals("Titel",todo.getTitel());
    assertEquals("2025-01-01",todo.getDatum().toString());
    assertFalse(todo.getAfgevinkt());
        
    //omdat de toevoeging van vandaag niet aan de filters voldoet zou die niet in de gefilterde lijst horen
    List<Item> items = agenda.getItems(begindatum, einddatum);
    assertEquals(2,items.size());
    
    //inhoud ToDo 1 en 2
    ToDo todo0 = (ToDo) agenda.getItem(0); //bestaat niet
    ToDo todo1 = (ToDo) agenda.getItem(1);
    ToDo todo2 = (ToDo) agenda.getItem(2);
    ToDo todo3 = (ToDo) agenda.getItem(3);
  
    //in range id 1 en 2
    assertEquals(1,todo1.getId());
    assertEquals(2,todo2.getId());
    assertTrue(arrayHasItemId(todo1,items));
    assertTrue(arrayHasItemId(todo2,items));
    
    //out range elements id 0 en 3
    assertFalse(arrayHasItemId(todo0,items));
    assertFalse(arrayHasItemId(todo3,items));
    
    /*
     * Foutcondities 
     */
    
    //titel is een lege string
    id = agenda.maakToDo("", begindatum);
    assertEquals(-1,id);
    
    //titel is een string met alleen spaties
    id = agenda.maakToDo("  ", begindatum);
    assertEquals(-1,id);
    
    //datum is reeds verstreken
    LocalDate datumverleden = LocalDate.of(2023, Month.JANUARY, 1);
    id = agenda.maakToDo("Titel", datumverleden);
    assertEquals(-1,id);
    

    /**
     * Geen van deze foutcondities zou moeten resulteren in een verhoging van Agenda.nextId 
     */
    id = agenda.maakToDo("Titel", begindatum);
    assertEquals(4,id);
    
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
    
    //id ongeldig dus er wordt geen todo gevonden (todo is null)
    todo2 = (ToDo) agenda.getItem(5); //niet bestaande todo
    assertNull(todo2);
    //vinkaf op null
    assertFalse(agenda.vinkToDoAf(5)); 
   
  }
  
  
  @Test
  public void getItemsTest() throws AgendaException {
    
    //happy test 
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    LocalDate einddatum = LocalDate.of(2025, Month.FEBRUARY, 1);
    
    //voeg een aantal afspraken en todos in de range toe
    //id 1 t/m 5
    for(int i=0; i<5; i++) {
     agenda.maakEenmaligeAfspraak("Titel",begindatum,t1,t2);
    } 
    
    //periodieke afspraak die 5 afspraken maakt
    //id = 6 t/m 10
    agenda.maakPeriodiekeAfspraak("Titel",begindatum, einddatum, t1, t2,Frequentie.WEKELIJKS);
    
    //maak 1 todo op begindatum eugen halverwege en op einddatum
    //id 11, 12 en 13
    agenda.maakToDo("ToDo 1", begindatum);
    agenda.maakToDo("ToDo 2", LocalDate.of(2025, Month.JANUARY, 15));
    agenda.maakToDo("ToDo 3", einddatum);
    
    //voeg ook wat afspraken en todo's toe voor en da de begin en einddatum
    //id 14 t/m 17
    agenda.maakEenmaligeAfspraak("Titel",begindatum.minusDays(1),t1,t2);
    agenda.maakEenmaligeAfspraak("Titel",einddatum.plusDays(1),t1,t2);
    agenda.maakToDo("ToDo 4", begindatum.minusDays(1));
    agenda.maakToDo("ToDo 5", einddatum.plusDays(1));
    
    //en een periodieke afspraak die overloop heeft
    //id 18 t/m 24 waarvan id 19 t/m 23 in de range
    agenda.maakPeriodiekeAfspraak("Titel",begindatum.minusWeeks(1), einddatum.plusWeeks(1), t1, t2,Frequentie.WEKELIJKS);

    //test of we alleen de items in de range hebben
    List<Item> items = agenda.getItems(begindatum, einddatum);
    assertEquals(18, items.size());
    

    //en voeg nog 2 toe op einddatum
    //id 25 en 26
    agenda.maakEenmaligeAfspraak("Titel",einddatum,t1,t2);
    agenda.maakEenmaligeAfspraak("Titel",einddatum,t1,t2);
    
    items = agenda.getItems(begindatum, einddatum);
    assertEquals(20, items.size());
    
    //items die in range moeten zitten
    Item i1 = agenda.getItem(1);  //eenmalige afspraak
    Item i3 = agenda.getItem(3);  //eenmalige afspraak
    Item i5 = agenda.getItem(5);  //eenmalige afspraak
    Item i6 = agenda.getItem(6);  //periodieke afspraak
    Item i8 = agenda.getItem(8);  //periodieke afspraak
    Item i10 = agenda.getItem(10);  //periodieke afspraak
    Item i11 = agenda.getItem(11);  //todo
    Item i13 = agenda.getItem(13);  //todo
    
    //dit zou niet in range moeten zitten
    Item i14 = agenda.getItem(14);  //eenmalige afspraak
    Item i17 = agenda.getItem(17);  //todo
    Item i18 = agenda.getItem(18);  //periodieke afspraak
    Item i24 = agenda.getItem(24);  //periodieke afspraak
    
    //test of de items in de gefilterde reultaten zitten
    assertTrue(arrayHasItemId(i1,items));
    assertTrue(arrayHasItemId(i3,items));
    assertTrue(arrayHasItemId(i5,items));
    assertTrue(arrayHasItemId(i6,items));
    assertTrue(arrayHasItemId(i8,items));
    assertTrue(arrayHasItemId(i10,items));
    assertTrue(arrayHasItemId(i11,items));
    assertTrue(arrayHasItemId(i13,items));
    
    //test de items die niet in de gefilterde resultaten hoort te zitten
    assertFalse(arrayHasItemId(i14,items));
    assertFalse(arrayHasItemId(i17,items));
    assertFalse(arrayHasItemId(i18,items));
    assertFalse(arrayHasItemId(i24,items));
    
    //test einddatum komt na de begindatum moet resulteren in lege list
    items = agenda.getItems(einddatum, begindatum);
    assertEquals(0, items.size());
    
    //alleen de items van een week voor de begindatum
    items = agenda.getItems(begindatum.minusWeeks(1), begindatum.minusWeeks(1));
    assertEquals(1, items.size());
    assertTrue(arrayHasItemId(i18,items));

    //test er mag geen referentie zijn door de clone
    items = agenda.getItems(begindatum, einddatum);
    Item item1 = items.get(0);
    Item item2 = agenda.getItem(1);
    assertEquals(item1.getId(), item2.getId()); //zou zelfde item moeten zijn
    assertNotEquals(item1, item2); //niet zelfde referentie
   
  }
  
  
  @Test
  public void getTodosTest() {
    //happy
    
    //voeg 4 todos toe met verschillende datums
    LocalDate begindatum = LocalDate.of(2025, Month.JANUARY, 1);
    agenda.maakToDo("ToDo 1", begindatum);
       
    begindatum = LocalDate.of(2025, Month.MARCH, 1);
    for(int i=0; i<3; i++) {
      agenda.maakToDo("ToDo "+i, begindatum);
    }
    
    //vinkaf twee van de todo op 1 maart af
    agenda.vinkToDoAf(2);
    agenda.vinkToDoAf(3);
    
    ToDo todo1 = (ToDo) agenda.getItem(1);  //todo 1 jan
    ToDo todo2 = (ToDo) agenda.getItem(2);  //todo 1 maart afgevinkt
    ToDo todo3 = (ToDo) agenda.getItem(3);  //todo 1 maart afgevinkt
    ToDo todo4 = (ToDo) agenda.getItem(4);  //todo 1 maart
    
    //todo van 1 jan
    List<ToDo> todos = agenda.getToDos(LocalDate.of(2025, Month.JANUARY, 1), false);
    assertEquals(1,todos.size());
    assertTrue(arrayHasItemId(todo1,todos));  //id 1 in lijst
    assertFalse(arrayHasItemId(todo2,todos)); //id 2 niet in lijst
    assertFalse(arrayHasItemId(todo3,todos)); //id 3 niet in lijst
    assertFalse(arrayHasItemId(todo4,todos)); //id 3 niet in lijst
    
    //todos op 1 maart
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), false);
    assertEquals(1,todos.size());
    
    assertFalse(arrayHasItemId(todo1,todos)); //id 1 niet in lijst
    assertFalse(arrayHasItemId(todo2,todos)); //id 2 niet in lijst
    assertFalse(arrayHasItemId(todo3,todos)); //id 3 niet in lijst
    assertTrue(arrayHasItemId(todo4,todos));  //id 4 in lijst
    
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), true);
    assertEquals(2,todos.size());
    
    assertFalse(arrayHasItemId(todo1,todos)); //id 1 niet in lijst
    assertTrue(arrayHasItemId(todo2,todos));  //id 2 in lijst
    assertTrue(arrayHasItemId(todo3,todos));  //id 3 in lijst
    assertFalse(arrayHasItemId(todo4,todos)); //id 4 niet in lijst
    
    
    //vink laatste op 1 maart af
    agenda.vinkToDoAf(4);
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), false);
    assertEquals(0,todos.size());
    
    assertFalse(arrayHasItemId(todo1,todos));  //id 1 niet in lijst
    assertFalse(arrayHasItemId(todo2,todos));  //id 2 niet in lijst
    assertFalse(arrayHasItemId(todo3,todos));  //id 3 niet in lijst
    assertFalse(arrayHasItemId(todo4,todos));  //id 4 niet in lijst
    
    
    todos = agenda.getToDos(LocalDate.of(2025, Month.MARCH, 1), true);
    assertEquals(3,todos.size());
    
    assertFalse(arrayHasItemId(todo1,todos)); //id 1 niet in lijst
    assertTrue(arrayHasItemId(todo2,todos));  //id 2 in lijst
    assertTrue(arrayHasItemId(todo3,todos));  //id 3 in lijst
    assertTrue(arrayHasItemId(todo4,todos));  //id 4 in lijst
    
    //datum zonder todos
    todos = agenda.getToDos(LocalDate.of(2025, Month.JUNE, 1), false);
    assertEquals(0,todos.size());
    
    assertFalse(arrayHasItemId(todo1,todos));  //id 1 niet in lijst
    assertFalse(arrayHasItemId(todo2,todos));  //id 2 niet in lijst
    assertFalse(arrayHasItemId(todo3,todos));  //id 3 niet in lijst
    assertFalse(arrayHasItemId(todo4,todos));  //id 4 niet in lijst
    
    
    todos = agenda.getToDos(LocalDate.of(2025, Month.JUNE, 1), true);
    assertEquals(0,todos.size());
    
    assertFalse(arrayHasItemId(todo1,todos));  //id 1 niet in lijst
    assertFalse(arrayHasItemId(todo2,todos));  //id 2 niet in lijst
    assertFalse(arrayHasItemId(todo3,todos));  //id 3 niet in lijst
    assertFalse(arrayHasItemId(todo4,todos));  //id 4 niet in lijst
    
  }
  
  /**
   * Lookup klasse om na te gaan of een gegeven item.id zit in een list met items
   * Zal ook bij een kloon true geven als de id maar overeenkomt
   * @param item    expected item
   * @param items   lijst met items om te testen
   * @return        true als het item in de lijst zit
   */
  private boolean arrayHasItemId(Item item, List<?> items) {
    if(item == null) {
      return false;
    }
    for(Object elm: items) {
      if(((Item) elm).getId() == item.getId()) {
        return true;
      }
    }
    return false;
  }
  

  @Test
  public void arrayHasItemIdTest() {
    List<Item> list = new ArrayList<>();
    //test null
    assertFalse(arrayHasItemId(null,list));
    
    //toegevoegd item
    agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    Item item = agenda.getItem(1);
    list.add(item);
    assertTrue(arrayHasItemId(item,list));
    
    //niet toegevoegd item
    agenda.maakEenmaligeAfspraak("Titel",d1,t1,t2);
    Item item2 = agenda.getItem(2);
    assertFalse(arrayHasItemId(item2,list));
    
    //test item niet gevonden item (item is dus null)
    Item item3 = agenda.getItem(3);
    assertFalse(arrayHasItemId(item3,list));
  }

  



} 
