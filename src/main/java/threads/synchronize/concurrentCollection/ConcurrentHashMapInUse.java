package threads.synchronize.concurrentCollection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (6.2)
 * Klasa ConcurrentHashMap jest bardzo użyteczna w programowaniu współbieżnym, gdzie wątki pracują na wspólnej kolekcji.
 * Klasa ta dostarcza użyteczne metody compute(), computeIfPresent(), computeIfAbsent() pozwalające atomowo przeprowadzać operacje,
 * które same w sobie atomowe nie są.
 * <p>
 * operacja:
 * <p>
 * int key = 44;
 * int val = map.get(key);
 * if (val != null) val += key;
 * else val = key;
 * map.put(key, val);
 * <p>
 * przy użyciu metod atomowych wygląda tak:
 * <p>
 * int key = 44;
 * compute(key, (k,v) -> v == null ? k : k + v);
 * <p>
 * W przykładzie przechodzimy przez podkatalogi katalogu file i w plikach .java zliczqmy wystąpienia słów kluczowych.
 * Wykorzystujemy tu statyczną metodę find() z klasy Files, która zwraca strumień obiektów ścieżek Path.
 * Metoda ta przyjmuje trzy argumenty: ścieżkę początkową Path, maksymalna głębokość drzewa podkatalogów, predykat określający warunki po których spełnieniu ścieżki dodawane są do listy
 * Dla każdej ścieżki uruchomimy zadanie Callable utworzone dzięki statycznej metodzie callable(Runnable) z klasy Executors konwertującej
 * obiekty Runnable w obiekty typu Callable<Object>.
 * Dzięki temu, że mamy listę Callable będziemy mogli skorzystać z metody invokeAll(List<Callable>).
 * Metoda zleca do wykonania zadania z podanej kolekcji i zwraca listę wyników Future. Lista jest zwracana dopiero po zakończeniu
 * wykonania wszystkich zadań.
 * W miejscu wywołania invokeAll() wątek główny czeka na zwrócenie listy wyników typu Future, co nastąpi po wykonaniu wszystkich zadań.
 * Do tego czasu kod poniżej invokeAll() nie wykona się.
 * <p>
 * W przykładzie wykorzystujemy również metody które dają dodatkowe możliwości przy pracy z mapą (foreach, search, reduce):
 * Ich pierwszy argumet to próg paralelizmu, czyli wartość long oznaczająca liczbę elementów przy której operacje są zrównoleglane.
 * <p>
 * -foreach(long, BiFunction, Consumer),  pozwala na (ewentualnie równoległe) zastosowanie funkcji transformującej (BiFunction) wobec każdej
 * pary klucz wartosc a jej wynik jako argument użyc w funkcji konsumującej (Consumer)
 * <p>
 * -search(long, BiFunction), pozwala na (ewentualnie równoległe) zastosowanie funkcji transformującej (BiFunction) wobec kolejnych par klucz wartosc
 * i jeśli zwracany przez funkcję transformującą wynik jest różny od null, to search kończy działanie i zwraca ten wynik.
 * W ten sposób możemy odnajdywac pierwszy element w mapie spełniający jakiś warunekm
 * <p>
 * -reduce(long, BiFunction) metoda wykonuje równoległą redukcję na kluczach, wartościach lub obiektach Entry
 */

public class ConcurrentHashMapInUse implements Runnable {

    private static ConcurrentHashMap<String, Integer> keyWords; //konkurencyjna mapa statyczna
    private File file;

    static {
        keyWords = new ConcurrentHashMap<>();
        Arrays.asList(
                "abstract", "boolean", "catch", "default", "else", "finally",
                "if", "long", "new", "private", "return", "static", "this", "void"
        ).forEach(s -> keyWords.put(s, 0));
    }

    public ConcurrentHashMapInUse(File file) {
        this.file = file;
    }

    @Override
    public void run() {

        try (FileReader reader = new FileReader(file)) {

            StreamTokenizer tokenizer = new StreamTokenizer(reader);
            tokenizer.slashStarComments(true);  //pomijamy komentarze /* */
            tokenizer.slashSlashComments(true);  //pomijamy komentarze //
            tokenizer.wordChars('.', '.'); //te znaki traktujemy jako składowe słowa
            tokenizer.wordChars('_', '_'); //te znaki traktujemy jako składowe słowa
            tokenizer.wordChars('$', '$'); //te znaki traktujemy jako składowe słowa
            tokenizer.quoteChar('"'); //pomijamy lierały napisowe

            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) { //wykonuj aż do końca pliku
                if (tokenizer.ttype == StreamTokenizer.TT_WORD) { //jeśli typ tokena to słowo ...
                    keyWords.computeIfPresent(tokenizer.sval, (key, value) -> value + 1); //to jeśli slowo to występuje w mapie, wykonaj wyliczenie, czyli zwiększ wartość o 1
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConcurrentHashMap<String, Integer> getMap() {
        return keyWords;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Stream<Path> paths = Files.find(Paths.get("C:/Users/CP24/Desktop/SimpleToolsInPractice"), 10, (path, basicFileAttributes) -> path.toString().endsWith(".java") && basicFileAttributes.isRegularFile());
        ExecutorService exec = Executors.newCachedThreadPool();

        List<Callable<Object>> clist = paths.map(path -> Executors.callable(new ConcurrentHashMapInUse(path.toFile()))).collect(Collectors.toList());
        exec.invokeAll(clist); //w tym miejscu wątek główny czeka na zwrócenie listy wyników typu Future, co nastąpi po wykonaniu wszystkich zadań. Do tego czasu kod poniżej nie wykona się.
        //Dzięki temu nie musimy usypiać wątku głównego.

        //paths.forEach(path -> exec.submit(new ConcurrentHashMapInUse(path.toFile())));
        //try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        exec.shutdown();

        getMap().entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .forEach(entry -> System.out.println(entry.getKey() + "=" + entry.getValue()));

        getMap().forEach(5, (k, v) -> "key=" + k + ", value=" + v, s -> System.out.println(s)); //ewentualnie konkurencyjna (dla mapy z co najmniej 5 elementami) metoda foreach
        String found = getMap().search(5, (k, v) -> v > 150 ? k : null); ////ewentualnie konkurencyjna (dla mapy z co najmniej 5 elementami) metoda search
        System.out.println("First key word with occurrence > 150 times : " + found);
        int occurSum = getMap().reduceValues(5, (val1, val2) -> val1 + val2); ////ewentualnie konkurencyjna (dla mapy z co najmniej 5 elementami) metoda reduce
        System.out.println("Overall Number of occurrences of key words: " + occurSum);
    }
}
