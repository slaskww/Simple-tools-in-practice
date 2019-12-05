package threads.basicExecutors;


import java.util.concurrent.Callable;

/**
 * N-ty wyraz ciągu Fibonacciego obliczamy jako sumę jego dwóch wcześniejszych wyrazów:  n = (n-1) + (n-2) dla n >= 2
 * Algorytm na n-ty element ciągu fibo obliczamy rekurencyjnie. Warunkiem bazowym jest n1 = 1 i n0 = 0
 */

public class FiboWithCallable implements Callable<Long> {

    private final int number;

    public FiboWithCallable(int number) {
        this.number = number;
    }

    private long fibo(int n){

        return n < 2 ? n : fibo(n-1) + fibo(n -2);
    }

    @Override
    public Long call() throws Exception {

        return fibo(number);
    }
}
