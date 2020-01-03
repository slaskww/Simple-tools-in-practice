package threads.recursiveExecutors.countedCompelterExample;

import java.io.*;
import java.util.Map;
import java.util.concurrent.CountedCompleter;

/**
 * Zadanie ProcessFile oblicza częstość występowania słów kluczowych w podanym pliku.
 * Obliczenia w metodzie compute() wykorzystują Streamtokenizer, by w łatwy sposób móc wyłuskać słowa w strumienu znakowym.
 * Obiekt klasy StreamTokenizer przyjmuje w konstruktorze obiekt strumieniowej klasy przedmiotowej (FileReader).
 *
 *
 */

public class ProcessFile extends CountedCompleter<Map<String, Integer>> {

    private Map<String, Integer> result = JavaKeyWords.map; //korzystamy z pomocniczej klasy statycznej JavaKeyWords i jej statycznej zmiennej map
    private File file;

    public ProcessFile(ProcessDirectory parent, File file) {
        super(parent);
        this.file = file;
    }

    @Override
    public void compute() {

        try (FileReader fileReader = new FileReader(file)){
            StreamTokenizer streamTokenizer = new StreamTokenizer(fileReader);
            streamTokenizer.slashSlashComments(true);  //pomijamy komentarze //
            streamTokenizer.slashStarComments(true);  //pomijamy komentarze /* ... */
            streamTokenizer.quoteChar('"'); //pomijamy lierały napisowe
            streamTokenizer.wordChars('.', '.'); //te znaki traktujemy jako składowe słowa
            streamTokenizer.wordChars('_', '_');
            streamTokenizer.wordChars('$', '$');


            while ((streamTokenizer.nextToken()) != StreamTokenizer.TT_EOF){ //petla wykonuje się do momentu, aż napotka na koniec pliku
                if (streamTokenizer.ttype == StreamTokenizer.TT_WORD){
                   String word = streamTokenizer.sval;
                   Integer num = result.get(word); //pobieramy biezącą liczbę wystąpień danego wyrazu

                   if (num != null){
                       result.put(word, num + 1);
                   }
                }
            }
            tryComplete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> getRawResult() {
        return result;
    }
}
