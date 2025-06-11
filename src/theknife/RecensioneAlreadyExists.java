package theknife;
/**
 * Eccezione personalizzata legata alle recensioni.
 * 
 * <p>Viene lanciata quando si cerca di aggiungere una recensione già esistente.</p>
 */
public class RecensioneAlreadyExists extends Exception {
    /**
     * Costruttore dell'eccezione {@code RecensioneAlreadyExists}.
     *
     * @param error Messaggio di errore.
     */      
    public RecensioneAlreadyExists(String error) {
        super(error);
    }
}
