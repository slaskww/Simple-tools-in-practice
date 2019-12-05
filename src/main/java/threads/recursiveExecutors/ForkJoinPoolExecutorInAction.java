package threads.recursiveExecutors;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * ForkJoinPool jest specjalnym rodzajem wykonawcy. Został stworzony do rozwiązywania zadań rekursywnych, czyli takich,
 * które dadzą się podzielić na mniejsze podzadania i mogą być rozwiązywane niezależnie od siebie.
 *
 * Inaczej niż w przypadku ThreadPoolExecutorów, w ForkJoinPool kazdy z wątków realizujących zadania ma
 * zaimplementowaną własna kolejkę zadań. Wątki mogą tworzyć własne zadania i wstawiac je na początek swojej kolejki.
 * Kazdy wątek pobiera zadania z początku swojej kolejki. Jeśli wątek nie ma aktualnie zadań w kolejce, może 'podkradać' zadania innym wątkom.
 * Taki wątek 'kradnie' zadanie z konca kolejki innego wątku.
 *
 * Zadania realizowane przez Wykonawcę są zawsze typu ForkJoinTask. Obiekt ForkJoinTask posiada wszystkie metody obiektu Future (m.in get(), cancel(), isDone(), ...),
 * oraz dodatkowo:
 *      fork() - obiekt zadania ForkJoinTask na którym wykonano metodę zostaje odłożony do kolejki i może być 'skradziony' przez wolny wątek
 *      invoke() - obiekt zadania ForkJoinTask na którym wykonano metodę będzie realizowany przez wątek, który jest jego właścicielem, metoda wykonuje zadanie i zwraca jego wynik
 *              a'pod spodem' wywoła metode compute() zadania ForkJoinTask
 *      join() - czeka na zakończenie zadania na którym wcześniej wykonano metodę fork() i może zwrócić jego wynik (w przypadku obiektu RecursiveTask)
 *
 *  Klasa ForkJoinTask jest abstrakcyjna, więc by móc z niej skorzystać, należy zdefiniować jej metody w klasie dziedziczącej po ForkJoinTask.
 *  Alternatywnie możemy skorzystać z trzech gotowych już klas dziedziczących klasę ForkJoinTask:
 *      RecursiveAction - reprezentuje rekursywne zadanie niezwracające wyniku
 *      RecursiveTask - określa reurencyjne zadanie zwracające wynik
 *      CountedCompleter - stosowana gdy kończone zadania sygnalizują możliwość zakończenia innych zadań w hierarchii zależności zadań
 *  klasy te rozszerzamy i dodatkowo implementujemy metode compute().
 *
 */

public class ForkJoinPoolExecutorInAction {



    public static void recursiveActionInUse(){
        int size = 100;
        Double[] array = new Double[size];
        Arrays.fill(array, 3.5);

        TaskAsRecursiveAction<Double> task = new TaskAsRecursiveAction<Double>(array, 0, array.length);
        UnaryOperator<Double> unaryOp = aDouble -> aDouble * 2;
        TaskAsRecursiveAction.set(3, unaryOp);

        //przy wykonywaniu zadania korzystamy z tzw. wspólnej puli typu ForkJoinPool. domyślnie jej stopień zrównoleglenia wynosi liczba procesorów - 1
        //task.invoke();

        //alternatywnie możemy podać zadanie typu ForkJoinTask Wykonawcy stworzonemu przez metodę fabryczną z Executors i wywołać metodę invoke()
        ForkJoinPool exec = (ForkJoinPool) Executors.newWorkStealingPool();
        exec.invoke(task);

        //lub stworzyć nową instancję wykonawcy bez użycia metody fabrycznej
        //ForkJoinPool exec2 = new ForkJoinPool();
        //exec2.invoke(task);
        System.out.println("Skradzionych zadań: " + exec.getStealCount());
    }


    public static void recursiveTaskInUse(){

        String[] array = {"ARRAY", "BINARY", "CHARACTER", "DIRECTORY", "ENTITY", "FORK",
                "GRAPH", "HIERARCHY", "INPUTSTREAM", "JAVA", "KEY", "LINE", "MAP",
                "NIO", "OBJECT", "PREDICATE", "RETURN", "SERIALIZABLE", "TIME", "UTIL", "WRITER", "ZERO"};

        Function<String, Character> oper = (s) -> s.toLowerCase().charAt(0);

        ForkJoinTask task = new TaskAsRecursiveTask(array, 0, array.length, oper);
        String result =  task.invoke().toString();
        System.out.println(result);
    }

    public static void main(String[] args) {

       // recursiveActionInUse();
        recursiveTaskInUse();
    }


}

