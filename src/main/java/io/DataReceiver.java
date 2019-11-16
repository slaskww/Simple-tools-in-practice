package io;

import java.io.IOException;
import java.io.InputStream;

public class DataReceiver extends Thread{

    private InputStream inputStream;

    public DataReceiver(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {


        try{
            int ch;
            while ((ch = inputStream.read()) != -1){
                System.out.print((char) ch);
            }
            inputStream.close();

        } catch (IOException e) {
           return;
        }
    }
}
