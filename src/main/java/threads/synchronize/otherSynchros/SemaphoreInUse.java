package threads.synchronize.otherSynchros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * (3.3)
 * Semafor jest synchronizatorem, który zapewnia ograniczenie dostępu wątków do zasobu.
 * Obiekt semafora prowadzi licznik pozwoleń dostępu do zasobu.
 * Gdy jakiś wątek chce otrzmać dostęp do zasobu, wywołuje na rzecz semafora metodę aquire().
 * Jeśli liczba pozwoleń jest większa od 0, to wątek taki dostęp otrzymuje, a liczba pozwoleń zmniejsza się o 1.
 * Jeśli brak jest pozwoleń, kod w takim wątku jest blokowany na metodzie aquire(), dopóki liczba pozwoleń nie wzrośnie.
 * Alternatywnie możemy wykorzystać metodę tryAcquire(), która zwraca true lub false, w zależności czy uzyskano dostęp do zasobu.
 * Metoda ta nie blokuje wykonanywania operacji w wątku, gdy licznik pozwoeń jest równy 0.
 * Każdy wątek, który otrzymał pozwolenie na dostęp, po zakończeniu przetwarzania kodu wywołuje na rzecz semafora metodę release().
 * Powoduje to zwiekszenie liczby pozwoleń o 1.
 *
 * Działanie semafora można porównać do funkcjonowania parkingu z bramkami wjazdowymi o ograniczonej pojemności.
 * Jednorazowo skorzystać z parkingu może z góry określona liczba pojazdów. Kakżdy pojazd ponad maksymalna liczbę zmuszony jest
 * oczekiwać na zwolnienie miejsca na parkingu.
 *
 *
 */

public class SemaphoreInUse {


      private Semaphore semaphore;
      private List<String> carpark = new ArrayList<>();

    public SemaphoreInUse(int counter) {
        this.semaphore = new Semaphore(counter);
    }

    public void enter(String car){

        try {
            semaphore.acquire(); //Gdy wątek chce otrzymać dostęp do zasobu, wywołuje na rzecz semafora metodę aquire(). Jeśli brak jest pozwoleń, kod w takim wątku jest blokowany na metodzie aquire()
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addCar(car);
        System.out.println(car + " entered the carpark.");
    }

    public void leave(String car){
        semaphore.release(); // po zakończeniu przetwarzania kodu wywołujemy na rzecz semafora metodę release(). Powoduje to zwiekszenie liczby pozwoleń o 1
        removeCar(car);
        System.out.println(car + " left the carpark.");
    }

    public synchronized List<String> getCars(){ //metoda musi być synchronizowana, gdyż lista jest kolekcją niesynchronizowaną
        return carpark;
    }

    private synchronized void addCar(String car){ //metoda musi być synchronizowana, gdyż lista jest kolekcją niesynchronizowaną
        carpark.add(car);
    }

    private synchronized void removeCar(String car){ //metoda musi być synchronizowana, gdyż lista jest kolekcją niesynchronizowaną
        carpark.remove(car);
    }

    public static void main(String[] args) {

        SemaphoreInUse carpark = new SemaphoreInUse(4);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (String c : Arrays.asList("Mazda", "Suzuki", "Toyota", "VW", "Fiat", "Ford")){
            exec.execute(() -> carpark.enter(c));
        }

        try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }

        for (String c : carpark.getCars().subList(0,2)){ //dwa pierwsze pojazdy opuszczaja parking
           exec.execute(() -> carpark.leave(c));
        }

        /*List<String> cars = new ArrayList<>(carpark.getCars().subList(0,2));
        for (String c : cars) {
            carpark.leave(c);
        }*/
        exec.shutdown();
    }
}
