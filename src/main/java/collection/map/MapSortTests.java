package collection.map;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MapSortTests {

    @Test
    public void shouldSortHashMapByKeyUsingTreeMap(){

        /**
         * Chcac posortować mapę, możemy użyć mapy TreeMap. Wiąże się to jednak z pewnym obostrzeniem.
         * TreeMapy możemy użyć, jeśli chcemy posortować elementy po wartościach ich kluczy - wg. porządku naturalnego.
         * Jeśli zechcemy jednak sortować mapę po dowolnym kryterium, wtedy TreeMap nie jest dobrym rozwiązaniem.
         * W teście sortujemy mapę wg. wartości jej kluczy. Sortowanie wg. porządku naturalnego zakończy się sukcesem.
         */

        Map<String, Integer> map = new HashMap<>();
        MapUtils.fill(map, "kot", 7, "pies",14, "mysz", 3, "słoń", 75 );

        Map<String, Integer> tmap = new TreeMap<>();
        tmap.putAll(map);
        assertEquals(4, tmap.size());
    }

    @Test
    public void shouldNotSortHashMapByKeyLengthUsingTreeMap(){

        /**
         * Chcac posortować mapę, możemy użyć mapy TreeMap. Wiąże się to jednak z pewnym obostrzeniem.
         * TreeMapy możemy użyć, jeśli chcemy posortować elementy po wartościach ich kluczy - wg. porządku naturalnego.
         * Jeśli zechcemy jednak sortować mapę po dowolnym kryterium, wtedy TreeMap nie jest dobrym rozwiązaniem.
         * W teście sortujemy mapę wg. długości jej kluczy.
         * W konsekwencji komparator traktuje dwa klucze o tej samej długości jako identyczne i nie pozwala wstawić takich 'duplikatów'.
         * Ostatecznie TreeMap będzie zawierała jedynie dwa odwzorowania. Dwa zostaną wyeliminowane na etapie porównania.
         */

        Map<String, Integer> map = new HashMap<>();
        MapUtils.fill(map, "kot", 7, "pies",14, "mysz", 3, "słoń", 75 );

        Map<String, Integer> tmap = new TreeMap<>((s, t1) -> s.length() - t1.length());
        tmap.putAll(map);

        assertNotEquals(4, tmap.size());
        assertEquals(2, tmap.size());
    }


    @Test
    public void shouldSortHashMapByValuesUsingList(){

        /**
         * Chcąc posortować mapę po dowolnym kryterium, należy stworzyc lisę z widoku wejść tej mapy (Map.Entry<K,V>), a następnie sortować listę po dowolnym kryterium.
         * Map.Entry posiada kilka metod statycznych zwracających komparatory:
         * comparingByKey()
         * comparingByKey(Comparator)
         * comparingByValue()
         * comparingByValue(Comparator)
         * Na końcu dodajemy do LinkedHashMap zawartość posrtowanej listy
         */

        Map<String, Integer> map = new LinkedHashMap<>();
        MapUtils.fill(map, "kot", 7, "pies",14, "mysz", 3, "słoń", 75 );

        List<Map.Entry<String, Integer>> mlist = new ArrayList<>(map.entrySet());
        mlist.sort(Map.Entry.comparingByValue());
        map.clear();
        mlist.forEach(stringIntegerEntry -> map.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));

        System.out.println(map);
        assertEquals(4, map.size());
        assertEquals("{mysz=3, kot=7, pies=14, słoń=75}", map.toString());
    }
}
