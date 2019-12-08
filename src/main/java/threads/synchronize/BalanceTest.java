package threads.synchronize;

import java.util.concurrent.*;

public class BalanceTest {

    public static void main(String[] args) {


        Balance balance = new Balance();
        ExecutorService exec = Executors.newCachedThreadPool();


        int result = 0;
        for (int i = 1; i <= 4; i++) {
            exec.execute(new BalanceFuture(new BalanceTask(balance, 100000), "T" + i));
        }
        exec.shutdown();


    }
}
