package threads.basicExecutors;

import org.junit.Test;

import java.time.LocalTime;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class RepeatedTaskTest {

    /**
     * Test unaocznia zasadę, którą kieruje się Wykonawca (inny niż Scheduled)
     * Zasada mówi, że raz wykonane zadanie (obiekt FutureTask) nie może być wykonane ponownie przez tego samego Wykonawce.
     *
     */

    @Test
    public void shouldNotRepeatAlreadyExecutedFutureTask(){

        Executor exec = Executors.newSingleThreadExecutor();
        Callable<String> callableTask = () -> {
            String res = "";
            for (int i = 0; i < 3; i++){
                res += LocalTime.now() + " ";
            }
            return res;
        };

        FutureTask<String> futureTask = new FutureTask<>(callableTask);
        exec.execute(futureTask);
        String result = "";
        try {
            result = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        exec.execute(futureTask);
        String repeatedResult = "";
        try {
            repeatedResult = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assertEquals(result, repeatedResult);
    }

    /**
     * Test pokazuje, że to samo Callable<T> możemy ponownie przekazywać temu samemu Wykonawcy.
     * W teście dwukrotnie wywołujemy metodę submit(Callable<T>).
     * Metoda ta za każdym razem tworzy nowe zadanie (nowy obiekt FutureTask).
     */

    @Test
    public void shouldRepeatAlreadyExecutedTaskAsNewFutureTask(){

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<String> task = () -> {
            String result = "";

            for (int i = 0; i < 3; i++){
                result += LocalTime.now() + " ";
            }
            return result;
        };
        Future<String> fut = exec.submit(task);
        String firstResult = "";

        try {
            firstResult = fut.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        fut = exec.submit(task);
        String secondResult = "";

        try {
            secondResult = fut.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assertNotEquals(secondResult, firstResult);

    }
}
