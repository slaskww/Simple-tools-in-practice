package collection.queue;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTests {

    @Test
    public void shouldAddElemAtTheBeginningOfTheQueue(){

        /**
         * ArrayDeque (Double-ended queue) jest kolekcją podwójną, co oznacza, że operacje dodawania, pobierania i usuwania elementów sa możliwe z obu jej końców
         */

        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addFirst("Ala");
        deque.addFirst("ma");
        deque.addFirst("psa");
        String firstElem = deque.getFirst();
        assertEquals("psa", firstElem);

    }

    @Test
    public void shouldAddElemAtTheEndOfTheQueue(){

        /**
         * ArrayDeque (Double-ended queue) jest kolekcją podwójną, co oznacza, że operacje dodawania, pobierania i usuwania elementów sa możliwe z obu jej końców
         */

        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addLast("Ala");
        deque.addLast("ma");
        deque.addLast("psa");
        String lastElem = deque.getLast();
        assertEquals("psa", lastElem);

    }


    @Test
    public void shouldRemoveElemFromThBeginningOfTheQueue(){

        /**
         * ArrayDeque jest kolekcją podwójną, co oznacza, że operacje dodawania, pobierania i usuwania elementów sa możliwe z obu jej końców
         */

        ArrayDeque<String> deque = new ArrayDeque<>(Arrays.asList("Ala", "ma", "komputer"));

        String firstElem = deque.removeFirst();
        assertEquals("Ala", firstElem);

    }


    @Test
    public void shouldRemoveElemFromTheEndOfTheQueue(){

        /**
         * ArrayDeque jest kolekcją podwójną, co oznacza, że operacje dodawania, pobierania i usuwania elementów sa możliwe z obu jej końców
         */

        ArrayDeque<String> deque = new ArrayDeque<>(Arrays.asList("Ala", "ma", "komputer"));

        String lastElem = deque.removeLast();
        assertEquals("komputer", lastElem);

    }

}
