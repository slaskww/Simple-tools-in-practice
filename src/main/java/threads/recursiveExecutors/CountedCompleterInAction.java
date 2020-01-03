package threads.recursiveExecutors;


import java.util.concurrent.ForkJoinPool;

/**
 * (2)
 * CountedCompleter jest klasa zadania dziedziczącą po abstrakcyjnej klasie ForkJoinTask
 * Specyfika takich zadań polega na tym, że mogą wyzwalać kończenie innych zadać typu CountedCompleter.
 */

public class CountedCompleterInAction {

    public static void main(String[] args) {

        ForkJoinPool exec = new ForkJoinPool();
        TaskAsCountedCompleter task  = new TaskAsCountedCompleter(null, 'H', 8);
        String result = exec.invoke(task);
        System.out.println("Rezultat: " + result);
    }
}
