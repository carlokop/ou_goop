package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;

import org.junit.Before;
import org.junit.Test;

import agenda.Item;
import agenda.Todo;
import exceptions.DatumVerledenException;

public class TestItem {
  
  private Item i1;
  private Item i2;
  private Item i3;
  private LocalDate d1 = LocalDate.of(2024, Month.JUNE, 14);
  private LocalDate d2 = LocalDate.of(2024, Month.AUGUST, 14);
  private LocalDate d3 = LocalDate.of(2024, Month.DECEMBER, 14);
  private LocalTime t1 = LocalTime.of(0, 0, 0);
  private LocalTime t2 = LocalTime.of(12, 0, 0);
  private LocalTime t3 = LocalTime.of(18, 0, 0);
  private LocalDateTime dt1 = LocalDateTime.of(d1,t1);
  private LocalDateTime dt2 = LocalDateTime.of(d2,t2);
  private LocalDateTime dt3 = LocalDateTime.of(d3,t3);
  
  @Test
  public void ItemHappyTest() throws IllegalStateException, DatumVerledenException {
    //Zelfde dag opvolgende tijd
    i1 = new Item(1,"Afspraak", d1, d2, t1, t2);
    assertEquals(i1.getTitel(),"Afspraak");
    assertEquals(i1.getId(),1);
    assertEquals(i1.getBeginDatum().toString(),"2024-06-14");
    assertEquals(i1.getEindDatum().toString(),"2024-08-14");
    
    
    //negatieve id
    i1 = new Item(-1,"Afspraak", d1, d2, t1, t2);
    assertEquals(i1.getId(),-1);
    
  }
  
  @Test
  public void ItemOngeldigeWaardeTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException {
    //titel is null of lege string
    assertThrows(NullPointerException.class, () -> { new Item(1,null, d1, d2, t1, t2); });
    assertThrows(IllegalArgumentException.class, () -> { new Item(1,"", d1, d2, t1, t2); });
    
    //Combi datum en tijd kunnen niet afzonderlijk worden gebruikt. Zorgt voor vreemde fouten
    
    LocalDateTime dt1 = LocalDateTime.now();                 //nu
    LocalDateTime dt2 = LocalDateTime.now().minusHours(4);   //4 uur geleden
    LocalDateTime dt3 = LocalDateTime.now().plusHours(1);    //+1 uur
    
    LocalDate d1 = dt1.toLocalDate();
    LocalDate d2 = dt2.toLocalDate();   
    LocalDate d3 = dt3.toLocalDate();  
    
    LocalTime t1 = dt1.toLocalTime();
    LocalTime t2 = dt2.toLocalTime();   
    LocalTime t3 = dt3.toLocalTime(); 
        
    //begindatum == einddatum && eindtijd == begintijd
    assertThrows(IllegalStateException.class, () -> { new Item(1,"Afspraak", d1, d1, t1, t1); });
    
    LocalDate d4 = LocalDate.of(2024, Month.MARCH, 10);
    LocalDate d5 = LocalDate.of(2024, Month.JUNE, 10);
    LocalDate d6 = LocalDate.of(2024, Month.DECEMBER, 10);
    LocalTime t4 = LocalTime.of(12, 0, 0);
    LocalTime t5 = LocalTime.of(6, 0, 0);
    LocalTime t7 = LocalTime.of(0, 0, 0);
    LocalDateTime dt4 = LocalDateTime.of(d1,t1);
    LocalDateTime dt5 = LocalDateTime.of(d1,t2);
    
    //begindatum verleden && begintijd < eindtijd
    assertThrows(DatumVerledenException.class, () -> { new Item(1,"Afspraak", d4, d5, t1, t2); });
    
    //begindatum vandaag && begintijd < nu
    assertThrows(DatumVerledenException.class, () -> { new Item(1,"Afspraak", d1, d1, t7, t3); });
    
    //begindatum > einddatum
    assertThrows(IllegalStateException.class, () -> { new Item(1,"Afspraak", d6, d5, t7, t3); });
    //begindatum == einddatum && eindtijd < begintijd
    assertThrows(IllegalStateException.class, () -> { new Item(1,"Afspraak", d6, d6, t5, t7); });
    
  }
  
  @Test 
  public void cloneTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException, CloneNotSupportedException {
    i1 = new Item(1,"Afspraak", d1, d2, t1, t2);
    Item clone = i1.clone();
    //alle attributen
    assertTrue(i1.getBeginDatum().equals(clone.getBeginDatum()));
    assertTrue(i1.getEindDatum().equals(clone.getEindDatum()));
    assertTrue(i1.getBeginTijd().equals(clone.getBeginTijd()));
    assertTrue(i1.getEindtijd().equals(clone.getEindtijd()));
    assertTrue(i1.getId() == (clone.getId()));
    assertTrue(i1.getTitel().equals(clone.getTitel()));
    
    //zelfde text
    assertTrue(i1.toString().equals(clone.toString()));
    
    //Test probleem met mili of nanoseconden die ontstaan bij LocalTime.now()
    LocalTime t1 = LocalTime.now();
    LocalDate d1 = LocalDate.now();
    i1 = new Item(1,"Afspraak", d1, d2, t1, t2);
    clone = i1.clone();
    //alle attributen
    assertTrue(i1.getBeginDatum().equals(clone.getBeginDatum()));
    assertTrue(i1.getEindDatum().equals(clone.getEindDatum()));
    assertTrue(i1.getBeginTijd().equals(clone.getBeginTijd()));
    assertTrue(i1.getEindtijd().equals(clone.getEindtijd()));    
  }

















} //class
