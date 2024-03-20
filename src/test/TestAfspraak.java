package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import org.junit.Test;

import agenda.Afspraak;
import agenda.exceptions.DatumVerledenException;

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
  public void ItemHappyTest() throws IllegalStateException, DatumVerledenException {
    //Zelfde dag opvolgende tijd
    i1 = new Afspraak(1,"Afspraak", d1, t1, t2);
    assertEquals(i1.getTitel(),"Afspraak");
    assertEquals(i1.getId(),1);
    assertEquals(i1.getDatum().toString(),"2024-06-14");
    
    //negatieve id of id = 0
    assertThrows(IllegalArgumentException.class, () -> { new Afspraak(-1,"Afspraak", d1, t1, t2); });
    assertThrows(IllegalArgumentException.class, () -> { new Afspraak(0,"Afspraak", d1, t1, t2); });
    
  }
  
  @Test
  public void ItemOngeldigeWaardeTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException {
    //titel is null of lege string
    assertThrows(NullPointerException.class, () -> { new Afspraak(1,null, d1, t1, t2); });
    assertThrows(IllegalArgumentException.class, () -> { new Afspraak(1,"", d1, t1, t2); });
    
    //Combi datum en tijd kunnen niet afzonderlijk worden gebruikt. Zorgt voor vreemde fouten
    
    LocalDateTime dt1 = LocalDateTime.now();                 //nu
    LocalDateTime dt2 = LocalDateTime.now().minusHours(4);   //4 uur geleden
    LocalDateTime dt3 = LocalDateTime.now().plusHours(1);    //+1 uur
    
    LocalDate d1 = dt1.toLocalDate();
    
    LocalTime t1 = dt1.toLocalTime();
    LocalTime t2 = dt2.toLocalTime();   
    LocalTime t3 = dt3.toLocalTime(); 
        
    //begindatum == einddatum && eindtijd == begintijd
    assertThrows(IllegalStateException.class, () -> { new Afspraak(1,"Afspraak", d1, t1, t1); });
    
    LocalDate d4 = LocalDate.of(2024, Month.MARCH, 10);
    LocalDate d6 = LocalDate.of(2024, Month.DECEMBER, 10);
    LocalTime t5 = LocalTime.of(6, 0, 0);
    LocalTime t7 = LocalTime.of(0, 0, 0);
    
    //begindatum verleden && begintijd < eindtijd
    assertThrows(DatumVerledenException.class, () -> { new Afspraak(1,"Afspraak", d4, t1, t2); });
    
    //begindatum vandaag && begintijd < nu
    assertThrows(DatumVerledenException.class, () -> { new Afspraak(1,"Afspraak", d1, t7, t3); });

    //eindtijd < begintijd
    assertThrows(IllegalStateException.class, () -> { new Afspraak(1,"Afspraak", d6, t5, t7); });
    
  }
  
  @Test 
  public void cloneTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException, CloneNotSupportedException {
    i1 = new Afspraak(1,"Afspraak", d1, t1, t2);
    Afspraak clone = i1.clone();
    //alle attributen
    assertTrue(i1.getDatum().equals(clone.getDatum()));
    assertTrue(i1.getBeginTijd().equals(clone.getBeginTijd()));
    assertTrue(i1.getEindtijd().equals(clone.getEindtijd()));
    assertTrue(i1.getId() == (clone.getId()));
    assertTrue(i1.getTitel().equals(clone.getTitel()));
    //zelfde text
    assertTrue(i1.toString().equals(clone.toString()));
    
    //Test probleem met mili of nanoseconden die ontstaan bij LocalTime.now()
    LocalTime t1 = LocalTime.now();
    LocalTime t2 = LocalTime.now().plusHours(1);
    LocalDate d1 = LocalDate.now();
    i1 = new Afspraak(1,"Afspraak", d1, t1, t2);
    clone = i1.clone();
    //alle attributen
    assertTrue(i1.getDatum().equals(clone.getDatum()));
    assertTrue(i1.getBeginTijd().equals(clone.getBeginTijd()));
    assertTrue(i1.getEindtijd().equals(clone.getEindtijd()));    
  }
  
  @Test
  public void maakAfgerondeDateTimeTest() {
    //correct afronden tijd Item.maakAfgerondeDateTime(LocalDateTime)
    LocalDate date = LocalDate.of(2024, Month.APRIL, 1);
    LocalTime time = LocalTime.of(12, 04, 10, 5); //12:04 en 10sec en 5 nanoseconde
    LocalTime time2 = LocalTime.of(12, 04); //12:04 zonder seconden en nanoseconden
    LocalDateTime dt2 = LocalDateTime.of(date,time2);
    LocalDateTime dt3 = Afspraak.maakAfgerondeDateTime(dt2);
    LocalDateTime dt4 = Afspraak.maakAfgerondeDateTime(dt2);
    assertEquals(dt2,dt3);
    assertEquals(dt2,dt4);
    assertEquals(dt3,dt4);
    
    //correct afronden tijd Item.maakAfgerondeDateTime(LocalDate, LocalTime)
    LocalDateTime dt5 = Afspraak.maakAfgerondeDateTime(date, time);
    LocalDateTime dt6 = Afspraak.maakAfgerondeDateTime(date, time2);
    assertEquals(dt2,dt5);
    assertEquals(dt3,dt5);
    assertEquals(dt3,dt6);
    assertEquals(dt5,dt6);
  }

















} //class
