package theknife;
/**
 * Eccezione personalizzata legata all'aggiuta di un ristorante..
 * 
 * <p>Viene lanciata quando si cerca di aggiungere un ristorante già esistente.</p>
 */
public class RestaurantAlreadyExists extends Exception {
    /**
     * Costruttore dell'eccezione {@code RistoranteAlreadyExists}.
     *
     * @param error Messaggio di errore.
     */         
    public RestaurantAlreadyExists(String error) {
        super(error);
    }
}
