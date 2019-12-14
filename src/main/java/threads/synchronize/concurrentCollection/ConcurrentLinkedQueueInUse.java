package threads.synchronize.concurrentCollection;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Dużym wsparciem w programowaniu współbieżnym są konkurencyjne kolekcje z biblioteki java.util.concurrent
 * należą do nich:
 *      kolejki:
 *          -ConcurrentLinkedQueue
 *          -ConcurrentLinkedDeque
*       mapy:
 *          -ConcurrentHashMap
 *          -ConcurrentSkipListMap
*       zbiory:
 *          -ConcurrentSkipListSet
 *  Kolekcje te zapewniają bezpieczne wątkowo operacje wstawiani i pobierania i modyfikacji.
 *  Kolekcji należy używać w sytuacji, gdy wiele wątków współdzieli jakąś kolekcję.
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
        System.out.println("Car " + car + " entered the carpark");
    }

    public void leave(String car){
        semaphore.release();
        System.out.println("Car " + car + " left the carpark");

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

        for (int i = 1; i <= 4; i++){
            String car = carpark.getCars().poll(); //usuwamy samochód z kolejki, metoda zwrca usunięty pojazd

            if (car != null){
                exec.submit(() -> carpark.leave(car));
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }

            }
        }
        exec.shutdown();
    }
}
