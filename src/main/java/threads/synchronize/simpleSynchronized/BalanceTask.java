package threads.synchronize.simpleSynchronized;

import java.util.concurrent.Callable;

public class BalanceTask implements Callable<Integer> {


    private Balance b;
    private int count;

    public BalanceTask(Balance b, int count) {
        this.b = b;
        this.count = count;
    }

    @Override
    public Integer call() throws Exception {

        int result = 0;
        for (int i = 0; i < count; i++){
             result = b.balance();

            if (result != 0){
                break;
            }
        }
        return result;
    }
}
