package parsing.scanner;

import java.util.Scanner;

public class ScannerWithRegex {

    public static void nextWithDelimiterInUse(){

        String delimiter = "(SURNAME:)|(FIRSTNAME:)|(CITY:)"; //delimiter - separator - w tym przypadku to dowolna z trzech nazw (uzylismy operatora alternatywy logicznej)

        String txt = "FIRSTNAME: Ann SURNAME: Doe CITY: NYC " +
                "FIRSTNAME: Bob SURNAME: Smith CITY: Washington DC";

        Scanner scanner = new Scanner(txt).useDelimiter(delimiter);

        while(scanner.hasNext()){
            String elem = scanner.next();
            elem = elem.trim();

            System.out.println("'" + elem + "'");
        }
    }


    public static void sumNumbers(String txt){

        String delimiter = "[^0-9]+";

        Scanner scanner = new Scanner(txt).useDelimiter(delimiter);

        int sum = 0;
        while(scanner.hasNext()){
            sum += scanner.nextInt();
        }
        System.out.println("Suma liczb w łancuchu '" + txt + "' wynosi " + sum);

    }

    public static void main(String[] args) {

        nextWithDelimiterInUse();
        sumNumbers(" Dodaj 1 do 3 i jeszcze 5 do 1. Ile otrzymasz?");
    }
}
