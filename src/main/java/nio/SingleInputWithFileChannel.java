package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class SingleInputWithFileChannel extends Thread {

    private String filename = "file_channel_Double.txt";

    @Override
    public void run() {

        try(FileChannel channel = FileChannel.open(Paths.get(filename))){

            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            channel.read(byteBuffer);
            byteBuffer.flip();
            DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();

            while (doubleBuffer.hasRemaining()){

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(doubleBuffer.get());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
