package theknife;

import java.io.*;
import java.util.*;

/**
 * Classe astratta che gestisce le funzionalità di gestione utenti
 * come la registrazione e il login.
 */

public abstract class GestioneUtenti {   

    /**
     * Legge il file users.csv e restituisce una lista di utenti.
     * Ogni utente è rappresentato come una lista di stringhe, ovvero i campi.
     * 
     * @return Lista degli utenti
     * @throws IOException Se errore durante lettura del file.
     */
    public static LinkedList<List<String>> getUsers() throws IOException {
        LinkedList<List<String>> users = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(GestioneFile.getPathUtenti()));
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

    /**
     * Registra un nuovo utente nell'applicazione.
     * 
     * @param username Username.
     * @param psw Password (in chiaro).
     * @param nome Nome dell'utente.
     * @param cognome Cognome dell'utente.
     * @param domicilio Indirizzo di domicilio dell'utente.
     * @param ruolo Ruolo dell'utente ("utente" o "ristoratore").
     * @return Oggetto GestioneUtente appena creato.
     * @throws UserAlreadyExists Se l'username è già esistente.
     * @throws IOException Se errore durante scrittura del file.
     */
    public static GestioneUtenti register(String username, String psw, String nome, String cognome, String domicilio,
            String ruolo) throws UserAlreadyExists, IOException {
        if (checkUser(username))
            throw new UserAlreadyExists("Errore: L'utente e' gia' esistente.");

        GestioneUtenti newUser = null;

        FileWriter fr = new FileWriter(GestioneFile.getPathUtenti(), true);
        try {
            fr.write("\n" + username + "," + Password.encrypt(psw) + "," + nome + "," + cognome + ","
                    + domicilio.replace(",", "") + "," + ruolo.toLowerCase());
            if (ruolo.equals("utente")) {
                newUser = new Utente(username, psw, nome, cognome, domicilio, ruolo);
            } else if (ruolo.equals("ristoratore")) {
                newUser = new Ristoratore(username, psw, nome, cognome, domicilio);
            }
            fr.close();
        }

        catch (IOException e) {
            System.out.println("Errore durante la registrazione...");
        }

        return newUser;

    }

    /**
     * Effettua il login verificando username e password.
     * 
     * @param username Username.
     * @param psw Password (in chiaro).
     * @return Oggetto Utente appena loggato.
     * @throws ErroreLogin Se username o password non corrispondono.
     * @throws IOException Se errore durante lettura del file.
     */
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

    /**
     * Verifica se, un dato username, l'utente e' gia' registrato.
     * 
     * @param username Username da controllare.
     * @return True se l'utente esiste, false altrimenti.
     * @throws IOException Se errore nella lettura del file.
     */
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

    /**
     * Restituisce il ruolo associato a un utente (dato il suo username).
     * 
     * @param username Username.
     * @return String ruolo dell'utente ("utente", "ristoratore", "guest") oppure null se non viene trovato.
     * @throws IOException Se errore nella lettura del file.
     */
    public static String checkRuolo(String username) throws IOException {
        LinkedList<List<String>> users = getUsers();
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                return user.get(5);
            }
        }
        return null;

    }

    /**
     * Restituisce lo username dell'utente.
     * 
     * @return String username
     */
    public abstract String getUsername();
    /**
     * Restituisce il ruolo dell'utente.
     * 
     * @return String ruolo
     */    
    public abstract String getRuolo();
    /**
     * Restituisce il domicilio (indirizzo) dell'utente.
     * 
     * @return String domicilio
     */    
    public abstract String getDomicilio();

}
