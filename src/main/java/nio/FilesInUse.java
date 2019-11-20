package nio;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Pakiet java.nio.file.Files zawiera szereg metod statycznych, które umożliwiają operacje na plikach.
 * Są wygodną alternatywą dla strumieni wejścia-wyjścia z pakietu java.io.
 * Większość metod klasy Files zawiera argumenty typu Path reprezentującego ścieżkę obiektów plikowych (plików, katalogów).
 * Obiekt Path uzyskujemy za pomocą statycznej metody get z klasy Paths.
 * W klasie wykorzystujemy następujące metody:
 * copy(Path, Path, CopyOption)
 * readAllLines(Path) zwraca zawartość pliku jako listę wszystkich wierszy w postaci łańcuchów znaków List<String>
 * readAllBytes(Path) zwraca zawartość pliku tablicę bajtów byte[]
 * write(Path, Iterable) zapisuje do pliku zawartość obiektu iterowalnego, czyli m.in listy i innych kolekcji
 * lines(Path) zwracającej strumień Stream<T>
 * newBufferedReader(Path) zwracającej obiekt BufferedReader
 * newBufferedWriter(Path) zwracającej obiekt BufferedWriter
 * newInputStream(Path) zwracającej obiekt InputStream
 * newOutputStream(Path) zwracającej obiekt OutputStream
 */

public class FilesInUse {

    public static void copyFile() throws IOException {

        Path src = Paths.get("Files_input.txt");
        Path dest = Paths.get("Files_output.txt");
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }
    public static void processFile() throws IOException {

        Path src = Paths.get("Files_input.txt");

        List<String> lines = Files.readAllLines(src);

        for (String line: lines){
            System.out.println(line);
        }

    }
    public static void saveAsByteArray() throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get("Files_input.txt"));
        Files.write(Paths.get("Files_output.txt"), bytes, StandardOpenOption.WRITE);
    }

    public static void processFileAsStream() throws IOException {

        try(Stream<String> lines =  Files.lines(Paths.get("Files_input.txt"))){

            String result = lines.map(s -> s.toUpperCase().concat("(").concat(String.valueOf(s.length()).concat(")")))
                    .collect(Collectors.joining("\n"));
            System.out.println(result);
        }

    }

    public static void useNewBufferedReaderAndNewBufferedWriter() throws IOException {


       try(BufferedReader reader =  Files.newBufferedReader(Paths.get("Files_input.txt"));
           BufferedWriter writer = Files.newBufferedWriter(Paths.get("Files_output.txt"))){

           String line;
           while ((line = reader.readLine()) != null){
               String outp = String.join("~", line.split("[ ,.]"));
               writer.write(outp);
               writer.newLine();
               System.out.println(outp);
           }
       }

    }

    public static void useNewInputStreamAndNewOutputStream() throws IOException {


        try(InputStream reader =  Files.newInputStream(Paths.get("Files_input.txt"));
            OutputStream writer = Files.newOutputStream(Paths.get("Files_output.txt"))){

            int ch;
            int counter = 1;
            while ((ch = reader.read()) != -1){
                if (ch == 10 || ch == 13){
                    writer.write(ch);
                    continue;
                }
                if (counter++ % 11 == 0){
                    System.out.println((char) ch + "=" + ch + "\t");
                } else System.out.print((char) ch + "=" + ch + "\t");

                writer.write(ch + 1);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        copyFile();
        processFile();
        saveAsByteArray();
        processFileAsStream();
        useNewBufferedReaderAndNewBufferedWriter();
        useNewInputStreamAndNewOutputStream();
    }
}
