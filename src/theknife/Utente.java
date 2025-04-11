package theknife;

public class Utente extends GestioneUtenti {
    protected String nome, cognome;
    protected static String username;
    protected String psw;
    protected String domicilio;
    protected String ruolo;

    //costruttore
    public Utente(String username, String psw, String nome, String cognome, String domicilio, String ruolo) {
        this.username = username;
        this.psw = psw;        
        this.nome = nome;
        this.cognome = cognome;
        this.domicilio = domicilio;
        this.ruolo=ruolo;
    }

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