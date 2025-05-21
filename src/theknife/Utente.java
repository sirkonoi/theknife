package theknife;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utente extends GestioneUtenti {
    protected String nome, cognome;
    protected static String username;
    protected String psw;
    protected String domicilio;
    protected String ruolo;

    // costruttore
    public Utente(String username, String psw, String nome, String cognome, String domicilio, String ruolo) {
        this.username = username;
        this.psw = psw;
        this.nome = nome;
        this.cognome = cognome;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    public static void visualizzaProfilo(Utente user) throws IOException {
        TheKnife.pulisci();TheKnife.printLogo();
        String sceltaMenu = "";
        String psw = "";
        Scanner sc = new Scanner(System.in);

        for(int i=0; i<user.getPsw().length(); i++) {
            psw +="*";
        }

        while (true) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + psw);                 
            System.out.println("Nome: " + user.getNome().toUpperCase());
            System.out.println("Cognome: " + user.getCognome().toUpperCase());
            System.out.println("Domicilio: " + user.getDomicilio().toUpperCase());      
            System.out.println("Sei un: " + user.getRuolo().toUpperCase());

            System.out.println("1 - Visualizza password.");            
            System.out.println("2 - Torna al menu' principale.");
            sceltaMenu = sc.nextLine();

            if (sceltaMenu.equals("1")) {
                TheKnife.pulisci();TheKnife.printLogo();
                psw = Password.decrypt(user.getPsw());
            } 
            else if (sceltaMenu.equals("2")) {
                TheKnife.main_menu();
                break;
            }            
            else {
                TheKnife.pulisci();TheKnife.printLogo();
                System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    public static void getPreferiti(String user) {
        
    }
    
    // metodi Get
    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPsw() {
        return this.psw;
    }

    public String getDomicilio() {
        return this.domicilio;
    }

    public String getRuolo() {
        return this.ruolo;
    }

}