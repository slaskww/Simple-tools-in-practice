package threads;

public class Counter implements Runnable {

private final int NUMBER;
private String name;

    public Counter(int number, String name) {
        NUMBER = number;
        this.name = name;
    }

    @Override
    public void run() {

        int sum = 0;
        for (int i = 1, k = 0; i <= NUMBER; i++){
            if (i % 1000 == 0){
                System.out.println(name + " ... " + (k += 1000));
                sum += i;
            }
        }
        System.out.println(name + " sum: " + sum);
    }
}
