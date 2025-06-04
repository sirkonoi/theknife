package theknife;

/**
 * Eccezione personalizzata legata ad errore durante il login.
 *
 * Viene utilizzata per segnalare credenziali errate all'utente durante il login.
 */

public class ErroreLogin extends Exception {

    /**
     * Costruttore dell'eccezione ErroreLogin.
     *
     * @param errore Messaggio di errore.
     */    
    public ErroreLogin(String errore) {
        super(errore);
    }
}