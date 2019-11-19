package parsing.stringTokenizer.example;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * StringTokenizer is a legacy class that is retained for compatibility reasons ALTHOUGH ITS USE IS DISCOURAGE IN NEW CODE.
 * It is RECOMMENDED that anyone seeking this functionality use the SPLIT METHOD OF SPRING or the JAVA.UTIL.REGEX package instead.
 * Tak więc używanie obiektów klasy StringTokenizer nie jest rekomendowane dla nowego kodu.
 * Należy używać metody split(String regex) wywołanej na obiektach łańuchowych String.
 */

public class StrTokenizer {

    public static List<String> tokenizuj(String source, String separator){
        List<String> result = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(source, separator);

        System.out.println("method countTokens(): " + st.countTokens());

        while (st.hasMoreTokens()){
            result.add(st.nextToken());
        }

        return result;
    }

    public static void main(String[] args) {

        String source = "The   string tokenizer\nclass allows.an application+to.break a string into tokens.";
        String separator = " \n+."; //separatorami są znak spacji, nowej linii, '+' oraz '.'

        List<String> res = tokenizuj(source, separator);
        String resultWithJoin = String.join("-", res);
        System.out.println(resultWithJoin);
    }
}
