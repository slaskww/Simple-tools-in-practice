package threads.synchronize.concurrentCollection;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * (6.1)
 * Dużym wsparciem w programowaniu współbieżnym są konkurencyjne kolekcje z biblioteki java.util.concurrent
 * należą do nich:
 *      kolejki:
 *          -ConcurrentLinkedQueue - zapewniają wielowątkowo bezpieczne operacje wstawiania i pobierania
 *          -ConcurrentLinkedDeque - zapewniają wielowątkowo bezpieczne operacje wstawiania i pobierania
 *          -ArrayBlockingQueue, LinkedBlockingQueue, SynchronousQueue - blokujące kolejki, operacje odczytu
 *              są blokowane gdy kolejka jest pusta, operacje odczytu blokowane gdy  kolejka jest pełna
 *      mapy:
 *          -ConcurrentHashMap
 *          -ConcurrentSkipListMap - dodatkowo zapewnia uprządkowanie kluczy
 *      zbiory:
 *          -ConcurrentSkipListSet - zbiór uporządkowany
 *  Kolekcje te zapewniają bezpieczne wątkowo operacje wstawiani i pobierania i modyfikacji.
 *  Kolekcji należy używać w sytuacji, gdy wiele wątków współdzieli jakąś kolekcję.
 *
 *  W naszym przykładzie zaimplementujemy konkurencyjną kolejkę która reprezentuje parking do ktorego dodajemy lub usuwamy pojazd.
 *
 */
public class ConcurrentLinkedQueueInUse {


    private ConcurrentLinkedQueue<String> carpark = new ConcurrentLinkedQueue<>();
    private Semaphore semaphore;

    public ConcurrentLinkedQueueInUse(int carparkSize) {
        this.semaphore = new Semaphore(carparkSize);
    }

    public void enter(String car){

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        carpark.add(car);
        System.out.println("\to=o> " + car + " entered the carpark");
    }


    public void leave(){
       String car =  carpark.poll();
       if (car != null){
           System.out.println("<o=o " + car + " left the carpark");
           semaphore.release();
       }
    }

    public ConcurrentLinkedQueue<String> getCars(){
        return carpark;
    }

    public static void main(String[] args) {

        ConcurrentLinkedQueueInUse carpark = new ConcurrentLinkedQueueInUse(4);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (String c : Arrays.asList("Toyota", "BMW", "Lexus", "VW Golf", "Mercedes", "Fiat", "Nissan")){
            exec.submit(() -> carpark.enter(c));
        }
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        for (int i = 1; i <= 4; i++) carpark.leave();
        exec.shutdown();
    }
}
