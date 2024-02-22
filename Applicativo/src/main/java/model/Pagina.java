package model;

import java.util.ArrayList;

/**
 * Classe che mi conserva le informazioni sulle pagine create dagli utenti
 */
public class Pagina {
    /**
     * Il titolo della pagina
     */

    public String titolo;

    /**
     * L'autore della pagina
     */
    public Autore autorePagina;

    /**
     * Il testo della pagina
     */
    public Testo testo;

    /**
     * I temi della pagina
     */
    public ArrayList<String> tema;

    /**
     * Le frasi linkate della pagina
     */
    public ArrayList<Frase> frasiLinkate = new ArrayList<>();

    /**
     * Il costruttore della pagina
     * @param titolo il titolo della pagina
     * @param testo il testo della pagina
     * @param nomeAutore il nomeautore della pagina
     */
    public Pagina(String titolo,Testo testo,String nomeAutore)
    {
        this.titolo=titolo;
        this.testo=testo;
        Autore autore = new Autore(nomeAutore);
        this.autorePagina=autore;
    }

    /**
     * Il costruttore vuoto
     */
    public Pagina()
    {

    }

    /**
     * Un costruttore che prende come parametro il testo della pagina
     * @param testo
     */
    public Pagina(Testo testo)
    {
        this.testo=testo;
    }


    /**
     * Un costuttore che prende come parametro il titolo della pagina
     * @param titolo il titolo
     */
    public Pagina(String titolo)
    {
        this.titolo=titolo;
    }

    /**
     * Un costruttore che prende come parametro il titolo e il testo della pagina
     * @param titolo il titolo della pagina
     * @param testo il testo della pagina
     */
    public Pagina(String titolo,Testo testo)
    {
        this.titolo=titolo;
        this.testo=testo;
    }

    /**
     * mi aggiunge le frasiLinkate della pagina
     * @param parola la parola da linkare
     */

    public void addFrasiLinkate(String parola)
    {
        Frase frase = new Frase(parola);
        frasiLinkate.add(frase);
    }

    /**
     * Mi restituisce il testo della pagina
     * @return
     */

    public Testo getTesto() {
        return testo;
    }

    /**
     * Mi restituisce le frasi linkate della pagina
     * @return un array list che contiene le frasiLinkate
     */

    public ArrayList<Frase> getFrasiLinkate()
    {
        return frasiLinkate;
    }
}
