package threads;


import java.util.concurrent.ForkJoinPool;

public class CountedCompleterInAction {

    public static void main(String[] args) {

        ForkJoinPool exec = new ForkJoinPool();
        TaskAsCountedCompleter task  = new TaskAsCountedCompleter(null, 'H', 8);
        String result = exec.invoke(task);
        System.out.println("Rezultat: " + result);
    }
}
