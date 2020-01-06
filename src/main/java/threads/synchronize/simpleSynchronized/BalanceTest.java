package threads.synchronize.simpleSynchronized;

import java.util.concurrent.*;

/**
 * (1)
 * Ryglowanie w ogólnym pojęciu polega na zapewnieniu wyłącznego dostępu do pól obiektu lub pól statycznych klasy, realizowanym przez synchronizowane metody get() i set()
 * Dodatkowo ryglowanie zabezpiecza fragmenty kodu przed równoczesnym wywołaniem z różnych wątków.
 * Kod który może być wykonywany w danej chwili tylko przez jeden wątek nazywamy sekcją krytyczną.
 * Taką sekcję (jedną) mogą stanowić np. dwie synchronizowane metody get() i set() obiektu.
 *
 * Podstawowy mechanizm ryglowania zapewnia opatrzenie metody słowem synchronized, lub wykorzystanie instrukcji synchronized wewnątrz metody.
 * Rygle zapobiegają równoczesnemu działaniu wątków na tym samym obiekcie (m.in. zapobiegają równoczesnej zmianie stanu obiektu przez różne wątki).
 *
 * Jeśli w danym wątku wywoływana jest na rzecz jakiegoś obiektu metoda synchronizowana, następuje zamknięcie rygla na tym obiekcie (rygiel dotyczy sekcji krytycznej obiektu).
 * Jeśli w tym czasie inne wątki próbują wywołać na tym obiekcie jakąś synchronizowaną metodę lub wykonać instrukcję synchronized z referencją do tego obiektu,
 * są blokowane i oczekują na zwolnienie rygla (poprzez zakończenie metody synchronizowanej lub wyrzucenie wyjątku ).
 * Poniższy przykład pokazuje, że w sytuacji zaryglowania obiektu w jednym wątku, wciąż można wywoływac na nim metody (w innych wątkach), które nie są synchronizowane.
 *
 * Blok synchronizujący przyjmuje w nawiasie referencję do:
 * obiektu (zakładając blokacę na tym obiekcie) lub
 * obiektu-klasy (zakładajac blokadę na klasie) synchronizując dostęp do metod statycznych
 */

public class BalanceTest {

    public static void main(String[] args) {

        Balance balance = new Balance();
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 1; i <= 4; i++) {
            exec.execute(new BalanceFuture(new BalanceTask(balance, 10), "T" + i)); //wątek pierwszy przejmuje obiekt Balance i ma wyłączny dostę do jego sekcji krytycznej
            exec.execute(() -> balance.unsynchronizedMeth()); //wątek drugi wciąż ma dostęp do metod kóre nie są częścią sekcji krytycznej (nei posiadają słowa synchronized)
        }
        exec.shutdown();
    }
}
