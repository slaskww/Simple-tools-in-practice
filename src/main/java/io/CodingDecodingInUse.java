package io;

import java.io.*;
import java.nio.charset.Charset;

public class CodingDecodingInUse {

    /**
     * InputStreamReader i OutputStreamWriter
     * Kodowanie i dekodowanie przy użyciu strumieni wejścia-wyjścia.
     * Java posługuje się znakamu w formacie Unicode, a dane zapisywane są w plikach w postaci sekwencji bajtów.
     * Strumienie znakowe potrafią przekształcać bajtowe źródła danych pochodzących z plików w znaki Unicode i
     * odwrotnie. Konwersją taką zajmują się klasy InputStreamReader i OutputStreamWriter
     * W programie wykorzystujemy obiekt klasy BufferedReader i BufferedWriter by odczytywać / zapisywać
     * całe porcje danych, a więc ograniczyć liczbę fizycznych odwołań do pliku. Przy okazji korzystamy z metody newLine()
     * pozwalającej na dodanie separatora wierszy niezależnie od platformy systemowej i readLine() umożliwiającą
     * czytanie pliku tekstowego wiersz po wierszu.
     */

    public static void main(String[] args) throws IOException {



        String p = System.getProperty("file.encoding"); //sprawdzamy jakie jest domyslne kodowanie


        try(FileInputStream fi = new FileInputStream("inp.txt"); //strumieniowa klasa przedmiotowa
            BufferedReader br = new BufferedReader(new InputStreamReader(fi, p)); //strumieniowa klasa przetwarzająca
            FileOutputStream fo = new FileOutputStream("out.txt", true); //strumieniowa klasa przedmiotowa
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo, p))){ //strumieniowa klasa przetwarzająca

            String line;

            while((line = br.readLine()) != null){
                System.out.println(line);
                bw.write(line);
                bw.newLine();
                bw.newLine();
        }

        }



    }

}
