package threads.synchronize.atomicClass;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * (5)
 * Alternatywą dla specyfikatora volatile stosowanego z blokiem synchronized  przy operacjach nieatomistycznych jest zestaw klas z pakietu
 * java.util.concurrent.atomic (m.in AtomicInteger, AtomicBoolean i AtomicLong) i ich metody, które w sposób atomowy definiują operacje nieatomistyczne
 * (np. pobieranie wartości a następnie jej update, porównanie wartości  a następnie update, pobranie wartości a następnie jej zwiększenie),
 * które będąc wykonywane na typach prostych musiałyby byc synchronizowane.
 *
 * W przykładzie dokonujemy porównania wyników inkrementacji liczby typu protego int oraz typu AtomicInteger.
 */

public class AtomicInUse {

   private AtomicInteger anumber; //korzystamy z obiektu AtomicInteger, który posiada 'zatomizowane' metody wykonujące nieatomistyczne operacje modyfikujące
   private CountDownLatch latch; //skorzystaliśmy przy okazji z zasuwy, dzięki ktorej wyświetlenie końcowego komunikatu w wątku głownym
                                 //wykona się po zakończeniu działań wątków podrzędnych.
   private int pnumber;

    public AtomicInUse(int number, CountDownLatch count) {
        this.anumber = new AtomicInteger(number);
        this.pnumber = number;
        this.latch = count;
    }
    public void incrementValues(){
        incrementAtomicValue();
        incrementPrimitiveValue();
        latch.countDown();
    }

    private void incrementAtomicValue(){ //dzięki wykorzystaniu obiektu z biblioteki concurrent.atomic, nie musimy deklarować metody jako synchronizowanej
        anumber.incrementAndGet();
   }

   private void incrementPrimitiveValue(){
       int temp = pnumber;
       temp += 1;
       pnumber = temp;
   }


    public int getAtomicValue(){
        return anumber.get();
   }

    public int getPrimitiveValue(){
        return pnumber;
    }


    public static void main(String[] args) {

        int tnumber = 2000;
        CountDownLatch latch = new CountDownLatch(tnumber);
        AtomicInUse atomicInUse = new AtomicInUse(0, latch);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < tnumber; i++){
            exec.submit(() -> atomicInUse.incrementValues());
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final value of AtomicIntiger  = " + atomicInUse.getAtomicValue() + "\nFinal value of integer = " + atomicInUse.getPrimitiveValue());
        exec.shutdown();
    }
}
