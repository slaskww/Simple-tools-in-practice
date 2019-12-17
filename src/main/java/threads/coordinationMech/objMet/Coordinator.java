package threads.coordinationMech.objMet;

public class Coordinator {

    private String txt = null;
    private boolean isNew = false;



    public synchronized void write(String txt) throws InterruptedException {

        while(isNew) this.wait();
        this.txt = txt;
        isNew = true;
        this.notify();
    }

    public synchronized String read() throws InterruptedException {

        while(!isNew) wait();
        isNew = false;
        this.notify();
        return txt;
    }
}
