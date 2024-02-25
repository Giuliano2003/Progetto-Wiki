package dao;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import model.*;

/**
 * The interface Wiki dao.
 */
public interface WikiDao {
    /**
     * Aggiunge utente al database.
     *
     * @param nome    Il nome dell'utente che si sta registrando
     * @param cognome Il cognome dell'utente che si sta registando
     * @param email   L'email dell'utente che si sta registrando
     * @return Un intero che indica se la registrazione è andata a buon fine, altrimenti mi indica
     * la causa dell'errore
     * @see Utente
     */
    public int addUtenteDB(String nome,String cognome,String email);

    /**
     * Metodo che aggiunge una frase con il rispettivo collegamento al database.
     *
     * @param contenuto     Il contenuto della frase
     * @param collegamento Il collegamento della frase
     * @see Frase
     */
    public void addFrasiDB(String contenuto,String collegamento);

    /**
     * Metodo che mi aggiunge solo il contenuto della frase al database.
     *
     * @param contenuto Il contenuto della frase
     * @see Frase
     */
    public void addFrasiWithoutLinkDB(String contenuto);

    /**
     * Metodo che aggiunge al database il titolo e il nome dell'autore.
     *
     * @param titolo     Il titolo dell'autore
     * @param nomeAutore Il nome dell'autore
     * @see Pagina
     */
    public void addPaginaDB(String titolo,String nomeAutore);

    /**
     * Metodo che riempe la lista delle parole della pagina associata.
     *
     * @param titolo              Il titolo della pagina.
     * @param parolePaginaCercata Le parole della pagina.
     * @see Pagina
     */
    public void cercaPaginaDB(String titolo, List<String> parolePaginaCercata);

    /**
     * Mi restituisce la pagina di destinazione di quella parola in quella determinata Pagina.
     * @param pagina La pagina di riferimento dove vado a prendere il collegamento
     * @param parola La parola parola da cercare
     * @return Il collegamento della parola associata in quella Pagina
     * @see Pagina
     */
    public String getCollegamentoDB(String pagina,String parola);

    /**
     * Metodo che aggiunge la modifica proposta dell'autore al DB.
     *
     * @param contenuto    Il contenuto proposto
     * @param nomeAutore   Il nome dell'autore che ha proposto la modifica
     * @param titoloPagina Il titolo della pagina
     * @see Autore
     * @see ModificaTesto
     */
    public void addModifichetestoDB(String contenuto,String nomeAutore,String titoloPagina);

    /**
     * Mi restituisce il primo contenuto non letto dall'utente(la traccia richiede che
     * che le notifiche vanno lette dalla più antica alla più recente quindi vado a prendere il primo
     * contenuto non letto in ordine di vecchiaia).
     *
     * @param nomeAutore Il nomeautore che deve leggere i contenuti
     * @return Una lista dei contenuti non letti dall utente db
     */
    public List<String> getContenutiNonLettiDallUtenteDB(String nomeAutore);

    /**
     * Metodo che mi restituisce i nomeUtenti nel db.
     *
     * @return Una lista dei nomi utenti nel database
     * @see Utente
     */
    public List<String> getNomeUtentiDB();

    /**
     * Metodo che mi restituisce le password degli utenti del db.
     *
     * @return Una lista delle password degli utenti
     * @see Utente
     */
    public List<String> getPasswordDB();

    /**
     * Metodo che mi restituisce le frasiLinkate nel database.
     *
     * @param titolo Il titolo della pagina di cui si vuole conoscere il titolo
     * @return Una lista di frasi linkate
     * @see Frase
     * @see Pagina
     */
    public List<String> getFrasiLinkateDB(String titolo);

    /**
     * Mi restituisce il contenuto della pagina nel db.
     *
     * @param titolo Il titolo della pagina cui si vuole sapere il contenuto
     * @return Una lista del contenuto della pagina (diviso per frasi cioè è una lista dove in ogni indice
     * ci sono le frasi del db)
     * @see Pagina
     */
    public List<String> getContenutoPaginaDB(String titolo);

    /**
     * Mi restituisce la datacreazione della modifica effettuata dall'utente.
     *
     * @param contenuto Il contenuto della modifica
     * @param titolo    Il titolo della pagina di riferimento della modifica
     * @return La data di creazione della modifica
     * @see Date
     * @see Pagina
     */
    public Date getDataCreazioneDB(String contenuto, String titolo);

    /**
     * Metodo che mi restituisce l'ora creazione della modifica effettuata dall'utente.
     *
     * @param contenuto Il contenuto della modifica
     * @param titolo    Il titolo della pagina di riferimento della modifica
     * @return L' ora creazione della modifica
     * @see Timestamp
     * @see Pagina
     */
    public Timestamp getOraCreazioneDB(String contenuto, String titolo);

