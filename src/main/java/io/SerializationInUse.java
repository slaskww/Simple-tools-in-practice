package io;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Serializacja służy do utrwalenia obiektów. Obiekty stworzone przez program są dostępne w czasie działania programu.
 * Kiedy program kończy działanie, dane znajdujące się w przestrzeni adresowej w pamięci zostają usunięte i nie można ich odtworzyć.
 * W celu utrwalenia takich danych korzysta się z serialzacji i klas strumieniowych ObjectInputStream i ObjectOutputStream. Są to klasy przetwarzające, więc przyjmują w
 * konstruktorach obiekty klas przedmiotowych, które skolei przyjmują w konstruktorach fizyczne źródło danych. Następuje więc opakowanie klas przedmiotowych przez klasy przetwarzające.
 * Do strumieni moga być zapisywane tylko obiekty implementujące interfejs Serializable. Prawie wszystkie klay standardowych pakietów Javy implementują ten interfejs.
 * Klasa ObjectInputStream posiada metodę readObject() która odczytuje obiekt ze strumienia (tworzy obiekt na podstawie charakterystyki obiektu zapisanej w strumieniu i inicjuje go
 * odczytanymi wartościami i zwraca refwerencję do tego obiektu). Posiada także metody takie jak writeUTF(), czy readInt(), obsługujące serializację typów prostych i Stringów.
 * Podczas serializacji nie są zapisywane pola statyczne (bo nie należą do obiektu, lecz do klasy) oraz pola ze specyfikatorem transient.
 * Pola obiektowe, których klasy nie implementują interfejsu Serializable także nie są zapisywane. Jeśli chcemy, by tak było, musimy zadbać o to, aby i one implementowały Serializable.
 *
 * W programie skorzystamy z klasy WeatherData, która implementuje interfejs Serializable
 */

public class SerializationInUse {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        WeatherData data = new WeatherData(LocalDate.now(), "It was raining all day",  new ArrayList<>(Arrays.asList(14.0, 17.8, 19.2, 21.0, 22.1, 20.4, 18.8, 16.5)));

        WeatherData.save(data); //wywołujemy statyczną metodę save, która serializuje obiekt 'data' podany w parametrze

        WeatherData loadedData = WeatherData.load("serialOut.txt"); //wywołujemy statyczną metodę load, która deserializuje dane w pliku o nazwie podanej w parametrze
        System.out.println(loadedData);

        }


    }

