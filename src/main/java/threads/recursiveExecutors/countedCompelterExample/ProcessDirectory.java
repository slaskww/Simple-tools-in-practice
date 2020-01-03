package threads.recursiveExecutors.countedCompelterExample;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountedCompleter;

/**
 * Zadanie ProcessDirectory przegląda dany katalog i uruchamioa asynchronicznie zadania ProcessFile dla plików .java z tego katalogu
 * oraz zadania ProcessDirectory dla podkatalogów.
 * Zadania ProcessDirectory i ProcessFile umieszczamy na liście, by na koniec móc się do nich odwołać i zebrać wyniki.
 */

public class ProcessDirectory extends CountedCompleter<Map<String, Integer>> {

    private ProcessDirectory parent;
    private File directory;
    private List<ProcessDirectory> subdirectories = new ArrayList<>(); //lista zadań przetwarzanących podkatalogi dla danego katalogu
    private List<ProcessFile> subFiles = new ArrayList<>(); //lista zadań przetwarzanących pliki w danym katalogu
    private Map<String, Integer> result = new HashMap<>(); //rezultat zebrany z plików katalogu bieżącego i plików z jego podkatalogów

    public ProcessDirectory(ProcessDirectory parent, File directory) {
        super(parent);
        this.parent = parent;
        this.directory = directory;
    }

    /**
     * Dla danego katalogu pobieramy wszystkie jego podkatalogi i pliki jako tablicę obiektow File.
     * Przechodzimy przez elementy tablicy i
     *      -jeśli obiekt File jest plikiem, i jego nazwa kończy się na ".java"
     *      to uruchamiamy asynchronicznie (fork) podzadanie zliczające słowa kluczowe a zadanie to dodajemy do listy podzadań plikowych.
     *      Naturalnie musimy zwiększyć pending o 1, dla każdego takiego pozadania.
     *      - jeśli obiekt jest katalogiem, tworzymy i uruchamiamy asynchronicznie (fork) podzadanie dla katalogu, dodajemy podzadanie do listy podzadań katalogowych
     *      i zwiększamy pending
 *      Na końcu wywołujemy metodę tryComplete()
     */

    @Override
    public void compute() {

       File[] flist = directory.listFiles();

       if (flist != null && flist.length != 0){ //jeśli w katalogu są jakies inne katalogi lub pliki...
           for(File f : flist){
               if (f.isFile() && f.getName().endsWith(".java")){ //jesli jest plikiem, stworz podzadanie dla pliku
                   ProcessFile pf = new ProcessFile(this, f);
                   this.addToPendingCount(1);
                   pf.fork();
                   subFiles.add(pf);
               } else if (f.isDirectory()){  //jesli jest katalogiem, stworz podzadanie dla katalogu
                   ProcessDirectory pd = new ProcessDirectory(this, f);
                   this.addToPendingCount(1);
                   pd.fork();
                   subdirectories.add(pd);
               }
           }
       }
       this.tryComplete();
    }

    /**
     * W strumieniu dla każdego zadania ProcessFile i ProcessDirectory wywołujemy metodę zwracającą wynik,
     * a następnie wynik wykorzystujemy jako argument w metodzie złączającej.
     */

    @Override
    public void onCompletion(CountedCompleter<?> countedCompleter) {

        subFiles.stream().forEach(processFile -> mergeResults(processFile.getRawResult()));
        subdirectories.stream().forEach(processFile -> mergeResults(processFile.getRawResult()));
    }



    @Override
    public Map<String, Integer> getRawResult() {
        return result;
    }

    /**
     * Metoda łączy dwie mapy, tzn do mapy result zostanie dołączona zawartość mapy map2
     * Wykorzystuje tutaj  metodę merge wywołaną na mapie result.
     * Pierwszym argumentem metody merge() jest nazwa klucza. Metoda sprawdza, czy w mapie result występuje taki klucz i jaka jest wartość odwzorowana dla tego klucza.
     * Jeśli wartość jest null, lub mapa nie posiada odwzorowania dla tego klucza, to dodawana jest nowa para klucz - wartość, gdzie
     * kluczem jest key, a wartością drugi parametr metody merge().
     * Jeśli mapa posiada odwzorowanie tego klucza i wartość nie jest null, to wartość ta zostanie zastąpiona wynikiem funkcji podanej jako trzeci argument metody merge().
     */

    private void mergeResults(Map<String, Integer> map2){
            if (!map2.isEmpty()){
                for (String key : map2.keySet()){
                    this.result.merge(key, map2.get(key), (integer, integer2) -> integer + integer2);
                }
            }
    }
}
