package threads.basicExecutors;

import org.junit.Test;

import java.time.LocalTime;
import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * (4)
 */

public class RepeatedTaskTest {

    /**
     * Test unaocznia zasadę, którą kieruje się Wykonawca (inny niż Scheduled)
     * Zasada mówi, że raz wykonane zadanie (obiekt FutureTask) nie może być wykonane ponownie przez tego samego Wykonawce.
     *
     */

    @Test
    public void shouldNotRepeatAlreadyExecutedFutureTask(){

        Executor exec = Executors.newSingleThreadExecutor();
        Callable<String> callableTask = getCallable();

        FutureTask<String> futureTask = new FutureTask<>(callableTask);
        exec.execute(futureTask);
        String result = getRes(futureTask);

        exec.execute(futureTask);
        String repeatedResult = getRes(futureTask);

        assertEquals(result, repeatedResult); //repeatedResult ma wartość result, co oznacza, że ponowna próba wykonania zadania nie powiodła się, został zwrócony wynik zadania pierwotnie wykonanego
    }

    /**
     * Test pokazuje, że to samo Callable<T> możemy ponownie przekazywać temu samemu Wykonawcy.
     * W teście dwukrotnie wywołujemy metodę submit(Callable<T>).
     * Metoda ta za każdym razem tworzy nowe zadanie (nowy obiekt FutureTask).
     */

    @Test
    public void shouldRepeatAlreadyExecutedTaskAsNewFutureTask(){

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<String> task = getCallable();

        Future<String> fut = exec.submit(task);
        String firstResult = getRes(fut);

        fut = exec.submit(task);
        String secondResult = getRes(fut);

        assertNotEquals(secondResult, firstResult); //wyniki są różnie, co oznacza, że egzekutor wykonał dwa zadania

    }

    private Callable<String> getCallable(){
        return () -> {
            String result = "";

            for (int i = 0; i < 3; i++){
                result += LocalTime.now() + " ";
            }
            return result;
        };
    }

    private String getRes(Future<String> fut){
        try {
            return fut.get();
        } catch (InterruptedException | ExecutionException e) {
            return "thrown InterruptedException";
        }
    }
}
