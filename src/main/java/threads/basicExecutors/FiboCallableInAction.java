package threads.basicExecutors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * (2)
 * runnable i Callable są różnymi wersjmi definiowania zadania do wykonania.
 * Zarówno Runnable jak i Callable określają w swych metodach kod, który ma zostać wykonany w wątku.
 * Callable zapewnia jednak, że zostanie zwrócony wynik działania kodu oraz daje możliwość zgłoszenia wyjątku kontrolowanego.
 * By móc uzyskać wynik działania kodu metody call() z Callable, musimy odnieść się do obiektu typu Future<T> który
 * reprezentuje wynik asynchronicznych obliczeń.
 *
 * Metoda execute() naszego wykonawcy przyjmuje w parametrze obiekt typu Runnable. Nie możemy mu więc w ten sposób przekazać zadania zdefiniowanego w Callable.
 * Możemy tego dokonac na dwa sposoby:
 *
 * - tworząc obiekt FutureTask lub własna klase rozszerz. FutureTask. FutureTask implementuje interf. Runnable i Future, przyjmuje  w konstruktorze obiekt typu Callable.
 *   Nastepnie obiekt ten podajemy jako argument w metodzie execute(Runnable)
 *
 * - korzystając z metody submit() naszego Wykonawcy. Poniżej skorzystamy z drugiego wariantu.
 *
 * Executor posiada metodę submit(Callable<T>) (oraz submit(Runnable)),  ktora przyjmuje obiekt Callable,
 * zleca zadanie podane w metodzie call() obiektu Callable i zwraca obiekt Future<T>, który możemy odpytać o wynik zwracany przez call()
 *
 *  obiekt Future<T> zawiera następujące metody:
 *
 *      boolean cancel(boolean mayInterruptIfRunning) - próbuje anulowac wykonanie zadania, argument mówi o tym, czy można prerwać zadanie ktore jest w trakcie wykonania.
 *          Nierozpoczete jeszcze zadania są usuwane z listy zadań wykonawcy
 *      V get() - pobiera wynik zadania, czeka jeśli zadanie jeszcze się nie zakończyło
 *      V get(long timeout, TimeOut unit) - pobiera wynik zadania, czeka określony czas, jeśli zadanie jeszcze się nie zakończyło,
 *      boolean isCancelled() - zwraca informację, czy zadanie zostało anulowane
 *      boolean isDone() - zwraca informację, czy zadanie zostało zakończone (przez zwykłe zakończenie lub anulowanie)
 *
 *      W zadaniu tworzymy Wykonawcę, któremu dajemy cztery zadania implementujące Callable (FiboCallableInAction).
 *      Zwracane obiekty Future umieszczamy na liście i w pętli co 30 ms. usypiamy wątek oraz iteracyjnie odpytujemy każdy z obiekt Future, czy zadanie zostalo wykonane.
 *      Jeśli zadanie zostało wykonane, usuwamy obiekt Future z listy.
 *      Petlę kończymy, jeśli wszystkie zadania zostaną wykonane, a tym samym na liście nie będzie już żadnych obiektów Future<T>
 */

public class FiboCallableInAction {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool();
        List<Future<Long>> futures = new ArrayList<>();

        for (int i : new int[]{16, 23, 32}) {
            Future<Long> f = exec.submit(new FiboWithCallable(i)); //submit przyjmuje zadanie do wykonania i zwraca obiekt wyniku typu Future<t> (Uwaga, metoda nie czeka ze zwróceniem obiektu Future na zakończenie zadania )
            futures.add(f);
        }

        while (futures.size() > 0) {
            Thread.sleep(30);

      /*      for (int i = 0; i < futures.size(); i++) {
                Future<Long> fut = futures.get(i);
                if (fut.isDone()) {
                    try {
                        System.out.println("Rezultat: " + fut.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    futures.remove(i);
                    System.out.println("Pozostalo watkow: " + futures.size());
                    break;
                }
                System.out.println("Pozostalo watkow: " + futures.size());
            }*/

            for (Iterator<Future<Long>> fit = futures.iterator(); fit.hasNext(); ) {
                Future<Long> fut = fit.next();
                if (fut.isDone()) {
                    try {
                        System.out.println("Rezultat: " + fut.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    fit.remove();
                }
                System.out.println("Pozostalo watkow: " + futures.size());
            }

        }
        exec.shutdown();
    }
}
