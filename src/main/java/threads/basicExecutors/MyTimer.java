package threads.basicExecutors;

/**
 * KOD WYKONUJĄCY SIĘ W WĄTKU określany jest przez obiekt klasy implementującej interfejs funkcyjny RUNNABLE ('uruchamialny') i zawierający deklarację metody run().
 * W naszym przypadku jest to obiekt MyTimer, który implementuje interfejs Runnable i implementuje metode run().
 * Kod metody run() będzie wykonywany w osobnym wątku. Sam obiekt MyTimer zostanie przekazany w parametrze do obiektu wątku (klasa Thread)
 *
 *
 */
public class MyTimer implements Runnable {
    @Override
    public void run() {

        int time = 0;
        int mins, sec;

        while (true){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            time++;
            mins = time / 60;
            sec = time % 60;
            System.out.println("Upłynęło: " + (mins < 10 ? "0" + mins : mins) + ":" + (sec < 10 ? "0" + sec : sec));
        }
    }
}
