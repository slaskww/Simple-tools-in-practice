package threads.synchronize.volatileWord;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * (4)
 * Specyfikator volatile daje gwarancję, że zmiany na zmiennej dokonane w jednym wątku, będą widoczne dla innych wątków.
 * Bez użycia tego specyfikatora nie mamy gwarancji, że wątek wykonujący się później zobaczy aktualną wartość zmiennej zmienionej we wcześniejszym wątku.
 * Wiąże się to z mechanizmem optymalizacji wątków, który kopiuje zmienne do lokalnej pamieci wątków i wartość ta nie zawsze będzie aktualna.
 * Jeśli na zmiennej z volatile wykonujemy operacje atomowe (wykonywane w jednym takcie procesora, np. przypisanie wartości do zmiennej lub odczyt wartości zmiennej)
 * to nie musimy synchronizować dostępu do takiej zmiennej (nasze metody get() i set() nie muszą być synchronized).
 * Jeśli jednak wykonujemy operacje, które nie są atomowe (np. operację inkrementacji, która wymaga operacji odczytu aktualnej wartości a następnie przypisania nowej wartości)
 * powinniśmy oprócz volatile uczynić taką operację synchronizowaną (poprzez blok synchronized lub metode synchronizowaną).
 *
 * Alternatywnie możemy skorzystać z jednej z klas biblioteki java.util.concurrent.atomic pozwalających pracować na obiektach AtomicInteger, AtomicLong i AtomicBoolean
 */

public class Counter {

    volatile private int number;

    public Counter(int number) {
        this.number = number;
    }

      public synchronized void increment(String thread){ //jeśli nie uczynimy metody wynchronizowaną, to otrzymamy zaburzone wyniki. Stanie się tak, dlatego, że inkrementacja nie jest operacją atomową
        number++;
        System.out.println(thread + " incremented the value.");
    }

    public int get(){
        return number;
    }

    public static void main(String[] args) {
        Counter counter = new Counter(0);
        int tnumber = 1000;
        CountDownLatch latch = new CountDownLatch(tnumber); //skorzystaliśmy przy okazji z zasuwy, dzięki ktorej wyświetlenie końcowego komunikatu w wątku głownym
                                                            //wykona się po zakończeniu działań wątków podrzędnych.
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < tnumber; i++){
            int temp = i + 1;
            exec.submit(() -> {counter.increment("Thread"+ temp);
            latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Number after incrementation: " + counter.get());
        exec.shutdown();
    }
}