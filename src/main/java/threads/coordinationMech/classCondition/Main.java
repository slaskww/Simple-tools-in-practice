package threads.coordinationMech.classCondition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        Texts texts = new Texts();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new MyReader(texts));
        exec.submit(new MyWriter(texts));
        exec.shutdown();
    }

}