    /**
     * Metodo che mi inizializza l'esito della modificata effettuata dall'utente a true.
     *
     * @param contenuto     Il contenuto che si sta accettando
     * @param dataCreazione La data creazione del contenuto
     * @param oraCreazione  L' ora creazione del contenuto
     * @see Date
     * @see Timestamp
     * @see Pagina
     * @see ModificaTesto
     */
    public void setEsito(String contenuto,Date dataCreazione,Timestamp oraCreazione);

    /**
     * Metodo che permette all'utente di rifiutare una modifica fatto da un'altro utente.
     *
     * @param contenuto     Il contenuto che si vuole rifiutare
     * @param dataCreazione La data creazione del contenuto
     * @param oraCreazione  L' ora creazione del contenuto
     * @see Date
     * @see Timestamp
     * @see Pagina
     * @see ModificaTesto
     */
    public void setEsitoFalseDB(String contenuto,Date dataCreazione,Timestamp oraCreazione);

    /**
     * Metodo che mi riempe la lista data come parametro con le frasi della pagina in input che hanno una pagina destinazione
     *  non vuota nel database
     * @param titolo       Il titolo della pagina da cui si vogliono prendere le frasi
     * @param frasiLinkate La lista frasi linkate che si riempirà di frasi con pagine destinazioni diverse da null
     * @see Pagina
     */
    public void setFrasiLinkateDB(String titolo,List<String> frasiLinkate);

    /**
     * Metodo che mi restituisce la pagina di destinazione associata a quella parola di quella determinata pagina.
     *
     * @param titolo Il titolo della pagina cui si vuole sapere la pagina di destinazione
     * @param parola La parola di cui si vuole sapere la pagina di destinazione
     * @return La pagina di destinazione
     * @see Pagina
     */
    public String getPaginaDestinazioneDB(String titolo,String parola);

    /**
     * Metodo che mi riempe le liste dati in input prendendo dal Database i titoli delle pagine che ha creato
     * l'autore dato in input,quindi questo metodo prende la database le pagine che ha creato l'utente
     * inserendo anche nelle altre 2 liste la data e l'ora della creazione,questo metodo serve per
     * creare una tabella di tutte le pagine che può consultare l'utente, cioè le sue create e quelle in
     * cui è stata proposta una modifica(dato da traccia), visualizzabile qui: {@link dao.WikiDao#addPagineModificateTabellaDB(List, List, List, List, String)}
     *
     *
     * @param titoli     I titoli delle pagine da aggiungere alla tabella
     * @param date       Le date di creazione delle pagine
     * @param ore        L'orario di creazione delle pagine
     * @param nomeAutore Il nomeautore da cui si deve andare a prendere le pagine create
     *
     * @see Pagina
     * @see Autore
     * @see Date
     * @see Timestamp
     */
    public void addPagineTabellaDB(List<String> titoli, List<Date> date, List<Time> ore, String nomeAutore);

    /**
     * Questo metodo mi riempe le liste delle pagine dove l'utente ha proposto una modifica.
     * Dato che la traccia chiedeva che l'utente potesse vedere anche la storia delle pagine
     * in cui ha proposto una modifica,questo metodo serve proprio a questo,grazie a quest'ultimo posso
     * risalire alle pagine in cui l'utente ha proposto una modifica e aggiungerla alla tabella
     *
     * @param titoli     I titoli delle pagine recuperate e che verranno aggiunte alla tabella
     * @param date       Le date di creazione delle pagine
     * @param ore        L' ora di creazione delle pagine
     * @param autori     Autori del testo dove l'utente ha proposto una modifica
     * @param nomeAutore Il nomeutente dove recuperare le pagine
     * @see Utente
     * @see Pagina
     * @see Date
     * @see Timestamp
     */
    public void addPagineModificateTabellaDB(List<String> titoli, List<Date> date, List<Time> ore,List<String> autori, String nomeAutore);

    /**
     * Questo metodo serve a ricostruire la storia della pagina presa in input( grazie al titolo)
     *
     * @param frasi              Questa list conterrà alla fine del metodo le frasi della pagina in quella
     *                           determinata ora e data
     * @param pagineDestinazione La pagina di destinazione che quella frase aveva in quella determinata
     *                           situazione
     * @param autoreModifica     L'autore della modifica
     * @param titoloPagina       Il titolo della pagina a cui ci stiamo riferendo
     * @param datasql            la data della modifica in formato sql
     * @param orasql             l'ora della modifica   in formato sql
     * @see Frase
     * @see Pagina
     * @see Date
     * @see Timestamp
     */
    public void setCronologiaTestiDB(List<String> frasi,List<String> pagineDestinazione, List<String>autoreModifica, String titoloPagina, List<java.sql.Date> datasql,List<Time> orasql);

