package threads.recursiveExecutors.countedCompelterExample;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountedCompleter;

public class ProcessDirectory extends CountedCompleter<Map<String, Integer>> {

    private ProcessDirectory parent;
    private File directory;
    private List<ProcessDirectory> subdirectories = new ArrayList<>(); //lista zadań przetwarzanących podkatalogi dla danego katalogu
    private List<ProcessFile> subFiles = new ArrayList<>(); //lista zadań przetwarzanących pliki w danym katalogu
    private Map<String, Integer> result = new HashMap<>();

    public ProcessDirectory(ProcessDirectory parent, File directory) {
        super(parent);
        this.parent = parent;
        this.directory = directory;
    }

    @Override
    public void compute() {

       File[] flist = directory.listFiles();

       if (flist != null && flist.length != 0){
           for(File f : flist){
               if (f.isFile() && f.getName().endsWith(".java")){
                   ProcessFile pf = new ProcessFile(this, f);
                   this.addToPendingCount(1);
                   pf.fork();
                   subFiles.add(pf);
               } else if (f.isDirectory()){
                   ProcessDirectory pd = new ProcessDirectory(this, f);
                   this.addToPendingCount(1);
                   pd.fork();
                   subdirectories.add(pd);
               }
           }
       }
       this.tryComplete();
    }

    @Override
    public void onCompletion(CountedCompleter<?> countedCompleter) {

        for (ProcessDirectory pd: subdirectories){
            Map<String, Integer> pdmap =  pd.getRawResult();
            if (!pdmap.isEmpty()){
                for (String key : pdmap.keySet()){
                    this.result.merge(key, pdmap.get(key), (integer, integer2) -> integer + integer2);
                }
            }
        }

        for (ProcessFile pf : subFiles){
            Map<String, Integer> pfmap =  pf.getRawResult();
            if (!pfmap.isEmpty()){
                for (String key : pfmap.keySet()){
                    this.result.merge(key, pfmap.get(key), (integer, integer2) -> integer + integer2);
                }
            }
        }
    }

    @Override
    public Map<String, Integer> getRawResult() {
        return result;
    }
}
