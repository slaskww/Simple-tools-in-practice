package threads;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

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
        exec.shutdown();
    }


    public static void cashedThreadPoolExecutor(){

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 1; i <= 4; i++){
            exec.execute(new Counter(i* 10000, "cashed thread pool: thread" + i));
        }
        exec.shutdown();
    }

    /**
     * Metoda invokeAll() obiektu executor zleca wykonawcy listę zadań typu Callable<T> i dopiero po ich wykonaniu zwraca listę wyników Future<T>.
     * Jeśli zależy nam na tym, by otrzymac dostęp do wyników w miarę ich kończenia, należy skorzystać z klasy ExecutorCompletionService()
     */
    public static void invokeAllInUse(){

        ExecutorService exec = Executors.newCachedThreadPool();

        List<Callable<Long>> tasks = new ArrayList<>(Arrays.asList(
                new FiboWithCallable(24),
                new FiboWithCallable(15),
                new FiboWithCallable(7)
        ));

        List<Future<Long>> results = new ArrayList<>();
        try {
          results =  exec.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Future<Long> f: results){
            try {
                Long res = f.get();
                System.out.println("InvokeAll, wynik: " + res);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
    }

    /**
     * Klasa przyjmuje w konstruktorze obiekt typu Executor i pozwala na pobieranie wyniku zadań w miarę ich kończenia. Obiekt posiada dwie metody pozwalające pobierać wynik:
     *  -take() - metoda działa blokująco, czeka na wynik zakończonego zadania, usuwa z wewnętrznej kolejki i zwraca
     *  -pool() - metoda usuwa z wewnętrznie zaimplementowanej kolejki obiekt Future wykonanego zadania i zwraca go, jeśli zadania nie ma, zwraca NULL
     */

    public static void executorCompletionServiceInUse(){

        ExecutorService exec = Executors.newCachedThreadPool();
        ExecutorCompletionService<Long> ecs = new ExecutorCompletionService<>(exec);
        int[] tasks = new int[]{24, 13, 7};

        for (int i : tasks){
            ecs.submit(new FiboWithCallable(i));
        }

        for (int i = 0; i < tasks.length; i++){
            Future<Long> result = null;

            try {
                result = ecs.take(); //metoda take() czeka na zakończenie zadania, po zakończeniu zadania zwraca wynik
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Long fibo;

            try {
                fibo = result.get();
                System.out.println("ExecutorCompletionService, wynik: " + fibo);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        exec.shutdown();
    }

    /**
     * Metoda invokeAny() obiektu executor zleca wykonawcy listę zadań typu Callable<T> i zwraca bezpośredni wynik pierwszego wykonanego zadania (a nie obiekt Future)
     * Pozostałe zadania zostaną anulowane.
     */

    public static void invokeAnyInUse(){

        ExecutorService exec = Executors.newCachedThreadPool();

        List<Callable<Long>> tasks = new ArrayList<>(Arrays.asList(
                new FiboWithCallable(24),
                new FiboWithCallable(15),
                new FiboWithCallable(7)
        ));

        try {
          Long result =  exec.invokeAny(tasks);
            System.out.println("invokeAny, wynik pierwszego zakonczonego zadania: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        exec.shutdown();
    }

    public static void main(String[] args) {

       // fixedThreadPoolExecutor();
       // cashedThreadPoolExecutor();
       // invokeAllInUse();
       // invokeAnyInUse();
        executorCompletionServiceInUse();
    }

}
