package threads.basicExecutors;



import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**(3)
 * Alternatywą dla zwykłego, bezpośredniego tworzenia wątków Thread są gotowe klasy egzekutorów, które same zarządzają pulą wątków.
 * Obiekty implementujące ExecutorService - wykonawcy - to obiekty tworzące pule wątków i zarządzające tymi pulami.
 * Wykonawca jest więc obiektem klasy implementujacej interf. Executor zawierającej jedną metodę execute(Runnable).
 * Interf. ExecutorService rozszerza int. Executor dostarczając dodatkowych metod, m.in do zlecania zadań i kończenia działania egzekutora.
 * Posiada met submit(Callable) zwracającą wynik Future<T>.
 *
 * Obiekt egzekutora zamykamy po wykonaniu zadań. W tym celu wywołujemy metodę shutdown()
 *
 * Możemy wybrać spośród kilku gotowych już egzekutorów:
 *  ExecutorService exec = Executors.newSingleThreadExecutor(); - egzekutor uruchamiający zadanie w jednym wątku po kolei
 *  ExecutorService exec =  Executors.newFixedThreadPool(int) - egzekutor prowadzacy pulę wątków o zadanym rozmarze
 *  ExecutorService exec =  Executors.newCachedThreadPool(); - egzekutor prowadzi pulę wątków o dynamicznym rozmiarze
 *  ScheduledExecutorService exec =  Executors.newScheduledThreadPool(); - egzekutor zarządzający tworzeniem i wykonaniem zadań w określonym czasie, zwraca wynik typu ScheduleFuture<T>
 *  ExecutorService exec =  Executors.newWorkStealingPool(); - egzekutor zarządzający pulą wątków z kolejkami zadań przypisanymi do wątków, wykorzystywany jest do zadań rekurencyjnych
 *
 *  dodatkowo:
 *  ExecutorCompletionService exec = new ExecutorCompletionService(Executor); - egzekutor, który przyjmuje w konstruktorze obiekt typu Executor i pozwala na pobieranie wyniku zadań
 *  w miarę ich kończenia. Jeśli któryś z wątków wykona zadanie, to jego wynik w postaci obiektu Future<T> zostanie dodany do wewnętrznej kolejki egzekutora, skąd będzie go można pobrac
 *  metodą take() lub pool()
 *
 *  Klasy egzekutorów posiadają przydatną metody:
 *      - invokeAll(Collection<Callable>) która przyjmuje obiekt kolekcji zadań Callable, np List<Callable> i zwraca listę wyników dopiero po zakończeniu wykonywania wszystkich zadań,
 *      - invokeAny(Collection<Callable>) która przyjmuje obiekt kolekcji zadań Callable, np List<Callable> i zwraca wynik T pierwszego wykonanego zadania, anulując wykonywanie pozostałych zadań
 *
 *  W poniższych przykładach wykonywanie kodu zadania uruchamiamy wywołując na egzekutorze metodę execute(Runnable) interfejsu Executor, która przyjmuje kod zadania typu Runnable i nie zwraca wartości.
 *  Alternatywnie możemy skorzystać z metody submit(Callable) interfejsu ExecutorService, która przyjmuje zadanie typu Callable i zwraca wynik w formie obiektu Future<T>.
 */

public class ExecutorsInAction {

    /**
     * metoda execute nie zwraca wyniku, by uzyskać wynik typu Future<T> należy skorzystać z metody submit() z interfejsu ExecutorService
     */

