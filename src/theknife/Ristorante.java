package theknife;

import java.util.*;
import java.io.*;

public class Ristorante {

    // separatore file
    public static String sep = (File.separator);

    // campi
    private LinkedList<List<String>> listaRistoranti;

    // costruttore 1, prende intero file
    public Ristorante(LinkedList<List<String>> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
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

    public static Ristorante filtraDelivery(Ristorante listaRistoranti) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String servizi = ristorante.get(13).toLowerCase();
            if (servizi.contains("deliv") || servizi.contains("order") || servizi.contains("takeaway")) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

    public static Ristorante filtraBooking(Ristorante listaRistoranti) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String servizi = ristorante.get(13).toLowerCase();
            if (servizi.contains("book") || servizi.contains("online") || servizi.contains("reserve")) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

    public static Ristorante filtraPrezzo(Ristorante listaRistoranti, int prezzo) {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            String prezzoRistorante = ristorante.get(3);
            String prezzoUtente = Integer.toString(prezzo);
            if (prezzoUtente.length() == prezzoRistorante.length()) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new Ristorante(ristorantiFiltrati);
    }

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

    public static Ristorante filtraVicinanza(Ristorante listaRistoranti, String indirizzo, int raggio) throws IOException {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();
        float[] coordinate = geoTheKnife.getLatitudineLongitudine(indirizzo);
        float latitudineUtente = coordinate[0];
        float longitudineUtente = coordinate[1];
        boolean isFirst = true;

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
            if (isFirst) {
                isFirst = false;
                continue; // salta intestazione
            }
            float latitudineRistorante = Float.parseFloat(ristorante.get(6));
            float longitudineRistorante = Float.parseFloat(ristorante.get(5));
            double distanza = distanzaSemplificata(latitudineUtente, longitudineUtente, latitudineRistorante,
                    longitudineRistorante);
            if (distanza <= raggio) {
                ristorantiFiltrati.add(ristorante);
            }
        }

        return new Ristorante(ristorantiFiltrati);
    }

    public static double distanzaSemplificata(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = (lat2 - lat1) * 111; // 1 grado di latitudine ≈ 111 km ovunque
        double lonDistance = (lon2 - lon1) * 111; // idem longitudine
        return Math.sqrt(latDistance * latDistance + lonDistance * lonDistance);
    }


    //METODI GET
    public LinkedList<List<String>> getListaRistoranti() {
        return listaRistoranti;
    }
    
}