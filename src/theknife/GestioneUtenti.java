package theknife;

import java.io.*;
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
            fr.write("\n" + username + "," + Password.encrypt(psw) + "," + nome + "," + cognome + "," + domicilio + "," + ruolo.toLowerCase());
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
                if (Password.decrypt(user.get(1)).equals(psw)) {
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

    //Cerca un ristorante
    public static void cercaRistorante(int input, String tipologia) throws IOException {
        Ristorante listaRistoranti = Ristorante.getRistoranti();
        List<Ristorante> ristorantiFiltrati = new LinkedList<>();

        switch (input) {
            case 12:
            if (tipologia.equals("delivery")) {
            
                for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
                    if (ristorante.get(13).toLowerCase().contains("deliv") || ristorante.get(13).toLowerCase().contains("order") ||
                    ristorante.get(13).toLowerCase().contains("takeaway")) {
                        ristorantiFiltrati.add(new Ristorante(ristorante));
                    }
                }
            }
            else if (tipologia.equals("booking")) {
                for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
                    if (ristorante.get(13).toLowerCase().contains("book") || ristorante.get(13).toLowerCase().contains("online") || 
                    ristorante.get(13).toLowerCase().contains("reserve")) {
                        ristorantiFiltrati.add(new Ristorante(ristorante));
                    }
                }
            }
                break;
            default:
            for (List<String> ristorante : listaRistoranti.getListaRistoranti()) {
                if (ristorante.get(input).equalsIgnoreCase(tipologia))  {
                    ristorantiFiltrati.add(new Ristorante(ristorante));
                }
            }
                break;
        }
    
        System.out.println("Totale ristoranti filtrati: " + ristorantiFiltrati.size());
        for (Ristorante r : ristorantiFiltrati) {
            System.out.println(r.getDatiRistorante().get(0)); // stampa la nome del ristorante
        }
    }

    //metodi get
    public abstract String getUsername();
    public abstract String getRuolo();
    public abstract String getDomicilio();    
    
}
