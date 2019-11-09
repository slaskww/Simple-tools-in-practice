package collection;


import java.util.*;

public class SplitteratorInParallel {

   public static void splitterate() throws InterruptedException {

        List<String> list = Arrays.asList(
                "kot", "pies", "szczur", "mysz", "słoń", "koń", "lis", "wilk"
        );

        List<StringBuilder> lsb = new ArrayList<>();

        for (String s : list) lsb.add(new StringBuilder(s));

        long start = System.nanoTime();
        stringOperation(lsb.spliterator());
        long stop = System.nanoTime() - start;

       System.out.println("Runtime.getRuntime().availableProcessors(): " + Runtime.getRuntime().availableProcessors());
       System.out.printf("Czas przetwarzania sekwencyjnego: %.3f ms.\n", (double) stop/1000000);
       lsb.forEach(System.out::println);

       lsb.clear(); //czyścimy listę

       for (String s : list) lsb.add(new StringBuilder(s));

       Spliterator<StringBuilder> spliterator1 = lsb.spliterator();
       Spliterator<StringBuilder> spliterator2 = spliterator1.trySplit();
       Spliterator<StringBuilder> spliterator3 = spliterator2.trySplit();
       Spliterator<StringBuilder> spliterator4 = spliterator1.trySplit();

       Thread[] threads = {
               new Thread(() -> stringOperation(spliterator1)),
               new Thread(() -> stringOperation(spliterator2)),
               new Thread(() -> stringOperation(spliterator3)),
               new Thread(() -> stringOperation(spliterator4))
       };

       start = System.nanoTime();
       for (Thread t: threads){
           t.start();
       }

       for (Thread t : threads){
           t.join();
       }

       stop = System.nanoTime() - start;
       System.out.printf("Czas przetwarzania równoległego: %.3f ms.\n", (double) stop/1000000);
       lsb.forEach(System.out::println);
    }

    public static void stringOperation(Spliterator<StringBuilder> spl){

       spl.forEachRemaining(stringBuilder -> {
           for (int i = 0; i < stringBuilder.length(); i+=2){//przeskakujemy o dwie pozycje
               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               stringBuilder.insert(i+1, stringBuilder.charAt(i)); //umieszcza znak charAt(i) na pozycję i+1
           }
       });
    }

    public static void main(String[] args) throws InterruptedException {
        splitterate();
    }


}
