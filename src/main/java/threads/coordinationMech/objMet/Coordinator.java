package threads.coordinationMech.objMet;

public class Coordinator {

    private String txt = null;
    private boolean isNew = false;



    public synchronized void write(String txt) throws InterruptedException {

        while(isNew) this.wait(); //wątek zostaje zatrzymany jeśli jest nowy tekst i inny wątek go jeszcze nie pobrał i nie wywołał met. notify()
        this.txt = txt;
        isNew = true;
        this.notify(); //powiadom wątek oczekujący
    }

    public synchronized String read() throws InterruptedException {

        while(!isNew) wait(); //wątek zostaje zatrzymany jeśli nie ma nowego tekstu do pobrania (isNew = false) i inny wątek nie wywołał met. notify()
        isNew = false;
        this.notify();
        return txt;
    }
}
