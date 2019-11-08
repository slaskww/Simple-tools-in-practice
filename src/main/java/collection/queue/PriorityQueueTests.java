package collection.queue;

import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.Assert.*;

public class PriorityQueueTests {

    @Test
    public void shouldPeekLeastValueOfElem(){

        /**
         * PriorityQueue jest kolejką, której metoda peek() pobiera element z początku, ale początek okreslany jest przez kryteria porównywania obiektów
         * W tym wypadku pierwszym pobranym elementem będzie element o najniższej wartości liczbowej
         */

        PriorityQueue<Integer> queue = new PriorityQueue<>(Arrays.asList(34, 56, 78, 9, 44, 11, 23));
        int initialSize = queue.size();
        int leastValue = queue.peek();
        int sizeAfterPeek = queue.size();

        assertEquals(9, leastValue);
        assertEquals(initialSize, sizeAfterPeek);

    }

    @Test
    public void shouldPollLeastValueOfElem(){

        /**
         * PriorityQueue jest kolejką, której metoda poll() pobiera i usuwa element z początku, ale początek okreslany jest przez kryteria porównywania obiektów
         * W tym wypadku pierwszym usunietym elementem będzie element o najniższej wartości liczbowej
         */

        PriorityQueue<Integer> queue = new PriorityQueue<>(Arrays.asList(34, 56, 78, 9, 44, 11, 23));
        int initialSize = queue.size();
        int leastValue = queue.poll();
        int sizeAfterPoll = queue.size();
        assertEquals(9, leastValue);
        assertNotEquals(initialSize, sizeAfterPoll);

    }


    @Test
    public void shouldRemoveLeastValueOfElem(){

        /**
         * PriorityQueue jest kolejką, której metoda remove() pobiera i usuwa element z początku, ale początek okreslany jest przez kryteria porównywania obiektów
         * W tym wypadku pierwszym usunietym elementem będzie element o najniższej wartości liczbowej
         */

        PriorityQueue<Integer> queue = new PriorityQueue<>(Arrays.asList(34, 569, 78, 9, 44, 11, 23));
        int initialSize = queue.size();
        int leastValue = queue.remove();
        int sizeAfterRemove = queue.size();
        assertEquals(9, leastValue);
        assertNotEquals(initialSize, sizeAfterRemove);

    }
}
