package threads;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Proces (program) wykonuje się w dwóch wątkach.
 * Głowny wątek wykonuje się po uruchomieniu metody main().
 * Równolegle wykonuje się wątek utworzony w metodzie main() i uruchomiony metodą start().
 *
 * KOD WYKONUJĄCY SIĘ W WĄTKU określany jest przez obiekt klasy implementującej interfejs RUNNABLE i zawierający deklarację metody run().
 * W naszym przypadku jest to obiekt MyTimer, który implementuje Runnable i implementuje metode run(). Kod metody run() będzie wykonywany w osobnym wątku.
 *
 * OBIEKT WĄTKU tworzymy wykorzystując klasę THREAD. Klasa ta pozwala na tworzenie nowych wątków i uruchamianie w nich kodu wykonujacego.
 * Obiekt klasy Thread przyjmuje w konstruktorze argument typu Runnable (np. nasz obiekt MyTimer), a wywołanie metody start() powoduje uruchomienie kodu
 * metody run() naszego Timera w nowo utworzonym wątku.
 */

public class CapitalsRunnableInAction {

    public static void main(String[] args) {

        Set<String> capitals = new HashSet<>(Arrays.asList("BERLIN", "PRAGA", "BRATYSŁAWA", "KIJÓW", "MIŃSK", "WILNO", "MOSKWA"));
        Set<String> answers = new HashSet<>();

        int count = 0;

        Thread thread = new Thread(new MyTimer());
        thread.start();


        showMessageDialog(null, "Podaj stolice krajów ościennych Polski.");
        while (count < capitals.size()){

            String input = showInputDialog("Poprawnych odpowiedzi: " + count + "/" + capitals.size() + "\n Wpisz kolejną stolicę:");

            if (input == null){
                break;
            }

            input = input.toUpperCase();

            if (!answers.contains(input) && capitals.contains(input)){
                count++;
                showMessageDialog(null, "Poprawna odpowiedź! " + input + " jest stolicą jednego z ościennych państw Polski.");
                answers.add(input);
            }
        }

    }
}
