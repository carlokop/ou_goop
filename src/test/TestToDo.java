package test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agenda.Afspraak;
import agenda.AgendaException;
import agenda.Item;
import agenda.PeriodiekeAfspraak;
import agenda.ToDo;
import agenda.exceptions.DatumVerledenException;
import agenda.exceptions.ReedsAfgevinktException;

/**
 * Tests de ToDo klasse
 * @author carlo
 *
 */
public class TestToDo {
  
  private ToDo t1;
  private LocalDate d1 = LocalDate.of(2024, Month.JUNE, 14);
  
  @Test
  public void TodoTest() throws DateTimeException, AgendaException  {
    
    //happy
    t1 = new ToDo(1,"todo", d1);
    assertEquals("todo", t1.getTitel());
    assertEquals(1, t1.getId());
    assertEquals("2024-06-14", t1.getDatum().toString());
    assertFalse(t1.getAfgevinkt());
    
    //titel null of lege string
    assertThrows(AgendaException.class, () -> { new ToDo(1,"", d1); });
    assertThrows(AgendaException.class, () -> { new ToDo(1,null, d1); });
    
    //ongeldige id
    assertThrows(AgendaException.class, () -> { new ToDo(0,"Titel", d1); });
    assertThrows(AgendaException.class, () -> { new ToDo(-1,"Titel", d1); });
    
    //datum reeds verstreken
    LocalDate verleden = LocalDate.of(2023, Month.JUNE, 14);
    assertThrows(DateTimeException.class, () -> { new ToDo(1,"Titel", verleden); });
   
  }
  
  @Test
  public void vinkToDoAfTest() throws DateTimeException, AgendaException {

    //happy
    t1 = new ToDo(1,"todo", d1);
    assertFalse(t1.getAfgevinkt());
    t1.vinkToDoAf();
    assertTrue(t1.getAfgevinkt());
    
    //todo is reeds afgevinkt
    assertThrows(AgendaException.class, () -> { t1.vinkToDoAf(); });
    
  }
  
  @Test
  public void cloneTest() throws DateTimeException, AgendaException {
    
    //kloon
    t1 = new ToDo(1,"todo", d1);
    ToDo kloon = new ToDo(t1);
    assertEquals("todo", kloon.getTitel());
    assertEquals(1, kloon.getId());
    assertEquals("2024-06-14", kloon.getDatum().toString());
    assertFalse(kloon.getAfgevinkt());
    
    //kloon niet gelijk aan origineel
    assertNotEquals(kloon,t1);
    
    //als we het origineel afvinken dan zou de kloon niet afgevinkt moeten zijn
    t1.vinkToDoAf();
    assertFalse(kloon.getAfgevinkt());
    
  }
  

 


}
