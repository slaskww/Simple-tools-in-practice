package threads.synchronize;

public class Balance {

    private int n = 0;

    public int balance(){
        n++;
        n++;
        n--;
        n--;

        return n;
    }
}
