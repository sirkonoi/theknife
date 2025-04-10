package theknife;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utente {
    private String nome, cognome, username;
    private Password psw;
    private String domicilio;
    private String ruolo;

    //costruttore
    public Utente(String nome, String cognome, String username, Password psw, String domicilio, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.psw=psw;
        this.domicilio = domicilio;
        this.ruolo=ruolo;
    }

    //metodi
    public static boolean checkUser(String username) throws IOException{
        boolean isRegistered = false;
        List<List<String>> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                users.add(Arrays.asList(values));
            }
        }
        for (List<String> user : users) {
            String currentUser = user.get(0); 
            if (currentUser.equals(username)) {
                isRegistered = true;
                break;
            }
        }               
        return isRegistered;
    }

    /*public static Utente register() {
        
    }

    public static boolean login() {
        boolean login = false;
        return login;
    }*/



    //metodi Get
    public String getNome() {
        return this.nome;
    }
    public String getCognome() {
        return this.cognome;
    }
    public String getUsername() {
        return this.username;
    }
    public Password getPsw() {
        return this.psw;
    }
    public String getDomicilio() {
        return this.domicilio;
    }
    public String getRuolo() {
        return this.ruolo;
    }

}