package parsing.stringSplit.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Metoda split() z klasy String korzysta 'pod spodem' z metody split() zdefiniowanej w klasie Pattern
 */

public class StrSplit {

    public static List<String> tokenizuj(String source, String regex){

        List<String> result = new ArrayList<>();
        result = Arrays.asList(source.split(regex));
        return result;

    }

    public static void main(String[] args) {

        String source = "The9string tokenizer\nclass allows.an application+to.break a string into tokens.";
        String regex = "[0-9\\n\\. +]"; //w nawiasach [] definiujemy klasę znaków - szukamy wystąpnienia każdego z tych znaków (cyfry z zakresu 0-9, znak \n , ., spacji i +)

        List<String> res = tokenizuj(source, regex);
        String resAsString = String.join("-", res);
        System.out.println(resAsString);
    }
}
