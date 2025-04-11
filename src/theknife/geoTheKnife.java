package theknife;

import java.io.*;
import java.net.*;

public class geoTheKnife {
    
    public static boolean domicilioEsistente(String domicilio) throws IOException {
      String urlString = "https://nominatim.openstreetmap.org/search?q=" + domicilio.replace(" ", "+") + "&format=json&limit=1";
      URL url = new URL(urlString);
    
      InputStream response = url.openStream(); //manda una richiesta a Nominatim OpenStreetMap
      
      BufferedReader rd = new BufferedReader(new InputStreamReader(response)); //legge la risposta -> se la linea è vuota non esiste l'indirizzo
      String line = rd.readLine(); 
      rd.close();

      return line != null && !line.equals("[]");
    }

    public static String getLatitudineLongitudine(String indirizzo) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + indirizzo.replace(" ", "+") + "&format=json&limit=1";
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
            return "Indirizzo non valido o non trovato.";
        }
    
        String lat = json.substring(latIndex + 7, json.indexOf("\"", latIndex + 7));
        String lon = json.substring(lonIndex + 7, json.indexOf("\"", lonIndex + 7));
    
        return "Latitudine: " + lat + ", Longitudine: " + lon;
    }
    
    
}
