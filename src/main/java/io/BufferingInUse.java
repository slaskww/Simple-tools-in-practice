package io;

import java.io.*;

/**
 * Użycie obiektów klasy BufferedReader, BufferedWriter, BufferedInputStream, BufferedOutputStream
 * ogranicza liczbę fizycznych odwołań do urządzeń zewnetrzncyh (np. pliku).
 * Dzięki tym obiektom ficzyczny zapis i odczyt dotyczy całych porcji danych zgromadzonych w buforze.
 * Ma to duży sens przy odczycie danych z dużych plików tekstowych.
 * BufferedReader, BufferedWriter to klasy przetwarzające, więc nie możemy podać w ich konstruktorach fizycznego źródła danych w sposób
 * bezpośredni. Tę umiejętność posiadają jedynie strumieniowe klasy przedmiotowe (m.in FileReader, FileWriter) i to właśnie obiekty tych klasy
 * opakowujemy obiektami BufferedReader, BufferedWriter. Przy okazji korzystamy z metody newLine() zdefiniowanej w BufferedWriter
 * pozwalającej na dodanie separatora wierszy niezależnie od platformy systemowej i readLine() umożliwiającą czytanie pliku tekstowego wiersz po wierszu.
 * W programie korzystamy z klasy LineNumberReader, która dziedziczy klase BufferedReader, dzięki której możemy uzyskać informację o numerze bieżącego wiersza
 * (metoda getLineNumber()).
 */

public class BufferingInUse {

    public static void main(String[] args) {

      try(
              FileReader fr = new FileReader("inp.txt"); //obiekt strumieniowej klasy przedmiotowej, otrzymuje w konstruktorze fizyczne źródło danych (plik tekstowy)
              LineNumberReader br = new LineNumberReader(fr); //obiekt strumieniowej klasy przetwarzającej, otrzymuje w konstruktorze obiekt  strumieniowej klasy przedmiotowej
              BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt")) //obiekt strumieniowej klasy przetwarzającej, otrzymuje w konstruktorze obiekt  strumieniowej klasy przedmiotowej
              ) {

          String line;
          while ((line = br.readLine()) != null ){

              bw.write(br.getLineNumber() + " " + line);
              bw.newLine(); //dodanei separatora wiersza
          }

      }  catch (IOException e) {
          e.printStackTrace();
      }

    }
}
