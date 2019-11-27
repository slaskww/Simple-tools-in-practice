package threads;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Obiekty pmplementujące ExecutorService - wykonawcy - to obiekty tworzące pule wątków i zarządzające tymi pulami.
 * Wykonawca jest więc obiektem klasy implementujacej interf. Executor zawierającej jedną metodę ezecute(Runnable).
 * Interf. ExecutorService rozszerza int. Executor dostarczając dodatkowych metod, m.in do zlecania zadań i kończenia działania egzekutora.
 *
 * Możemy wybrać spośród kilku gotowych już egzekutorów:
 *   Executors.newSingleThreadExecutor(); - egzekutor uruchamiający zadanie w jednym wątku po kolei
 *   Executors.newFixedThreadPool(int) - egzekutor prowadzacy pulę wątków o zadanym rozmarze
 *   Executors.newCachedThreadPool(); - egzekutor prowadzi pulę wątków o dynamicznym rozmiarze
 *   Executors.newScheduledThreadPool(); - egzekutor zarządzający tworzeniem i wykoaniem zadań w określonym czasie
 *   Executors.newWorkStealingPool(); - egzekutor zarządzający pulą wątków z kolejkami zadań przypisanymi do wątków
 *
 *
 */

public class ExecutorsInAction {

    public static void fixedThreadPoolExecutor(){


        ExecutorService exec = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 4; i++){
            exec.execute(new Counter(i* 10000, "Fixed thread pool: thread" + i));
        }
    }


    public static void cashedThreadPoolExecutor(){



        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 1; i <= 4; i++){
            exec.execute(new Counter(i* 10000, "cashed thread pool: thread" + i));
        }
    }
    public static void main(String[] args) {

       // fixedThreadPoolExecutor();
        cashedThreadPoolExecutor();
    }

}
