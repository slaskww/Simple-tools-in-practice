package nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class FileChannelAndByteBufferInUse {

    public static void writeChannel(String filename, byte[] data) throws IOException {

        System.out.println("writeChannel():");
        //Tworzymy kanał plikowy przy użyciu statycznej metody open() z klasy FileChannel

        try (FileChannel fileChannel = FileChannel.open(Paths.get(filename), WRITE)) {//tworzymy nowy kanał plikowy
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

        System.out.println("readChannel():");
        try (FileChannel fileChannel = (FileChannel) Files.newByteChannel(Paths.get(filename), READ)) {

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

        System.out.println("readAndWriteChannel():");
        //odczytujemy dane z kanału plikowego do bufora

        try (FileChannel fileChannel = FileChannel.open(Paths.get(fileName), READ, WRITE)) {

            int size = (int) fileChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate(size);

            fileChannel.read(buffer);
            buffer.flip();

            for (int i = 0; i < size; i++) {
                byte oldValue = buffer.get(i);
                System.out.print((char) oldValue + " ");

                byte newValue = (byte) (oldValue + 4);
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

            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get() + " ");
            }
            System.out.println();
        }

    }

    public static void byteBufferAsDoubleBuffer() throws IOException {

        /**
         * Obiekty ByteBuffer pozwalają na wygenerowanie widoku na ten bufor w postaci dowolnego bufora na typ prosty.
         * Takie bufory (np. DoubleBuffer, IntBuffer, CharBuffer) pozwalają w łatwy sposób przetwarzać dane typów prostych pobieranych
         * z kanału i zapisywac dane typu prostego do kanału.
         * Trzeba jednak pamiętać, że pozycja i limit buforu bajtowego są niezależne od pozycji i limitu jego widoku.
         * Wyłołując metode flip() dla widoku, nie będzie to miało wpływu na pozycje i limit bufora bajtowego.
         */

        System.out.println("byteBufferAsDoubleBuffer():");
        String fileName = "file_channel_Double.txt";
        initDouble(fileName); //tworzymy testowy plik i wypełniamy go danymi

        //Tworzymy i otwieramy kanał plikowy
        FileChannel channel = FileChannel.open(Paths.get(fileName));

        //Alokujemy odpowiednią liczbę bajtów dla bufora
        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());

        //odczytujemy dane z kanału plikowego i zapisujemy je w buforze(sec.)
        channel.read(buffer);

        //Przestawiamy bufor - ustawiamy limit na bieżącą pozycję i ustawiać wskaźnik pozycji na zero
        buffer.flip();

        //Tworzymy widok buforu bajtowego jako bufor double. Z racji tego, ze jest to widok, pracujemy wciąż na oryginalnym buforze bajtowym
        DoubleBuffer doubleBuffer = buffer.asDoubleBuffer();


        while (doubleBuffer.hasRemaining()) {
            System.out.print(doubleBuffer.get() + " ");
        }
        System.out.println();
    }

    public static void byteBufferAsCharbuffer() {

        /**
         * Bufor znakowy CharBuffer reprezentuje sekwencję wartości typu char (znaki Unicode).
         * Ma on wszystkie właściwości bufora, a z racji tego, że implementuje interfejs CharSequence
         * (podobnie jak String, StringBuffer i StringBuilder), posiada pewne dodatkowe możliwości.
         * Posiada wszystkie metody tego interfejsu.
         * Klasa CharBuffer posiada również wygodną statyczną metode wrap(CharSequence), która zamienia dowony
         * obiekt implementujący CharSequence w bufor znakowy
         *
         *      String str = "May the force B with U";
         *      CharBuffer buffer CharBuffer.wrap(str);
         *
         * Przy kanałowym wczytywaniu i zapisywaniu strumieni tekstowych powstaje problem kodowania i dekodowania znaków.
         * Problem ten rozwiązuje klasa Charset której obiekty reprezentują stronę kodową. Klasa ta dostarcza metod
         * encode(CharBuffer) i decode(ByteBuffer) do prostego kodowania i dekodowania znaków.
         *
         *      Charset charset = Charset.forName("ISO-8859-2")
         *      ByteBuffer buf = charset.encode(chbuf);
         *      CharBuffer cbuf = charset.decode(buf);
         *
         * Strony kodowania dla danej implementacji JVM odczytamy wywołując statyczną metodę availCharsets()
         *
         *          Map<String, Charset> charsets =  Charset.availableCharsets();
         *          charsets.entrySet().stream().
         *                  forEach(stringCharsetEntry -> System.out.println(stringCharsetEntry.getKey()));
         */

        System.out.println("byteBufferAsCharbuffer():");
        String fileName = "file_channel_char.txt";
        Charset charsetIn = Charset.forName("UTF-8");
        Charset charsetOut = Charset.forName("ISO-2022-JP-2");

        try (FileChannel channel = FileChannel.open(Paths.get(fileName), WRITE, READ)) {

            //tworzymy bufor bajtowy
            ByteBuffer bbuffer = ByteBuffer.allocate((int) channel.size());
            channel.read(bbuffer);
            bbuffer.flip();

            //tworzymy bufor znakowy, wykorzystując obiekt Charset i jego metodę decode() zwracającą obiekt CharBuffer
            CharBuffer cbuffer = charsetIn.decode(bbuffer);

            System.out.println(cbuffer.toString());

            //bufor znakowy kodujmey na bufor bajtowy w oparciu o stronę kodową zawartą w charsetOut.
            bbuffer = charsetOut.encode(cbuffer);

            //metoda truncate(long size) pozwala na zmianę rozmiaru pliku na size przez usunięcie bajtów wykracZających poza określony przez size koniec pliku
            //w naszym przypadku ustalamy rozmiar na 0, więc de facto usuwamy zawartość całego pliku.

            channel.truncate(0);

            //zapisujemy do pliku dane z bufora bajtowego ze zmienioną stroną kodową
            channel.write(bbuffer);

            //ponownie odczytujemy dane z kanału do bufora
            channel.read(bbuffer);
            bbuffer.flip();

            //tworzymy bufor znakowy, wykorzystując obiekt Charset i jego metodę decode() zwracającą obiekt CharBuffer
            cbuffer = charsetOut.decode(bbuffer);

            String text = cbuffer.toString();
            System.out.println(text);

            //bufor znakowy kodujmey na bufor bajtowy w oparciu o stronę kodową zawartą w charsetOut.
            bbuffer = charsetIn.encode(CharBuffer.wrap(text));

            channel.truncate(0);
            //zapisujemy do pliku dane z bufora bajtowego z pierwotną stroną kodową
            channel.write(bbuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileChannelWithMultiBuffers() {

        /**
         * Kanały umożliwiają wprowadzanie danych za jednym odwołaniem do wielu buforów, lub zapisywaniem danych w wielu buforów.
         * Odbywa się to przy pomocy metod kanału: write(ByteBuffer[]) i read(ByteBuffer[]) przyjmujące jako argument tablicę buforów bajtowych.
         * Taka jednorazowa (atomistyczna) operacja zapisu/odczytu za pomocą wielu buforów bajtowych jest wygodna i efektywna.
         *
         */

        System.out.println("fileChannelWithMultiBuffers():");

        String fileName = "file_channel_multi_buffers.txt";
        int size = 3;
        initShortAndDouble(fileName, size);

        try (FileChannel fileChannel = FileChannel.open(Paths.get(fileName), READ, WRITE)) {


            //Alokujemy w buforach tyle pamięci ile bajtów zajmują wartości typu short (2 bajty) i double (8 bajtów)
            ByteBuffer[] buffers = {
                    ByteBuffer.allocate(size * 2),
                    ByteBuffer.allocate(size * 8)
            };

            fileChannel.read(buffers);

            buffers[0].flip();
            buffers[1].flip();

            ShortBuffer sbuf = buffers[0].asShortBuffer();
            DoubleBuffer dbuf = buffers[1].asDoubleBuffer();

            while (sbuf.hasRemaining()) {

                short el = sbuf.get();
                System.out.println(el);
            }

            while (dbuf.hasRemaining()) {

                double el = dbuf.get();
                System.out.println(el);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void fileAsMappedByteBuffer() throws IOException {

        System.out.println("fileAsMappedByteBuffer():");
        String fileName = "file_to_mapping.txt";
        initDouble(fileName);


        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
        FileChannel channel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = channel.map(
                FileChannel.MapMode.READ_WRITE,    //tryb odczyt-zapis
                0,                              //od początku pliku
                channel.size()                     //do końca pliku
        );

        DoubleBuffer doubleBuffer = mappedByteBuffer.asDoubleBuffer();

        int count = 0;
        while (doubleBuffer.hasRemaining()) {
            double d = doubleBuffer.get();
            System.out.println(d);
            doubleBuffer.put(count++, d * 100); //zmieniamy wartość i-tego elementu. Działamy na widoku, więc wynik zostanie odzwierciedlony w buferze bajtowym
        }
        mappedByteBuffer.force(); // wymuszamy na buforze bajtowym by odzwierciedlił zmiany w pliku

        try(DataInputStream dis = new DataInputStream(new FileInputStream(fileName))){

            while (true){
                System.out.println(dis.readDouble());
            }

        }catch (EOFException e){
            return;
        }
    }

    private static void initDouble(String fileName) throws IOException {

        //obiekt strumienowej klasy przetwarzającej. Dane typów pierwotnych i łańcuchy znakow (w tym przypadku typ double) zamienia w strumień wartości binarnych

        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName));
        for (int i = 0; i < 10; i++) {
            dos.writeDouble(i + 0.5);
        }
        dos.close();
    }

    private static void initShortAndDouble(String fileName, int size) {

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {

            short[] shorts = new short[size];
            double[] doubles = new double[size];

            for (int i = 0; i < size; i++) {
                shorts[i] = (short) (i + 1);
                doubles[i] = i + 4.5;
            }

            for (short aShort : shorts) {
                dos.writeShort(aShort);
            }
            for (double aDouble : doubles) {
                dos.writeDouble(aDouble);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        String fileName = "file_channel.txt";
        byte[] data = {78, 79, 80, 81};

        writeChannel(fileName, data);
        byte[] res = readChannel(fileName);
        System.out.println(Arrays.toString(res));

        readAndWriteChannel(fileName);
        byteBufferAsDoubleBuffer();
        byteBufferAsCharbuffer();
        fileChannelWithMultiBuffers();
        fileAsMappedByteBuffer();

    }

}
