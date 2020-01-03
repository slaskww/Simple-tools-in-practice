package threads.recursiveExecutors.countedCompelterExample;

import java.io.File;
import java.util.Map;

/**
 * (3)
 */

public class Main {

    public static void main(String[] args) {

        String path = "C:/Users/CP24/Desktop/SimpleToolsInPractice";

        File file = new File(path);
        ProcessDirectory processDirectory = new ProcessDirectory(null, file);
        processDirectory.invoke();
        Map<String, Integer> result = processDirectory.getRawResult();
        result.entrySet().stream().forEach(stringIntegerEntry -> System.out.println("key=" + stringIntegerEntry.getKey() + ", value=" + stringIntegerEntry.getValue()));
    }
}
