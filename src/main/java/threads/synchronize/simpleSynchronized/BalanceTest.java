package threads.synchronize.simpleSynchronized;


import java.util.concurrent.*;

public class BalanceTest {

    public static void main(String[] args) {


        Balance balance = new Balance();
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 1; i <= 4; i++) {
            exec.execute(new BalanceFuture(new BalanceTask(balance, 100000), "T" + i));
        }
        exec.shutdown();


    }
}
