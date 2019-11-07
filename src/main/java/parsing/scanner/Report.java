package parsing.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Report {

    private String report;

    public Report(String fileName) throws FileNotFoundException { //konstruktor przyjmuje nazwę pliku .txt

        Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\\Z"); //wczytujemy cały plik korzystając z separatora \\Z oznaczającego koniec wejścia
        report = scanner.next(); //pobieramy całą treść pliku
        scanner.close();
    }

    public double sum(String category){

        Scanner scanner = new Scanner(report);
        String regex = "\\d\\." + category + ":"; //separatorem jest dowolna cyfra, następnie znak kropki, następnie nazwa kategorii, następnie znak dwukropka ':'


        //findWithinHorizon() wyszukuje kolejne wystąpienie tekstu pasującego do wzorca.
        //Drugi argument metody określa limit przeszuiwanego tekstu (horizon). Dla wartości 0 jest on nieograniczony.
        //If horizon is 0, then the horizon is ignored and this method continues to search through the input looking for
        // the specified pattern without bound. In this case it may buffer all of the input searching for the pattern
       if (scanner.findWithinHorizon(regex, 0) == null){
           throw new IllegalArgumentException();
       }

       double sum = 0.0;

       do {
           while (scanner.hasNextDouble()){
               sum += scanner.nextDouble();
           }
       } while (scanner.findWithinHorizon(regex, 0) != null);

       return sum;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Report report = new Report("report.txt");

        System.out.println("Wydatki: " + report.sum("Wydatki"));
        System.out.println("Przychody: " + report.sum("Przychody"));
    }
}
