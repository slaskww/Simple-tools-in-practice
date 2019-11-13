package stream;

import org.junit.Test;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamTests {

    @Test
    public void shouldSplitStringAndConvertIntoStreamAndSum(){

        /**
         * Metoda splitToStream() dzieli dany łańcuch znaków na podłańcuchy znaków,
         * przyjmując, że sepratorem będzie pojedynczy jeden lub wiele znaków niebędący liczbą("\\D"+).
         * Następnie z podłańcuchów tworzy strumień (obiekt klasy Stream<String>) .
         */

        String source = "Ala ma 44 królikow, 33 zające i 23 fredki. Ile ma łącznie zwierząt?";

        String regex = "\\D+";
        Pattern pattern = Pattern.compile(regex);
        Stream<String> splittedStrings = pattern.splitAsStream(source);

        int sumOfAnimals = splittedStrings.filter(s -> !s.equals(""))
                .mapToInt(s -> Integer.parseInt(s))
                .sum();

        assertEquals(100, sumOfAnimals);
    }
}
