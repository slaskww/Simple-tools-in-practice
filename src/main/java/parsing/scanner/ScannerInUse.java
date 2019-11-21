package parsing.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ScannerInUse {

public static void initiateScannerWithPath(){


    String delimiter = "\\p{L}+";
    try(Scanner scanner = new Scanner(Paths.get("inp.txt")).useDelimiter("\\D+")){

        int sum = 0;
        while (scanner.hasNextInt()){
            sum += scanner.nextInt();
        }

        System.out.println("Sum: "+ sum);
    }
    catch (IOException e) {
        e.printStackTrace();
    }


}
public static void initiateScannerWithFile(){

    String delimiter = "[[0-9]\\s]+"; //separatorem jest wystąpnienie jeden lub wiele razy znaku z podanego zbioru - dowolna cyfra lub wiele cyfr, lub jeden i więcej białych znaków
    try(Scanner scanner = new Scanner(new File("inp.txt")).useDelimiter(delimiter)){//.useDelimiter(delimiter)){


        List<String> cont = new ArrayList<>();
        while (scanner.hasNext()){

            cont.add(scanner.next());
        }

        System.out.println(cont);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

}

public static void findInLineWithScanner(){

    String source = " a round black platform with model blackish horses, blackcurrants, etc. that turns around and around and that blackbirds ride on at a blackboard";

    try(Scanner scanner = new Scanner(source)){

        String word;
        List<String> words = new ArrayList<>();
        while ((word = scanner.findInLine("(black)[a-z]*"))!= null){ //szukamy słów wg wzorca black+zero lub więcej dowolnych liter
            words.add(word);
        }
        System.out.println(words);
    }
}

public static void skipWithScanner(){

    String source = "a round black platform with model blackish horses, blackcurrants, etc. that turns around and around and that blackbirds ride on at a blackboard";

    try(Scanner scanner = new Scanner(source)){
        scanner.skip("[a-z]");
        System.out.println(scanner.nextLine());
    }

}

    public static void main(String[] args) {
        initiateScannerWithPath();
        initiateScannerWithFile();
        findInLineWithScanner();
        skipWithScanner();
    }
}
