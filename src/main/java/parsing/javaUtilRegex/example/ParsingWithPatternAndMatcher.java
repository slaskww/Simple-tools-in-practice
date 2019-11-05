package parsing.javaUtilRegex.example;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Obiekt klasy Pattern - reprezentuje skompilowane wyrażenie regularne,
 * uzyskujemy je za pomocą jednej ze statycznych metod Pattern.compile(...)
 * mających za argument tekst wyrażenia regularnego
 *
 * Obiekt klasy Matcher - wykonuje operacje wyszukiwania w tekście za pomoca
 * interpretacji skompilowanego wyrażenia regularnego i dopasowania go do tekstu
 * lub jego części. Obiekt matcher jest zawsze związany danym wzorcem więc uzyskujemy go
 * za pomoca metody matcher(...) wywołanej na obiekcie Pattern. Metoda matcher(...) prtzyjmuje w argumencie
 * przeszukiwany tekst
 *
 * Metody klasy Matcher:
 * boolean matches() - stwierdza, czy cały tekst pasuje do wzorca
 * boolean find() - odnajduje w napisie kolejne podnapisy pasujące do wzorca
 * group() - sluzy do pobrania dopasowanego podłancucha
 * start() - zwraca początkową pozycję dopasowanego podłańcucha
 * end()  - zwraca końcową pozycję dopasowanego podłańcucha
 * group(int n) - wyłuskujemy fragmenty (tzw. grupy - elementy wyrażenia regex ujete w nawiasy okrągłe) dopadowanego tekstu
 * reset(...) - zeruje stany matchera a także pozwala na podanie nowego tekstu, na którym matcher ma działać
 * split(...) -  służy do rozbioru tekstu, wywoływana na obiekcie Pattern
 * replaceFirst(String rpl) - zastępuje w napisie pierwsze wystapnienie podnapisu pasującego do wzorca napisem rpl
 * replaceAll(String rpl) - zastępuje w napisie wszystkie wystąpienia podnapisu pasującego do wzorca podanym napisem rpl
 *
 */

public class ParsingWithPatternAndMatcher {

    private String regex;
    private String txt;
    private Pattern pattern;
    private Matcher matcher;

    public void matchesInUse(){

        System.out.println("metoda matches()");

        //wzorzec
        regex = "[0-9]+"; //wzorzec reprezentuje jedno lub więcej wystapień cyfry z zakresu 0-9

        //tekst wejściowy
        txt = "1357986420";

        //kompilacja wzorca
        pattern = Pattern.compile(regex);

        //uzyskanie matchera
        matcher = pattern.matcher(txt);

        //sprawdzamy, czy tekst pasuje do wzorca
        boolean match = matcher.matches();

        System.out.println("\tTekst " + txt + (match ? "" : " nie") + " pasuje do wzorca " + regex);

        //resetujemy matchera i opdajemy mu nowy tekst wejściowy
        txt = "135 790";
        matcher.reset(txt);
        match = matcher.matches();

        System.out.println("\tTekst " + txt + (match ? "" : " nie") + " pasuje do wzorca " + regex);
    }

    public void findInUse(){
        System.out.println("metoda find()");

        regex = "[0-9]+";
        txt = "135 246 78c9";

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(txt);

        System.out.print("Tekst: \'" + txt + "\', regex: " + regex);

        while(matcher.find()){
            System.out.println("\t dopasowano podlancuch " + matcher.group() +
                    "\n\t od pozycji " + matcher.start() + " do " + matcher.end());
        }
    }


    public void groupInUse(){
        System.out.println("metoda group(int n)");

        //w nawiasy okrągłe obejmujemy tzw. grupy. Potem będziemy sie mogli odwoływać do każdej z grup (numeracja grup od 1 do 6)
        //fragment wyrażenia nieujety w nawias okrągły (początkowe litery Unikode i spacje) nie stanowi grupy.
        //w przykładzie dopasowujemy tekst opisujący produkt za pomocą indentyfikatora liczbowego, nazwy oraz ceny w postaci liczby całkowitej > 0
        //między tymi składnikami moga występować spacje
        regex = "\\p{L}+\\s+([0-9]+)\\s+(\\p{L}+)\\s+([1-9][0-9]*)"; //jedna lub więcej liter Unicode, jeden lub więcej białych znaków (grupa 1: cyfra od 0 do 9), jeden lub więcej białych znaków, (grupa 2: jedna lub więcej liter Unicode), jeden lub więcej białych znaków, (grupa 3: dowolna liczba całkowita) > 0
        txt = "produkt 1234 fruit 134";

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(txt);

        System.out.println("Tekst: \'" + txt + "\', regex: " + regex);

        boolean isMatching = matcher.matches(); //czy cały tekst pasuje do wzorca?
        if (isMatching){

            int groups = matcher.groupCount(); //liczba grup

            for (int i = 1; i <= groups; i++){
                System.out.println("\tGrupa " + i + ", zawartosc: " + matcher.group(i));
            }

        } else System.out.println("\n\tNie znaleziono dopasowania.");
    }

