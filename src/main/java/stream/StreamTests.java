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
         * przyjmując, że sepratorem będzie pojedynczy jeden lub wiele znaków niebędących liczbą("\\D"+).
         * Następnie z podłańcuchów tworzy strumień (obiekt klasy Stream<String>) .
         */

        String source = "Ala ma 44królikow, 33. zające i 23 fredki. Ile ma łączn10e zwierząt?";

        String regex = "\\D+"; //sepratorem będzie jeden lub wiele znaków niebędących liczbą
        Pattern pattern = Pattern.compile(regex);
        Stream<String> splittedStrings = pattern.splitAsStream(source);

        int sumOfAnimals = splittedStrings.filter(s -> !s.equals(""))
                .mapToInt(s -> Integer.parseInt(s))
                .sum();

        assertEquals(110, sumOfAnimals);
    }
}
