package theknife;

/**
 * La classe Guest rappresenta un utente ospite nell'app, ovvero che non ha effettuato la registrazione.
 * Un guest ha un username fisso "Guest" e un domicilio associato, per la ricerca dei ristoranti nelle vicinanze.
 * La classe estende {@link GestioneUtenti}.
 */

public class Guest extends GestioneUtenti {
    private String username;
    private String domicilio;

    /**
     * Costruttore della classe Guest, riceve come parametro il domicilio.
     * L'username è impostato di default a "Guest".
     * 
     * @param domicilio Il domicilio associato al guest.
     */    
    public Guest(String domicilio) {
        this.username = "Guest";
        this.domicilio = domicilio;
    }
    
    /**
     * Restituisce il domicilio associato al guest.
     * 
     * @return Il domicilio del guest.
     */
    public String getDomicilio() {
        return domicilio;
    }
    /**
     * Restituisce l'username ("Guest") associato al guest.
     * 
     * @return "Guest"
     */    
    public String getUsername() {
        return username;
    }
    /**
     * Restituisce il ruolo associato al guest.
     * 
     * @return Il ruolo del guest.
     */    
    public String getRuolo() {
        return "guest";
    }    
}