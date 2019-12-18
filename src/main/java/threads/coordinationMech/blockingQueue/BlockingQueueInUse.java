package threads.coordinationMech.blockingQueue;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Biblioteka java.util.concurrent zawiera zestaw przydatnych i łatwiejszych w użyciu środków do koordynacji wątków.
 * Są nimi kolejki blokujące:
 *  -ArrayBlockingQueue
 *  -LinkedBlockingQueue
 *
 * Obie klasy implementują interfejs BlockingQueue kolejki typu fifo.
 * Dwie metody istotne z punktu widzenia koordynacji działania wątków to:
 *  -put(element) - metoda dodaje element na koniec kolejki, lub czeka (blokując wątek), gdy kolejka jest pełna
 *  -take() - metoda pobiera i zwraca element z początku kolejki, czeka (blokując wątek), jeśli kolejka jest pusta.
 *
 * Stosując BlockingQueue unikamy konieczności stosowania synchronizacji i koordynacji działania wątków przy użyciu metod wait() i notify()
 */

public class BlockingQueueInUse {

    private LinkedBlockingDeque<String> quest = new LinkedBlockingDeque<>();
    private LinkedBlockingDeque<String> answ = new LinkedBlockingDeque<>(Arrays.asList("Thank you for having me."));

    Runnable journalist = () -> {
        String[] questions = {"Are you famous?", "Have you got any dreams?", "Do you want me to finish the interview?", ""};
        for(String q : questions){

            try {
               String a = answ.take();
                System.out.println("Journalist read the answer: " + a);
                Thread.sleep(1000);
                quest.put(q);
            } catch (InterruptedException e) {
                return;
            }
        }
    };

    Runnable star = () -> {
        String q = "empty";
        while(!q.equals("")){
            try {
                q = quest.take();
                Thread.sleep(1000);
                if (!q.equals("")) System.out.println("Star read the question: " + q);
                answ.put("Yes, duh!");
            } catch (InterruptedException e) {
                return;
            }
        }
    };

    public static void main(String[] args) {
        BlockingQueueInUse coord = new BlockingQueueInUse();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(coord.journalist);
        exec.execute(coord.star);
        exec.shutdown();
    }
}
