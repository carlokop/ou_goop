package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.Test;

import agenda.Item;
import agenda.PeriodiekItem;
import exceptions.DatumVerledenException;
import exceptions.NietUniekeIdException;

public class TestPeriodiekItem {

  private PeriodiekItem p1;
  private PeriodiekItem p2;
  private PeriodiekItem p3;
  private LocalDate d1 = LocalDate.of(2024, Month.JUNE, 14);
  private LocalDate d2 = LocalDate.of(2024, Month.AUGUST, 14);
  private LocalDate d3 = LocalDate.of(2024, Month.DECEMBER, 14);
  private LocalTime t1 = LocalTime.of(0, 0, 0);
  private LocalTime t2 = LocalTime.of(12, 0, 0);
  private LocalTime t3 = LocalTime.of(18, 0, 0);
  private LocalDateTime dt1 = LocalDateTime.of(d1,t1);
  private LocalDateTime dt2 = LocalDateTime.of(d2,t2);
  private LocalDateTime dt3 = LocalDateTime.of(d3,t3);
  
  /*
   * Beste docent:
   * Ik wist niet zeker of ik alle testen die ik reeds in de supereklasse moest doen nogmaals moest herhalen
   * Dit lijkt mij overbodig maar ik heb in iedergeval de volgende twee testmethodes nagenoeg gekopieerd uit de testklasse van de Item klasse
   */
  
  @Test
  public void ItemHappyTestSuper() throws IllegalStateException, DatumVerledenException, NullPointerException, IllegalArgumentException, NietUniekeIdException {
    //Zelfde dag opvolgende tijd
    p1 = new PeriodiekItem(1,"Afspraak", d1, d2, t1, t2, 999);
    assertEquals(p1.getTitel(),"Afspraak");
    assertEquals(p1.getId(),1);
    assertEquals(p1.getBeginDatum().toString(),"2024-06-14");
    assertEquals(p1.getEindDatum().toString(),"2024-08-14");
    
    //negatieve id
    assertThrows(IllegalArgumentException.class, () -> { new PeriodiekItem(-1,"Afspraak", d1, d2, t1, t2,999); });
    
  }
  
  @Test
  public void PeriodiekItemOngeldigeWaardeTestSuper() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException {
    //titel is null of lege string
    assertThrows(NullPointerException.class, () -> { new PeriodiekItem(1,null, d1, d2, t1, t2, 999); });
    assertThrows(IllegalArgumentException.class, () -> { new PeriodiekItem(1,"", d1, d2, t1, t2, 999); });
    
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
    assertThrows(IllegalStateException.class, () -> { new PeriodiekItem(1,"Afspraak", d1, d1, t1, t1, 999); });
    
    LocalDate d4 = LocalDate.of(2024, Month.MARCH, 10);
    LocalDate d5 = LocalDate.of(2024, Month.JUNE, 10);
    LocalDate d6 = LocalDate.of(2024, Month.DECEMBER, 10);
    LocalTime t4 = LocalTime.of(12, 0, 0);
    LocalTime t5 = LocalTime.of(6, 0, 0);
    LocalTime t7 = LocalTime.of(0, 0, 0);
    LocalDateTime dt4 = LocalDateTime.of(d1,t1);
    LocalDateTime dt5 = LocalDateTime.of(d1,t2);
    
    //begindatum verleden && begintijd < eindtijd
    assertThrows(DatumVerledenException.class, () -> { new PeriodiekItem(1,"Afspraak", d4, d5, t1, t2, 999); });
    
    //begindatum vandaag && begintijd < nu
    assertThrows(DatumVerledenException.class, () -> { new PeriodiekItem(1,"Afspraak", d1, d1, t7, t3, 999); });
    
    //begindatum > einddatum
    assertThrows(IllegalStateException.class, () -> { new PeriodiekItem(1,"Afspraak", d6, d5, t7, t3, 999); });
    //begindatum == einddatum && eindtijd < begintijd
    assertThrows(IllegalStateException.class, () -> { new PeriodiekItem(1,"Afspraak", d6, d6, t5, t7, 999); });
    
  }
  
  @Test
  public void ItemHappyTest() throws IllegalStateException, DatumVerledenException, NullPointerException, IllegalArgumentException, NietUniekeIdException {
    //Zelfde dag opvolgende tijd
    p1 = new PeriodiekItem(1,"Afspraak", d1, d2, t1, t2, 999);
    assertEquals(p1.getPeriodiekItemId(),999);    
  }
  
  @Test
  public void PeriodiekItemOngeldigeWaardeTest() {
    //periodiekId == id
    assertThrows(NietUniekeIdException.class, () -> { new PeriodiekItem(1,"Afspraak", d2, d3, t1, t2, 1); });
  }
  
  @Test 
  public void cloneTest() throws NullPointerException, IllegalArgumentException, IllegalStateException, DatumVerledenException, CloneNotSupportedException, NietUniekeIdException {
    /*
     * deze testen staan ook in de superklasse
    */
    
    p1 = new PeriodiekItem(1,"Afspraak", d1, d2, t1, t2,99);
    PeriodiekItem clone = p1.clone();
    //alle attributen
    assertTrue(p1.getBeginDatum().equals(clone.getBeginDatum()));
    assertTrue(p1.getEindDatum().equals(clone.getEindDatum()));
    assertTrue(p1.getBeginTijd().equals(clone.getBeginTijd()));
    assertTrue(p1.getEindtijd().equals(clone.getEindtijd()));
    assertTrue(p1.getId() == (clone.getId()));
    assertTrue(p1.getTitel().equals(clone.getTitel()));
    
    //zelfde text
    assertTrue(p1.toString().equals(clone.toString()));
    
    //Test probleem met mili of nanoseconden die ontstaan bij LocalTime.now()
    LocalTime t1 = LocalTime.now();
    LocalDate d1 = LocalDate.now();
    p1 = new PeriodiekItem(1,"Afspraak", d1, d2, t1, t2, 999);
    clone = p1.clone();
    //alle attributen
    assertTrue(p1.getBeginDatum().equals(clone.getBeginDatum()));
    assertTrue(p1.getEindDatum().equals(clone.getEindDatum()));
    assertTrue(p1.getBeginTijd().equals(clone.getBeginTijd()));
    assertTrue(p1.getEindtijd().equals(clone.getEindtijd())); 
    
    /**
     * Deze testen staan niet in de superklasse
     */
    assertTrue(p1.getPeriodiekItemId() == clone.getPeriodiekItemId());
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

}
