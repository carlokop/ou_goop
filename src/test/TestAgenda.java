package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import agenda.Agenda;
import agenda.AgendaItem;
import agenda.PeriodiekeAfspraak;
import agenda.Frequentie;
import agenda.Item;
import agenda.ToDo;
import exceptions.AgendaException;
import exceptions.DatumVerledenException;

public class TestAgenda {
  
  private Agenda agenda;
  private LocalDate d1;
  private LocalDate d2;
  private LocalDate d3;
  private LocalTime t1;
  private LocalTime t2;
  private LocalDateTime dt1;
  private LocalDateTime dt2;
  
  @Before
  public void setUp() {
    agenda = new Agenda();
    d1 = LocalDate.of(2025, Month.JANUARY, 1);
    d2 = LocalDate.of(2025, Month.JANUARY, 2);
    d3 = LocalDate.of(2023, Month.JANUARY, 2);
    t1 = LocalTime.of(6, 0);
    t2 = LocalTime.of(7, 0);
    dt1 = LocalDateTime.of(d1,t1);
    dt2 = LocalDateTime.of(d2,t2);
  }

  @Test
  public void maakTodoTest() {
    //happy
    //test juist ID wordt toegewezen en teruggegeven
    int id = agenda.maakToDo("titel", d1);
    assertTrue(id == 1);
    id = agenda.maakToDo("titel", d1);
    assertTrue(id == 2);
    
    //null of lege string in titel geeft -1 terug en fout wordt afgehandeld
    id = agenda.maakToDo("", d1);
    assertTrue(id == -1);
    id = agenda.maakToDo(null, d1);
    assertTrue(id == -1);
    
    //datum in het verleden
    id = agenda.maakToDo("titel", d3);
    assertTrue(id == -1);   
    
  }
  
  @Test 
  public void getToDoTest() {
    //krijgt todo
    int id = agenda.maakToDo("titel:", d1);
    ToDo todo = agenda.getTodo(id);
    assertTrue(todo != null);
    assertEquals(todo.getTitel(),"titel:");
    assertEquals(todo.getId(),1);
    assertEquals(todo.getDatum().toString(),"2025-01-01");
  }
  
  @Test
  public void vinkTodoAfTest() {
    //voeg 2 todos toe
    int id1 = agenda.maakToDo("titel", d1);
    int id2 =  agenda.maakToDo("titel", d1);
    
    //vink todo met gegeven id af
    agenda.vinkToDoAf(1);
    ToDo todo1 = agenda.getTodo(1);
    ToDo todo2 = agenda.getTodo(2);
    assertTrue(todo1.getAfgevinkt());
    assertFalse(todo2.getAfgevinkt());
    
    //vink 2x dezelfde af
    //voor dit project worden de meeste fouten in de agenda klasse afgehandeld om het systeem niet te laten crashen
    //ik heb op deze plek dan ook geen assertThrows maar er zou wel een system.out.print() zichtbaar moeten zijn.
    agenda.vinkToDoAf(2);
  }
  
  @Test
  public void getToDosTest() {
    //lege array
    ArrayList<ToDo> todos = (ArrayList<ToDo>) agenda.getToDos(d1, false);
    assertTrue(todos.size() == 0);
    
    //add 5 todos allen niet afgevinkt en zelfde datum
    for(int i=0; i<5; i++) {
      agenda.maakToDo("titel: " +i, d1);
    }
    
    todos = (ArrayList<ToDo>) agenda.getToDos(d1, false);
    assertTrue(todos.size() == 5);
    
    //andere datum
    agenda.maakToDo("titel: nieuwe datem", d2);
    todos = (ArrayList<ToDo>) agenda.getToDos(d1, false);
    assertTrue(todos.size() == 5);
    
    todos = (ArrayList<ToDo>) agenda.getToDos(d2, false);
    assertTrue(todos.size() == 1);
    
    //vink een af
    agenda.vinkToDoAf(1);
    todos = (ArrayList<ToDo>) agenda.getToDos(d1, false);
    assertTrue(todos.size() == 4);
    
    ArrayList<ToDo> todosafgevinkt = (ArrayList<ToDo>) agenda.getToDos(d1, true);
    assertTrue(todosafgevinkt.size() == 1);
    agenda.vinkToDoAf(2);
    todosafgevinkt = (ArrayList<ToDo>) agenda.getToDos(d1, true);
    assertTrue(todosafgevinkt.size() == 2);
    
  }
  