    public static void fixedThreadPoolExecutor() throws ExecutionException, InterruptedException {


        ExecutorService exec = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 4; i++){
            exec.execute(new Counter(i* 10000, "Fixed thread pool: thread" + i));
        }
        exec.shutdown(); //po wykonaniu zadań zamykamy obiekt egzekutora
    }


    public static void cashedThreadPoolExecutor(){

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 1; i <= 4; i++){
            exec.execute(new Counter(i* 10000, "cashed thread pool: thread" + i));
        }
        exec.shutdown();
    }

    /**
     * Wykonawca ScheduledExecutorService wykonuje zadania w określonych cyklach czasu.
     * Podstawowe metody tego Wykonawcy to:
     *
     *     ScheduledFuture<?> schedule(Callable<T>, wartośćOpoznienia, TimeUnit) - zadnie zostanie wykonane raz po upływie czasu 'wartośćOpoznienia'
     *     ScheduledFuture<?> scheduleAtFixedRate(Runnable, init, odstepCzasu, TimeUnit) - uruchamia zadanie po upływie 'init' a następnie wykonuje zadanie cyklicznie co odstepCzasu
     *     ScheduledFuture<?> scheduleWithFixedDelay(Runnable, init, wartośćOpoznienia, TimeUnit) - uruchamia zadanie po upływie 'init' a następnie wykonuje zadanie co wartośćOpoznienia
     *
     * Metody zwracają obiekt wyniku typu ScheduledFuture<?>
     *
     * Zadania będą wykonywane do momentu aż Wykonawca nie zostanie zamknięty (exec.shutDown())
     * lub zadania nie zostaną anulowane metodą cancel() wywołaną na obiekcie wyniku Future<T>(fut.cancel(boolean)),
     * gdzie argument typu boolean informuje, czy wykonywanie bieżącego zadania powinno zostać przerwane
     */

    public static void scheduledThreadPoolExecutor() throws InterruptedException {

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(4);

        Runnable rtask = () -> { //zadanie typu Runnable, nie zwraca wyniku
            System.out.println("Wykonano o: " + LocalTime.now());
        };

        Callable<String> ctask = () -> { //zadanie typu Callable, zwraca wynik typu łańcuch znaków
            return "Wykonano o" + LocalTime.now();
        };

        ScheduledFuture<?> fut = exec.schedule(ctask, 5, TimeUnit.SECONDS); //zadnie zostanie wykonane raz po upływie 5 sekund

        try {
            System.out.println("exec.schedule() z Callable: " + fut.get()); //zwracaną wartość typu String zadania pobieramy z obiektu wyniku ScheduledFuture
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("exec.scheduleAtFixedRate:");
        fut = exec.scheduleAtFixedRate(rtask, 1, 1, TimeUnit.SECONDS); //uruchamia zadanie po upływie 1 sek. a następnie wykonuje zadanie cyklicznie co 1 sek.
        Thread.sleep(5000);
        fut.cancel(false); //przerwij dalsze wykonywanie zadań, ale pozwól na wykonanie biezącego zadania

        System.out.println("exec.scheduleWithFixedDelay:");
        fut = exec.scheduleWithFixedDelay(rtask, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(5000);
        exec.shutdown(); //metoda kończy  wykonywanie zadań harmonogramowanych (podobnie jak cancel() ) i zamyka egzekutora
    }

    /**
     * Metoda invokeAll() obiektu ExecutorService zleca wykonawcy listę zadań typu Callable<T> i dopiero po ich wykonaniu zwraca listę wyników Future<T>.
     * Jeśli zależy nam na tym, by otrzymac dostęp do wyników w miarę ich kończenia, należy skorzystać z egzekutora typu ExecutorCompletionService
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
          results =  exec.invokeAll(tasks); //przypisanie referencji do listy wyników results nastąpi po wykonaniu wszystkich zadań z listy tasks
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
     * Metoda invokeAny() obiektu executor zleca wykonawcy listę zadań typu Callable<T> i zwraca bezpośredni wynik T pierwszego wykonanego zadania (a nie obiekt wyniku Future<T>)
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


    /**
     * Klasa ExecutorCompletionService przyjmuje w konstruktorze obiekt typu Executor i pozwala na pobieranie wyniku zadań w miarę ich kończenia.
     * Obiekt posiada dwie metody pozwalające pobierać wynik:
     *  -take() - metoda działa blokująco, czeka na wynik zakończonego zadania, usuwa z wewnętrznej kolejki i zwraca
     *  -pool() - metoda działa nieblokująco,  usuwa z wewnętrznie zaimplementowanej kolejki obiekt Future wykonanego zadania i zwraca go, jeśli zadania nie ma, zwraca NULL
     */

    public static void executorCompletionServiceInUse(){

        ExecutorService exec = Executors.newCachedThreadPool();
        ExecutorCompletionService<Long> ecs = new ExecutorCompletionService<>(exec);
        int[] tasks = new int[]{24, 13, 7};

        for (int i : tasks){
            ecs.submit(new FiboWithCallable(i)); //metoda wykonuje zadania i zwraca wynik typu Future<T>, dodatkowo umieszcza obiekty wyniku Future<T> na wewnetrznej kolejce obiektu completion egzekutora
        }

        for (int i = 0; i < tasks.length; i++){
            Future<Long> result = null;

            try {
                result = ecs.take(); //metoda take() czeka na zakończenie zadania (działa blokująco), po zakończeniu zadania zwraca wynik pobierając go z wewnętrznej kolejki completion egzekutora
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

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        fixedThreadPoolExecutor();
       // cashedThreadPoolExecutor();
       // invokeAllInUse();
       // invokeAnyInUse();
       // executorCompletionServiceInUse();
       // scheduledThreadPoolExecutor();
    }

}
