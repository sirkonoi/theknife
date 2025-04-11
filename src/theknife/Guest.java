package theknife;

public class Guest extends GestioneUtenti {
    private String username;
    private String luogo;

    public Guest(String luogo) {
        this.username = "Guest";
        this.luogo = luogo;
    }
    
    //metodi get
    public String getDomicilio() {
        return luogo;
    }
    public String getUsername() {
        return username;
    }
    public String getRuolo() {
        return "guest";
    }    
}