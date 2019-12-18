package threads.coordinationMech.classCondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
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
