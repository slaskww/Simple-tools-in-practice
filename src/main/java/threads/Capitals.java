package threads;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class Capitals {

    public static void main(String[] args) {

        Set<String> capitals = new HashSet<>(Arrays.asList("BERLIN", "PRAGA", "BRATYSŁAWA", "KIJÓW", "MIŃSK", "WILNO", "MOSKWA"));
        Set<String> answers = new HashSet<>();

        int count = 0;

        Thread thread = new Thread(new Timer());
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
