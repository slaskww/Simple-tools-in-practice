package threads.synchronize.synchroLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Obiekt rygla ReentrantReadWriteLock umożliwia warunkowe synchronizowanie dostępu do zasobu.
 * Tworzac instancję tej klasy możemy z niej pobrać obiekty blokera dla odczytu readLock() i zapisu writeLock().
 * Rygiel do czytania (readLock) będzie działał blokująco na sekcji krytycznej jedynie w sytuacji
 * gdy równolegle zamknięty jest rygiel do zapisu (writeLock).
 * W przeciwnym wypadku synchronizacji przy odczycie nie będzie.
 * Mechanizm ten jest szczególnie przydatny w sytuacjach, gdy równolegle działających operacji odczytu jest znacząco
 * więcej niż operacji modyfikujących.
 *
 * Alternatywnie do ReadWriteLock możemy korzystać z blokera StampedLock.
 *
 * Rygiel powinniśmy otwierać w bloku finally
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
           System.out.println("Thread " + tname + ", writing and reading locked");
           char[] stringAsChars = s.toCharArray();
           StringBuilder reversedStr = new StringBuilder();
           for (int i = stringAsChars.length - 1; i >= 0; i--){
               reversedStr.append(stringAsChars[i]);
           }
           s = reversedStr.toString();
       } finally {
           writeLock.unlock();
           System.out.println("Thread " + tname + ", writing and reading unlocked");
       }

    }

    private void read(String tname){

        try{
            readLock.lock();
            System.out.println("Thread " + tname + ", string value: " + s);
        } finally {
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

        ReentrantReadWriteLockInUse rwl = new ReentrantReadWriteLockInUse("Read-Write-Lock");
        rwl.executeThreads(4);
    }
}