    public void splitInUse(){
        System.out.println("metoda split()");

        regex = "[\\s\\p{Punct}]+"; //słowa rozdzielone białymi znakami lub znakami interpunkcyjnymi
        txt = "Wiadomość (generowana) <jest> {automatycznie}. /Nie/ odpowiadaj na ;. podany, adres email,";

        System.out.println("Tekst: \'" + txt + "\', regex: " + regex);

        pattern = Pattern.compile(regex);

        String[] result = pattern.split(txt);

        System.out.println("\tLiczba wyroznionych slow: " + result.length);

        for (String s : result){
            System.out.println("\t" + s);
        }
    }

    public void replaceFirstInUse() throws IOException {
        System.out.println("metoda replaceFirst()");

        regex = "\\s*//.*"; //wystąpienie zero lub więcej białych znaków, wystąpienie ukosników (znaku komentarza w javie), wystąpienie bądź nie innych znaków
        Path input = Paths.get("input.txt");
        Path output = Paths.get("output.txt");

        Charset cs = Charset.defaultCharset(); //ustawiamy domyślną stronę kodowania

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher("");

        List<String> lines = Files.readAllLines(input, cs);
        List<String> newLines = new ArrayList<>();

        for (String line : lines){
            matcher.reset(line);
            String newLine = matcher.replaceFirst("");
            newLines.add(newLine);
        }

        Files.write(output, newLines, cs);
    }

    public void replaceAllInUse(){
        System.out.println("metoda replaceAll()");

        //znak '(', (grupa 1: dowolna cyfra), bialy znak, znak '-', bialy znak, (grupa 2: dowolna cyfra), znak ')'
        //znak '(' oraz ')' są znakami specjalnymi, więc musimy użyć \\(
        regex = "\\((\\d)\\s-\\s(\\d)\\)";
        txt = "(NOVEMBER 2) Eintracht Frankfurt (1 - 5) FC Bayern München, AFC Bournemouth (0 - 1) Manchester United";
        System.out.println("regex: " + regex);

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(txt);

        System.out.println("\tTekst przed zmianą:");
        System.out.println("\t\t" + txt);
        System.out.println("\tTekst po zmianie:");
        String newTxt = matcher.replaceAll("[$2:$1]"); //podając tekst zastępujący możemy się odwołać do zawartości grupy wzorca regex. Stosujmey w tym celu znak $
        System.out.println("\t\t" + newTxt);
    }

    public void regexAsPredicateInUse(){
        System.out.println("metoda asPredicate()");


        regex = "\\d+\\D+"; //wystąpi jedna lub wiele cyfr, następnie jeden lub wiele znaków nieliczbowych
        pattern = Pattern.compile(regex);

        Predicate<String> pred = pattern.asPredicate(); //metoda zwraca obiekt typu interfejsu Predicate<String>
        List<String> result = findAll(pred, "jeden 1", "dwa", "3 trzy", "4 cztery 4", "piec i wiecej");
        System.out.println("\t" + result);
    }


    private List<String> findAll(Predicate<String> predicate, String ... args){

        List<String> result = new ArrayList<>();

        for (String s : args){
            if (predicate.test(s)){ //metoda test obiektu Predicate<String> wywołuje metode find() Matchera ustawionego na tekst s. Wynik metody test jest więc wynikiem metody find()()
                result.add(s);
            }
        }
        return result;
    }
    public static void main(String[] args) throws IOException {

        ParsingWithPatternAndMatcher pars = new ParsingWithPatternAndMatcher();
        pars.matchesInUse();
        pars.findInUse();
        pars.groupInUse();
        pars.splitInUse();
        pars.replaceFirstInUse();
        pars.replaceAllInUse();
        pars.regexAsPredicateInUse();
    }

}
