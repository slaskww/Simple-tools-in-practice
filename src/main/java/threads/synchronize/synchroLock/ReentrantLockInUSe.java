package threads.synchronize.synchroLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockInUSe {

    private ReentrantLock lock = new ReentrantLock();

    private int number;

    public ReentrantLockInUSe(int number) {
        this.number = number;
    }

    private void increase(String name) throws InterruptedException {

        lock.lock();
        System.out.println("Thread " + name + ": initial value = " + number);
        number++;
        Thread.sleep(1000);
        System.out.println("Thread " + name + ": increased value = " + number);
        lock.unlock();

    }

    public void executeThreads(int tnumber){

        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < tnumber; i++){
            int n = i+1;
            Runnable runnable = () -> {
                try {
                    increase(""+n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            exec.execute(runnable);
        }
        exec.shutdown();
    }

    public static void main(String[] args) {

        ReentrantLockInUSe reentrantLockInUSe = new ReentrantLockInUSe(0);
        reentrantLockInUSe.executeThreads(5);
    }
}
