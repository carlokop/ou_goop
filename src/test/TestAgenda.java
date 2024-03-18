package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import agenda.Agenda;
import agenda.AgendaItem;
import agenda.ToDo;
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
    assertTrue(id == 0);
    id = agenda.maakToDo("titel", d1);
    assertTrue(id == 1);
    
    //null of lege string in titel geenft -1 terug en fout wordt afgehandeld
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
  }
  
  @Test
  public void vinkTodoAfTest() {
    //voeg 2 todos toe
    int id1 = agenda.maakToDo("titel", d1);
    int id2 =  agenda.maakToDo("titel", d1);
    
    //vink todo met gegeven id af
    agenda.vinkToDoAf(1);
    ToDo todo1 = agenda.getTodo(0);
    ToDo todo2 = agenda.getTodo(1);
    assertFalse(todo1.getAfgevinkt());
    assertTrue(todo2.getAfgevinkt());
    
    System.out.println("id1 = " + id1);
    System.out.println("todo1 = " + todo1.toString());
    
    //vink 2x dezelfde af
    //voor dit project worden de meeste fouten in de agenda klasse afgehandeld om het systeem niet te laten crashen
    //ik heb op deze plek dan ook geen assertThrows maar er zou wel een system.out.print() zichtbaar moeten zijn.
    agenda.vinkToDoAf(1);
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
  public void maakEenmaligeAfspraakHappytest() {
    //maak afspraak instantie
    
    //test alle attributen
    
    //er moeten hier geen excepties plaatsvinden die worden in de methode van de agenda afgehandeld
    
  }
  
  @Test
  public void maakEenmaligeAfspraakTest() {
    //maak instantie met iedere keer een datum of tijd = null
    
    //titel is null
    
    //titel is lege string
    
    //afspraak begindatum in het verleden
    
    //afspraak datum vandaag maar tijd in het verleden
    
    //afspraak einddatum voor de begindatum
    
    //afspraak vandaag en begintijd in de toekomst maar eindtijd in het verleden
    
    //afspraak begin en einddatum in de toekomst maar eindtijd voor begintijd
    
    //afspraak toekomsst datum en tijd gelijk
    
    ///afspraak verleden datum en tijd gelijk
    
  }
  
  
  
  

} //class
