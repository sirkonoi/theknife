package theknife;

import java.util.*;
import java.io.*;

public class ListaRistorante {

    // campi
    private List<Ristorante> listaRistoranti;

    // costruttore 1, prende intero file
    public ListaRistorante(List<Ristorante> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
    }

    public static ListaRistorante getRistoranti() throws IOException {
        List<Ristorante> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(GestioneFile.getPathRistoranti()))) {
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

    public static ListaRistorante filtraBooking(ListaRistorante listaRistoranti) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

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

    public static ListaRistorante filtraStelle(ListaRistorante listaRistoranti, int stelle) throws FileNotFoundException, IOException {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getListaRistoranti()) {
            if (stelle == (int)Recensione.getMediaVoti(ristorante.getNome())) {
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
