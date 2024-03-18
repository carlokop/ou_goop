package agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import exceptions.AgendaException;
/**
 * @author OU
 */



/* 
 * NB. DEZE FILE NIET AANPASSEN!
 * U dient dezelfde output in het console te krijgen als in de opdrachtomschrijving is getoond. 
 */
public class AgendaMain {
    public static void main(String[] args) throws AgendaException {
    	int id;
        List<Integer> ids;
        Agenda agenda = new Agenda();
        LocalDate d1 = LocalDate.of(2024, Month.MARCH, 21);
        LocalDate d2 = LocalDate.of(2024, Month.APRIL, 21);
        LocalDate d4 = LocalDate.of(2024, Month.AUGUST, 22);
        LocalDate d5 = LocalDate.of(2024, Month.DECEMBER, 31);
        LocalTime t1 = LocalTime.of(10, 0);
        LocalTime t2 = LocalTime.of(11, 0);
        LocalTime t5 = LocalTime.of(20, 0);
        LocalTime t6 = LocalTime.of(21, 0);
//        id = agenda.maakEenmaligeAfspraak("Afspraak1", d1, t5, t6);
//        System.out.println("Afspraak 1 heeft id " + id);//1
//        id = agenda.maakEenmaligeAfspraak("Afspraak2", d4, t5, t6);
//        System.out.println("Afspraak 2 heeft id " + id);//1
//        ids = agenda.maakPeriodiekeAfspraak("Afspraak3", d1, d2, t1, t2, Frequentie.WEKELIJKS);
//        System.out.print("Periodieke afspraak  genereert afspraken met id's ");
//        for (int i = 0; i < ids.size() - 2; i++) {
//            System.out.print("" + ids.get(i) + ",");
//        }
//        System.out.println(ids.get(ids.size() - 1));
        id = agenda.maakToDo("Todo1", d1);
        System.out.println("Todo 1 heeft id " + id); // 15
        id = agenda.maakToDo("Todo2", d2);
        System.out.println("Todo 2 heeft id " + id); //16
        id = agenda.maakToDo("Todo3", d5);
        System.out.println("Todo 3 heeft id " + id); //17
        System.out.println();
        agenda.vinkToDoAf(8);
        agenda.vinkToDoAf(10);
        System.out.println("** OPEN TODOs op " + d2);
//        List<ToDo> todos = agenda.getToDos(d2, false);
//        for (ToDo todo : todos) {
//            System.out.println(todo);
//        }
//        System.out.println();
//        System.out.println("** ALLE Items TUSSEN " + d2 + " en " + d5);
//        List<Item> items = agenda.getItems(d2, d5);
//        for (Item item : items) {
//            System.out.println(item);
//        }
//        System.out.println();
//        System.out.println("Item met id = 5");
//        System.out.println(agenda.getItem(5));
    }

}
