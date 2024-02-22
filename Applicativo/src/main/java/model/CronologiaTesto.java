package model;

import java.sql.Time;
import java.util.Date;

/**
 * Classe per conservare le modifiche che ha avuto il testo
 */

public class CronologiaTesto {
    /**
     * Il contenuto della frase
     */
    public String contenuto;
    /**
     * il contenuto della frase
     */
    public String paginaDestinazione;
    /**
     * Il contenuto della pagina di destinazione
     */

    /**
     * La data di inserimento della modifica
     */


    public Date dataInserimento;

    /**
     * L'ora di creazione della modifica
     */

    public Time oraInserimento;

    /**
     * Il testo di riferimento della modifica
     */
    public Testo testoRiferimento;

    /**
     * Il costruttore
     * @param testo il testo della modifica
     * @param pagina la pagina di riferimento
     */

    public CronologiaTesto(Testo testo,Pagina pagina)
    {
        this.testoRiferimento=testo;
        this.testoRiferimento.pagina=pagina;
    }

    /**
     * Il costruttore vuoto
     */
    public CronologiaTesto()
    {
            // costruttore vuoto
    }
}
