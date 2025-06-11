package theknife;

import java.io.*;

public class Ristorante {

    private String nome;
    private String indirizzo;
    private String localita;
    private String prezzo;
    private String tipoCucina;
    private double longitudine;
    private double latitudine;
    private String telefono;
    private String url;
    private String sitoWeb;
    private String premio;
    private String greenStar;
    private String servizi;
    private String descrizione;
    private boolean delivery;
    private boolean booking;
    /**
     * Costruisce un nuovo oggetto Ristorante.
     *
     * @param name Nome del ristorante.
     * @param address Indirizzo del ristorante.
     * @param location Città, zona.
     * @param price Fascia di prezzo (€, €€, €€€, €€€€).
     * @param cuisine Tipo di cucina.
     * @param phoneNumber Numero di telefono.
     * @param url URL di riferimento (esempio instagram, facebook etc...).
     * @param webSiteUrl Sito web.
     * @param award Numero di premi ottenuti.
     * @param greenStar Stella verde (sostenibilità).
     * @param facilitiesAndServices Servizi e strutture che vengono offerti.
     * @param description Descrizione del ristorante.
     * @param delivery Se offre il servizio di delivery.
     * @param booking Se accetta prenotazioni.
     */
    public Ristorante(String name, String address, String location, String price, String cuisine,
            double longitudine, double latitudine, String phoneNumber, String url, String webSiteUrl,
            String award, String greenStar, String facilitiesAndServices, String description,
            boolean delivery, boolean booking) {
        this.nome = name;
        this.indirizzo = address;
        this.localita = location;
        this.prezzo = price;
        this.tipoCucina = cuisine;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
        this.telefono = phoneNumber;
        this.url = url;
        this.sitoWeb = webSiteUrl;
        this.premio = award;
        this.greenStar = greenStar;
        this.servizi = facilitiesAndServices;
        this.descrizione = description;
        this.delivery = delivery;
        this.booking = booking;
    }

    /**
     * Controlla se un dato ristorante e' gia' esistente.
     *
     * @param nomeRistorante Il nome del ristorante da cercare.
     * @return True se il ristorante è gia' esistente, false altrimenti.
     * @throws IOException Se si verifica un errore durante il caricamento della lista di ristoranti.
     */
    public static boolean checkRistoranti(String nomeRistorante) throws IOException {
        ListaRistorante restaurants = ListaRistorante.getRistoranti();
        for (Ristorante restaurant : restaurants.getDatiRistoranti()) {
            if (restaurant != null && restaurant.getNome().equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scrive le informazioni di un nuovo ristorante nel file restaurants.csv.
     *
     * @param name Nome del ristorante.
     * @param address Indirizzo del ristorante.
     * @param location Città, zona.
     * @param price Fascia di prezzo (€, €€, €€€, €€€€).
     * @param cuisine Tipo di cucina..
     * @param longitudine Longitudine.
     * @param latitudine Latitudine geografica.
     * @param phoneNumber Numero di telefono.
     * @param url URL di riferimento (esempio instagram, facebook etc...).
     * @param webSiteUrl Sito web.
     * @param award Numero di premi ottenuti.
     * @param greenStar Stella verde (sostenibilità).
     * @param facilitiesAndServices Servizi e strutture che vengono offerti.
     * @param description Descrizione del ristorante.
     * @param delivery Se offre il servizio di delivery.
     * @param booking Se accetta prenotazioni.
     */
    public static void scriviRistorante(String name, String address, String location, String price, String cuisine, double longitudine, double latitudine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar, String facilitiesAndServices, String description, boolean delivery, boolean booking) {
        try (FileWriter fr = new FileWriter(GestioneFile.getPathRistoranti(), true)) {
            fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + longitudine
                    + "," + latitudine + "," + phoneNumber + "," + url + "," + webSiteUrl + "," + award + ","
                    + greenStar + "," + facilitiesAndServices + "," + description + "," + delivery + "," + booking);
        } catch (IOException e) {
            System.out.println("Errore: la scrittura del ristorante e' fallita: " + e.getMessage());
        }
    }

    /**
     * Visualizza le informazioni dettagliate di un dato ristorante.
     * 
     * @param ristorantiFiltrati Lista di ristoranti tra cui cercare.
     * @param nomeRistorante Nome del ristorante che si vuole visualizzare.
     * @throws IOException Se si verifica un errore durante il caricamento della lista di ristoranti.
     */
    public static void visualizzaRistorante(ListaRistorante ristorantiFiltrati, String nomeRistorante)
            throws FileNotFoundException, IOException {
        Utility.pulisci();
        String topb = "+------------------------------------------------------+";
        String separator = "|------------------------------------------------------|";
        String bottomb = "+------------------------------------------------------+";

        double media = Recensione.getMediaVoti(nomeRistorante);

        for (Ristorante ristorante : ristorantiFiltrati.getDatiRistoranti()) {
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

    public String getNome() {
        return nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getLocalita() {
        return localita;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getTipoCucina() {
        return tipoCucina;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUrl() {
        return url;
    }

    public String getSitoWeb() {
        return sitoWeb;
    }

    public String getPremio() {
        return premio;
    }

    public String getGreenStar() {
        return greenStar;
    }

    public String getServizi() {
        return servizi;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public boolean isBooking() {
        return booking;
    }
}
