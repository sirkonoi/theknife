package theknife;

import java.util.*;
import java.io.*;

public class Ristorante {

    // separatore file
    public static String sep = (File.separator);
    public String filePath = "";

    // campi
    private LinkedList<List<String>> listaRistoranti;
    private List<String> datiSingoloRistorante;

    // costruttore 1, prende intero file
    public Ristorante(LinkedList<List<String>> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
    }

    // costruttore singolo ristorante
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

    public static boolean checkRistoranti(String name) throws IOException {
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
    
    public static void scriviRistorante (String name, String address, String location, String price, String cuisine, double longitudine, double latitudine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar, String facilitiesAndServices, String description) throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "restaurants.csv", true);
        try {
            fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + longitudine + "," + latitudine + "," + phoneNumber + "," + award + "," + greenStar + "," + facilitiesAndServices);
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore...");
        }
    }

    //METODI FILTRI

    public static Ristorante filtraTipologia(Ristorante listaRistoranti, String tipologia) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            if (ristorante.get(4).equalsIgnoreCase(tipologia) || ristorante.get(4).contains(tipologia)) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

    //14
    public static Ristorante filtraDelivery(Ristorante listaRistoranti) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String servizi = ristorante.get(14).toLowerCase();
            if (servizi.equals("true")) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

    //da rifare
    public static Ristorante filtraBooking(Ristorante listaRistoranti) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        //15
        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String servizi = ristorante.get(15).toLowerCase();
            if (servizi.equals("true")) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

    public static Ristorante filtraPrezzo(Ristorante listaRistoranti, String prezzo) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String prezzoRistorante = ristorante.get(3);
            if (prezzo.length() == prezzoRistorante.length()) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

    //inutile?????
    public static Ristorante filtraPosizione(Ristorante listaRistoranti, String localita) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String localitaRistorante = ristorante.get(2).toLowerCase();
            if (localita.equalsIgnoreCase(localitaRistorante)) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }


    //METODI GET
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