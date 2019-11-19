package io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa StreamTokenizer umożliwia analizę składniową strumieni tekstowych.
 * Pozwala wyłuskać ze strumienia liczby (ciągi cyfr z ewent. znakiem - i kropką dziesiętną),
 * słowa (ciągi znaków zawierających symbole alfanumeryczne, ale nie będące samymi cyframi, np. Ala, ala22)
 * i znaki niewchodzące w skład liczb i słów (#, %, *, @, +, (, ), ').
 * W analizie pomijane są białe znaki (spacje, tabulatory), komentarze w stylu javy (//komentarz)
 * oraz literały znakowe ('c') i napisowe ("literał znakowy").
 * Do rozbioru podanego tekstu wykorzystywana jest metoda nextToken(), zwracająca liczbę całkowitą, która odzwierciedla
 * typ i wartość zwracanego symbolu (w klasie StreamTokenizer wartość ta znajduje się pod zmienną ttype). Tak więc metoda nextToken() zwraca
 * tak na prawdę wartość zmiennej ttype. Jeśli wartość ttype = -3 (= TT_WORD), to symbol jest wyrazem,
 * jeśli ttype = -2 (=TT_NUMBER), to symbol jest liczbą, jeśli ttype = -1 (=TT_EOF), to mamy do czynienia z końcem wejścia.
 * Jeśli symbol jest liczbą, to jego wartość możemy odczytać odnosząc się do zmiennej nval obiektu StreamTokenizer.
 * Jeśli natomiast symbol jest wyrazem, to jego wartość odczytujemy ze zmiennej sval obiektu StreamTokenizer.
 * Jeśli symbol nie jest ani liczbą ani wyrazem, to jest zwykłym znakiem, którego kod jest równy wartości ttype.
 */

public class StreamTokenizerInUse {

    public static void main(String[] args) throws IOException {

        String source = String.join("\n", "The 44%%StreamTokenizer $class ", "takes 55 an%input stream and parses it into 66", "//tokens", "allowing.the 77 tokens ", "to be#read one at a@time. 88");


        System.out.println("source:\n" + source);

        Reader reader = new StringReader(source); //strumień znakowy, który z łańcucha znaków zwraca strumień kodów znaków.

        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);

        List<String> words = new ArrayList<>();
        List<Double> numbers = new ArrayList<>();
        List<Character> chars = new ArrayList<>();

        int token;

        while ((token = streamTokenizer.nextToken()) != StreamTokenizer.TT_EOF){ //TT_EOF to stała równa -1, nextToken zwraca wartość zmiennej ttype

            switch(token){
                case StreamTokenizer.TT_NUMBER : numbers.add(streamTokenizer.nval); break; //jeśli wartość tokena jest równa -2, to wyłuskujemy ze zmiennej nval wartość będącą numerem
                case StreamTokenizer.TT_WORD : words.add(streamTokenizer.sval); break; //jeśli wartość tokena jest równa -3, to wyłuskujemy ze zmiennej sval wartość będącą wyrazem
                default: chars.add((char) streamTokenizer.ttype); //w przeciwnym wypadku mamy do czynienia ze znakiem, którego wartość kryje się w zmiennej ttype (oraz naszej zmiennej token).
            }
        }

        System.out.println("Words:\n" + words);
        System.out.println("Numbers :\n" + numbers);
        System.out.println("Characters:\n" + chars);

    }


}
