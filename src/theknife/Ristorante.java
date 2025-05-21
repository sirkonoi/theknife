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

    public static Ristorante getRistoranti() throws IOException {
        LinkedList<List<String>> restaurants = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data" + sep + "restaurants.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> restaurant = GestioneFile.parseRiga(line);
                restaurants.add(restaurant);
            }
        }

        return new Ristorante(restaurants);
    }

    public static List<String> getTipiCucina() throws IOException {
        Ristorante restaurants = getRistoranti();
        List<String> tipiCucina = new ArrayList<>();

        for (List<String> restaurant : restaurants.getListaRistoranti()) {
            if (!restaurant.isEmpty()) {
                String tipi = restaurant.get(4);
                String[] tipi_splittati = tipi.split(",");

                for (String tipo : tipi_splittati) {
                    tipo = tipo.trim();
                    if (!tipiCucina.contains(tipo)) {
                        tipiCucina.add(tipo);
                    }
                }
            }
        }

        return tipiCucina;
    }

    public static boolean checkRistoranti(String name) throws IOException {
        Ristorante restaurants = getRistoranti();
        for (List<String> restaurant : restaurants.getListaRistoranti()) {
            if (!restaurant.isEmpty() && restaurant.get(0).equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static void visualizzaRistorante(Ristorante ristorantiFiltrati, String nomeRistorante) {
        TheKnife.pulisci();
        String topb = "+------------------------------------------------------+";
        String separator = "|------------------------------------------------------|";
        String bottomb = "+------------------------------------------------------+";

        for (List<String> ristorante : ristorantiFiltrati.getListaRistoranti()) {
            if (ristorante.get(0).equalsIgnoreCase(nomeRistorante)) {
                System.out.println(topb);
                System.out.println("| NOME: " + ristorante.get(0) + " (" + ristorante.get(10) + ")");
                System.out.println(separator);
                System.out.println("| TELEFONO: " + ristorante.get(7));
                System.out.println("| INDIRIZZO: " + ristorante.get(1));
                System.out.println("| TIPO DI CUCINA: " + ristorante.get(4));
                System.out.println("| BOOKING: " + (ristorante.get(11).isEmpty() ? "Non disponibile"
                        : ristorante.get(15).equals("True") ? "Si" : "No"));
                System.out.println("| DELIVERY: " + (ristorante.get(12).isEmpty() ? "Non disponibile"
                        : ristorante.get(14).equals("True") ? "Si" : "No"));
                System.out
                        .println("| WEBSITE: " + (ristorante.get(9).isEmpty() ? "Non disponibile" : ristorante.get(9)));
                System.out.println(separator);
                System.out.println("| DESCRIZIONE:\n " + ristorante.get(13));
                System.out.println(bottomb);
            }
        }
    }
    
    public static void scriviRistorante (String name, String address, String location, String price, String cuisine, double longitudine, double latitudine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar, String facilitiesAndServices, String description, boolean delivery, boolean booking) throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "restaurants.csv", true);
        try {
           fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + longitudine + "," + latitudine + "," + phoneNumber + "," + url + "," + webSiteUrl + "," + award + "," + greenStar + "," + facilitiesAndServices + "," + description + "," + delivery + "," + booking);
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
    public LinkedList<List<String>> getListaRistoranti() {
        return listaRistoranti;
    }

    /*@Override
    public String toString() {
        return datiSingoloRistorante != null ? datiSingoloRistorante.toString() : "Ristorante non inizializzato correttamente.";
    }*/



}