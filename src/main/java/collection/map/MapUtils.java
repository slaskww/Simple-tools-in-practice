package collection.map;

import java.util.Map;

public class MapUtils {

    public static <K, V> Map<K, V> fill(Map<K, V> map, Object...pairs){
        validate(pairs);

        for (int i = 0; i < pairs.length; i+=2 ){
            map.put((K) pairs[i], (V) pairs[i+1]);
        }

        return map;
    }

    private static void validate( Object...pairs){

        if (pairs.length % 2 != 0) throw new IllegalArgumentException();
        for (int i = 0; i<pairs.length - 2; i++){
            if (pairs[i].getClass() != pairs[i+2].getClass()) throw new IllegalArgumentException();
        }
    }
}
