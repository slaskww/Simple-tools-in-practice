package threads.basicExecutors;

/**
 * KOD WYKONUJĄCY SIĘ W WĄTKU określany jest przez obiekt klasy implementującej interfejs funkcyjny RUNNABLE ('uruchamialny') i zawierający deklarację metody run().
 * W naszym przypadku jest to obiekt MyTimer, który implementuje interfejs Runnable i implementuje metode run().
 * Kod metody run() będzie wykonywany w osobnym wątku. Sam obiekt MyTimer zostanie przekazany w parametrze do obiektu wątku (klasa Thread)
 *
 * Statyczna metoda interrupted() sprawdza, czy flaga przerwania jest ustawiona, jeśli tak, to kończy metode run()
 * Jeśli flaga wątku ustawiona jest na 'przerwany' (dzieje się tak po wcześniejszym wywołaniu metody interrupt() z pozycji innego wątku),
 * to zatrzymanie wątku spowoduje powrotną flagi na 'nieprzerwany' i wyrzucenie wyjątku InterruptedException, który mżemy obsłuży  kończąc metodę run()
 */
public class MyTimer implements Runnable {
    @Override
    public void run() {

        int time = 0;
        int mins, sec;

         while (true) {
            if (Thread.interrupted()) return; //statyczna metoda interrupted() sprawdza, czy flaga przerwania jest ustawiona (zwraca wartość true), jeśli tak, to kończy metode run()

            try {
                Thread.sleep(1000); // flaga wątku ustawiona na 'przerwany' spowoduje wyrzucenie wyjątku InterruptedException
            } catch (InterruptedException e) {
                return;
            }

            time++;
            mins = time / 60;
            sec = time % 60;
            System.out.println("Upłynęło: " + (mins < 10 ? "0" + mins : mins) + ":" + (sec < 10 ? "0" + sec : sec));
        }
    }
}
