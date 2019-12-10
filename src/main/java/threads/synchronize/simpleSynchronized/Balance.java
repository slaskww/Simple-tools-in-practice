package threads.synchronize.simpleSynchronized;

public class Balance {

    private int n = 0;

    synchronized public int balance(){
        n++;
        n++;
        n--;
        n--;

        return n;
    }
}
