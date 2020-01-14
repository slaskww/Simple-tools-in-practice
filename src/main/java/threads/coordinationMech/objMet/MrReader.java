package threads.coordinationMech.objMet;


/**
 * W metodzue run synchronizujemy obiekt Coordination, by móc skoordynować wydruki MrWritera i MrReadera.
 * Inaczej, po zakończeniu wykonywania metody coord.read (która działa odblokowująco na wątek MrWritera), wątek MrWriter mógłby rozpocząć działanie (wydrukować komunikat) zanim
 * MrWriter zakończy wydruk odczytanej informacji.
 */

public class MrReader implements Runnable {

    private final Coordinator coord;

    public MrReader(Coordinator coord) {
        this.coord = coord;
    }

    @Override
    public  void run() { //metoda w nieskończonej pętli wywołuje metodę read próbując odczytać tekst koordynatora

        String txt = null;
        while(true){

        synchronized (coord){ //synchronizujemy obiekt Coordination, by móc skoordynować wydruki MrWritera i MrReadera
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

    private synchronized void printIt(String txt) throws InterruptedException {

        System.out.print("\tMr Reader read: ");
        for (char c : txt.toCharArray()){
            System.out.print(c+"");
            Thread.sleep(100);
        }
        System.out.println();    }
}
