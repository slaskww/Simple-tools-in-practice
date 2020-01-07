package threads.synchronize.synchroLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * (2.2)
 * Obiekt rygla ReentrantReadWriteLock daje większą elastyczność niż obiekty typu ReentrantLock.
 *
 * Tworzac instancję tej klasy możemy z niej pobrać obiekty blokera dla odczytu readLock() i zapisu writeLock().
 * Rygiel do czytania (readLock) będzie działał blokująco (dla innych wątków) na sekcji krytycznej jedynie w sytuacji
 * gdy równolegle zamknięty jest rygiel do zapisu (writeLock).
 * W przeciwnym wypadku synchronizacji przy odczycie nie będzie.
 * Rygiel do odczytu (readLock) blokuje kod sekcji krytycznej próbujący zamknać rygiel do zapisu (writeLock).
 *
 * Ten typ rygla możliwia więc warunkowe synchronizowanie dostępu do zasobu (jeśli rygiel do zapisu nie jest ustawiony, to rygiel do odczytu nie
 * blokuje, nie synchronizuje dostępu do doczytu dla innych wątków, blokuje on zaś dostęp do zapisu dla innych wątków).
 *
 * Mechanizm ten jest szczególnie przydatny w sytuacjach, gdy równolegle działających operacji odczytu jest znacząco
 * więcej niż operacji modyfikujących.
 * Może się zdarzyć, że i w takiej sytuacji uzyskamy małą efektywność, a blokada wątków modyfikujących przez rygiel do odczytu
 * spowoduje ich 'zagłodzenie'.
 * Rozwiązaniem dla tego problemu będzie skorzystanie z obiektu klasy StampedLock i metody tryOptimisticRead(), dzięki której możemy próbować
 * odczytać dane nawet, jeśli rygiel writeLock jest zamknięty.
 *
 * Alternatywnie do ReadWriteLock możemy korzystać z blokera StampedLock.
 *
 * Rygiel (podobnie jak w przypadku ReentrantLock) powinniśmy otwierać w bloku finally by mieć pewność, że rygie ten zostanie odblokowany także w sytuacji w której kod sekcji krytycznej wyrzuciłby wyjątek.
 * Bez użycia bloku finally narazilibyśmy się na ryzyko nieotwarcia się rygla (wskutek wyrzuconego wyjątku) a tym samym zablokowania innych wątków, czekających na otwarcie rygla.
 */

public class ReentrantReadWriteLockInUse {

    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock();
    private Lock writeLock = rwl.writeLock();

    private String s;

    public ReentrantReadWriteLockInUse(String s) {
        this.s = s;
    }

    private void reverse(String tname){

       try{
           writeLock.lock();
           System.out.println("Thread " + tname + "(writeLock locked -> writing and reading locked) process reversing");
           char[] stringAsChars = s.toCharArray();
           StringBuilder reversedStr = new StringBuilder();
           for (int i = stringAsChars.length - 1; i >= 0; i--){
               reversedStr.append(stringAsChars[i]);
           }
           s = reversedStr.toString();
           Thread.sleep(500);
       } catch(InterruptedException e){
           e.printStackTrace();
       }finally {
           System.out.println("Thread " + tname + ", locker unlocked");
           writeLock.unlock();
       }

    }

    private void read(String tname){

        try{
            readLock.lock();
            System.out.println("Thread " + tname + "(readLock locked -> writing locked, reading unlocked) string value: " + s);
            Thread.sleep(500);
        } catch(InterruptedException e){
            e.printStackTrace();
        } finally {
            System.out.println("Thread " + tname + ", locker unlocked");
            readLock.unlock();

        }
    }

    public void executeThreads(int tnumber){

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < tnumber; i++){

            int n = i + 1;
            Runnable runnable = () -> {

                if ((n % 2) == 0){
                    reverse(""+n);
                    read(""+n);
                } else read(""+n);

            };
            exec.execute(runnable);
        }
        exec.shutdown();
    }

    public static void main(String[] args) {

        ReentrantReadWriteLockInUse rwl = new ReentrantReadWriteLockInUse("ReadWriteLock");
        rwl.executeThreads(4);
    }
}
