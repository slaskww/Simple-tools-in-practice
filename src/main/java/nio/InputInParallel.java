package nio;

/**
 * Tradycyjne wejście - wyjście w Javie jest blokujące, tzn, wątek, który próbuje odczytać dane ze strumienia jest blokowany, jeśli danych w strumieniu jeszcze nie ma.
 * Brak tam mechanizmów równoległego przetwarzania tego samego pliku. Koncepcja kanałów pozwala rozwiązać ten problem.
 * Kanał reprezentuje otwarte połączenie do obiektu (pliku, gniazda sieciowego). Kanały podłączone do plików umożliwiają nieblokujące i asynchroniczne wejścia - wyjścia.
 * Wszystkie kanały mają wspólną właściwość: kanały wejściowe wprowadzają dane do buforów bajtowych, a kanały wyjściowe odbierają dane z buforów bajtowych.
 *
 */

public class InputInParallel {

    public static void main(String[] args) throws InterruptedException {

        testWithChannel();
        testWithoutChannel();

    }

    public static void testWithChannel() throws InterruptedException {

        SingleInputWithFileChannel[] threads = {
                new SingleInputWithFileChannel(),
                new SingleInputWithFileChannel(),
                new SingleInputWithFileChannel(),
                new SingleInputWithFileChannel()
        };

        long start = System.currentTimeMillis();

        for (SingleInputWithFileChannel thread : threads){
            thread.start();
        }
        for (SingleInputWithFileChannel thread : threads){
            thread.join();
        }
        long stop = System.currentTimeMillis();

        System.out.println("Całkowity czas (sec.): " + (double) ((stop - start) / 1000));
    }

    public static void testWithoutChannel() throws InterruptedException {

        SingleInputWithDataInputStream[] threads = {
                new SingleInputWithDataInputStream(),
                new SingleInputWithDataInputStream(),
                new SingleInputWithDataInputStream(),
                new SingleInputWithDataInputStream()

        };

        long start = System.currentTimeMillis();

        for (SingleInputWithDataInputStream thread : threads){
            thread.start();
        }

        for (SingleInputWithDataInputStream thread : threads){
            thread.join();
        }

        long stop = System.currentTimeMillis() - start;

        System.out.println("Całkowity czas (sec.): " + (double) (stop / 1000));
    }
}