  @Test
  public void getItemTest() {
    int id = agenda.maakEenmaligeAfspraak("Titel", d1, t1, t2);
    
    //happy
    Item item = agenda.getItem(id);
    assertTrue(item != null);
    assertEquals(item.getTitel(),"Titel");
    assertEquals(item.getId(),1);
    assertEquals(item.getDatum().toString(),"2025-01-01");
    
    item = agenda.getItem(-1);
    assertNull(item);
  }
  
  @Test
  public void maakEenmaligeAfspraakHappytest() {
    //maak afspraak instantie
    int id = agenda.maakEenmaligeAfspraak("Titel", d1, t1, t2);
    
    //getItem
    Item item = agenda.getItem(id);
    
    //zou geen exepties moeten opgooien wordt in de agenda klasse afgehandeld. Wel System.out.println()
    agenda.maakEenmaligeAfspraak(null, d1, t1, t2);
    agenda.maakEenmaligeAfspraak("", d1, t1, t2);
    
    //begintijd > eindtijd
    agenda.maakEenmaligeAfspraak("Titel", d1, t2, t1);
    
    //begintijd == eindtijd
    agenda.maakEenmaligeAfspraak("Titel", d1, t1, t1);
    
    //datum in het verleden
    d1 = LocalDate.of(2023, Month.JANUARY, 1);
    agenda.maakEenmaligeAfspraak("Titel", d1, t1, t2);
    
    //datum in het verleden + begintijd == eindtijd
    agenda.maakEenmaligeAfspraak("Titel", d1, t1, t1);
    
    //datum vandaag maar begintijd al verstreken
    LocalDateTime nu = Item.maakAfgerondeDateTime(LocalDateTime.now());
    LocalDate datenu = nu.toLocalDate();
    LocalTime uurgeleden = nu.toLocalTime().minusHours(1);
    LocalTime overeenuur = nu.toLocalTime().plusHours(1);
    agenda.maakEenmaligeAfspraak("Titel", datenu, uurgeleden, overeenuur);
    
    //afspraak vandaag en begintijd in de toekomst maar eindtijd in het ver
    agenda.maakEenmaligeAfspraak("Titel", datenu, overeenuur, uurgeleden);

  }
  
  @Test
  public void getItemsTest() throws AgendaException {
    d1 = LocalDate.of(2025, Month.JANUARY, 1);
    d2 = LocalDate.of(2025, Month.JANUARY, 2);
    d3 = LocalDate.of(2025, Month.JANUARY, 3);
    
    //voeg 10 items toe met datum 1 jan 2025
    for(int i=1; i<11; i++) {
      agenda.maakEenmaligeAfspraak("Titel", d1, t1, t2);
    }
    
    //voeg item toe 2 januari 2025
    agenda.maakEenmaligeAfspraak("Titel", d2, t1, t2);
    
    //voeg 5 todos toe aan de lijst
    for(int i=1; i<6; i++) {
      agenda.maakToDo("titel", d1);
    }
    
    //voeg wat periodieke items toe
    d3 = LocalDate.of(2025, Month.FEBRUARY, 3);
    List<Integer> list = agenda.maakPeriodiekeAfspraak("titel",d1, d3, t1, t2, Frequentie.WEKELIJKS);
    assertTrue(list.size() == 5);
    
    d3 = LocalDate.of(2025, Month.MARCH, 3);
    list = agenda.maakPeriodiekeAfspraak("titel",d1, d3, t1, t2, Frequentie.WEKELIJKS);
    assertTrue(list.size() == 9);
    assertEquals(list.toString(),"[22, 23, 24, 25, 26, 27, 28, 29, 30]");
    
    
    //alle items incl todos
    ArrayList<AgendaItem> items = (ArrayList<AgendaItem>) agenda.getItems(d1,d3);
    assertTrue(items.size() == 30);
    
    //alle afspraken en periodieke afspraken
    ArrayList<Item> afspraken = (ArrayList<Item>) agenda.getAfspraken(d1,d3);
    assertTrue(afspraken.size() == 25);
    
    ArrayList<PeriodiekeAfspraak> periodiekeAfspraken = (ArrayList<PeriodiekeAfspraak>) agenda.getPeriodiekeAfspraken(d1,d3);
    assertTrue(periodiekeAfspraken.size() == 14);
    
        
  }
  

  
  
  
  

} //class
