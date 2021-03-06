package threads.recursiveExecutors;

import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

/**
 * (1b)
 *  Tworzymy klasę zadania typu ForkJoinTask. Korzystamy z gotowej abstrakcyjnej klasy RecursiveTask posiadającej
 *  jedną abstrajkcyjną metode compute(), którą musimy zaimplementować. Zadania tego typu zwracają wynik.
 *
 *  W zadaniu dostarczamy w konstruktorze tablicę elementów typu String, zakres danych w tabeli i implementacje interfejsu funkc Function.
 *  Jeśli długość tablicy jest co najwyżej równa wielkości progu podziału zadania, to mamy do czynieniea z sytuacją bazową.
 *  W takim wypadku wykonujemy działania na elementach tablicy i zwracamy wynik.
 *  Gdy nie mamy do czynienia z sytuacją bazową, wątek dzieli zadanie na dwa podzadania.
 *  Na pierwszym z nich wywołuje metodę invoke() i je realizuje, a na drugim wywołuje metodę fork() i dodaje do kolejki. Wynik forkowanego zadania pobiera metodą join();
 *  Łączymy rezultaty metod invoke() i join() i zwracamy wynik.
 */

public class TaskAsRecursiveTask extends RecursiveTask<StringBuilder> {

    private static int THRESHOLD = 3;

    private Function<String, Character> oper;
    private String[] array;
    int from, to;


    public TaskAsRecursiveTask(String[] array, int from, int to, Function<String, Character> oper) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.oper = oper;
    }

    @Override
    protected StringBuilder compute() {

        StringBuilder sb = new StringBuilder();
        if (to - from <= THRESHOLD){
            for (int i = from; i < to; i++){
                char ch = oper.apply(array[i]);
                sb.append(ch).append(" ");
            }
            return sb;
        } else{
            int middle = (from + to) / 2;
            TaskAsRecursiveTask left = new TaskAsRecursiveTask(array, from, middle, oper);
            TaskAsRecursiveTask right = new TaskAsRecursiveTask(array, middle, to, oper);

            right.fork(); // zadanie  right zostanie dodane do kolejki i wykonane asynchronicznie, inny wątek może je 'ukrasc' i wykonać, jeśli będzie wolny
           return left.invoke().append(right.join()); //łączymy wyniki zadania lewego i prawego i zwracamy
        }


    }
}
