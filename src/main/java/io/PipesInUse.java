package io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Pipes (potoki) służą do przesyłania danych między równolegle działającymi wątkami.
 * Umożliwiają więc uproszczenie komunikacji między wątkami, gdy kody posługują się strumieniami java.io (klasami typu InputStream, OutputStream, Reader i Writer).
 * Nie musimy się martwić o synchronizację i koordynację działania wątków. Odczytywanie i zapisywanie danych za pomocą potoków zapewnia bowiem taką synchronizacje i koordynację.
 * Wątek produkujący dane zapisuje je w potoku wyjściowym (PipedWriter albo PipedOutputStream - są to klasy przedmiotowe)
 * Klasa potoku wejściowego przyjmuje w konstruktorze potok wyjściowy jako źródło danych (mowa o klasach przedmiotowych,
 * więc o klasach, które moga przyjmować źródła danych).
 * Teraz wątek odczytujący otrzyma obiekt potoku wejściowego (PipedInputStream lub PipedReader), z którego odczyta dane.
 * W przykładzie posłużymy się pomocniczymi klasami DataGenerator i DataReceiver. Obie rozszerzają klasę Thread.
 *
 *
 *
 */

public class PipesInUse {

    public static void main(String[] args) throws IOException {

        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);

        new DataGenerator(pos).run();
        new DataReceiver(pis).run();
    }


}
