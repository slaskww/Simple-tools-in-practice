package threads;

public class Timer implements Runnable {
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