    /**
     * Mi restituisce l'autore della pagina in ingresso.
     *
     * @param titolo della pagina di cui voglio sapere l'autore
     * @return L'autore della pagina
     * @see Autore
     * @see Pagina
     */
    public String getAutorePaginaDB(String titolo);

    /**
     * Metodo che mi riempie le due liste delle frasi della pagina e delle pagine di destinazioni rispettive.
     *
     * @param titolo             Il titolo della pagina di cui voglio il contenuto
     * @param frasi              Le frasi della pagina
     * @param pagineDestinazioni Le pagine destinazioni delle frasi associate
     * @see Frase
     * @see Pagina
      */
    public void setFrasiPaginaDB(String titolo,List<String> frasi,List<String> pagineDestinazioni);

    /**
     * Metodo che mi ritorna le pagine create dall'utente in ingresso.
     *
     * @param  nomeUtente il nomeutente di cui voglio sapere il numero di paginecreate
     * @return Le pagine create dall'utente
     * @see Utente
     * @see Pagina
     */
    public int getPagineCreateDB(String nomeUtente);

    /**
     * Mi ritorna il numero di modifiche dell'utente.
     *
     * @param nomeUtente Il nomeutente di cui voglio sapere le modifiche
     * @return Il numero di modifiche dell'utente
     * @see Utente
     * @see Pagina
     */
    public int getNumeroModificheDB(String nomeUtente);

    /**
     * Ritorna il Il rango dell'utente.
     *
     * @param nomeUtente il NomeUtente di cui voglio sapere il rango
     * @return Il rango dell'utente
     * @see Utente
     */
    public String getRangoDB(String nomeUtente);

    /**
     * Metodo che mi restituisce il numero di testi non letti dall'utente.Metodo che serve a dire se
     * l'utente ha testi da leggere
     *
     * @param nomeUtente Il nomeutente di cui voglio sapere il numero
     * @return Il numero di testi non letti
     * @see Utente
     */
    public int getNumeroTestiNonLetti(String nomeUtente);

    /**
     * Metodo che mi restituisce il contenuto della pagina che aveva in quella data e in quell'ora.Creato
     * appositamente per visualizzare i cambiamenti della pagina durante il tempo ad esempio:
     * La modifica che ha avuto prima e ha avuto dopo il cambiamento.
     *
     * @param titolopagina  Il titolopagina a cui mi riferisco
     * @param dataCreazione La data creazione del contenuto
     * @param oraCreazione  L' ora creazione del contenuto
     * @return Il contenuto della testo sottoforma di stringa
     * @see Pagina
     */
    public String getcontenutoPaginaModificataDB(String titolopagina, java.sql.Date dataCreazione,Time oraCreazione);

    /**
     * Metodo che aggiunge i temi della pagina al database.
     *
     * @param tema il tema da aggiungere
     * @see Pagina
     */
    public void addTemiDB(String tema);

    /**
     * Metodo utilizzato per far visualizzare all'utente le richieste effettuate .
     * Questo metodo riempi le liste in ingresso.Per poi farle visualizzare all'utente in una tabella
     *
     * @param utente         L'utente che vuole visualizzare le richieste
     * @param testiRichiesti Il titolo del testo che verrà aggiunto in una tabella
     * @param esito          L'esito del modifica
     * @param autorePagina   l'Autore di quella pagina
     * @param titolopagina   Il titolo della pagina di riferimento
     * @see Utente
     * @see Autore
     * @see Pagina
     */
    public void getRichiesteDB(String utente,List<String> testiRichiesti,List<String> esito,List<String> autorePagina,List<String> titolopagina);

    /**
     * Metodo utilizzato per visualizzare in fase di creazione se la pagina a cui stiamo linkando esiste.
     *
     * @param titolo il titolo della pagina che sto controllando
     * @return the boolean
     * @see Pagina
     */
    public boolean check_paginadestinazioneDB(String titolo);

    /**
     * Metodo utilizzato per cancellare una pagina dal database.
     *
     * @param titolo il titolo della pagina che sto cancellando
     * @see Pagina
     */
    public void deletePaginaDB(String titolo);

    /**
     * Metodo che restituisce l'ora creazione della pagina
     * @param titolo il titolo della pagina
     * @return un time che mi indica l'ora di creazione della pagina
     */
    public Time getOraCreazioneDB(String titolo);

    /**
     * Metodo che restituisce la data di creazione della pagina
     * @param titolo il titolo della pagina
     * @return una date che contiene la datacreazione della pagina
     */
    public Date getDataCreazioneDB(String titolo);


}
