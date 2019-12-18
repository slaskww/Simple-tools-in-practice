package threads.coordinationMech.objMet;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ryglowanie (dokonywane przy użyciu słowa synchronized lub obiektów typu Lock) zapobiega niechcianym interakcjom między wątkami.
 * By wątki mogły współdziałać, potrzebujemy dodatkowo mechanizmu ich koordynacji.
 * Może nam bowiem zależeć, by wątki zachowywały pewną kolejność działań w ramach wzajemnej interakcji.
 * Podstawowy mechanizm skoordynowania interakcji między wątkami umożliwiają metody klasy Object:
 *  -wait(), w wersji z argumentem oznaczającym max. czasem oczekiwania
 *  -notify()
 *  -notifyAll()
 *
 * Koordynacja sprowadza sie do poniższych kroków:
 *      -wątek A wywołuje metodę wait() na rzecz obiektu O, oczekując zmiany stanu tego obiektu (zmiany wartości pól obiektu dokonanej przez inny wątek)
 *      -wywolanie tej metody blokuje wątek A (odsuwa go od procesora) a jednocześnie zwalnia rygiel na obiekcie O, udostępniając go innym wątkom
 *      -wywołanie metody wait() wywołujemy w sekcji krytycznej (metodzie synchronized), w której obiekt O jest ryglowany
 *      -teraz wątek B może zmienić stan obiektu O i wywołać na nim metode notify() powiadamianąc wątek oczekujący o dokonanej zmianie
 *      -odblokowanie ze stanu wait nastepuje również w wypadku gdy rzucony zostanie wyjątek InterruptedException (w sytuacji gdy wątek otrzyma sygnał interrupt)
 *      -notify() odblokowuje jeden z oczekujących wątków (może to byc dowolny z nich), notifyAll() odblokowuje wszystkie wątki czekające na obiekcie O
 *      -metody notify() i notifyAll() również powinny być wywołane w sekcji krytycznej
 *
 *  Warunek zakończenia oczekiwania (stan flagi) należy sprawdzać w pętli .
 *
 *      Przykład opiera się na dwóch wątkach wykonujących zadanie typu Runnable (MrWriter i MrReader). Każdy zwątków operuje na tym samym obiekcie Coordinator.
 *      Zmiana stanu obiektu Coordinator dokonana przez jeden wątek wywołuje interakcję drugiego wątku w postaci odczytu tej zmiany.
 *      Odczyt powoduje interakcję watku pierwszego i kolejną zmianę stanu obiektu Coordinator, itd.
 */

public class MyMain {


    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        Coordinator coord = new Coordinator();
        exec.submit(new MrWriter(coord, Arrays.asList("Hi dude, it's me, Writer.",
                "I'm testing coordination mechanism implemented in Object class.",
                "It is the most basic way to coordinate the work of our threads.",
                "In fact, there are much easier ways to do the job.",
                "Take a glimpse at blocking queues.", null)));
        exec.submit(new MrReader(coord));
        exec.shutdown();
    }

}
