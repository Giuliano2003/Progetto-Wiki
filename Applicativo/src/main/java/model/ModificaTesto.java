package model;
import java.sql.Time;
import java.util.Date;

/**
 * Classe che mi conserva le informazioni sulle modifiche del testo proposte dagli utenti
 */
public class ModificaTesto {
    /**
     * Il testo richiesto dall'utente
     */
    public String testoRichiestoUtente;

    /**
     * L'esito del testo
     */
    public boolean esito;

    /**
     * La data creazione della modifica
     */
    public Date dataCreazione;

    /**
     * L'ora creazione della modifica
     */

    public Time oraCreazione;

    /**
     * L'utente che ha richiesto il testo
     */
    public Utente utente;

    /**
     * Il testo di riferimento
     */
    public Testo testoRiferimento;

    /**
     * Metodo che mi settal'utente che ha chiesto il testo
     * @param utente l'utente richiedente
     */

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Metodo che restituisce l'utente richiedente
     * @return un utente
     */

    public Utente getUtente() {
        return utente;
    }

    /**
     * Costruttore vuoto
     */
    public ModificaTesto()
    {

    }

    /**
     * Costruttore che istanzia una nuova modifica assegnando l'utente il testo di riferimento e il contenuto
     * @param utente l'utente
     * @param testoRiferimento il testo di riferimento dell'utente
     * @param contenuto il contenuto
     */
    public ModificaTesto(Utente utente,Testo testoRiferimento,String contenuto)
    {
        this.utente=utente;
        this.testoRiferimento=testoRiferimento;
        this.testoRichiestoUtente=contenuto;
    }
}
