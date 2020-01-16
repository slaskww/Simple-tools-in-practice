package threads.coordinationMech.classCondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * (3)
 * Alternatywą dla mechnanizmu wait-notify jest mechanizm przy użyciu metod klasy Condition z biblioteki java.util.concurrent.
 * Obiekty warunków (Condition) pobieramy z obiektu Lock. Tak więc tworzone warunki zawsze osadzone są w kontekście jakiejś blokady.
 * Obiekty warunków posiadają dwie kluczowe metody:
 *      - await() - (analogia do wait z Object) zatrzymujący bieżący wątek do czasu, aż inny wątek nie wywoła na rzecz tego warunku metody signal()
 *      -signal() - (analogia do notify z Object) metoda budzi watek, w którym wcześniej została wywołana metoda await()
 *      -signalAll() - budzi wszystkie czekajace wątki
 *
 *  Uwaga: samo wywołanie metody signal() mimo, że budzi wątek, nie sprawia, że ma on automatyczny dostęp do sekcji krytycznej.
 *  Musi on zaczekać, aż bieżący wątek otworzy blokadę i udostępni tę sekcję.
 *  Opisowe przedstawienie działania mechanizmu Lock i condition zawiera klasa ConditionInDetails.
 *
 * lock() zakłada blokade na sekcję krytyczną (w naszym przykładzie jedną sekcję krytyczną tworzą oba bloki  kodu zawierające lock() - unlock() ),
 * więc pozostałe wątki muszą czekać na jej zwolnienie, bądź na wywołanie metody await().
 */

public class Texts {

    private Lock lock = new ReentrantLock();
    private Condition txtWritten = lock.newCondition();
    private Condition txtRead = lock.newCondition();

    private String txt;
    private boolean newTxt = false;

     public void setTxt(String txt){
        lock.lock(); //rygiel jest zamykany
            try {
                while(newTxt) txtWritten.await(); // await powoduje, że rygiel jest otwierany, wątek przechodzi do stanu WAITING, do momentu aż inny wątek nie wywyoła metody signal() lub nie wystąpi wyjątek
                System.out.println("w setTxt, getTxt dał mi sygnał, lub dopiero startuję. Działam");
                this.txt = txt;
                newTxt = true;
                txtRead.signal(); //wątek którego kod wywołał txtRead.await() wraca do stanu RUNNABLE, ale będzie on mógł wywołać metodę getTxt dobiero, gdy bieżący lock zostanie otwarty.
                Thread.sleep(5000);
                System.out.println("w setTxt, po tym, jak dalem sygnał do txt.read");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("otwieram lock w setTxt");
                lock.unlock();
            }
    }

     public  String getTxt(){

        lock.lock();
            try {
                while(!newTxt) txtRead.await(); // await powoduje, że rygiel jest otwierany, wątek przechodzi do stanu WAITING, do momentu aż inny wątek nie wywyoła metody signal() lub nie wystąpi wyjątek
                System.out.println("w getTxt, setTxt dał mi sygnał. Działam");
                newTxt = false;
                txtWritten.signal(); //wątek którego kod wywołał  txtWritten.await() wraca do stanu RUNNABLE, ale będzie on mógł wywołać metodę getTxt dobiero, gdy bieżący lock zostanie otwarty.
                Thread.sleep(2000);
                System.out.println("w getTxt, po tym, jak dalem sygnał do txtWritten");
                return txt;

            } catch (InterruptedException e) {
                return null;
            } finally {
                lock.unlock();
            }
    }
}
