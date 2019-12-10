package threads.synchronize.simpleSynchronized;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BalanceFuture extends FutureTask<Integer> {

    private String name;

    public BalanceFuture(BalanceTask balanceTask, String name) {
        super(balanceTask);
        this.name = name;
    }

    @Override
    protected void done() {
        try {
            System.out.println("Wynik Wątku " + name + ": " + get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}




