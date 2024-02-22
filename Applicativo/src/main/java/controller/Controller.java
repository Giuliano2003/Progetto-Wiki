package controller;

import implementazionePostgresDao.WikiImplementazionePostgresDao;
import model.*;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import static gui.CreaPagina.isEqual;

/**
 * La classe controller si occupa di ricevere e gestire richieste dall'utente
 * per soddisfare tali richieste interagisce con il model e interroga il DB.
 */
public class Controller {

    /**
     * Utente.
     * l'utente che userà l'applicazione.
     */
    public Utente utente;
    /**
     * Pagina.
     * Istanza che serve a mantenere i dati delle pagine fra una navigazione e l'altra
     */
    public Pagina pagina = new Pagina();

    /**
     * ModificaTesto.
     * Istanza che serve a mantenere i dati delle modifiche fra una navigazione e l'altra
     */

    public ModificaTesto modificaTesto = new ModificaTesto();

    public Testo testo = new Testo();
    /**
     * Cronologia testo.
     * Istanza che serve a mantenere i dati delle cronologie fra una navigazione e l'altra
     */
    public CronologiaTesto cronologiaTesto = new CronologiaTesto();

    /**
     * Gli autori delle pagine
     */
    Autore autore = new Autore();

    public Controller()
        {
            // crea un istanza del controller vuoto
        }

    /**
     * Set cronologia testo.
     *
     * @param cronologiaTesto La cronologia del testo
     */
    public void setCronologiaTesto(CronologiaTesto cronologiaTesto) {
            this.cronologiaTesto = cronologiaTesto;
        }
    /**
     * Set pagina.
     *
     * @param pagina La pagina
     */
    public void setPagina(Pagina pagina) {
            this.pagina = pagina;
        }

    /**
     * Get pagina.
     *
     * @return La pagina
     */
    public Pagina getPagina() {
            return pagina;
        }

    /**
     * Get titolo.
     *
     * @return Il titolo
     */
    public String getTitolo()
        {
            return pagina.titolo;
        }

    /**
     * Get utente.
     *
     * @return Un oggetto di tipo utente
     */
    public Utente getUtente() {
            return utente;
        }

    /**
     * Get nome utente.
     *
     * @return Il nome dell'utente
     */
    public String getNomeUtente()
        {
            return utente.username;
        }

    /**
     * Set utente.
     *
     * @param utente L'utente
     */
    public void setUtente(Utente utente) {
            this.utente = utente;
    }

    /**
     * Metodo che setta il testo con delle frasi in input
     * @param frasi le frasi in input
     */

    public void setTesto(ArrayList<String> frasi) {
        Testo testo1 = new Testo(frasi);
        this.testo=testo1;
    }

    /**
     * Set il nome dell'utente nel controller
     * @param nome il nome utente
     */
    public void setUtente(String nome)
    {
        Utente utente1 = new Utente(nome);
        this.utente=utente1;
    }

    /**
     * Restituisce il testo
     * @return Un testo
     */
    public Testo getTesto() {
        return testo;
    }

    /**
     * Setta il testo
     */
    public void setTesto() {
        Testo testo1 = new Testo(this.pagina);
        this.testo=testo1;
    }

    /**
     * Setta la cronologiaTesto del controller
     */

    public void setCronologiaTesto()
    {
        CronologiaTesto cronologiaTesto1 = new CronologiaTesto(this.testo,this.pagina);
        this.cronologiaTesto=cronologiaTesto1;
    }

    /**
     * Setta la pagina del controller con un titolo in ingresso
     * @param titolo il titolo in input
     */

    public void setPagina(String titolo)
    {
        Pagina pagina1 = new Pagina(titolo);
        this.pagina=pagina1;
    }

    /**
     * Setta la pagina con un titolo e un testo
     * @param titolo il titolo in input
     * @param testo il testo in input
     */
    public void setPagina(String titolo,Testo testo)
    {
        Pagina pagina1 = new Pagina(titolo,testo);
        this.pagina=pagina1;
    }

    /**
     * Set utente richiedente.
     *
     * @param utente L'utente
     */
    public void setUtenteRichiedente(Utente utente)
        {
            modificaTesto.setUtente(utente);
        }

