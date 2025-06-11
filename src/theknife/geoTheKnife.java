package theknife;

import java.io.*;
import java.net.*;
import java.util.*;
    /**
     * Classe che gestisce le funzionalità geografiche per l'applicazione TheKnife:
     * verifica della validità di un indirizzo, il calcolo di latitudine e longitudine,
     * e il filtraggio dei ristoranti nei pressi di un dato indirizzo.
     */
public class geoTheKnife {

   /**
     * Verifica se un indirizzo esiste utilizzando il servizio Nominatim di OpenStreetMap.
     * <a href="https://nominatim.org/release-docs/latest/api/Search/" target="_blank">Documentazione ufficiale Nominatim Search</a>.    
     *
     * @param domicilio L'indirizzo da verificare.
     * @return True se l'indirizzo esiste, false altrimenti.
     * @throws IOException
     */    
    public static boolean domicilioEsistente(String domicilio) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + domicilio.replace(" ", "+")
                + "&format=json&limit=1";
        URL url = URI.create(urlString).toURL();

        InputStream response = url.openStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(response));
        String line = rd.readLine();
        rd.close();

        return line != null && !line.equals("[]");
    }

    /**
     * Ottiene latitudine e longitudine di un indirizzo tramite l'API Nominatim.
     *
     * @param indirizzo L'indirizzo di cui vogliamo sapere le coordinate.
     * @return Un array float dove l'indice 0 è la latitudine e l'indice 1 è la longitudine, oppure null se l'indirizzo non è trovato.
     * @throws IOException Se si verifica un errore durante la richiesta HTTP.
     */    
    public static float[] getLatitudineLongitudine(String indirizzo) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + indirizzo.replace(" ", "+")
                + "&format=json&limit=1";
        URL url = URI.create(urlString).toURL();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String json = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            json = json + inputLine;
        }
        in.close();

        int latIndex = json.indexOf("\"lat\":\"");
        int lonIndex = json.indexOf("\"lon\":\"");

        if (latIndex == -1 || lonIndex == -1) {
            return null;
        }

        float[] a = new float[2];
        String lat = json.substring(latIndex + 7, json.indexOf("\"", latIndex + 7));
        String lon = json.substring(lonIndex + 7, json.indexOf("\"", lonIndex + 7));

        a[0] = Float.parseFloat(lat);
        a[1] = Float.parseFloat(lon);
        return a; //a[0] è la latitudine e a[1] è long
    }

    /**
     * Filtra una lista di ristoranti, restituisce solo quelli entro un certo raggio da un dato indirizzo.
     *
     * @param listaRistoranti La lista di ristoranti da filtrare.
     * @param indirizzo L'indirizzo di riferimento.
     * @param raggio Il raggio massimo entro cui cercare i ristoranti (es. 30).
     * @return Lista di ristoranti filtrati in base alla vicinanza.
     * @throws IOException
     */    
    public static ListaRistorante filtraVicinanza(ListaRistorante listaRistoranti, String indirizzo, int raggio)
            throws IOException {
        List<Ristorante> ristorantiFiltrati = new LinkedList<>();
        float[] coordinate = geoTheKnife.getLatitudineLongitudine(indirizzo);
        float latitudineUtente = coordinate[0];
        float longitudineUtente = coordinate[1];
        listaRistoranti.getDatiRistoranti().remove(0);

        for (Ristorante ristorante : listaRistoranti.getDatiRistoranti()) {

            float latitudineRistorante = (float) ristorante.getLatitudine();
            float longitudineRistorante = (float) ristorante.getLongitudine();
            double distanza = calcolaDistanza(latitudineUtente, longitudineUtente, latitudineRistorante,
                    longitudineRistorante);
            if (distanza <= raggio) {
                ristorantiFiltrati.add(ristorante);
            }
        }

        return new ListaRistorante(ristorantiFiltrati);
    }

    /**
     * Restituisce la lista di ristoranti entro un certo raggio da un dato indirizzo.
     *
     * @param indirizzo L'indirizzo.
     * @param raggio Il raggio massimo per la ricerca.
     * @return La lista filtrata di ristoranti vicini all'indirizzo.
     * @throws IOException
     */    
    public static ListaRistorante cercaVicinanza(String indirizzo, int raggio) throws IOException {
        ListaRistorante listaFiltrati = ListaRistorante.getRistoranti();
        listaFiltrati = geoTheKnife.filtraVicinanza(listaFiltrati, indirizzo, raggio);
        return listaFiltrati;
    }

    /**
     * Metodo per calcolare la distanza approssimativa, utilizzando una formula semplificata, tra due coordinate 
     * geografiche (latitudine e longitudine)
     *
     * @param lat1 Latitudine del primo punto.
     * @param lon1 Longitudine del primo punto.
     * @param lat2 Latitudine del secondo punto.
     * @param lon2 Longitudine del secondo punto.
     * @return La distanza approssimativa.
     */
    public static double calcolaDistanza(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = (lat2 - lat1) * 111;
        double lonDistance = (lon2 - lon1) * 111;
        return Math.sqrt(latDistance * latDistance + lonDistance * lonDistance);
    }
}
