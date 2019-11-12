package collection.map;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class HashMapTests {

    @Test
    public void shouldKeySetViewRemoveOriginalValue(){

        /**
         * Korzystając z metody keySet() pobieramy widok na zbiór kluczy.
         * Wykonując operacje na widokach, wykonujemy operacje na oryginalnej mapie, co pokazuje test
         */

        Map<Integer, String> map = new HashMap<>();
        map.put(22, "dwa-dwa");
        map.put(33, "trzy-trzy");
        map.put(44, "cztery-cztery");
        Set<Integer> keys = map.keySet();

        keys.remove(22);

        assertFalse(map.containsKey(22));
    }

    @Test
    public void shouldValueCollectionViewRemoveOriginalValue(){

        /**
         * Korzystając z metody values() pobieramy widok na kolekcję wartości.
         * Wykonując operacje na widokach, wykonujemy operacje na oryginalnej mapie, co pokazuje test
         */

        Map<Integer, String> map = new HashMap<>();
        map.put(22, "dwa-dwa");
        map.put(33, "trzy-trzy");
        map.put(44, "cztery-cztery");
        Collection<String> values = map.values();

        values.remove("dwa-dwa");
        assertFalse(map.containsValue("dwa-dwa"));
    }

    @Test
    public void shouldViewRemoveOriginalValue(){

        /**
         * Korzystając z metody entrySet() pobieramy widok na zbiór par klucz-wartość.
         * Wykonując operacje na widokach, wykonujemy operacje na oryginalnej mapie, co pokazuje test
         */

        Map<Integer, String> map = new HashMap<>();
        map.put(22, "dwa-dwa");
        map.put(33, "trzy-trzy");
        map.put(44, "cztery-cztery");
        Set<Map.Entry<Integer, String>> entry = map.entrySet();
        entry.removeIf(integerStringEntry -> integerStringEntry.getKey() == 22);

        assertFalse(map.containsKey(22));

        //int count = Collections.frequency(map.values(), "trzy-trzy");
        //assertEquals(1, count);

    }

}
