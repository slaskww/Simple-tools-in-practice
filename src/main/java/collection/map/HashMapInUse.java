package collection.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class HashMapInUse {

    public static void main(String[] args) throws FileNotFoundException {

        Map<String, String> shops = new HashMap<>();

        Scanner scan = new Scanner(new File("addresses.txt"));

        while (scan.hasNextLine()){

            String name = scan.nextLine();
            String address = scan.nextLine();
            shops.put(name, address);
        }

        String sname;
        while ((sname = showInputDialog("Podaj nazwę sklepu")) != null){
            String address = shops.get(sname);
            if (address == null){
                address = "brak adresu";
            }
            showMessageDialog(null, "Sklep " + sname + "\n" + address);
        }
    }


}
