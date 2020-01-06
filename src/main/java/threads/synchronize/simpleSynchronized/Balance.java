package threads.synchronize.simpleSynchronized;

/**
 * (1)
 * Ryglowanie w ogólnym pojęciu polega na zapewnieniu wyłącznego dostępu do pól obiektu lub pól statycznych klasy, realizowanym przez synchronizowane metody get() i set()
 * Dodatkowo ryglowanie zabezpiecza fragmenty kodu przed równoczesnym wywołaniem z różnych wątków.
 * Kod który może być wykonywany w danej chwili tylko przez jeden wątek nazywamy sekcją krytyczną.
 * Taką sekcję (jedną) mogą stanowić np. dwie synchronizowane metody get() i set() obiektu.
 *
 *  Podstawowy mechanizm ryglowania zapewnia opatrzenie metody słowem synchronized, lub wykorzystanie instrukcji synchronized wewnątrz metody.
 * Rygle zapobiegają równoczesnemu działaniu wątków na tym samym obiekcie.
 *
 * Jeśli w danym wątku wywoływana jest na rzecz jakiegoś obiektu metoda synchronizowana, następuje zamknięcie rygla na tym obiekcie.
 * Jeśli w tym czasie inne wątki próbują wywołać na tym obiekcie jakąś synchronizowaną metodę lub wykonać instrukcję synchronized z referencją do tego obiektu,
 * są blokowane i oczekują na zwolnienie rygla (poprzez zakończenie metody synchronizowanej lub wyrzucenie wyjątku ).
 * Poniższy przykład pokazuje, że w sytuacji zaryglowania obiektu w jednym wątku, wciąż można wywoływac na nim metody (w innych wątkach), które nie są synchronizowane.
 *
 * Blok synchronizujący przyjmuje w nawiasie referencję do:
 * obiektu (zakładając blokacę na tym obiekcie) lub
 * obiektu-klasy (zakładajac blokadę na klasie) synchronizując dostęp do metod statycznych
 */

public class Balance {

    private int n = 0;

    synchronized public int balance(){
        System.out.println("balance lock()");
        n++;
        n++;
        n--;
        n--;
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("balance unlock()");
        return n;
    }

    public void unsynchronizedMeth(){
        System.out.println("Unsynchronized method invoked.");
    }
}
