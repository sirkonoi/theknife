package theknife;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static LinkedList<List<String>> getFileRecensioni() throws FileNotFoundException, IOException {
        LinkedList<List<String>> recensioni = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data" + sep + "recensioni.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> recensione = GestioneFile.parseRiga(line);
                recensioni.add(recensione);
            }
        }

        return recensioni;
    }

    public static LinkedList<List<String>> getFileRistoratori() throws FileNotFoundException, IOException {
        LinkedList<List<String>> ristoratori = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data" + sep + "ristoratori.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> recensione = GestioneFile.parseRiga(line);
                ristoratori.add(recensione);
            }
        }

        return ristoratori;
    }

    public static void salvaFileRecensioni(List<List<String>> recensioni) throws IOException {
        File file = new File("data" + sep + "recensioni.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // scrivi una sola volta l'header
            writer.write("utente_recensore,\"Nome Ristorante\",Valutazione,\"Recensione\"");
            writer.newLine();

            for (List<String> riga : recensioni) {
                // Salta righe che sono un duplicato dell'header
                if (riga.get(0).equalsIgnoreCase("utente_recensore")) {
                    continue;
                }

                for (int i = 0; i < riga.size(); i++) {
                    String campo = riga.get(i);
                    if (campo.contains(",") || campo.contains("\"")) {
                        campo = "\"" + campo.replace("\"", "\"\"") + "\"";
                    }
                    writer.write(campo);
                    if (i < riga.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        }
    }

}
