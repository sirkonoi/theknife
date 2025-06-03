package theknife;

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
