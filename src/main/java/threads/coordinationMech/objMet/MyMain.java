package threads.coordinationMech.objMet;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyMain {

    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        Coordinator coord = new Coordinator();
        exec.submit(new MrWriter(coord, Arrays.asList("Hi dude, it's me, Writer.",
                "I'm testing coordination mechanism implemented in Object class.",
                "It is the most basic way to coordinate the work of our threads.",
                "In fact, there are much easier ways to do the job.",
                "Take a glimpse at blocking queues.", null)));
        exec.submit(new MrReader(coord));
        exec.shutdown();
    }

}
