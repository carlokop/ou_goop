package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import org.junit.Test;

import agenda.Afspraak;
import agenda.AgendaException;

/**
 * Tests de Afspraak klasse
 * @author carlo
 *
 */
public class TestAfspraak {
  
  private Afspraak i1;
  private LocalDate d1 = LocalDate.of(2024, Month.JUNE, 14);
  private LocalTime t1 = LocalTime.of(0, 0, 0);
  private LocalTime t2 = LocalTime.of(12, 0, 0);
  
  @Test
  public void AfspraakTest() throws AgendaException, DateTimeException {
    //happy
    i1 = new Afspraak(1,"Afspraak", d1, t1, t2);
    assertEquals("Afspraak",i1.getTitel());
    assertEquals(1,i1.getId());
    assertEquals("2024-06-14",i1.getDatum().toString());
    //en de attributen van alleen de Afspraak klasse
    assertEquals("00:00",i1.getBeginTijd().toString());
    assertEquals("12:00",i1.getEindtijd().toString());
       
    //titel is null of lege string
    assertThrows(AgendaException.class, () -> { new Afspraak(1,null, d1, t1, t2); });
    assertThrows(AgendaException.class, () -> { new Afspraak(1,"", d1, t1, t2); });
    
    //negatieve id of id = 0
    assertThrows(AgendaException.class, () -> { new Afspraak(-1,"Afspraak", d1, t1, t2); });
    assertThrows(AgendaException.class, () -> { new Afspraak(0,"Afspraak", d1, t1, t2); });
    
    //datum reeds verstreken
    LocalDate vertrekendatum = LocalDate.of(2023, Month.JUNE, 14);
    assertThrows(DateTimeException.class, () -> { new Afspraak(1,"Afspraak", vertrekendatum, t1, t2); });
    
    //begintijd verstreken
    LocalDate vandaag = LocalDate.now();
    LocalTime tweeminutengeleden = LocalTime.now().minusMinutes(2);
    LocalTime overeenuur = LocalTime.now().plusHours(1);
    assertThrows(DateTimeException.class, () -> { new Afspraak(1,"Afspraak", vandaag, tweeminutengeleden, overeenuur); });
    
    //Eindtijd voor de begintijd
    LocalTime tijdnu = LocalTime.now();
    assertThrows(DateTimeException.class, () -> { new Afspraak(1,"Afspraak", d1, overeenuur, tijdnu); });
    
    //begin en eindtijd zijn gelijk
    assertThrows(DateTimeException.class, () -> { new Afspraak(1,"Afspraak", d1, overeenuur, overeenuur); });
    
 
  }
  
  
  @Test 
  public void cloneTest() throws AgendaException, DateTimeException {
    //origineel
    i1 = new Afspraak(1,"Afspraak", d1, t1, t2);
    assertEquals("Afspraak",i1.getTitel());
    assertEquals(1,i1.getId());
    assertEquals("2024-06-14",i1.getDatum().toString());
    //en de attributen van alleen de Afspraak klasse
    assertEquals("00:00",i1.getBeginTijd().toString());
    assertEquals("12:00",i1.getEindtijd().toString());
    
    //maak diepe kloon
    Afspraak clone = i1.clone();
    assertEquals("Afspraak",clone.getTitel());
    assertEquals(1,clone.getId());
    assertEquals("2024-06-14",clone.getDatum().toString());
    //en de attributen van alleen de Afspraak klasse
    assertEquals("00:00",clone.getBeginTijd().toString());
    assertEquals("12:00",clone.getEindtijd().toString());
    
    //orig en clone zijn niet hetzelfde object
    assertNotEquals(clone,i1);
    
  }
  
  
  




} //class
