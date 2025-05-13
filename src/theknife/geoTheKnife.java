package theknife;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class geoTheKnife {

    public static boolean domicilioEsistente(String domicilio) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + domicilio.replace(" ", "+")
                + "&format=json&limit=1";
        URL url = new URL(urlString);

        InputStream response = url.openStream(); // manda una richiesta a Nominatim OpenStreetMap

        BufferedReader rd = new BufferedReader(new InputStreamReader(response)); // legge la risposta -> se la linea è
                                                                                 // vuota non esiste l'indirizzo
        String line = rd.readLine();
        rd.close();

        return line != null && !line.equals("[]");
    }

    public static float[] getLatitudineLongitudine(String indirizzo) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + indirizzo.replace(" ", "+")
                + "&format=json&limit=1";
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String json = response.toString();

        // Trova latitudine
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
        return a; // restituisce array in cui pos. 0 è latitudine e pos.1 è longitudine
    }

    public static Ristorante filtraVicinanza(Ristorante listaRistoranti, String indirizzo, int raggio) throws IOException {
        LinkedList<List<String>> ristorantiFiltrati = new LinkedList<>();
        float[] coordinate = geoTheKnife.getLatitudineLongitudine(indirizzo);
        float latitudineUtente = coordinate[0];
        float longitudineUtente = coordinate[1];
        listaRistoranti.getListaRistoranti().remove(0);

        for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {

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
        double latDistance = (lat2 - lat1) * 111;
        double lonDistance = (lon2 - lon1) * 111;
        return Math.sqrt(latDistance * latDistance + lonDistance * lonDistance);
    }

}
