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

    public Ristorante(String nome, String indirizzo, String localita, String prezzo, String tipoCucina,
            double longitudine, double latitudine, String telefono, String url, String sitoWeb,
            String premio, String greenStar, String servizi, String descrizione,
            boolean delivery, boolean booking) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.localita = localita;
        this.prezzo = prezzo;
        this.tipoCucina = tipoCucina;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
        this.telefono = telefono;
        this.url = url;
        this.sitoWeb = sitoWeb;
        this.premio = premio;
        this.greenStar = greenStar;
        this.servizi = servizi;
        this.descrizione = descrizione;
        this.delivery = delivery;
        this.booking = booking;
    }


    public static boolean checkRistoranti(String nomeRistorante) throws IOException {
        ListaRistorante restaurants = ListaRistorante.getRistoranti();
        for (Ristorante restaurant : restaurants.getListaRistoranti()) {
            if (restaurant != null && restaurant.getNome().equalsIgnoreCase(nomeRistorante)) {
                return true;
            }
        }
        return false;
    }

    public static void visualizzaRistorante(ListaRistorante ristorantiFiltrati, String nomeRistorante)
            throws FileNotFoundException, IOException {
        Utility.pulisci();
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

    public static void scriviRistorante(String name, String address, String location, String price, String cuisine, double longitudine, double latitudine, String phoneNumber, String url, String webSiteUrl, int award, String greenStar, String facilitiesAndServices, String description, boolean delivery, boolean booking) {
        try (FileWriter fr = new FileWriter(GestioneFile.getPathRistoranti(), true)) {
            fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + longitudine
                    + "," + latitudine + "," + phoneNumber + "," + url + "," + webSiteUrl + "," + award + ","
                    + greenStar + "," + facilitiesAndServices + "," + description + "," + delivery + "," + booking);
        } catch (IOException e) {
            System.out.println("Errore: la scrittura del ristorante e' fallita: " + e.getMessage());
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
