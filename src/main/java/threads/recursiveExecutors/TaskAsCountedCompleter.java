package threads.recursiveExecutors;

import java.util.concurrent.CountedCompleter;

/**
 * CountedCompleter jest klasa zadania dziedziczącą po abstrakcyjnej klasie ForkJoinTask
 * Specyfika takich zadań polega na tym, że mogą wyzwalać kończenie innych zadać typu CountedCompleter.
 *  np. subzadanie zadania task (nazwijmy je subTask) może wywołać zakonczenie zadania task wywołując jego metode tryComplete() a pośrednio metodę onCompletion().
 *  CountedCompleter posiada abstrakcyjną metode obliczeniową compute(), którą nadpisujemy.
 *  W zadaniu nadpisujemy metody:
 *      -compute() abstrakcyjna metode obliczeniową
 *      -onCompletion() metodą kończąca zadanie
 *      -getRawResult() metoda zwracająca wynik zadania
 *  oraz wywołujmey metody domyslne:
 *      -setPendingCount()
 *      -tryComplete()
 *
 * Podobnie jak w przypadku zadań typu RecursiveTask i RecursiveAction, wywołanie metody invoke() na obiekcie CountedCompleter (w metodzie main) wywołuje metodę obliczeniową compute().
 * W naszym przykładzie zadanie przyjmuje w konstruktorze licznik zadań do wykonania, obiekt zadania nadrzędnego (parent) i id zadania.
 * Metoda obliczeniowa coumpute() sprawdza czy licznik jest większy od 0, jeśli tak, to tworzy i uruchamia asynchronicznie nowe podzadanie.
 * Ustawia wartość zmiennej pending odzwierciedlającej liczbę podzadań w toku dla bieżącego zadania (setPendingCount()). Następnie wywouje metode tryComplete().
 *      Podzadanie otrzymuje w konstruktorze zdekrementowany licznik zadań i w metodzie compute() sprawdza czy jego wartość jest większa od 0.
 *      Jeśli tak jest, tworzy podzadanie i uruchamia je asynchronicznie. Zadanie to otrzymuje w konstruktorze zdekrementowany licznik, obiekt zadania nadrzędnego (parent) i id zadania.
 *      Następnie metoda compute() wywouje metode tryComplete()
 *          Sytuacja się powtarza (tworzone sa nowe zadania, wywoływana jest metoda compute() i tryComplete()) dopóki licznik zadań jest większy od 0;
 *
 *  Metoda tryComplete() wywoływana w compute() ma specjalną rolę w kończeniu zadania i działa w następujący sposób:
 *     - dla zadania bieżącego sprawdzana jest wartość zmiennej pending (liczba zadań w toku dla tego zadania)  i jeśli jest większa niż 0, to wartość zmiennej pending jest zmniejszana o 1 i metoda tryComplete kończy zadanie
 *       Jest natomiast jest równa 0, to wywoływana jest metoda onCopletion()
 *     - procedura sprawdzania wartości pending (i dalej jego dekrementację bądź wywołanie metody onCompletion() gdy pending = 0) powtarzana jest dla zadania ustanowionego w konstruktorze jako zadanie nadrzędne (parent).
 *       A to zadanie z kolei wykonuje takie same kroki dla swojego zadania nadrzędnego (parent) itd.
 *     - ostatecznie, jeśli tryComplete() dotrze do zadania które nie ma zadania nadrzędnego (subTask = null), to ustali jego stan na zakończone i skończy działanie.
 *
 */

public class TaskAsCountedCompleter extends CountedCompleter<String> {


    private char taskId;
    private int taskCount;
    private CountedCompleter subTask; //zmienna pozwala nam na zachowanie referencji do podzadania. Skorzystamy z niej w metodzie onCompletion() pobierając wynik dla tego podzadania
    private String result = "";


    public TaskAsCountedCompleter(CountedCompleter parentCountedCompleter, char taskId, int taskCount) {
        super(parentCountedCompleter); //nie możemy zapomnieć o wywołaniu konstruktora z nadklasy z argumentem zadania nadrzędnego (rodzica)
        this.taskId = taskId;
        this.taskCount = taskCount;
    }

    @Override
    public void compute() {

        /**
         * w naszym zadaniu inicjalnie (w konstruktorze) podajemy wartość licznika, który odzwierciedla liczbę zadań do wykonania. W instrukcji if odwołujemy się do tego licznika
         */
        if (taskCount > 0){

            char subTaskId = (char)(taskId + 1);
            TaskAsCountedCompleter toFork = new TaskAsCountedCompleter(this, subTaskId , --taskCount);
            toFork.fork();
            subTask = toFork;
            setPendingCount(1); //ustawiamy wartość zmiennej pending, która odzwierciedla liczbę podzadań do zakończenia dla zadania 'this'. W tym przypadku mamy jedno podzadanie (toFork) więc pending = 1

        }

        /**
         * Sztucznie opóźniamy dalsze wykonanie metody compute(), by uzyskać efekt w którym podzadanie toFork zakończy działanie przed poniższym blokiem kodu metody compute().
         * Usypiamy wątek realizujący bieżące zadanie, jeśli zadanie to ma id = 'K'.
         * Poskutkuje to tym, że zadanie toFork wyzeruje wartość zmiennej pending swojego parenta (zadanie this)
         * zanim zdąży zrobić to metoda tryComplete w biezącej metodzie compute().
         * Wywołanie tryComplete() w bieżącej metodzie complete() wywoła więc metode onCompletion() z argumetentem równym zadnaniu this.
         */
      if (taskId == 'K')  try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = String.valueOf(taskId);

        /**
         *  metode compute() kończymy wywołaniem tryComplete().
         *  Zmniejszy ona pending zadania do 0 lub gdy pending
         *  ma wartość 0 to wywoła metode onCompletion()
         */
        tryComplete();
    }

    /**
     * metoda otrzymuje w argumencie to zadanie (ten kompleter)
     * ktory wywołał metodę onCompletion(). Może to więc być zadanie bieżące lub jego podzadanie.
     * W metodzie sprawdzane jest, czy obiekt zadania wywołujący tę metodę to zadanie bieżące czy jego subzadanie.
     * Jeśli callerem jest subzadanie, to dodajemy jego wynik do wyniku zadania this.
     * Jeśli callerem jest this, to sprawdzamy, czy this ma jakieś subzadanie.
     * Jeśli ma, to pobieramy wynik z tego subzadania i dodajemy je do wyniku zadania this.
     */
    @Override
    public void onCompletion(CountedCompleter caller) {

        System.out.println("Zadanie " + this
                + " zakończone. Metodę kończącą onCompletion() wywołało zadanie "
                + caller + (caller != this ? " (podzadanie)" : " (bieżące zadanie)"));
        if (caller != this){
            result += caller.getRawResult();
        } else if (subTask != null){
            result += subTask.getRawResult();
        }

    }

    /**
     * metoda umożliwia pobranie wyniku dla zadania. Musimy posiłkować się tą metodą
     * gdyż metoda complete() z klasy CountedCompleter nie zwraca żadnego wyniku.
     * Jeśli nie przedefiniujemy tej metody, to domyślna definicja z klasy CountedCompleter zwróci wartość null
     */

    @Override
    public String getRawResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Task" + taskId;
    }
}
