package threads.coordinationMech.classCondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * (3)
 * Alternatywą dla mechnanizmu wait-notify jest mechanizm przy użyciu metod klasy Condition z biblioteki java.util.concurrent.
 * Obiekty warunków (Condition) pobieramy z obiektu Lock. Tak więc tworzone warunki zawsze osadzone są w kontekście jakiejś blokady.
 * Obiekty warunków posiadają dwie kluczowe metody:
 *      - await() zatrzymujący bieżący wątek do czasu, aż inny wątek nie wywoła na rzecz tego warunku metody signal()
 *      -signal() - metoda budzi watek, w którym wcześniej została wywołana metoda await()
 *      -signalAll() - budzi wszystkie czekajace wątki
 *
 *  Uwaga: samo wywołanie metody signal() mimo, że budzi wątek, nie sprawia, że ma on automatyczny dostęp do sekcji krytycznej.
 *  Musi on zaczekać, aż bieżący wątek otworzy blokadę i udostępni tę sekcję.
 *  Opisowe przedstawienie działania mechanizmu Lock i condition zawiera klasa ConditionInDetails.
 *
 * lock() zakłada blokade na sekcję krytyczną (w naszym przykładzie jedną sekcję tworzą oba bloki  kodu zawierające lock() - unlock() ),
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
                while(newTxt) {
                    txtWritten.await(); // await powoduje, że rygiel jest otwierany, wątek przechodzi do stanu WAITING, do momentu aż inny wątek nie wywyoła metody signal() lub nie wystąpi wyjątek
                    }
                this.txt = txt;
                newTxt = true;
                txtRead.signal(); //wątek którego kod wywołał txtRead.await() wraca do stanu RUNNABLE
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
    }

     public  String getTxt(){

        lock.lock();
            try {
                while(!newTxt) txtRead.await(); // await powoduje, że rygiel jest otwierany, wątek przechodzi do stanu WAITING, do momentu aż inny wątek nie wywyoła metody signal() lub nie wystąpi wyjątek
                newTxt = false;
                txtWritten.signal(); //wątek którego kod wywołał  txtWritten.await() wraca do stanu RUNNABLE
                return txt;

            } catch (InterruptedException e) {
                return null;
            } finally {
                lock.unlock();
            }
    }
}
