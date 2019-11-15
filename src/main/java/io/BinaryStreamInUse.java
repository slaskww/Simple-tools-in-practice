package io;

import java.io.*;

public class BinaryStreamInUse {

    /**
     *  Klasy DataInputStream i DataOutputStream służą do zapisu i odczytu danych typów prostych i Stringów w postaci binarnej (zero-jedynkowej)
     *  Dla każdego typu prostego mamy osobną metodę do zapisu i odczytu (np dla integera mamy readInt() i writeInt() a dla
     *  Stringów mamy readUTF() i writeUTF().
     *
     *
     */

    public static void main(String[] args) {

        String text = "Tekst do zapisana w formie binarnej";
        int number = 12345;

        try(FileOutputStream fo = new FileOutputStream("binaryOut");
            DataOutputStream dos = new DataOutputStream(fo);
            DataInputStream dip = new DataInputStream(new FileInputStream("binaryOut"))
        ){

            dos.writeUTF(text);
            dos.writeInt(number);

            System.out.println("Odczyt");
            String textFromFile = dip.readUTF();
            int numberFromFile = dip.readInt();
            System.out.println(textFromFile);
            System.out.println(numberFromFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
