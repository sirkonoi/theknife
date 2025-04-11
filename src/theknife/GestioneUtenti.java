package theknife;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class GestioneUtenti {

    public static String sep = (File.separator);

    // Restituisce LinkedList di List (una lista = 1 utente), presi da users.csv
    public static LinkedList<List<String>> getUsers() throws IOException {
        LinkedList<List<String>> users = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data" + sep + "users.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                users.add(Arrays.asList(values));
            }
        } finally {
            if (br != null)
                br.close();
        }

        return users;
    }

    // REGISTRAZIONE
    public static Utente register(String username, String psw, String nome, String cognome, String domicilio,
            String ruolo) throws UserAlreadyExists, IOException {
        if (checkUser(username))
            throw new UserAlreadyExists("Errore: L'utente e' gia' esistente.");

        Utente newUser = null;

        FileWriter fr = new FileWriter("data" + sep + "users.csv", true);
        try {
            fr.write("\n" + username + "," + psw + "," + nome + "," + cognome + "," + "\"" + domicilio + "\"" + ","
                    + ruolo.toLowerCase());
            newUser = new Utente(username, psw, nome, cognome, domicilio, ruolo);
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore durante la registrazione...");
        }

        return newUser;

    }

    // Ricorda di cambiare PSW !!!!!!
    // Login (check username, password)
    public static Utente login(String username, String psw) throws ErroreLogin, IOException {
        LinkedList<List<String>> users = getUsers();
        Utente u;
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                if (user.get(1).equals(psw)) {
                    return u = new Utente(user.get(0), user.get(1), user.get(2), user.get(3), user.get(4), user.get(5));
                }
            }
        }

        throw new ErroreLogin("Errore. Utente non esistente o credenziali errate.");
    }

    // Controlla se un utente è già registrato
    public static boolean checkUser(String username) throws IOException {
        boolean isRegistered = false;
        LinkedList<List<String>> users = getUsers();
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                isRegistered = true;
                break;
            }
        }
        return isRegistered;
    }

    // Controlla il ruolo di un dato utente
    public static String checkRuolo(String username) throws IOException {
        LinkedList<List<String>> users = getUsers();
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                return user.get(5);
            }
        }
        return null;

    }

    public abstract String getUsername();
    public abstract String getRuolo();
    public abstract String getDomicilio();

    public static void cercaRistorante(int input, String tipologia) throws IOException {
        LinkedList<List<String>> listaRistoranti = Ristorante.getRistoranti();
        LinkedList<String> listaFiltrati = new LinkedList<String>();
 
        System.out.println("Totale ristoranti letti: " + listaRistoranti.size());
 
        for (List<String> ristorante : listaRistoranti) {
            if (ristorante.get(4).equals(tipologia.substring(1, tipologia.length()-1))) {
                listaFiltrati.add(ristorante.get(0)); // Aggiunge solo il nome
            }
        }
 
        System.out.println("Totale ristoranti letti: " + listaFiltrati.size());
        for (String nome : listaFiltrati) {
            System.out.println(nome);
        }
    }
    
}
