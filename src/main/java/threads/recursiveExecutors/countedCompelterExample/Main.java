package threads.recursiveExecutors.countedCompelterExample;

import java.io.File;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        File file = new File("C:/Users/CP24/Desktop/SimpleToolsInPractice");
        ProcessDirectory processDirectory = new ProcessDirectory(null, file);
        processDirectory.invoke();
        Map<String, Integer> result = processDirectory.getRawResult();
        result.entrySet().stream().forEach(stringIntegerEntry -> System.out.println("key=" + stringIntegerEntry.getKey() + ", value=" + stringIntegerEntry.getValue()));
    }
}
