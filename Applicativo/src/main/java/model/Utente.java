package model;

import java.util.ArrayList;

/**
 * Classe che serve per memorizzare gli utenti
 */

public class Utente {
    /**
     * L'username dell'utente
     */
    public String username;

    /**
     * L'email dell'utente
     */
    public String email;

    /**
     * la password dell'utente
     */
    private String password;

    /**
     * il numero di modifiche dell'utente
     */
    public int numeroModifiche;

    /**
     * Costruttore che mi istanzia un utente settando username,password ed email
     * @param nomeUtente il nomeutente
     * @param password la password
     * @param email l'email
     */
    public Utente(String nomeUtente, String password, String email)
    {
        this.username=nomeUtente;
        this.password=password;
        this.email=email;
    }

    /**
     * Costruttore che mi istanzia un utente e li setta il nome
     * @param nome
     */
    public Utente(String nome)
    {
        this.username=nome;
    }

    /**
     * Costruttore che istanzia un utente vuoto
     */

    public Utente() {

    }

    /**
     * Metodo che fa ritornare l'username dell'utente
     * @return una string che contiene l'username
     */

    public String getUsername() {
        return username;
    }

    /**
     * Metodo che fa ritornare la password dell'utente
     * @return una string che contiene la password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Metodo che fa ritornare la mail dell utente
     * @return una string che contiene la mail
     */

    public String getEmail() {
        return email;
    }

    /**
     * Le modifiche dell'autore
     */

    public ArrayList<ModificaTesto> modificheTesto = new ArrayList<>();

    /**
     * Un metodo che mi ritorna il nome dell'utente
     * @return una stringa che contiene il nomeutente
     */

    public String getNomeUtente() {
        return username;
    }
}
