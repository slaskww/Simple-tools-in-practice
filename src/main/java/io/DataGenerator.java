package io;

import java.io.IOException;
import java.io.OutputStream;

public class DataGenerator extends Thread {

    private OutputStream outputStream;

    public DataGenerator(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {

            try {
                for (char ch = 'a'; ch <= 'z'; ch++){
                outputStream.write(ch);
            }
                outputStream.close();

            } catch (IOException e) {
                return;
            }
    }
}
