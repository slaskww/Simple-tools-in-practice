package threads.synchronize.otherSynchros;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * (3.1)
 * CountDownLatch czyli zasuwa jest narzędziem przydatnym, gdy pewien kod w jednym wątku musi zaczekać na wykonanie pewnych operacji w innych wątkach.
 * Działanie zasuwy oparte jest o licznik (jego wartość podajemy w konstruktorze zasuwy).
 * W wątku, który ma czekać wywołuje się metodę await() blokującą dalsze wykonywanie kodu do momentu, aż licznik zasuwy nie osiągnie wartości 0.
 * Licznik zasuwy zmniejsza się każdorazowo o 1 po wywołaniu metody countDown() w kadym z pracujących wówczas wątków.
 *
 */

public class CountDownLatchInUse {

    public static void main(String[] args) throws InterruptedException {

        int tnumber = 3;
        CountDownLatch countDownLatch = new CountDownLatch(tnumber);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < tnumber; i++){
            int t = i + 1;
            exec.execute(() -> {
                try { Thread.sleep(100 * t); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("Processing in thread " + t);
                countDownLatch.countDown(); //w każdym z osobnych wątków następuje zmnejszenie licznika  zasuwy o 1
            });
        }
        countDownLatch.await(); //kod w wątku głównym oczekuje w tym miejscu na moment, gdy zasuwa zostanie otwarta, czyli licznik osiągnie wartość 0
        System.out.println("Final processing in main thread");

        exec.shutdown();


    }

}
