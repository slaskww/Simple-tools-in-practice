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

        String source = "0 The9string 8 tokenizer\nclass 44 allows.an application+to.break a 11string into tokens.";
        String regex = "[0-9\\n\\. +]"; //w nawiasach [] definiujemy klasę znaków - szukamy wystąpnienia każdego z tych znaków (cyfry z zakresu 0-9, znak \n , ., spacji i +)

        String reg = "\\D+"; //separatorem będzie jeden lub wiele znaków niebędących cyfrą

        List<String> res1 = tokenizuj(source, regex);
        List<String> res2 = tokenizuj(source, reg);
        String resAsString1 = String.join("-", res1);
        String resAsString2 = String.join(", ", res2);
        System.out.println(resAsString1);
        System.out.println(resAsString2);
    }
}
