package theknife;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class GestioneFile {

    public static String sep = (File.separator);

    public static List<String> parseRiga(String line) {
        List<String> riga = new ArrayList<>();
        String campo = "";
        boolean isVirgolette = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                isVirgolette = !isVirgolette; // cambia stato virgolette
            } else if (c == ',' && !isVirgolette) {
                riga.add(campo.trim());
                campo = ""; // reset campo
            } else {
                campo += c; // concatena il carattere
            }
        }

        riga.add(campo.trim()); // aggiungi l'ultimo campo
        return riga;
    }

    public static LinkedList<List<String>> getFilePreferiti() throws IOException {
        LinkedList<List<String>> preferiti = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data" + sep + "preferiti.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> riga = GestioneFile.parseRiga(line);
                preferiti.add(riga);
            }
        }

        return preferiti;
    }
}
