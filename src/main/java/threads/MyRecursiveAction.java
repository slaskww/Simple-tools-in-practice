package threads;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.function.UnaryOperator;

/**
 * Tworzymy klasę zadania typu ForkJoinTask. Korzystamy z gotowej abstrakcyjnej klasy RecursiveAction posiadającej
 * jedną abstrajkcyjną metode compute(), którą musimy zaimplementować. Zadania tego typu nie zwracają wyniku.
 *
 * Zadanie polega na zmianie wartość wszystkich elementów tablicy wartości double.
 * Metoda set() ustawia próg podziału zadania na mniejsze podzadania oraz przypisuje referencję do implementacji interfejsu modyfikującego elementy tablicy
 *
 * W metodzie compute() zawsze sprawdzamy istnienie warunku bazowego (tak, jak to się odbywa przy zwykłym rozwiązaniu zadania rekurencyjnego). Jest nim
 * sytuacja, kiedy długość tablicy którą reprezentuje różnica indeksów 'to' i 'from' będzie mniejsza niż wielkość progu podziału.
 * W sytuacji bazowej wątek rozwiązuje zadanie, wykonując działania  na elementach tablicy.
 * Gdy nie mamy do czynienia z sytuacją bazową, wątek dzieli zadanie na dwa podzadania.
 * Na jednym z nich wywołujemy metodę invoke(), a na drugim metody fork() a następnie join();
 *
 */
public class MyRecursiveAction<T> extends RecursiveAction {

    private static int THRESHOLD; //próg dzielenia zadania na subzadania
    private static UnaryOperator CALC; //zmienna z referencją na implementację interfejsu funkcyjnego UnaryOperator przyjmującego obiekt T i zwracający obiekt tego samego typu

    private T[] array;
    private int from, to;

    public MyRecursiveAction(T[] array, int from, int to) {
        this.array = array;
        this.from = from;
        this.to = to;
    }

    public static void set(int threshold, UnaryOperator calc){
        THRESHOLD = threshold;
        CALC = calc;
    }

    @Override
    protected void compute() {

        if (to - from < THRESHOLD){
            UnaryOperator<T> calc = MyRecursiveAction.CALC;
            for (int i = from; i < to; i++){
                array[i] = calc.apply(array[i]);
            }
        } else{
            int middle = (to + from) /2;
            System.out.println("middle="+middle);
            MyRecursiveAction<T> leftSubTask = new MyRecursiveAction<>(array, from, middle);
            MyRecursiveAction<T> rightSubTask = new MyRecursiveAction<>(array, middle, to);

            leftSubTask.invoke(); //wątek zajmie się tym zadaniem, wywołuje compute()
            rightSubTask.fork(); //to zadanie zostanie dodane do kolejki i wykonane asynchronicznie, inny wątek może je 'ukrasc'
            rightSubTask.join(); //gdy zadanie zostanie zakończone, nastąpi złączenie rezultatów podzadań
        }

        System.out.println(Arrays.toString(array) + " w ");
    }
}
