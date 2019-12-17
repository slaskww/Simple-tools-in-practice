package threads.coordinationMech.objMet;

public class MrReader implements Runnable {

    private final Coordinator coord;

    public MrReader(Coordinator coord) {
        this.coord = coord;
    }

    @Override
    public synchronized void run() {

        String txt = null;
        while(true){

        synchronized (coord){
            try {
                txt = coord.read();
            } catch (InterruptedException e) {
                return;
            }
            if (txt == null) return;
            try { printIt(txt); } catch (InterruptedException e) { return; }
        }
        }
    }

    private void printIt(String txt) throws InterruptedException {

        System.out.print("\tMr Reader read: ");
        for (char c : txt.toCharArray()){
            System.out.print(c+"");
            Thread.sleep(100);
        }
        System.out.println();    }
}
