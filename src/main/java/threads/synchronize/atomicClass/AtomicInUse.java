package threads.synchronize.atomicClass;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Alternatywą dla specyfikatora volatile stosowanego z blokiem synchronized  przy operacjach nieatomistycznych jest zestaw klas z pakietu
 * java.util.concurrent.atomic (m.in AtomicInteger, AtomicBoolean i AtomicLong) i ich metody, które w sposób atomowy definiują operacje nieatomistyczne
 * (np. pobieranie wartości i jej update, porównanie i update, pobranie z zwiększenie), które będąc wykonywane na typach prostych musiałyby byc synchronizowane.
 *
 */

public class AtomicInUse {

   private AtomicInteger number; //korzystamy z obiektu AtomicInteger, który posiada 'zatomizowane' metody wykonujące nieatomistyczne operacje modyfikujące
   private CountDownLatch count; //skorzystaliśmy przy okazji z zasuwy, dzięki ktorej wyświetlenie końcowego komunikatu w wątku głownym
                                 //wykona się po zakończeniu działań wątków podrzędnych.

    public AtomicInUse(int number, CountDownLatch count) {
        this.number = new AtomicInteger(number);
        this.count = count;
    }

    public void calculate(){ //dzięki wykorzystaniu obiektu z biblioteki concurrent.atomic, nie musimy deklarować metody jako synchronizowanej
        number.incrementAndGet();
        count.countDown();
   }
   public int get(){
        return number.get();
   }


    public static void main(String[] args) {

        int tnumber = 20;
        CountDownLatch count = new CountDownLatch(tnumber);
        AtomicInUse atomicInUse = new AtomicInUse(0, count);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < tnumber; i++){
            exec.submit(() -> atomicInUse.calculate());
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final value = " + atomicInUse.get());
        exec.shutdown();
    }
}
