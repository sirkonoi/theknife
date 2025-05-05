package theknife;

import java.util.*;
import java.io.*;

public class Ristorante {

    // separatore file
    public static String sep = (File.separator);
    
    //campi
    private LinkedList<List<String>> listaRistoranti;
    private List<String> datiSingoloRistorante;

    //costruttore 1, prende intero  file
    public Ristorante(LinkedList<List<String>> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
    }

    //costruttore singolo ristorante
    public Ristorante(List<String> datiSingoloRistorante) {
        this.datiSingoloRistorante = datiSingoloRistorante;
    }

    public static List<String> parseRistorante(String line) {
        List<String> restaurant = new ArrayList<>();
        String campo = "";
        boolean isVirgolette = false;
    
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
    
            if (c == '\"') {
                isVirgolette = !isVirgolette; // cambia stato virgolette
            } else if (c == ',' && !isVirgolette) {
                restaurant.add(campo.trim());
                campo = ""; // reset campo
            } else {
                campo += c; // concatena il carattere
            }
        }
    
        restaurant.add(campo.trim()); // aggiungi l'ultimo campo
        return restaurant;
    }
    
    public static Ristorante getRistoranti() throws IOException {
        LinkedList<List<String>> restaurants = new LinkedList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader("data" + sep + "restaurants.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> restaurant = parseRistorante(line);
                restaurants.add(restaurant);
            }
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

    // metodi get
    public List<String> getDatiRistorante() {
        return datiSingoloRistorante;
    }

    public LinkedList<List<String>> getListaRistoranti() { 
        return listaRistoranti;
    }

    @Override
    public String toString() {
        return datiSingoloRistorante != null ? datiSingoloRistorante.toString() : "Ristorante non inizializzato correttamente.";
    }

}