    /**
     * Setta l'utente richiedente dato il nome
     * @param nome il nome dell'utente
     */

    public void setUtenteRichiedente(String nome)
    {
        Utente utente1 = new Utente(nome);
        ModificaTesto modificaTesto1 = new ModificaTesto();
        modificaTesto1.utente=utente1;
        this.modificaTesto=modificaTesto1;
    }

    /**
     * Ottieni L'username dell'utente che ha richiesto una modifica.
     *
     * @return Una stringa che contiene il nomeutente
     */
    public String getUsernameRichiedente(){
            return this.modificaTesto.utente.username;
        }


    /**
     * Set frasi linkate.
     *
     * @param frasiLinkate Le frasi linkate
     */
    public void setFrasiLinkate(ArrayList<String> frasiLinkate)
        {
            for (String s:frasiLinkate) {
                pagina.addFrasiLinkate(s);
            }
        }



    /**
     * Metodo che viene richiamato quando un utente clicca il tasto registrati,questo metodo
     * effettua l'inserimento dell'utente nel DB.
     * @param nome     Il nome dell'utente
     * @param password La password dell'utente
     * @param email    L' email dell'utente
     * @return un intero che mi indica com'è andata la registrazione
     * @throws SQLException the sql exception
     */
    public int addUtente(String nome, String password, String email) throws SQLException {
            int flag = 0; // aggiungo un flag per risolvere il fatto dell login cosi posso sapere se è andato male o no l'inserimento
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            flag=wikiImplementazionePostgresDao.addUtenteDB(nome,password,email);
            if(flag == 0) {
                Utente u = new Utente(nome, password, email);
                setUtente(u);
            }
            return flag;
    }

