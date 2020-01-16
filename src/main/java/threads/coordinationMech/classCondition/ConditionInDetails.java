package threads.coordinationMech.classCondition;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionInDetails {

    private String txt = null;
    private boolean newTxt = false;
    Lock lock = new ReentrantLock();
    Condition condRead = lock.newCondition();
    Condition condWrite = lock.newCondition();


    public void set(String text){
        lock.lock();
        System.out.println("\tZalozono lock na met set()");
        try{
            if (txt != null){
                while(newTxt) {
                    System.out.println("\twątek writera czeka w set(), zwalnia blokadę innemu wątkowi");
                    condWrite.await();
                }
            }
            System.out.println("\twątek writera przed dodaniem tekstu w set()");
            txt = text;
            newTxt = true;
            System.out.println("\twątek writera przed wywolaniem signal() na czytającym, w set()");
            condRead.signal();

        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\tZwolniono lock na met set()");
            lock.unlock();
        }
    }

    public String get(){
        lock.lock();
        System.out.println("Zalozono lock na met get()");

        try{
            while(!newTxt) {
                System.out.println("wątek readera czeka w get(), zwalnia blokadę dla innego wątku");
                condRead.await();
            }
            System.out.println("wątek readera przed odczytaniem tekstu w get()");
            newTxt = false;
            System.out.println("wątek readera przed wywolaniem signal() na writerze, w get()");
            condWrite.signal();
            System.out.println("wątek readera przed zwroceniem tekstu w get()");
            return txt;

        } catch (InterruptedException e) {
           return null;
        }finally {
            System.out.println("Zwolniono lock na met get()");
            lock.unlock();

        }
    }

    Runnable reader = () -> {
        String text = get();
        while(!text.equals("")){
            System.out.println("Reader read: " + text);
        text = get();
        }
    };

    Runnable writer = () -> {

        for (String s : Arrays.asList("pierwsze slowo", "drugie slowo", "trzecie slowo", "czwarte slowo", "")){
            set(s);
        }
    };

    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        ConditionInDetails t = new ConditionInDetails();
        exec.execute(t.writer);
        exec.execute(t.reader);
        exec.shutdown();
    }

}
