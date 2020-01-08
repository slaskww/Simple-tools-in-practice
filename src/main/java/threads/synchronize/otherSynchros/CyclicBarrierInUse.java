package threads.synchronize.otherSynchros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * (3.2)
 * CyclicBarrier czyli bariera działa podobnie jak zasuwa z tą różnicą, że wszystkie działające wątki
 * wykorzystujące barierę wykonują operacje do momentu aż dobiegną do bariery, czyli do miejsca w którym wątek wywołuje metodę await().
 * W tym miejscu wątek czeka, aż każdy z pozostałych wątków dobiegnie do bariery.
 * Gdy wszystkie wątki dobiegną juz do bariery, następuje ich uwolnienie i dalsze wykonywanie operacji za barierą.
 * Tutaj możemy wykorzystać tzw. akcję barierową (kod Runnable) która wykona się po zwolnieniu bariery.
 * W tym celu podajemy kod akcji w konstruktorze bariery jako drugi z jego argumentów. Argument ten jest opcjonalny.
 * Pierwszym argumentem jest liczba wątków, które mają oczekiwac na barierze.
 *
 * W poniższym przykadzie tworzymy wątki, które otrzymują liste liter tworzących dane słowo op czym odwracaja kolejność listy.
 * Następnie czekają przy barierze, aż wszystkie już tam dobiegną.
 * Wykonuje się kod akcji barierowej, polegającej na zlaczeniu lister z wynikowych list w słowa a nastepnie połączeniu tych słów w jeden łańcuch String i wyświetleniu go.
 * Ponownie wątki przetwarzają otrzymaną listę liter odwracajac kolejność listy.
 * Następnie czekają przy barierze, aż wszystkie już tam dobiegną.
 * Ponownie wykonuje się kod akcji barierowej.
 * Następuje zamknięcie włatków.

 */
public class CyclicBarrierInUse {

    public static void main(String[] args) {

        String[] words = "Here is an example of using a barrier".split("\\s");
        List<List<String>> wordsCharsList = new ArrayList<>(); //lista list liter będących stringami

        Runnable barrierAction = () ->{ //akcja wykonywaniu po zwolnieniu bariery
            wordsCharsList.forEach(sublist -> System.out.print(String.join("", sublist) + " "));
            System.out.println();};

        CyclicBarrier cb = new CyclicBarrier(words.length, barrierAction); //pierwszy argument to liczba wątków, które mają oczekiwac na barierze, drugim jest kod wykonujący się po zwolnieniu bariery

        ExecutorService exec = Executors.newCachedThreadPool();

        for (String w : words){
            List<String> word = new ArrayList<>(); //lista liter słowa
            for (char ch : w.toCharArray()){
                word.add(ch +  "");
            }
        String sWord = String.join("", word);
        wordsCharsList.add(word);
        exec.execute(() -> {
            try {
                System.out.println("Thread processing " + sWord + ": is working");
                Collections.reverse(word);
                Thread.sleep(100);
                System.out.println("Thread processing " + sWord + ": is waiting");
                cb.await();
                System.out.println("Thread processing " + sWord + ": is working again");
                Collections.reverse(word);
                Thread.sleep(100);
                System.out.println("Thread processing " + sWord + ": is waiting again");
                cb.await(); //W tym miejscu wątek czeka, aż każdy z pozostałych wątków dobiegnie do bariery
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        }
        exec.shutdown();
    }


}
