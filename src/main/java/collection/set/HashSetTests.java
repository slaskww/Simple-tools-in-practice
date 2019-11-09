package collection.set;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class HashSetTests {

    @Test
    public void shouldSetContainsTheSameElements(){

        /**
         * Kolekcja będąca zbiorem powinna zawierać unikalne elementy. Unikalność ta zostaje weryfikowana metodami hashcode() i equals() definiowanych w klasie elementu.
         * Jeśli set zawiera elementy modyfikowalne, to przy ich modyfikacji możemy doprowadzić do niespójności setu, co pokazano poniżej.
         * W teście tworzymy obiekt setu i wypełniamy go referencjami do obiektów Employee. Każdy z elementów różni unikalne nazwisko.
         * Kiedy zmodyfikujemy pole z wartością nazwiska w taki sposób, aby kazdy z elementów otrzymał tę samą wartość lastName, to każdy z elementów setu
         * wygeneruje identyczny hashcode.
         * Okaże się więc, że set przechowuje trzy identyczne elementy, co przeczy definicji zbioru.
         */

        HashSet<Employee> set = new HashSet<>(Arrays.asList(
                new Employee("Adam", "Niezgódka", 40),
                new Employee("Adam", "Malysz", 40),
                new Employee("Adam", "Wojcik", 40)
        ));

        System.out.println(set);
        assertEquals(3, set.size());


        for (Employee e :set){
            e.setLastName("Doe");
        }

        assertEquals(3, set.size());

        List<Integer> hashcodes = new ArrayList<>();
        for (Employee e : set){
            hashcodes.add(e.hashCode());
        }

        assertEquals(hashcodes.get(0), hashcodes.get(1));
        assertEquals(hashcodes.get(0), hashcodes.get(2));


        }

}
