package theknife;

import java.io.*;
import java.util.*;

public abstract class GestioneUtenti {

    public static String sep = (File.separator);

    //check se l'utente e' già registrato
    public static boolean checkUser(String username) throws IOException {
        boolean isRegistered = false;
        LinkedList<List<String>> users = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data"+ sep + "users.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                users.add(Arrays.asList(values));
            }
        } finally {
            if (br != null) br.close();
        }
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                isRegistered = true;
                break;
            }
        }
        return isRegistered;
    }

    public static String checkRuolo(String username) throws IOException {
        LinkedList<List<String>> users = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data"+ sep + "users.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                users.add(Arrays.asList(values));
            }
        } finally {
            if (br != null) br.close();
        }
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                return user.get(5);
            }
        }
        return null;        

    }

    // REGISTRAZIONE
    public static void register(String username, String psw, String nome, String cognome, String domicilio, String ruolo) throws UserAlreadyExists, IOException {
        if(checkUser(username)) throw new UserAlreadyExists("Errore: L'utente e' gia' esistente.");

        FileWriter fr = new FileWriter("data" + sep + "users.csv", true);
        try {
            fr.write("\n" + username + "," + psw + "," + nome + "," + cognome + "," + "\"" + domicilio + "\"" + ","+ ruolo.toLowerCase());
            fr.close();
        }

        catch(IOException e) {
            System.out.println("Errore...");
        } 
        
    }

//ricorda di cambiare PSW !!!!!!
    public static Utente login(String username, String psw) throws ErroreLogin, IOException {
        LinkedList<List<String>> users = new LinkedList<>();
        BufferedReader br = null;
        Utente u;
        try {
            br = new BufferedReader(new FileReader("data"+ sep + "users.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                users.add(Arrays.asList(values));
            }
        } finally {
            if (br != null) br.close();
        }
        for (List<String> user : users) {
            if (user.get(0).equals(username)) {
                if(user.get(1).equals(psw)) {
                    return u = new Utente(user.get(0), user.get(1), user.get(2), user.get(3), user.get(4), user.get(5));
                }
            }
        }
        
        throw new ErroreLogin("Errore. Utente non esistente o credenziali errate.");
    }


}
