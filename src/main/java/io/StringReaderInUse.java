package io;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * StringReader jest strumieniem znakowym, który przyjmuje łańcuch znaków i zwraca strumień kodów znaków.
 * StringWriter jest strumieniem znakowym, którego metoda write(int) przyjmuje podany kod znakowy a
 * 'pod spodem' rzutuje ten kod znakowy na typ char i dodaje go do obiektu StringBuffer.
 * Łańcych znakowy wyłuskujemy przez wywołanie metody toString().
 *
 */

public class StringReaderInUse {

    public static void main(String[] args) throws IOException {

        String source = "StringReader is a character stream whose source is a string.";
        StringReader reader = new StringReader(source);

        StringWriter writer = new StringWriter();


        int character;
        int count = 1;
        while ((character = reader.read()) != -1){

            if (count++ % 10 == 0){
                System.out.println((char) character + "=" + character + '\t');
            } else System.out.print((char) character + "=" + character + '\t');

        writer.write(character);
        }

        System.out.println(writer.toString());

    }
}
