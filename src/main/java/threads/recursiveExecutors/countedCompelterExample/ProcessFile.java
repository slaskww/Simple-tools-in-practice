package threads.recursiveExecutors.countedCompelterExample;

import java.io.*;
import java.util.Map;
import java.util.concurrent.CountedCompleter;

public class ProcessFile extends CountedCompleter<Map<String, Integer>> {

    private Map<String, Integer> result = JavaKeyWords.map; //korzystamy z pomocniczej klasy statycznej JavaKeyWords i jej statycznej zmiennej map
    private ProcessDirectory parent;
    private File file;

    public ProcessFile(ProcessDirectory parent, File file) {
        super(parent);
        this.parent = parent;
        this.file = file;
    }

    @Override
    public void compute() {

        try (FileReader fileReader = new FileReader(file)){
            StreamTokenizer streamTokenizer = new StreamTokenizer(fileReader);
            streamTokenizer.slashSlashComments(true);
            streamTokenizer.slashStarComments(true);
            streamTokenizer.quoteChar('"');
            streamTokenizer.wordChars('.', '.');
            streamTokenizer.wordChars('_', '_');
            streamTokenizer.wordChars('$', '$');


            while ((streamTokenizer.nextToken()) != StreamTokenizer.TT_EOF){
                if (streamTokenizer.ttype == StreamTokenizer.TT_WORD){
                   String word = streamTokenizer.sval;
                   Integer num = result.get(word);

                   if (num != null){
                       result.put(word, num + 1);
                   }
                }
            }
            tryComplete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> getRawResult() {
        return result;
    }
}