    /**
     * Metodo che prende in input le frasi della pagina,le frasiCliccabili cioè le frasi che l'utente
     * ha deciso di linkare e un hashmap che contiene le corrispondenze fra le frasiLinkate e le pagine
     * corrispondenti.
     *
     * @param frasi           Le frasi della pagina
     * @param frasiCliccabili Le frasi cliccabili che ha deciso di darel'utente
     * @param hashMap         Una hash map che contiene le corrispondenze fra le frasiLinkate
     *                        e le pagine.
     * @throws SQLException the sql exception
     */
    public void addFrasi(List<Frase> frasi, List<String> frasiCliccabili, Map<String,String> hashMap) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            for (Frase f: frasi) {
                ArrayList<String> paroleFrasi = f.getParole();
                String risultato = String.join(" ",paroleFrasi);
                String collegamento = restituisciCollegamento(risultato,frasiCliccabili);
                if(collegamento != null) {
                    String link=hashMap.get(collegamento);
                    wikiImplementazionePostgresDao.addFrasiDB(risultato, link);
                }
                else{
                    wikiImplementazionePostgresDao.addFrasiWithoutLinkDB(risultato);
                }
            }
        }

    /**
     * Metodo che aggiunge il titolo di una pagina e il nome dell'autore al database.
     *
     * @param titolo     Il titolo della pagina scritta dall'utente
     * @param nomeAutore Il nome autore dell'autore
     * @throws SQLException the sql exception
     *
     */
    public void addPagina(String titolo,String nomeAutore) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.addPaginaDB(titolo,nomeAutore);
            Pagina pagina1 = new Pagina(titolo);
            autore.pagineCreate.add(pagina1);

        }


    /**
     * Metodo che prende in input un titolo che sta ad indicare il titolo della pagina di cui voglio
     * le frasi.
     * e un List String che verrà riempita delle frasi della pagina che ha il titolo in input
     *
     * @param titolo              Il titolo della pagina di cui voglio le frasi
     * @param frasiPaginaCercata  le frasi della pagina che ho cercato
     * @throws SQLException the sql exception
     */
    public void cercaPagina(String titolo,List<String> frasiPaginaCercata) throws SQLException {
            //notare che questo metodo mi riempe l'array list delle parole però ricordiamo che non ci sono ancora i collegamenti associati
            //li cerchermo con la prossima funzione
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.cercaPaginaDB(titolo,frasiPaginaCercata);
            Testo testo = new Testo((ArrayList<String>) frasiPaginaCercata);
            Pagina p = new Pagina(titolo,testo);
            setPagina(p);
        }

    /**
     * Metodo che mi restituisce il collegamento della frase data in input in corrispondenza della pagina
     * data sempre in input.
     *
     * @param titolo Il titolo della pagina di cui voglio sapere il link
     * @param frase Il contenuto della frase
     * @return Una stringa che contiene il collegamento della frase.Se il collegamento non c'è
     * restituisce null
     * @throws SQLException the sql exception
     */
    public String ottieniCollegamentoParola(String titolo,String frase) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            String link = wikiImplementazionePostgresDao.getCollegamentoDB(titolo,frase);
            if(link != null) {
                pagina.addFrasiLinkate(frase);
            }
            return link;
        }

    /**
     * Metodo che permette di aggiungere una modifica proposta dall'utente nel DB.
     *
     * @param contenuto    Il contenuto della modifica
     * @param nomeAutore   Il nome  dell'autore che ha proposto la modifica
     * @param titoloPagina Il titolo della pagina
     * @throws SQLException the sql exception
     */
    public void addModifichetesto(String contenuto,String nomeAutore,String titoloPagina) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.addModifichetestoDB(contenuto,nomeAutore,titoloPagina);
            Utente utente1 = new Utente(nomeAutore);
            Pagina pagina1 = new Pagina(titoloPagina);
            Testo testo1 = new Testo(pagina1);
            ModificaTesto modificaTesto1 = new ModificaTesto(utente1,testo1,contenuto);
            utente.modificheTesto.add(modificaTesto1);
        }

    /**
     * Metodo che mi restiuisce un contenuto non letto dall'utente
     *
     * @param nomeAutore Il nome dell'autore di cui si vogliono sapere i contenenuti
     * @return Il contenuti non letto dall'utente(se c'è se sono di più viene dato quello più antico)
     * @throws SQLException the sql exception
     */

    //La traccia chiedeva espressamente di avere i contenuti dal più antico al più recente quindi facciamo
    //questo metodo per far visualizzare i contenuti all'utente uno alla volta quando clicca il bottone.
    public List<String> getContenutiNonLetti(String nomeAutore) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getContenutiNonLettiDallUtenteDB(nomeAutore);
        }

    /**
     * Metodo che mi restituisce le frasi linkate di una pagina dato un titolo.
     *
     * @param titolo Il titolo della pagina di cui si vogliono prendere le frasiLinkate
     * @return Una list string che contiene le frasi linkate
     * @throws SQLException the sql exception
     */
    public List<String> getFrasiLinkate(String titolo) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getFrasiLinkateDB(titolo);
        }

    /**
     * Metodo che si occupa di gestire il login dell'utente.
     *
     * @param nomeUtente Il nome utente che si vuole regitrare
     * @param password   La password dell'utente inserita
     * @return Un intero che indica che tipo di collissione si è avuta nel login
     * @throws SQLException the sql exception
     */
    public int loginUtente(String nomeUtente,String password) throws SQLException {
            int flagu=0;
            int flagp=0;
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            ArrayList<String> nomiUtenti = wikiImplementazionePostgresDao.getNomeUtentiDB();
            ArrayList<String> passwordUtenti = wikiImplementazionePostgresDao.getPasswordDB();
            for (String user:nomiUtenti) {
                if(user.equals(nomeUtente)){
                    flagu=1;
                    break;
                }
            }
            for (String pass:passwordUtenti) {
                if(pass.equals(password)){
                    flagp=1;
                    break;
                }
            }
            if(flagu == 1 && flagp == 1){
                return 1;
            }
            return 0;
        }


    /**
     * Metodo che prende in input un titolo e riempe la List string frasiLinkate
     * delle frasi con pagina destinazione non null che sono presenti in quella determinata pagina.
     *
     * @param titolo       Il titolo di cui si vogliono le frasiLinkate
     * @param frasiLinkate Le frasi linkate del titolo in input
     * @throws SQLException the sql exception
     */
    public void setFrasiLinkate(String titolo,List<String> frasiLinkate) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.setFrasiLinkateDB(titolo,frasiLinkate);
        }

    /**
     * metodo che viene usato perchè mi indica se la frase è presente nell'array list frasi che contiene
     * le frasilinkate della pagina
     * se non è presente mi restituisce null, se la frase è presente restituisce quest'ultima
     *
     * @param frase il contenuto della frase che si vuole confrontare
     * @param frasiLinkatePagina  Una List String di frasiLinkate della pagina che si vuole confrontare con una frase
     * @return the string

     */
    public static String restituisciCollegamento(String frase, List<String> frasiLinkatePagina) {
            // Pulisci la parola da spazi aggiuntivi e caratteri speciali
            frase = frase.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            for (String s : frasiLinkatePagina) {
                String fraseArrayList = s.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (fraseArrayList.equals(frase)) {
                    return s;
                }
            }
            return null;
        }

    /**
     * Mi restituisce la pagina destinazione della parola data in input.Se la pagina di destinazione
     * non esiste mi restituisce NULL
     *
     * @param titolo Il titolo in cui è presente la frase
     * @param frase il contenuto della frase
     * @return Una stringa che contiene la  pagina destinazione
     * @throws SQLException the sql exception
     */
    public String getPaginaDestinazione(String titolo,String frase) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getPaginaDestinazioneDB(titolo,frase);
        }

    /**
     * Un metodo che mi restituisce il contenuto della pagina.
     *
     * @param titolo Il titolo della pagina di cui si vogliono sapere i contenuti
     * @return Una List<String> contenente il contenuto della pagina,sottoforma di frasi
     * @throws SQLException the sql exception
     */

    //quindi ad ogni indice della lista c'è una nuova frase
    public List<String> getContenutoPagina(String titolo) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getContenutoPaginaDB(titolo);
        }

    /**
     * Metodo che restituisce la data creazione della modifica effettuata dall'utente.
     *
     * @param contenuto Il contenuto di cui si vuole sapere la modifica
     * @param titolo    Il titolo della pagina riferente la modifica
     * @return La datacreazione della modifica
     * @throws SQLException the sql exception
     */
    public Date getDataCreazione(String contenuto, String titolo) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            Date dataCreazione = wikiImplementazionePostgresDao.getDataCreazioneDB(contenuto,titolo);
            if(dataCreazione == null)
            {
                JOptionPane.showMessageDialog(null,"DataCreazione non trovata");
            }
            return dataCreazione;
        }

    /**
     * Metodo che restituisce l'oracreazione della modifica effettuata da un utente.
     *
     * @param contenuto  Il contenuto di cui si vuole sapere la modifica
     * @param titolo    Il titolo della pagina a cui si riferisce la modifica
     * @return un TimeStamp che indica L' ora creazione della modifica
     * @throws SQLException the sql exception
     */
    public Timestamp getOraCreazione(String contenuto, String titolo) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            Timestamp oraCreazione = wikiImplementazionePostgresDao.getOraCreazioneDB(contenuto,titolo);
            if(oraCreazione == null)
            {
                JOptionPane.showMessageDialog(null,"DataCreazione non trovata");
            }
            return oraCreazione;
        }

    /**
     * Metodo che permette di aggiornare l'esito della modifica a true.
     *
     * @param contenuto     Il contenuto che si vuole settare a true
      * @param dataCreazione La data creazione  del contenuto
     * @param oraCreazione  L' ora creazione del contenuto
     * @throws SQLException the sql exception
     */

    //Nota Bene: Prendiamo la data e l'ora per identificare il contenuto nel database
    public void setEsito(String contenuto, Date dataCreazione, Timestamp oraCreazione) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.setEsito(contenuto,dataCreazione,oraCreazione);
        }

    /**
     * Metodo che permette di rifiutare una modifica proposta dall'utente
     *
     * @param contenuto     Il contenuto che si vuole settare a false
     * @param dataCreazione La data creazione del contenuto
     * @param oraCreazione  L' ora creazione del contenuto
     * @throws SQLException the sql exception
     */

    //Nota Bene: Prendiamo la data e l'ora per identificare il contenuto nel database
    public void setEsitoFalse(String contenuto, Date dataCreazione, Timestamp oraCreazione) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.setEsitoFalseDB(contenuto,dataCreazione,oraCreazione);
        }


    /**
     * Metodo che mi permette di aggiungere contenuti alla tabella StoriaPagine tabella che
     * deve visualizzare le pagine create dall'utente.I dati vengono riempiti con questo metodo
     *
     * @param titoli I titoli delle pagine create dall'utente
     * @param date   La data di creazione delle pagine create dall'utente
     * @param ore    Le ore di creazione delle pagine create dall'utente
     * @throws SQLException the sql exception
     */
    public void addPagineTabella(List<String> titoli, List<Date> date, List<Time> ore) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.addPagineTabellaDB(titoli,date,ore,this.getNomeUtente());
        }

    /**
     * Metodo che mi permette di aggiungere ulteriori contenuti alla tabella StoriaPagine tabella che
     * deve visualizzare anche le pagine in cui l'utente
     * ha proposto una modifica .I dati vengono riempiti con questo metodo
     *
     * @param titoli I titoli delle pagine dove l'utente ha richiesto una modifica
     * @param date   Le date di creazione delle pagine
     * @param ore    Le ore di creazione delle pagine
     * @param autori Gli autori di quelle determinate pagine
     * @throws SQLException the sql exception
     */

    //nota bene: La traccia richiedeva anche di visualizzare le pagine in cui si è proposta una modifica
    public void addPagineModificateTabella(List<String> titoli, List<Date> date, List<Time> ore, List<String> autori) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.addPagineModificateTabellaDB(titoli,date,ore,autori,this.getNomeUtente());
        }

    /**
     * Metodo che riempe le ListString in ingresso dei testiPassati di una determinata pagina.
     * Questo metodo mi permette di risalire alla storia della pagina visualizzabile in VisualizzaStoria
     *
     *
     * @param frasi              Le frasi di quella pagina passata
     * @param pagineDestinazione Le pagine destinazione che aveva quella pagina
     * @param autoreModifica     L' autore della modifica di quella pagina.Serve per dire nella storia testo che autore ha effettuato la modifica
     * @param datasql            Data in formato sql
     * @param orasql             Ora in formato sql
     * @throws SQLException the sql exception
     */
    public void setCronologiaTesti(List<String> frasi, List<String> pagineDestinazione, List<String> autoreModifica, List<java.sql.Date> datasql, ArrayList<Time> orasql) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.setCronologiaTestiDB(frasi,pagineDestinazione,autoreModifica,this.getTitolo(),datasql,orasql);
        }

    /**
     * Metodo che mi ritorna l'autore della pagina con titolo dato in input.
     *
     * @param titolo Il titolo della pagina di cui si vuole sapere l'autore
     * @return Una stringa contenente l'autore della pagina
     * @throws SQLException the sql exception
    */

    public String getAutorePagina(String titolo) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getAutorePaginaDB(titolo);
        }

    /**
     * Metodo che riempe le List<String> in ingresso delle frasi della pagina e delle loro
     * rispettive pagine di destinazione.
     *
     * @param titolo             Il titolo della pagina
     * @param frasi              Le frasi della pagina
     * @param paginedestinazione Le paginedestinazione della pagina
     * @throws SQLException the sql exception

    */
    public void setFrasiPagina(String titolo,List<String> frasi,List<String> paginedestinazione) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.setFrasiPaginaDB(titolo,frasi,paginedestinazione);
        }

    /**
     * Metodo che restituisce il numero di pagine create.
     *
     * @param nomeUtente Il nome utente di cui si vogliono sapere le paginecreate
     * @return un intero che indica il numero di pagine create
     * @throws SQLException the sql exception

    */
    public int getPagineCreate(String nomeUtente) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getPagineCreateDB(nomeUtente);
        }

    /**
     * Metodo che restituisce il numero di modifiche di un utente.
     *
     * @param nomeUtente Il nome utente di cui si vogliono sapere il numero di modifiche
     * @return un intero che indica il numero delle modifiche
     * @throws SQLException the sql exception
     */
    public int getNumeroModifiche(String nomeUtente) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getNumeroModificheDB(nomeUtente);
        }

    /**
     * Metodo che restituisce il rango dell'utente.
     *
     * @param nomeUtente Il nome utente di cui si vuole sapere il rango
     * @return Una stringa contenente il rango dell'utente rango
     * @throws SQLException the sql exception
     */
    public String getRango(String nomeUtente) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getRangoDB(nomeUtente);
        }

    /**
     * Metodo che ritorna il numero di testi non letti.
     *
     * @param nomeUtente Il nome utente di cui si vuole sapere il numero di testi non letti
     * @return un intero che indica il numero di testi non letti
     * @throws SQLException the sql exception
     */
    public int getTestiNonLetti(String nomeUtente) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getNumeroTestiNonLetti(nomeUtente);
        }

    /**
     * Gets contenuto pagina.
     *
     * @param titolopagina  the titolopagina
     * @param datacreazione the datacreazione
     * @param oraCreazione  the ora creazione
     * @return the contenuto pagina
     * @throws SQLException the sql exception
     * mi restituisce il contenuto della pagina dato il titolo,la data e l'ora
     */
    public String getContenutoPagina(String titolopagina, java.sql.Date datacreazione, Time oraCreazione) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            return wikiImplementazionePostgresDao.getcontenutoPaginaModificataDB(titolopagina,datacreazione,oraCreazione);
        }

    /**
     * Aggiunte i temi della pagina al database.
     *
     * @param tema Il tema da aggiungere
     * @throws SQLException the sql exception
     */

    public void addTemi(String tema) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.addTemiDB(tema);
        }

    /**
     * Metodo che permette di aggiungere alla tabella delle statistiche le richieste che ha fatto l'utente
     * di modifica di altre pagine.
     *
     * @param nomeutente     Il nomeutente
     * @param testiRichiesti Il testi richiesto dall'utente
     * @param esito          L' esito della modifica
     * @param autorePagina   L' autore della pagina
     * @param titolopagina   Il titolo della pagina a cui si riferisce la modifica
     * @throws SQLException the sql exception
     */
    public void getRichieste(String nomeutente,List<String> testiRichiesti,List<String> esito,List<String> autorePagina,List<String> titolopagina) throws SQLException {
            WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
            wikiImplementazionePostgresDao.getRichiesteDB(nomeutente,testiRichiesti,esito,autorePagina,titolopagina);
    }


    /**
     * Metodo che controlla se la pagina data in input è presente fra le pagine nel database.
     * Se è presente allora restituisce true,altrimenti false
     *
     * @param titolo     the titolo
     * @throws SQLException the sql exception
     */

    //Nota bene : Questo metodo serve per controllare se la paginadestinazione in fase di creazione
    //della pagina è ben nota nel database
    public boolean checkPaginaDestinazione(String titolo) throws SQLException {
        WikiImplementazionePostgresDao wikiImplementazionePostgresDao = new WikiImplementazionePostgresDao();
        return wikiImplementazionePostgresDao.check_paginadestinazioneDB(titolo);
    }

    public void logOutUtente()
    {
        this.utente=null;
    }


    /**
     * Metodo che prende in input il titolo di una pagina e permette di cancellare una pagina dal database
     *
     * @param titolo     Il titolo della pagina che si vuole cancellare
     * @throws SQLException the sql exception
     */
    public void deletePagina(String titolo) throws SQLException {
        WikiImplementazionePostgresDao wikiImplementazionePostgresDao =new WikiImplementazionePostgresDao();
        wikiImplementazionePostgresDao.deletePaginaDB(titolo);
    }

    /**
     * Metodo che prende in input un testo e crea le frasi a partire dal testo in ingresso
     * @param testoInput Il testo che prende in input e creo le frasi.
     */

    public void makeFrasi(String testoInput)
    {
        testo.frasiTesto.clear();
        String[] parole = testoInput.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");
        Frase frase = new Frase(); // inizio con una frase vuota
        ArrayList<String> frasiSottolineate = new ArrayList<>();
        for (Frase f: pagina.getFrasiLinkate()) {
            ArrayList<String> res = f.getParole();
            for (String s: res) {
                frasiSottolineate.add(s);
            }
        }
        for (int i = 0; i < parole.length; i++) {
            if (frasiSottolineate.contains(parole[i])) {
                testo.frasiTesto.add(frase);
                frase = new Frase();
                frase.aggiungiParola(parole[i]);
                // Aggiungi tutte le parole successive fino al prossimo elemento frasiSottolineate
                while (i + 1 < parole.length && frasiSottolineate.contains(parole[i + 1])) {
                    frase.aggiungiParola(parole[i + 1]);
                    i++;
                }
                testo.frasiTesto.add(frase);
                frase = new Frase();
            } else if (!isEqual(parole[i], frasiSottolineate)) {
                frase.aggiungiParola(parole[i]);
                // Se la parola contiene un punto, termina la frase corrente
                if (parole[i].contains(".")) {
                    testo.frasiTesto.add(frase);
                    frase = new Frase();
                }
            }
        }
            if (!frase.getParole().isEmpty()) {
                testo.frasiTesto.add(frase);
            }
            Iterator<Frase> iterator = testo.frasiTesto.iterator();
            while (iterator.hasNext()) {
                Frase f = iterator.next();
                if (f.isEmpty(f)) {
                    iterator.remove();
                }
            }

    }

    /**
     * Metodo per generare il testo html
     * @param frasiLinkate le frasi linkate che serviranno al metodo per individuare quali frasi sottolineare
     *                     nel testo html
     * @param stringBuilder Stringa che mi contiene il testo formato
     * @return una StringBuilder che contiene il testo generato
     */
    public StringBuilder generateHTML(ArrayList<String> frasiLinkate,StringBuilder stringBuilder)
    {
        for (Frase f : this.testo.getFrasiTesto()) {
            ArrayList<String> parole = f.getParole();
            String res = String.join(" ", parole);
            if (isEqual(res,frasiLinkate)) {
                stringBuilder.append("<a href=\"").append("\">").append("<span style='font-family: Arial, sans-serif; color: #0066CC; text-decoration: none;'>").append(res).append("</span>").append("</a>").append(" ");
            } else {
                stringBuilder.append("<span style='font-family: Arial, sans-serif;'>").append(res).append("</span>").append(" ");
            }
            stringBuilder.append(" "); // Aggiungi uno spazio tra le parole
        }
        return stringBuilder;
    }

    /**
     * Metodo per generare il testo html
     * @param frasiLinkate le frasi linkate che serviranno al metodo per individuare quali frasi sottolineare
     *                     nel testo html
     * @param stringBuilder Stringa che mi contiene il testo formato
     * @return un Array list di frase che contiene le frasi create dal metodo
     */
    public ArrayList<Frase> generateHTMLwithLINK(ArrayList<String> frasiLinkate,StringBuilder stringBuilder)
    {
        ArrayList<Frase> frasi = new ArrayList<>();
        for (Frase f: this.testo.frasiTesto) {
            //questo frasiTestoNuovo è un vettore definito a livello di classe che ci servirà per
            //quando salveremo il testo le frasi che stanno qui andranno nel db.
            frasi.add(f);
            ArrayList<String> parole = f.getParole();
            String risultato = String.join(" ",parole);
            risultato=risultato.trim();
            if(sonoUguali(risultato,frasiLinkate))
            {
                stringBuilder.append("<a href=\"\">").append(risultato).append("</a> ");
            }
            else
            {
                stringBuilder.append(risultato).append(" ");
            }
        }
        return frasi;
    }

    /**
     * Metodo per verificare se una frase è contenuta nell'array dato in input
     * @param frase la frase che si vuole verificare
     * @param frasi l'array list in input
     * @return un boolean che indica com'è andato il processo di confronto
     */
    public boolean sonoUguali(String frase,ArrayList<String> frasi)
    {
        for (String s:frasi) {
            if(s.equals(frase))
            {
                return true;
            }
        }
        return false;
    }
}
