package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import agenda.Afspraak;
import agenda.Item;
import agenda.PeriodiekeAfspraak;
import agenda.ToDo;
import agenda.exceptions.AgendaException;
import agenda.exceptions.DatumVerledenException;
import agenda.exceptions.ReedsAfgevinktException;

/**
 * Test de generieke klasse Item<T>
 * @author carlo
 *
 */
public class TestItem {

  private LocalDate d1;
  private LocalDate d2;
  private LocalTime t1;
  private LocalTime t2;

  @Before
  public void setUp()  {
    d1 = LocalDate.of(2025, Month.JANUARY, 1);
    d2 = LocalDate.of(2025, Month.JANUARY, 2);
    t1 = LocalTime.of(6, 0);
    t2 = LocalTime.of(7, 0);
  }
  
  
  @Test
  public void maakTodoTest() throws IllegalArgumentException, NullPointerException, DatumVerledenException, ReedsAfgevinktException, AgendaException {
    //maken we hetzelfde object
    Item<ToDo> todo = new Item<ToDo>(1, "Titel", d1);
    ToDo todo2 = new ToDo(1, "Titel", d1);
    assertEquals(todo.toString(), todo2.toString());
    
    //kloon is gelijk
    Item<ToDo> kloon = todo.clone();
    assertFalse(todo.equals(kloon));
    assertEquals(todo.toString(),kloon.toString());
    
    //class == todo na getElm()
    assertTrue(todo.getElm().getClass().toString().equals("class agenda.ToDo"));
    
    //einddatumNogNietVerstreken true
    assertTrue(todo.einddatumNogNietVerstreken(d1));
    
    //einddatumNogNietVerstreken false
    assertFalse(todo.einddatumNogNietVerstreken(LocalDate.of(2023, Month.JANUARY, 1)));
    
    //getId
    assertEquals(todo.getId(),1);
    
    //getTitle
    assertEquals(todo.getTitel(),"Titel");
    
    //getDatum
    assertEquals(todo.getDatum(),d1);
    
    //vinkTodoaf
    assertFalse(todo.getAfgevinkt());
    todo.vinkToDoAf();
    assertTrue(todo.getAfgevinkt());
    
    //we kunnen niet twee keer todo afvinken
    assertThrows(ReedsAfgevinktException.class, () -> { todo.vinkToDoAf(); });
    
    //overige todo tests die niet happy path zijn staan in de todo klasse
  }

  //test maak eenmalige afspraak
  @Test
  public void maakEenmaligeAfspraakTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException {
    //maken we hetzelfde object
    Item<Afspraak> afspraak = new Item<Afspraak>(1,"Titel", d1, t1, t2);    
    Afspraak afspraak2 = new Afspraak(1,"Titel", d1, t1, t2); 
    assertEquals(afspraak.toString(), afspraak2.toString());
    
    //kloon is gelijk
    Item<Afspraak> kloon = afspraak.clone();
    assertFalse(afspraak.equals(kloon));
    assertEquals(afspraak.toString(),kloon.toString());
    
    //class == todo na getElm()
    assertTrue(afspraak.getElm().getClass().toString().equals("class agenda.Afspraak"));
    
    //einddatumNogNietVerstreken true
    assertTrue(afspraak.einddatumNogNietVerstreken(d1));
    
    //einddatumNogNietVerstreken false
    assertFalse(afspraak.einddatumNogNietVerstreken(LocalDate.of(2023, Month.JANUARY, 1)));
    
    //getId
    assertEquals(afspraak.getId(),1);
    
    //getTitle
    assertEquals(afspraak.getTitel(),"Titel");
    
    //getDatum
    assertEquals(afspraak.getDatum(),d1);
    
    /*
     * Deze methode is eigenlijk niet voor Afspraken maar ik kon geen oplossing vinden om die geheel uit de klasse te verwijderen
     * Zou altijd false moeten zijn
     * Afvinken zou altijd een fout op moeten gooien
     */
    assertFalse(afspraak.getAfgevinkt());
    assertThrows(AgendaException.class, () -> { afspraak.vinkToDoAf(); });
    
    //overige todo tests die niet happy path zijn staan in de todo klasse
    
  }
  
  @Test 
  public void maakPeriodiekeAfspraakTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException {
  //maken we hetzelfde object
    Item<PeriodiekeAfspraak> afspraak = new Item<PeriodiekeAfspraak>(1,"Titel", d1, t1, t2,999);    
    PeriodiekeAfspraak afspraak2 = new PeriodiekeAfspraak(1,"Titel", d1, t1, t2,999); 
    assertEquals(afspraak.toString(), afspraak2.toString());
    
    //kloon is gelijk
    Item<PeriodiekeAfspraak> kloon = afspraak.clone();
    assertFalse(afspraak.equals(kloon));
    assertEquals(afspraak.toString(),kloon.toString());
    
    //class == todo na getElm()
    assertTrue(afspraak.getElm().getClass().toString().equals("class agenda.PeriodiekeAfspraak"));
    
    //einddatumNogNietVerstreken true
    assertTrue(afspraak.einddatumNogNietVerstreken(d1));
    
    //einddatumNogNietVerstreken false
    assertFalse(afspraak.einddatumNogNietVerstreken(LocalDate.of(2023, Month.JANUARY, 1)));
    
    //getId
    assertEquals(afspraak.getId(),1);
    
    //getTitle
    assertEquals(afspraak.getTitel(),"Titel");
    
    //getDatum
    assertEquals(afspraak.getDatum(),d1);
    
    /*
     * Deze methode is eigenlijk niet voor Afspraken maar ik kon geen oplossing vinden om die geheel uit de klasse te verwijderen
     * Zou altijd false moeten zijn
     * Afvinken zou altijd een fout op moeten gooien
     */
    assertFalse(afspraak.getAfgevinkt());
    assertThrows(AgendaException.class, () -> { afspraak.vinkToDoAf(); });
    
    //overige todo tests die niet happy path zijn staan in de todo klasse
  }
  
  
  
  
  
  
  

}
