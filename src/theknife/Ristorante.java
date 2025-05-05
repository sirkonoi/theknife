package theknife;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Ristorante {

    // separatore file
    public static String sep = (File.separator);
    
    //campi
    private LinkedList<List<String>> listaRistoranti;

    private List<String> datiRistorante;

    //costruttore
    public Ristorante(LinkedList<List<String>> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
    }

    public Ristorante(List<String> datiRistorante) {
        this.datiRistorante = datiRistorante;
    }

    // metodi get
    public List<String> getDatiRistorante() {
        return datiRistorante;
    }

    public LinkedList<List<String>> getListaRistoranti() { 
        return listaRistoranti;
    }

    @Override
    public String toString() {
        return datiRistorante != null ? datiRistorante.toString() : "Ristorante non inizializzato correttamente.";
    }

    public static List<String> parseCSVLine(String line) {
        List<String> restaurant = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isQuotes = false;
    
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
    
            if (c == '\"') {
                isQuotes = !isQuotes; // stato virgolette
            } else if (c == ',' && !isQuotes) {
                restaurant.add(field.toString().trim());
                field.setLength(0); // reset buffer
            } else {
                field.append(c); // aggiunge il campo
            }
        }
    
        restaurant.add(field.toString().trim()); // aggiungi l'ultimo campo
        return restaurant;
    }

    public static Ristorante getRistoranti() throws IOException {
        LinkedList<List<String>> restaurants = new LinkedList<>();
        List<String> lines = Files.readAllLines(Paths.get("data" + sep + "restaurants.csv"));
        for (String line : lines) {
            List<String> restaurant = parseCSVLine(line);
            restaurants.add(restaurant);
        }
        return new Ristorante(restaurants);
    }

    public static boolean checkRistoranti (String name) throws IOException{
        boolean isCreated = false;
        Ristorante restaurants = getRistoranti();
        for (List<String> restaurant : restaurants.getListaRistoranti()) {
            if (restaurant.get(0).equals(name)) {
                isCreated = true;
                break;
            }
        }
        return isCreated;
    }

    public static void scriviRistorante (String name,String address, String location, String price, String cuisine, String phoneNumber, int award, String greenStar, String facilitiesAndServices) throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "restaurants.csv", true);
        try {
            fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + phoneNumber + "," + award + "," + greenStar + "," + facilitiesAndServices);
            fr.close();
        }

        catch(IOException e) {
            System.out.println("Errore...");
        }
    }
}