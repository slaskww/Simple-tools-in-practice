package threads.recursiveExecutors.countedCompelterExample;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaKeyWords {

    public static final Map<String, Integer> map = new HashMap<>();

    static{
        List<String> words = Arrays.asList(
                "abstract", "boolean", "catch", "default", "else", "finally",
                "if", "long", "new", "private", "return", "static", "this", "void"
        );
       words.forEach(s -> map.put(s, 0));
    }
}
