package threads.coordinationMech.classCondition;

public class MyReader implements Runnable {

    private Texts texts;

    public MyReader(Texts texts) {
        this.texts = texts;
    }

    @Override
    public void run() {

        String t = texts.getTxt();
        while(!t.equals("")){
            System.out.print(t+" ");
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            t = texts.getTxt();
        }
    }
}
