package threads.coordinationMech.objMet;

import java.util.List;

public class MrWriter implements Runnable {

    private final Coordinator coord;
    private List<String> txts;

    public MrWriter(Coordinator coord, List<String> txts ) {
        this.coord = coord;
        this.txts = txts;
    }

    @Override
    public void run() {

     for (int i = 0; i < txts.size(); i++){
         try {

             synchronized (coord){ //synchronizujemy obiekt Coordination, by móc skoordynować wydruki MrWritera i MrReadera
                 coord.write(txts.get(i));
                 if (txts.get(i) != null) System.out.println("MrWriter sent the message.");
                 Thread.sleep(1000);
             }
         } catch (InterruptedException e) {
             return;
         }
     }
    }
}
