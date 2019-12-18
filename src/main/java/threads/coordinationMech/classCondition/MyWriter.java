package threads.coordinationMech.classCondition;

public class MyWriter implements Runnable{

    private Texts texts;

    public MyWriter(Texts texts) {
        this.texts = texts;
    }

    @Override
    public void run() {

        String[] txts = {"“Will", "this", "agony", "never", "end","?“",""};
        for (String s : txts){
            texts.setTxt(s);
        }
    }
}
