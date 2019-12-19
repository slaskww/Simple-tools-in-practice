package threads.synchronize.synchroLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Obiekty klasy ReentrantLock odpowiadają mechanizmowi synchronizacji za pomocą słowa synchronized.
 * W odróżnieniu do synchronizacji z synchronized obiekty ReentrantLock:
 *      -mogą być przekazywane przez referencję jako argumenty metod lub w konstruktorach
 *       a co za tym idzie, moga być zamykane (lock) i otwierane (unlock) w różnych sekcjach kodu wykonywanego przez dany wątek
 *       (synchronizacja ze słowem synchronized moze być uzyte tylko w ramach tego samego bloku)
 *      -umożliwiaja sprawdzenie, czy rygiel jest zamknięty, co daje możliwość warunkowego zareagowania innego wątku na istnienie takiej blokady, tryLock()
 *      -umożliwiaja sprawdzenie, czy rygiel jest zamknięty i ewentualne oczekiwanie na jego otwarcie przez określony czas, tryLock(...)
 *      -umożliwiają przerwanie wątku, który jest zablokowany w oczekiwaniu na otwarcie rygla, lockInterruptibly()
 *
 *      Schemat działania obiektu rygla wygląda nastepująco:
 *          1. Tworzymy obiekt rygla
 *          2. Zamykamy rygiel wywołują metode lock()
 *          3. Wykonywany jest kod sekcji krytycznej
 *          4. Otwieramy rygiel metoda unlock()
 *
 * Rygiel powinniśmy otwierać w bloku finally
 */

public class ReentrantLockInUSe {

    private ReentrantLock lock = new ReentrantLock(); //Tworzymy obiekt rygla

    private int number;

    public ReentrantLockInUSe(int number) {
        this.number = number;
    }

    private void increase(String name) throws InterruptedException {


        lock.lock(); // zamykamy rygiel i dalej wykonujemy kod sekcji krytycznej

        try{

            System.out.println("Thread " + name + ": initial value = " + number);
            number++;
            Thread.sleep(1000);
            System.out.println("Thread " + name + ": increased value = " + number);

        } finally {
            lock.unlock(); // otwieramy rygiel w bloku finally, by zapewnić zwolnienie blokady w każdych warunkach, również w przypadku wystąpienia wyjątku w sekcji krytycznej
        }
    }

    public void executeThreads(int tnumber){

        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < tnumber; i++){
            int n = i+1;
            Runnable runnable = () -> {
                try {
                    increase(""+n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            exec.execute(runnable);
        }
        exec.shutdown();
    }

    public static void main(String[] args) {

        ReentrantLockInUSe reentrantLockInUSe = new ReentrantLockInUSe(0);
        reentrantLockInUSe.executeThreads(5);
    }
}
