package theknife;

import java.util.*;
import java.io.*;
/**
 * La classe {@code ListaRistorante} gestisce una lista di oggetti {@link Ristorante}.
 * Fornisce metodi per caricare la lista di ristoranti, ottenere le informazioni dei ristoranti,
 * e filtrare i ristoranti.
 */
public class ListaRistorante {

    private List<Ristorante> listaRistoranti;

    /**
     * Costruttore per costruire una la lista dei ristoranti.
     * 
     * @param listaRistoranti La lista di ristoranti.
     */
    public ListaRistorante(List<Ristorante> listaRistoranti) {
        this.listaRistoranti = listaRistoranti;
    }

    /**
     * Carica i ristoranti dal file restaurant.csv {@code GestioneFile.getPathRistoranti()}.
     * 
     * @return Un oggetto {@code ListaRistorante} contenente i ristoranti letti dal file
     * @throws IOException Se errore durante la lettura del file
     */    
    public static ListaRistorante getRistoranti() throws IOException {
        List<Ristorante> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(GestioneFile.getPathRistoranti()))) {
            String line;

            br.readLine();

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

    /**
     * Restituisce una lista di tutti i tipi di cucina presenti.
     * 
     * @return Lista di stringhe contenente i tipi di cucina (in modo unico).
     * @throws IOException Se errore durante il caricamento della lista di ristoranti.
     */
    public static List<String> getTipiCucina() throws IOException {
        ListaRistorante restaurants = getRistoranti();
        List<String> tipiCucina = new ArrayList<>();

        for (Ristorante restaurant : restaurants.getDatiRistoranti()) {
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


    /**
     * Filtra i ristoranti in base alla tipologia di cucina specificata.
     * 
     * @param listaRistoranti Lista di ristoranti.
     * @param tipologia La tipologia da filtrare.
     * @return {@code ListaRistorante} contenente solo i ristoranti della tipologia data.
     */
    public static ListaRistorante filtraTipologia(ListaRistorante listaRistoranti, String tipologia) {
        List<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            if (ristorante.getTipoCucina().equalsIgnoreCase(tipologia) ||
                    ristorante.getTipoCucina().contains(tipologia)) {
                ristorantiFiltrati.add(ristorante);
            }
        }

        return new ListaRistorante(ristorantiFiltrati);
    }

    /**
     * Filtra i ristoranti che offrono il servizio di delivery.
     * 
     * @param listaRistoranti Lista di ristoranti.
     * @return {@code ListaRistorante} contenente solo i ristoranti che offrono il servizio di delivery.
     */
    public static ListaRistorante filtraDelivery(ListaRistorante listaRistoranti) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            if (ristorante.isDelivery()) {
                ristorantiFiltrati.add(ristorante);
            }
        }

        return new ListaRistorante(ristorantiFiltrati);
    }
    /**
     * Filtra i ristoranti che offrono il servizio di prenotazione.
     * 
     * @param listaRistoranti Lista di ristoranti.
     * @return {@code ListaRistorante} contenente solo i ristoranti che offrono il servizio di prenotazione.
     */
    public static ListaRistorante filtraBooking(ListaRistorante listaRistoranti) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            if (ristorante.isBooking()) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }

    /**
     * Filtra i ristoranti in base alla fascia di prezzo.
     * 
     * @param listaRistoranti Lista di ristoranti.
     * @param prezzo una stringa di simboli (es. "€", "€€", "€€€", "€€€€") che indica la fascia di prezzo.
     * @return {@code ListaRistorante} contenente solo i ristoranti della data fascia di prezzo.
     */
    public static ListaRistorante filtraPrezzo(ListaRistorante listaRistoranti, String prezzo) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            String prezzoRistorante = ristorante.getPrezzo();
            if (prezzo.length() == prezzoRistorante.length()) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }

    /**
     * Filtra i ristoranti in base alla media di stelle (valutazione).
     * 
     * @param listaRistoranti Lista di ristoranti.
     * @param stelle il numero esatto di stelle da cercare
     * @return {@code ListaRistorante} contenente solo i ristoranti con la data media di stelle.
     * @throws IOException
     */    
    public static ListaRistorante filtraStelle(ListaRistorante listaRistoranti, int stelle) throws FileNotFoundException, IOException {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            if (stelle == (int)Recensione.getMediaVoti(ristorante.getNome())) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }    

    /**
     * Filtra i ristoranti in base alla località specificata.
     * 
     * @param listaRistoranti la lista originale di ristoranti
     * @param localita la località desiderata
     * @return una nuova {@code ListaRistorante} contenente solo i ristoranti nella località specificata
     */
    public static ListaRistorante filtraPosizione(ListaRistorante listaRistoranti, String localita) {
        LinkedList<Ristorante> ristorantiFiltrati = new LinkedList<>();

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {
            String localitaRistorante = ristorante.getLocalita().toLowerCase();
            if (localita.equalsIgnoreCase(localitaRistorante)) {
                ristorantiFiltrati.add(ristorante);
            }
        }
        return new ListaRistorante(ristorantiFiltrati);
    }

    public List<Ristorante> getDatiRistoranti() {
        return listaRistoranti;
    }
}
