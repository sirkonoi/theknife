package theknife;

import java.util.*;
import java.io.*;

public class ListaRistorante {

    // separatore file
    public static String sep = (File.separator);

    // campi
    private List<Ristorante> listaRistoranti;

    // costruttore 1, prende intero file
    public ListaRistorante(List<Ristorante> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
    }

    public static ListaRistorante getRistoranti() throws IOException {
        List<Ristorante> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data" + sep + "restaurants.csv"))) {
            String line;

            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                List<String> campi = GestioneFile.parseRiga(line);
                if (campi.size() < 16)
                    continue;

                Ristorante r = new Ristorante(
                        campi.get(0), campi.get(1), campi.get(2), campi.get(3),
                        campi.get(4), Double.parseDouble(campi.get(5)), Double.parseDouble(campi.get(6)),
                        campi.get(7), campi.get(8), campi.get(9), campi.get(10),
                        campi.get(11), campi.get(12), campi.get(13),
                        Boolean.parseBoolean(campi.get(14)), Boolean.parseBoolean(campi.get(15)));
                lista.add(r);
            }
        }
        return new ListaRistorante(lista);
    }

    public static List<String> getTipiCucina() throws IOException {
        ListaRistorante restaurants = getRistoranti();
        List<String> tipiCucina = new ArrayList<>();

        for (Ristorante restaurant : restaurants.getListaRistoranti()) {
            if (restaurant != null) {
                String tipi = restaurant.getTipoCucina();
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

    public static boolean checkRistoranti(String nomeRistorante) throws IOException {
        ListaRistorante restaurants = getRistoranti();
        for (Ristorante restaurant : restaurants.getListaRistoranti()) {
            if (restaurant != null && restaurant.getNome().equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }
        return false;
    }

    public static void visualizzaRistorante(ListaRistorante ristorantiFiltrati, String nomeRistorante)
            throws FileNotFoundException, IOException {
        TheKnife.pulisci();
        String topb = "+------------------------------------------------------+";
        String separator = "|------------------------------------------------------|";
        String bottomb = "+------------------------------------------------------+";

        double media = Recensione.getMediaVoti(nomeRistorante);

        for (Ristorante ristorante : ristorantiFiltrati.getListaRistoranti()) {
            if (ristorante.getNome().equalsIgnoreCase(nomeRistorante)) {
                System.out.println(topb);
                System.out
                        .println("| Nome: " + ristorante.getNome().toUpperCase() + " (" + ristorante.getPremio() + ")");
                System.out.println(separator);
                System.out.println("| Media Voti: " + (media < 0 ? "Nessuna valutazione." : media));
                System.out.println("| Telefono: " + ristorante.getTelefono());
                System.out.println("| Indirizzo: " + ristorante.getIndirizzo().toUpperCase());
                System.out.println("| Tipo di Cucina: " + ristorante.getTipoCucina().toUpperCase());
                System.out.println("| Booking: " + (ristorante.isBooking() ? "Si" : "No"));
                System.out.println("| Delivery: " + (ristorante.isDelivery() ? "Si" : "No"));
                System.out
                        .println("| Sito Web: "
                                + (ristorante.getSitoWeb().isEmpty() ? "Non disponibile" : ristorante.getSitoWeb()));
                System.out.println(separator);
                System.out.println("| Descrizione:\n " + ristorante.getDescrizione());
                System.out.println(bottomb);
            }
        }
    }

    public static void scriviRistorante(String name, String address, String location, String price, String cuisine,
            double longitudine, double latitudine, String phoneNumber, String url, String webSiteUrl, int award,
            String greenStar, String facilitiesAndServices, String description, boolean delivery, boolean booking)
            throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "restaurants.csv", true);
        try {
            fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + longitudine
                    + "," + latitudine + "," + phoneNumber + "," + url + "," + webSiteUrl + "," + award + ","
                    + greenStar + "," + facilitiesAndServices + "," + description + "," + delivery + "," + booking);
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore...");
        }
    }

    // METODI FILTRI

    public static ListaRistorante filtraTipologia(ListaRistorante listaRistoranti, String tipologia) {
        List<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            if (ristorante.getTipoCucina().equalsIgnoreCase(tipologia) ||
                    ristorante.getTipoCucina().contains(tipologia)) {
                ristorantiFiltrati.add(ristorante);
            }
        }

        return new ListaRistorante(ristorantiFiltrati);
    }

    // 14
    public static ListaRistorante filtraDelivery(ListaRistorante listaRistoranti) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            if (ristorante.isDelivery()) {
                ristorantiFiltrati.add(ristorante);
            }
        }

        return new ListaRistorante(ristorantiFiltrati);
    }

    // da rifare
    public static ListaRistorante filtraBooking(ListaRistorante listaRistoranti) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        // 15
        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            if (ristorante.isBooking()) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }

    public static ListaRistorante filtraPrezzo(ListaRistorante listaRistoranti, String prezzo) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            String prezzoRistorante = ristorante.getPrezzo();
            if (prezzo.length() == prezzoRistorante.length()) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }

    // inutile?????
    public static ListaRistorante filtraPosizione(ListaRistorante listaRistoranti, String localita) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            String localitaRistorante = ristorante.getLocalita().toLowerCase();
            if (localita.equalsIgnoreCase(localitaRistorante)) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }

    // METODI GET
    public List<Ristorante> getListaRistoranti() {
        return listaRistoranti;
    }
}
