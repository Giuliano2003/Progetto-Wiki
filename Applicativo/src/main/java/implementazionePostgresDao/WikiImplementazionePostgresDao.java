package implementazionePostgresDao;

import dao.WikiDao;
import database.ConnessioneDatabase;
import model.Pagina;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *   Classe che implementa i metodi per accedere al DB
 */
public class WikiImplementazionePostgresDao implements WikiDao {
    private Connection connection;

    /**
     * Instantiates a new Wiki implementazione postgres dao.
     *
     * @throws SQLException the sql exception
     */
    public WikiImplementazionePostgresDao() throws SQLException {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore Connessione");
        }
    }

    /**
     * Effettua l'inserimento dell'utente nel DB.
     *
     * @param nome Il nome dell'utente
     * @param password la password dell'utente
     * @param email l'email dell'utente
     * @return un flag che indica che l'operazione non è andata a buon fine
     */

    @Override
    public int addUtenteDB(String nome, String password, String email) {
        int flag = 0;
        String filePath = "popolaDB1.txt";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into utente (NomeUtente,password,email) values (?,?,?)");
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String testo = preparedStatement.toString();
            bufferedWriter.write(testo);
            preparedStatement.executeUpdate();
            bufferedWriter.close();
        } catch (SQLException e) {
            flag = 1;
            JOptionPane.showMessageDialog(null, "Errore nell'inserimento dell'utente");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Errore apertura file !");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * Effettua l'inserimento delle frasi nel DB.
     *
     * @param contenuto Il contenuto della frase
     * @param collegamento la pagina di destinazione della frase
     */

    @Override
    public void addFrasiDB(String contenuto, String collegamento) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into Frase (contenuto,paginadestinazione) values (?,?)");
            preparedStatement.setString(1, contenuto);
            preparedStatement.setString(2, collegamento);
            preparedStatement.executeUpdate();
            String testo = preparedStatement.toString();
            System.out.println(""+testo);
            // Altre operazioni o logica di business qui
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * Effettua l'inserimento delle frasi senza link nel db.
     *
     * @param contenuto Il contenuto della frase
     */
    @Override
    public void addFrasiWithoutLinkDB(String contenuto) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into Frase (contenuto) values (?)");
            preparedStatement.setString(1, contenuto);
            preparedStatement.executeUpdate();
            String testo = preparedStatement.toString();
            System.out.println(""+testo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con inserimento frasi");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Effettua l'inserimento della pagina nel DB.
     *
     * @param titolo Il titolo della pagina da inserire nel db
     * @param nomeAutore Il nome dell'autore da inserire nel db
     */

    @Override
    public void addPaginaDB(String titolo, String nomeAutore) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into pagina (titolo,autore) values (?,?)");
            preparedStatement.setString(1, titolo);
            preparedStatement.setString(2, nomeAutore);
            String testo = preparedStatement.toString();
            System.out.println(""+testo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore inserimento Pagina");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Riempe la lista frasiPaginaCercata delle frasi della pagina presa in input.
     *
     * @param titolo Il titolo in ingresso
     * @param frasiPaginaCercata lista di frasi presa in ingresso e che viene riempita di frasi della pagina
     */

    @Override
    public void cercaPaginaDB(String titolo, List<String> frasiPaginaCercata) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select f.contenuto from pagina p,pagina_frase pf,frase f where p.titolo=pf.titolopagina and pf.idfrase = f.idfrase and p.titolo = ? order by f.idfrase");
            preparedStatement.setString(1, titolo);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                frasiPaginaCercata.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Si è verificato un errore con la ricerca della pagina");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Restituisce la paginadestinazione di una parola data
     * in input di una certa pagina data sempre in input.
     * @param titolo Il titolo della pagina
     * @param parola parola di cui si vuole sapere il collegamento
     * @return una Stringa che indica la pagina di destinazione
     */
    @Override
    public String getCollegamentoDB(String titolo, String parola) {
        PreparedStatement preparedStatement = null;
        try {
            //ti spiego il ragionamento di quell'order by, da questa select potrebbero uscire due o più collegamenti
            //quello che si fa è che se sono presenti 2 o più collegamenti vuol dire che il collegamento c'è
            // e non è null quindi andremo a prendere il collegamento,questo potrebbe succedere ad esempio nel
            //database quando facciamo la query di darci due tuple una con due parole uguali però una ha una pagina vuota
            //quello che si fa è prendere il link e metterlo
            preparedStatement = connection.prepareStatement("select f.paginadestinazione from pagina p , pagina_frase pf, frase f where p.titolo=pf.titolopagina and pf.idfrase = f.idfrase and p.titolo = ? and f.contenuto = ? LIMIT 1;");
            preparedStatement.setString(1, titolo);
            preparedStatement.setString(2, parola);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con ottenimento dei collegamenti");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * Aggiunge al database la modifica proposta dall'utente.
     *
     * @param contenuto il contenuto della modifica
     * @param nomeAutore L'autore della modifica
     * @param titoloPagina Il titolo della pagina a cui si riferisce la modifica
     */

    @Override
    public void addModifichetestoDB(String contenuto, String nomeAutore, String titoloPagina) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into modificatesto (testorichiesto,autorerichiedente,paginariferimento) values (?,?,?)");
            preparedStatement.setString(1, contenuto);
            preparedStatement.setString(2, nomeAutore);
            preparedStatement.setString(3, titoloPagina);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con l'invio del testo modificato");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Restituisce una lista dei contenuti non letti dal database, me ne faccio restituire uno alla volta
     * perchè l'utente può visualizzarlo uno alla volta e inoltre dalla più antica alla più recente.
     *
     * @param nomeAutore Il nome dell'autore di cui voglio vedere la modifica
     * @return una lista string che contiene nei primi 3 indici il testo richiesto dall'utente,la pagina
     * di riferimento a cui si riferisce la modifica e l'autore della modifica
     */

    public List<String> getContenutiNonLettiDallUtenteDB(String nomeAutore) {
        PreparedStatement preparedStatement = null;
        List<String> risultati = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select testorichiesto,paginariferimento,autorerichiedente from modificatesto where esito is null and autorepagina = ? LIMIT 1");
            preparedStatement.setString(1, nomeAutore);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                risultati.add(resultSet.getString(1));
                risultati.add(resultSet.getString(2));
                risultati.add(resultSet.getString(3));
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero dei testi");
            e1.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return risultati;
    }


    /**
     * Restituisce i nomi utenti presenti nel database.
     *
     * @return un arraylist di string che contiene i nomiUtenti presenti nel database
     */
    public ArrayList<String> getNomeUtentiDB() {
        ArrayList<String> nomeUtenti = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            int i = 1; // Inizia l'indice da 1
            preparedStatement = connection.prepareStatement("SELECT nomeUtente FROM Utente");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                nomeUtenti.add(resultSet.getString(i));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore Nel Sistema");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return nomeUtenti;
    }

    /**
     * Restituisce le password degli utenti nel database.
     *
     * @return un arraylist di string che contiene le password degli utenti
     */

    public ArrayList<String> getPasswordDB() {
        ArrayList<String> password = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            int i = 1; // Inizia l'indice da 1
            preparedStatement = connection.prepareStatement("SELECT password FROM Utente");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                password.add(resultSet.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return password;
    }

    /**
     * Restituisce le frasi con collegamento diverso da null, cioè le frasi che sono collegate ad una pagina.
     *
     * @param titolo Il titolo della pagina
     * @return un ArrayList di string che contiene il contenuto delle frasi che sono collegate ad una pagina
     */

    @Override
    public ArrayList<String> getFrasiLinkateDB(String titolo) {
        ArrayList<String> frasiLinkate = new ArrayList<>();
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = connection.prepareStatement("select f.contenuto from  pagina p , pagina_frase pf, frase f where p.titolo=pf.titolopagina and pf.idfrase = f.idfrase and p.titolo = ? and f.paginadestinazione is not null order by f.idfrase");
            preparedStatement.setString(1, titolo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                frasiLinkate.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore nell ottenimento delle frasi linkate");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return frasiLinkate;
    }

    /**
     * Restituisce il contenuto della pagina .
     *
     * @param titolo Il titolo della pagina
     * @return un ArrayList di string tale che contiene le frasi della pagina in input
     */

    @Override
    public ArrayList<String> getContenutoPaginaDB(String titolo) {
        ArrayList<String> contenuto = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select f.contenuto from  pagina p , pagina_frase pf, frase f where p.titolo=pf.titolopagina and pf.idfrase = f.idfrase and p.titolo = ? order by f.idfrase");
            preparedStatement.setString(1, titolo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contenuto.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore nel ottenere il testo principale");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return contenuto;
    }

    /**
     * Ritorna la datacreazione della modifica effettuata da un autore.
     *
     * @param contenuto Il contenuto di cui si vuole sapere la datacreazione
     * @param titolo il titolo della pagina a cui si riferisce la modifica
     * @return una Date che indica la datacreazione della modifica
     */
    @Override
    public Date getDataCreazioneDB(String contenuto, String titolo) {
        Date dataCreazione = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select dataCreazione from modificatesto where testorichiesto = ? and paginariferimento = ?");
            preparedStatement.setString(1, contenuto);
            preparedStatement.setString(2, titolo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getDate(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il get di dataCreazione");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataCreazione;
    }

    /**
     * Ritorna l'oracreazione della modifica effettuata da un autore.
     *
     * @param contenuto Il contenuto di cui si vuole sapere l'oracreazione
     * @param titolo il titolo della pagina a cui si riferisce la modifica
     * @return un TimeStamp che indica l' oracreazione della modifica
     */

    @Override
    public Timestamp getOraCreazioneDB(String contenuto, String titolo) {
        Timestamp oraCreazione = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select oraCreazione from modificatesto where testorichiesto = ? and paginariferimento = ?");
            preparedStatement.setString(1, contenuto);
            preparedStatement.setString(2, titolo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getTimestamp(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il get di dataCreazione");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return oraCreazione;
    }

    /**
     * Fa un update nel database che permette all'utente di accettare un testo.
     *
     * @param contenuto Il contenuto che si vuole accettare
     * @param dataCreazione la datacreazione per identificare il contenuto da accettare
     * @param oraCreazione l'ora creazione per identificare il contenuto da accettare
     */

    @Override
    public void setEsito(String contenuto, Date dataCreazione, Timestamp oraCreazione) {
        PreparedStatement preparedStatement = null;
        try {
            java.util.Date utilDate = dataCreazione; // Supponendo che dataCreazione sia di tipo java.util.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            preparedStatement = connection.prepareStatement("update modificatesto set esito = true where testorichiesto = ? and dataCreazione = ? and oraCreazione = ?");
            preparedStatement.setString(1, contenuto);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setTimestamp(3, oraCreazione);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il settaggio dell esito");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Riempe la List string frasiLinkate del contenuto delle frasi che hanno almeno un link.
     *
     * @param titolo Il titolo della pagina di cui si vogliono sapere le frasi
     * @param frasiLinkate l'array list che viene riempito delle frasi con almeno una pagina destinazione
     */
    @Override
    public void setFrasiLinkateDB(String titolo, List<String> frasiLinkate) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("Select f.contenuto from pagina_frase pf,frase f where pf.idfrase = f.idfrase and pf.titolopagina=? and f.paginaDestinazione is not null");
            preparedStatement.setString(1, titolo);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                frasiLinkate.add(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con iil recupero delle frasi");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Restituisce la pagina di destinazione della parola data in input.
     *
     * @param parola La parola di cui si vuole sapere la pagina di destinazione
     * @param titolo il titolo della pagina
     * @return una String che indica la pagina di destinazione di quella parola
     */
    @Override
    public String getPaginaDestinazioneDB(String titolo, String parola) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("Select paginadestinazione from pagina_frase pf,frase f where pf.idfrase = f.idfrase and pf.titolopagina = ? and f.contenuto = ?");
            preparedStatement.setString(1, titolo);
            preparedStatement.setString(2, parola);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore con il salvataggio del testo modificato");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Riempe le List string dei dati delle pagine che l'utente ha creato.Serve per creare la tabella
     * che visualizza le pagine che ha creato l'utente in input
     *
     * @param titoli i titoli delle pagine create dall'utente in input
     * @param ore L'ora creazione delle pagine create dall'utente in input
     * @param date le data creazione delle pagine che ha creato l'utente in input
     * @param nomeAutore il nome dell'autore di cui si vuole sapere le pagine create
     *
     */
    @Override
    public void addPagineTabellaDB(List<String> titoli, List<Date> date, List<Time> ore, String nomeAutore) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select p.titolo,p.datacreazione,p.oracreazione,p.autore from pagina p where p.autore = ?");
            preparedStatement.setString(1, nomeAutore);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                titoli.add(rs.getString(1));
                date.add(rs.getDate(2));
                ore.add(rs.getTime(3));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero dei testi");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Riempe le List string dei dati delle pagine in cui l'utente ha proposto una modifica
     *
     * @param titoli I titoli delle pagine dove l'utente ha proposto una modifica
     * @param date date di creazione delle pagine dove l'utente ha proposto una modifica
     * @param ore ore di creazione delle pagine dove l'utente ha proposto una modifica
     */

    //la traccia voleva che l'utente possa visualizzare anche i testi in cui ha proposto una modifica
    @Override
    public void addPagineModificateTabellaDB(List<String> titoli, List<Date> date, List<Time> ore, List<String> autori, String nomeAutore) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select distinct mt.paginariferimento,p.datacreazione,p.oracreazione,mt.autorepagina from modificatesto mt,pagina p where mt.autorerichiedente = ? and mt.paginariferimento=p.titolo and mt.autorepagina <> ?");
            preparedStatement.setString(1, nomeAutore);
            preparedStatement.setString(2,nomeAutore);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                titoli.add(rs.getString(1));
                date.add(rs.getDate(2));
                ore.add(rs.getTime(3));
                autori.add(rs.getString(4));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero delle pagine");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Riempe le List string dei dati che servono per ottenere la storia della pagina data in input
     * questo metodo quindi mi prende dalla cronologia del testo la storia che ha avuto
     * cosi posso ricostuire la sua storia.
     *
     * @param frasi List di string che verrà riempita delle frasi delle pagine passate
     * @param pagineDestinazione le pagine di destinazioni delle frasi
     * @param autoreModifica gli autori delle modifiche, questo serve per indicare che autore
     *                       ha cambiato il testo
     * @param titoloPagina titolo della pagina di cui si vuole sapere la storia
     * @param datasql data dell'inserimento nella cronologia, cioè la data di quando è stata accettata questa modifica
     * @param orasql ora dell'inserimento nella cronologia, cioè l'ora di quando è stata accettata questa modifica
     * @return una Date che indica la datacreazione della modifica
     */
    @Override
    //è grazie alla data e all'ora che riusciamo ad
    //individuare il distacco da una pagina all'altra, cioè quando finiscono le frasi di una pagina
    //e ne incomincia un altra.
    public void setCronologiaTestiDB(List<String> frasi, List<String> pagineDestinazione, List<String> autoreModifica, String titoloPagina, List<java.sql.Date> datasql,List<Time> orasql) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select distinct ct.contenutofrase,ct.paginadestinazione,ct.paginariferimento,ct.datainserimento,ct.orainserimento,ct.proprietariomodifica,ct.idcronologia from cronologiatesto ct where ct.paginariferimento = ? order by idCronologia");
            preparedStatement.setString(1, titoloPagina);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                frasi.add(rs.getString(1));
                pagineDestinazione.add(rs.getString(2));
                datasql.add(rs.getDate(4));
                orasql.add(rs.getTime(5));
                autoreModifica.add(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Restituisce l'autore della pagina
     *
     * @param titolo titolo della pagina
     * @return una String che mi indica l'autore della pagina
     */

    @Override
    public String getAutorePaginaDB(String titolo) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select p.autore from pagina p where p.titolo = ? LIMIT 1");
            preparedStatement.setString(1, titolo);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero dell'autore");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Metodo che riempe le List string in ingresso delle frasi e rispettive pagineDestinazioni di
     * una determinata pagina
     *
     * @param titolo Il titolo della pagina in ingresso
     *
     *@param frasi frasi della pagina in ingresso
     * @param pagineDestinazioni pagine di destinazione delle frasi
     */
    @Override
    public void setFrasiPaginaDB(String titolo, List<String> frasi, List<String> pagineDestinazioni) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select f.contenuto,f.paginadestinazione from pagina_frase pf,frase f where pf.idfrase = f.idfrase and pf.titolopagina= ? order by f.idfrase");
            preparedStatement.setString(1, titolo);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                frasi.add(rs.getString(1));
                pagineDestinazioni.add(rs.getString(2));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero del testo");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Permette all'utente di rifiutare una modifica, facendo un update del contenuto a false
     *
     * @param contenuto Il contenuto della modifica
     * @param dataCreazione data di creazione del contenuto proposto dall'utente
     * @param oraCreazione ora di creazione del contenuto proposto dall'utente
     */

    @Override
    public void setEsitoFalseDB(String contenuto, Date dataCreazione, Timestamp oraCreazione) {
        PreparedStatement preparedStatement = null;
        try {

            java.util.Date utilDate = dataCreazione; // Supponendo che dataCreazione sia di tipo java.util.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            preparedStatement = connection.prepareStatement("update modificatesto set esito = false where testorichiesto = ? and dataCreazione = ?");
            preparedStatement.setString(1, contenuto);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il settaggio dell esito");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Restituisce il numero di pagine create dall'utente
     *
     * @param nomeUtente Il nome utente in input
     * @return un intero che indica il numero di pagine create
     */

    @Override
    public int getPagineCreateDB(String nomeUtente) {
        int pagineCreate = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select u.numeropaginecreate from utente u where u.nomeutente = ?");
            preparedStatement.setString(1, nomeUtente);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero delle pagine create");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return pagineCreate;
    }

    /**
     * Restituisce il numero di modifiche effettuate dall'utente in input
     *
     * @param nomeUtente Il nome utente in input
     * @return un intero che indica il numero di modifiche effettuate
     */
    @Override
    public int getNumeroModificheDB(String nomeUtente) {
        int numeroModifiche = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select u.numeromodifiche from utente u where u.nomeutente = ?");
            preparedStatement.setString(1, nomeUtente);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero delle pagine create");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return numeroModifiche;
    }

    /**
     * Restituisce il rango dell'utente
     *
     * @param nomeUtente Il nome utente in input
     * @return una stringa che indica il rango dell'utente
     */
    @Override
    public String getRangoDB(String nomeUtente) {
        String rango = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select u.rango from utente u where u.nomeutente = ?");
            preparedStatement.setString(1, nomeUtente);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Errore con il recupero delle pagine create");
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return rango;
    }

    /**
     * Restituisce il numero di modifiche non lette dall'utente
     *
     * @param nomeUtente Il nome utente in input
     * @return un intero che indica il numero di contenuti non letti dall'utente
     */
    @Override
    public int getNumeroTestiNonLetti(String nomeUtente) {
        int numeroTesti = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select count(*) as NumeroTestiNonLetti from modificaTesto mt where mt.esito is null and mt.autorePagina = ?");
            preparedStatement.setString(1, nomeUtente);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return numeroTesti;
    }

    /**
     * Questo metodo permette di creare la storia della pagina in modo completo, cioè facendo visionare all
     * 'autore della pagina la modificata prima e dopo della sua pagina,prende in input il titolo
     * che è il titolo della pagina di cui si vuole sapere la storia e restituisce la modificata che è stata accettata
     *
     *
     * @param titolopagina Il titolo della pagina
     * @param dataCreazione questa è la data di inserimento in cronologiaTesto cioè la data di quando è stata accettata la modifica
     * @param oraCreazione questa è l'ora di inserimento in cronologiaTesto cioè l'ora di quando è stata accettata la modifica
     * @return il contenuto della modifica sottoforma di stringa.
     */

    @Override
    public String getcontenutoPaginaModificataDB(String titolopagina, java.sql.Date dataCreazione, Time oraCreazione) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("SELECT testorichiesto from modificatesto where paginariferimento = ? and oraaccettazione = ? and dataaccettazione=?");
            preparedStatement.setString(1,titolopagina);
            preparedStatement.setTime(2,oraCreazione);
            preparedStatement.setDate(3,dataCreazione);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getString(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * Aggiunge un tema al database
     *
     * @param tema Il tema da aggiungere
     */
    @Override
    public void addTemiDB(String tema) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("insert into tema values(?) ON CONFLICT DO NOTHING");
            preparedStatement.setString(1,tema);
            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Riempe le List string in input dei dati delle richieste che ha effettuato l'utente
     *
     * @param utente Il nome utente di cui si vogliono sapere le richieste
     * @param testiRichiesti il contenuto della modifica dell'utente
     * @param esito  l'esito della modifica dell'utente
     * @param autorePagina autore della pagina originale
     * @param titolopagina titolo della pagina a cui si è proposta la modifica
     */

    @Override
    public void getRichiesteDB(String utente, List<String> testiRichiesti, List<String> esito, List<String> autorePagina, List<String> titolopagina) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("select testorichiesto,esito,autorepagina,paginariferimento from modificatesto mt where mt.autorerichiedente = ?");
            preparedStatement.setString(1,utente);
            ResultSet rs =preparedStatement.executeQuery();
            while ((rs.next()))
            {
                testiRichiesti.add(rs.getString(1));
                esito.add(rs.getString(2));
                autorePagina.add(rs.getString(3));
                titolopagina.add(rs.getString(4));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Controlla se il titolo è presente nei titoli delle pagine inserite nel db, metodo che serve
     * per controllare se la pagina destinazione inserita nella sezione crea è presente nel db
     *
     * @param titolo titolo della pagina che si vuole controllare
     * @return un boolean che indica se la pagina è presente o meno
     */
    @Override
    public boolean check_paginadestinazioneDB(String titolo) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select p.titolo from pagina p");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                if(titolo.equals(resultSet.getString(1))){
                    return true;
                }
            }
        }catch (SQLException e)
        {
                e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo che permette di cancellare una pagina dal database
     *
     * @param titolo titolo della pagina che si vuole cancellare
     */

    @Override
    public void deletePaginaDB(String titolo) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("delete from pagina p where p.titolo=?");
            preparedStatement.setString(1,titolo);
            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Metodo che mi permette di recuperare la datacreazione dato il titolo dal db
     * @param titolo il titolo della pagina
     * @return un date che contiene la data di creazione
     */
    @Override
    public Date getDataCreazioneDB(String titolo) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("select p.datacreazione from pagina p where p.titolo = ?");
            preparedStatement.setString(1,titolo);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getDate(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Metodo che mi permette di ottenere l'ora creazione della pagina dato il tiolo
     * @param titolo il titolo della pagina
     * @return un Time che indica l'ora creazione della pagina
     */
    @Override
    public Time getOraCreazioneDB(String titolo) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("select p.oracreazione from pagina p where p.titolo = ?");
            preparedStatement.setString(1,titolo);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getTime(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}



