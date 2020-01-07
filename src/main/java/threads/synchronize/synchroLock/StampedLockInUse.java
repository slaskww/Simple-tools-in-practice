package threads.synchronize.synchroLock;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * (2.3)
 * Klasa StampedLock odznacza się lepszą efektywnością niż ReadWriteLock.
 * Posiada metodę tryOptimisticRead(), która nie działa blokująco na kod modyfikujący, a dodatkowo pozwala odczytać dane, gdy założona jest blokada writeLock.
 * W odróżnieniu do ReadWriteLock, StampedLock nie implementuje interfejsu Lock.
 * Nie powinniśmy używać tej klasy do wywołań rekurencyjnych.
 *
 * Metody tej klasy zwracają znacznik stamp będący wartością typu long.
 * Znacznik ten wykorzystujemy jako argument w metodzie otwierającej rygiel, unlockRead(stamp), unlockWrite(stamp).
 * Klasa posiada metody z przyrostkiem try.. (tryReadLock, tryWriteLock, tryConvertToWriteLock, tryOptimisticRead) zwracające stamp a ich wywołanie
 * nie zawsze będzie blokujące. Jeśli zwrócona wartość stamp jest równa 0, oznacza to, że nie udało się wejść do sekcji krytycznej, bo inny wątek postawil tam rygiel.
 * Daje nam to pewne możliwości zareagowania na taki fakt.
 *
 * Metoda tryOptimisticRead() pozwala na postawienie rygla optymistycznego. Zakłada się tutaj, że modyfikacje danych i ich odczyt rzadko nakładają się na siebie.
 * Wobec tego możemy próbować odczytać dane nawet, jeśli rygiel writeLock jest zamknięty. Po pobraniu danych dokonujemy walidacji tych danyc metodą validate(stamp).
 * Jeśli metoda zwróci true, dane nie zostaly zmienione po naszym odczycie i możemy odczytane dane wykorzystać. Jeśli zwróci false (co oznacza, że dane zostały zmienione po naszym odczycie),
 * ustawiamy readLock w oczekiwaniu na to, aż inny wątek zwolni rygiel zapisu.
 *
 * StampedLock posiada metodę umożliwiającą konwersję  tryb ryglowania z Read na Write tryConvertToWriteLock(stamp). Po co?
 * Jeśli chcielibyśmy zmienić stan obiektu (wartości jego pól) gdy spełnia określone warunki, to wypadałoby najpierw odczytać ten stan i wtedy dokonac porównania.
 * Odczyt dokonujemy metodą readLock(), gdyż jest w mniejszym stopniu blokująca. Teraz, jeśli stan obiektu spełnia warunki i chcielibyśmy dokonać zmian wartości pól obiektu,
 * to skorzystamy z tryConvertToWriteLock() a gdy metoda zwróci wartość 0 (konwersja się nie uda), otwieramy rygiel readLock (unlockRead()) i ustawiamy normalny writeLock().
 */
public class StampedLockInUse {

    private int first,  last;
    private StampedLock lock = new StampedLock();

    public StampedLockInUse(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public void readValues(int tnumber){

        long stamp = lock.tryOptimisticRead();  //czytamy dane bez blokowania (nie stawiamy locka, więc nie musimy dbać o otwarcie w finally), nawet gdy inny wątek ustawił już writeLock.
                                                //Optymistycznie bowiem zakładamy, że między odczytem a wykorzystaniem
                                                //danych ich wartość nie ulegnie modyfikacji przez inny wątek
        int f = first;
        int l = last;

        if (lock.validate(stamp)){              //sprawdzamy, czy wartość danych zostala niezmieniona
           processData(f, l, tnumber);
        } else {                                //jeśli inny wątek zmienił wartość danych, to czekamy na normalny dostęp by ponownie odczytać dane. Wtedy jednak musimy zadbać o ponowne otwarcie locka
            stamp = lock.readLock();
            f = first;
            l = last;
            try{
               processData(f, l, tnumber);
            } finally {
                lock.unlockRead(stamp);
            }
        }
    }

    private void processData(int f, int l, int tnumber){
        String seq = IntStream.rangeClosed(f, l)
                .mapToObj(i -> String.valueOf(i))
                .collect(Collectors.joining(", "));
        System.out.println("Thread " + tnumber + ", readLock, sequence: " + seq);
    }

    public void setValues(int x, int y, int tnumber){

        long stamp = lock.readLock(); //zakladamy locka, dostajemy znacznik, po ew. oczekiwaniu na otwarcie rygla przez inny wątek
        try{
            if(first < x && last < y){ //jeśi nowe wartości są wyższe niż pierwotne, dokonujemy aktualizacji wartości
                long smpConv = lock.tryConvertToWriteLock(stamp); //chcemy zmodyfikować wartości, więc uzyskujemy lock do zapisu, robimy to przez konwertowanie readLocka

                if (smpConv != 0){ //jeśli konwersja sie udała, wykonujemy blok
                    first = x;
                    last = y;
                    stamp = smpConv;
                    System.out.println("Thread " + tnumber + ", ConvertToWriteLock, values set. New values: " + first + ", " + last);
                } else{            //konwersja z readLock do writeLock się nie udała...
                    lock.unlockRead(stamp); //zwalniamy readLock ...
                    stamp = lock.writeLock(); //i czekamy na dostęp w trybie pisania
                    first = x;
                    last = y;
                    System.out.println("Thread " + tnumber + ", writeLock, values set. New values: " + first + ", " + last);
                }
            }
            Thread.sleep(500);
        } catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock(stamp); //metoda unlock pozwala otworzyć writeLock i readLock
        }
    }


    public static void main(String[] args) {

        int left = 1;
        int right = 5;
        StampedLockInUse sl = new StampedLockInUse(left, right);
        ExecutorService exec = Executors.newCachedThreadPool();
        Random rand = new Random();

        for (int i : IntStream.range(1, 10).toArray()){
            Runnable runnable;

            int a = left + i;
            int b = right + i;

            if (rand.nextInt(2) == 0){
                runnable = () -> sl.setValues(a, b, i);
            } else{
                runnable = () -> sl.readValues(i);
            }
            exec.execute(runnable);
        }
        exec.shutdown();
    }
}
