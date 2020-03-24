package collection;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleCollectionTests {

    @Test
    public void shouldConvertListToSet(){

        /**
         * Konwersja kolekcji jednego typu na drugi.
         * Kazda szczegółowa implementacja interfejsu Collection (ArrayList, LinkedList, HashSet, itd) posiada konstruktor, który przyjmuje parametr typu Collection.
         * Stąd też z każdej kolekcji implementującej Collection możemy otrzymac inną kolekcję która implementuje ten interfejs.
         * Test sprawdza, czy uda się przekształcić listę na zbiór.
         * Mając na uwadze, że zbiór posiada elementy unikalne, po konwersji z listy będzie zawierał elementy: 1, 3 i 5. Metoda size() wywołana na obiekcie zbioru
         * powinna więc zwrócić wartość 3.
         */

        List<Integer> list = Arrays.asList(1, 3, 3, 3, 5);

        Set<Integer> set = new HashSet<>(list);
        assertThat(set.size(), is(3));
        assertTrue(set.containsAll(list));
    }

    @Test
    public void shouldConvertSetToDeque(){

        /**
         * Konwersja kolekcji jednego typu na drugi.
         * Kazda szczegółowa implementacja interfejsu Collection (ArrayList, LinkedList, HashSet, itd) posiada konstruktor, który przyjmuje parametr typu Collection.
         * Stąd też z każdej kolekcji implementującej Collection możemy otrzymac inną kolekcję która implementuje ten interfejs.
         * Test sprawdza, czy uda się przekształcić zbiór na kolejkę.
         * Metoda size() wywołana na obiekcie kolekji powinna więc zwrócić wartość 4.
         */

        Set<String> set = new HashSet<>(Arrays.asList("Gdansk", "Warsaw", "Cracow", "Wroclaw"));
        Queue<String> queue = new ArrayDeque<>(set);
        assertEquals(4, queue.size());
    }
}
