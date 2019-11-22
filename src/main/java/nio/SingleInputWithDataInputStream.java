package nio;

import java.io.*;

public class SingleInputWithDataInputStream extends Thread{

    private String filename = "file_channel_Double.txt";

    @Override
    public void run() {
        try(DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(filename)))){



        while (dataInputStream.available() != 0){

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(dataInputStream.readDouble());
        }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
