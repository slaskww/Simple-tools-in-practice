package io;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class WeatherData implements Serializable {

    private LocalDate date;
    private String description;
    private List<Double> temps;
    private double averTemp;

    public WeatherData(LocalDate date, String description, List<Double> temps) {
        this.date = date;
        this.description = description;
        this.temps = temps;
        this.averTemp = setAverTemp();
    }

    private double setAverTemp(){
        return temps.stream().mapToDouble(aDouble -> aDouble).average().orElse(0);
    }

    public double getAverTemp(){
        return this.averTemp;
    }

    public static void save(WeatherData wd){

        try(
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serialOut.txt"))
                ){

            oos.writeObject(wd);


        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WeatherData load(String filename) throws IOException, ClassNotFoundException {

        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))
                ){
            return (WeatherData) ois.readObject();
        }
    }

    @Override
    public String toString() {
        return "WeatherData{\n\t" +
                "date=" + date +
                "\n\tdescription='" + description + '\'' +
                "\n\ttemps=" + temps +
                "\n\taverTemp=" + averTemp +
                '}';
    }
}
