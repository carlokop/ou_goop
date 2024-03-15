package test;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import agenda.AgendaItem;
import agenda.Todo;
import exceptions.DatumVerledenException;
import exceptions.ReedsAfgevinktException;

public class TestToDo {
  
  private Todo t1;
  private Todo t2;
  private Todo t3;
  private LocalDate d1 = LocalDate.of(2024, Month.JUNE, 14);
  private LocalDate d2 = LocalDate.of(2024, Month.AUGUST, 22);
  private LocalDate d3 = LocalDate.of(2024, Month.DECEMBER, 31);

  @Before
  public void setUp() throws IllegalArgumentException, NullPointerException, IllegalStateException, DatumVerledenException {
    t1 = new Todo(1,"todo 1", d1);
    t2 = new Todo(2,"todo 2", d2);
    t3 = new Todo(3,"todo 3", d3);
  }
  
  @Test
  public void TodoTest() throws IllegalArgumentException, NullPointerException, IllegalStateException, DatumVerledenException {
    //happy
    assertEquals(t1.getTitel(),"todo 1");
    assertEquals(t1.getId(),1);
    assertEquals(t1.getEindDatum(),d1);
    
    //negatieve id
    t1 = new Todo(-1,"todo 1", d1);
    assertEquals(t1.getId(),-1);
    
    //titel is null of lege string
    assertThrows(NullPointerException.class, () -> { new Todo(1,null, d2); });
    assertThrows(IllegalArgumentException.class, () -> { new Todo(1,"", d2); });
    
  }
  
  @Test 
  public void ToDoTestEinddatum() throws IllegalArgumentException, NullPointerException, IllegalStateException, DatumVerledenException  {
    LocalDate d1 = LocalDate.now().minusWeeks(1); //verleden
    LocalDate d2 = LocalDate.now();               //vandaag
    LocalDate d3 = LocalDate.now().plusWeeks(1);  //toekomst
    t2 = new Todo(2,"todo 2", d2);
    t3 = new Todo(3,"todo 3", d3);
    assertTrue(t2.einddatumNogNietVerstreken(d2));
    assertTrue(t3.einddatumNogNietVerstreken(d3));
  
    //Dag in het verleden gekozen
    assertThrows(DatumVerledenException.class, () -> { new Todo(1,"Titel", d1); });
    
  }
  
  @Test
  public void afvinkenTest() throws ReedsAfgevinktException {
    assertEquals(t1.getAfgevinkt(),false);
    t1.vinkToDoAf();
    assertEquals(t1.getAfgevinkt(),true);
    assertThrows(ReedsAfgevinktException.class, () -> { t1.vinkToDoAf(); });
  }

 


}
