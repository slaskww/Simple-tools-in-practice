package threads;

import java.util.concurrent.CountedCompleter;

public class TaskAsCountedCompleter extends CountedCompleter<String> {


    private char taskId;
    private int taskCount;
    private CountedCompleter subTask;
    private String result = "";


    public TaskAsCountedCompleter(CountedCompleter parentCountedCompleter, char taskId, int taskCount) {
        super(parentCountedCompleter);
        this.taskId = taskId;
        this.taskCount = taskCount;
    }

    @Override
    public void compute() {

        if (taskCount > 0){

            char subTaskId = (char)(taskId + 1);
            TaskAsCountedCompleter toFork = new TaskAsCountedCompleter(this, subTaskId , --taskCount);
            toFork.fork();
            subTask = toFork;
            setPendingCount(1);
        }

      if (taskId == 'K')  try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = String.valueOf(taskId);
        tryComplete();
    }

    @Override
    public void onCompletion(CountedCompleter caller) {

        System.out.println("Zadanie " + this + " zakończone. Metodę kończącą onCompletion() wywołało zadanie " + caller + (caller != this ? " (podzadanie)" : " (bieżące zadanie)"));
        if (caller != this){
            result += caller.getRawResult();
        } else if (subTask != null){
            result += subTask.getRawResult();
        }

    }

    @Override
    public String getRawResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Task" + taskId;
    }
}
