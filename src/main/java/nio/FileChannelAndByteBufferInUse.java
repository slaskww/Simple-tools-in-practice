package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class FileChannelAndByteBufferInUse {

    public static void writeChannel(String filename, byte[] data) throws IOException {

        //Tworzymy kanał plikowy przy użyciu statycznej metody open() z klasy FileChannel

        try(FileChannel fileChannel = FileChannel.open(Paths.get(filename), WRITE)){//tworzymy nowy kanał plikowy
            ByteBuffer buffer = ByteBuffer.wrap(data);
            fileChannel.write(buffer);
        }

        /**
         * Alternatywnie możemy stworzyć kanał plikowy korzystając z metody Files.newByteChannel()
         * zwracającej obiekt SeekableByteChannel, któy potem rzutujemy na FileChannel
         *
         * Alternatywnie możemy stworzyć kanał plikowy z obiektu FileOutputStream
         *
         *         FileOutputStream out = new FileOutputStream(new File(filename));
         *         FileChannel fileChannel = out.getChannel();
         *         ByteBuffer buffer = ByteBuffer.wrap(data);
         *         fileChannel.write(buffer);
         */

    }
    public static byte[] readChannel(String filename) throws IOException {

        try(FileChannel fileChannel = (FileChannel) Files.newByteChannel(Paths.get(filename), READ)){

            int size = (int) fileChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate(size);
            fileChannel.read(buffer); //zapełniamy bufor danymi z kanału plikowego

           return buffer.array();
        }

        /**
         *  Alternatywnie możemy stworzyć tablice bajtów i do niej włożyć zawartość bufora
         *  Aby móc odczytać dane z bufora po tym, jak dane zostały do niego zapisane, musimy ten bufor 'przestawić'
         *  Przestawienie to odbywa się przy użyciu metody flip(). Metoda ta ustawia limit bufora na bieżącą pozycję
         *  a nastepnie ustawia pozycję na zero. Jeśli chcielibyćmy ponownie odczytać dane z bufora, musimy
         *  ten bufor 'przewinąć' do początku, stosując metodę rewind(), która ustala pozycję na zero.
         *
         *             buffer.flip();
         *             byte[] res = new byte[buffer.remaining()]; //tworzymy pomocniczą tablicę o długości bufora
         *             buffer.get(res);
         *             return res;
         */
    }

    public static void readAndWriteChannel(String fileName) throws IOException {

        //odczytujemy dane z kanału plikowego do bufora

        try(FileChannel fileChannel = FileChannel.open(Paths.get(fileName), READ, WRITE)){

            int size = (int) fileChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate(size);

            fileChannel.read(buffer);
            buffer.flip();

            for (int i = 0; i < size; i++){
                byte oldValue =  buffer.get(i);
                System.out.print((char) oldValue + " ");

                byte newValue = (byte)(oldValue + 4);
                buffer.put(i, newValue);
            }

            //pozycja pliku jest ustawiona na koniec
            //musimy ją zmienić, aby nie dopisywać danych, lecz je nadpisać - wywołujemy metodę position(0);

            fileChannel.position(0);
            fileChannel.write(buffer);


            System.out.println("\nBufer po odczycie danych/zapisie do kanału plikowego: " + buffer);
            buffer.rewind();
            System.out.println("Bufor po 'przewinieciu': " + buffer);

            fileChannel.position(0);
            fileChannel.read(buffer);

            System.out.println("Bufer po zapisie danych/odczycie z kanału plikowego: " + buffer);
            buffer.flip();
            System.out.println("Bufor po 'przewinieciu': " + buffer);

            while (buffer.hasRemaining()){
                System.out.print((char) buffer.get() + " ");
            }
        }

    }


    public static void main(String[] args) throws IOException {

        String fileName = "file_channel.txt";
        byte[] data = {78, 79, 80, 81};

        writeChannel(fileName, data);
        byte[] res = readChannel(fileName);
        System.out.println(Arrays.toString(res));

        readAndWriteChannel(fileName);
    }

}
