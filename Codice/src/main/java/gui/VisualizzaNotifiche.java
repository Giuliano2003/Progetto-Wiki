package gui;
import controller.Controller;
import model.Frase;
import model.ModificaTesto;
import model.Utente;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

/**
 *  La classe Visualizza notifiche che permette all'utente di visualizzare le notifiche ricevute.
 */
public class VisualizzaNotifiche {

    private static JFrame frame;

    /**
     * Il panel per visualizzare il testo originale.
     */
    JTextPane textPaneOriginale = new JTextPane();

    /**
     * Il panel per visualizzare il testo modificato.
     */
    JTextPane textPaneModificato = new JTextPane();

    /**
     * la datacreazione della modifica
     */
    private Date dataCreazione;

    /**
     * l'ora creazione della modifica
     */

    private Timestamp oraCreazione;

    /**
     * Il Contenuto del testo modificato restituito.
     */
    public String contenuto;



    /**
     * Le Frasi linkate del testo originale.
     */
    ArrayList<String> frasiLinkateTestoOriginale = new ArrayList<>();

    /**
     * La Hash map che mantiene i collegamenti fra le frasi e le pagine di destinazione.
     */
    HashMap<String,String> hashMap = new HashMap<>();

    /**
     * I Risultati.
     */
    List<String> risultati = new ArrayList<>();

    /**
     * Il Controller che servirà a recuperare la modifica dell'utente e a salvarla se necessario.
     */
    Controller controller = new Controller();

    /**
     * Instanzia a new Visualizza notifiche.
     *
     * @param frameChiamante Il frame chiamante
     * @param controller     Il controller
     */
    public VisualizzaNotifiche(JFrame frameChiamante, Controller controller) {
        this.controller=controller;
        frame = new JFrame("Visualizza Notifiche");
        this.controller=controller;
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        textPaneOriginale.setEditable(false);
        textPaneModificato.setEditable(false);
        JScrollPane scrollPaneOriginale = new JScrollPane(textPaneOriginale);
        JScrollPane scrollPaneModificato = new JScrollPane(textPaneModificato);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneOriginale, scrollPaneModificato);
        splitPane.setResizeWeight(0.5);

        JButton visualizzaButton = new JButton("Visualizza");
        JButton salvaButton = new JButton("Salva");
        JButton rifiutaButton = new JButton("Rifiuta");
        JButton indietroButton = new JButton("Indietro");
        JButton visualizzaUtenteButton = new JButton("Visualizza Utente");
        visualizzaButton.addActionListener(e -> {
            // Puoi aggiungere qui la logica per visualizzare il testo
            String testoOriginale = textPaneOriginale.getText();
            String testoModificato = textPaneModificato.getText();
            //JOptionPane.showMessageDialog(frame, "Testo Originale:\n" + testoOriginale + "\n\nTesto Modificato:\n" + testoModificato);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(salvaButton);
        salvaButton.setEnabled(false);
        buttonPanel.add(visualizzaButton);
        buttonPanel.add(rifiutaButton);
        buttonPanel.add(indietroButton);
        buttonPanel.add(visualizzaUtenteButton);
        visualizzaUtenteButton.setEnabled(false);
        rifiutaButton.setEnabled(false);
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        rifiutaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oraCreazione=null;
                dataCreazione=null;
                contenuto = null;
                String titolo=null;
                try {
                    // metodo che mi setta il testo a destra cioè il testo proposto
                    risultati=setTestoModificato();
                    titolo=getTitolo(risultati);
                    contenuto=getContenuto(risultati);
                    //mi setto il testo modificato e allo stesso tempo mi faccio ritornare il titolo
                    //del testo originale a cui si riferiva la modifica cosi vado a settare il testo originale
                } catch (Exception exception)
                {
                    JOptionPane.showMessageDialog(null,"Errore nel settare il testo modificato");
                }
                try {
                    dataCreazione = controller.getDataCreazione(contenuto, titolo);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con il recupero della data");
                }
                try {
                    oraCreazione = controller.getOraCreazione(contenuto, titolo);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con il recupero dell'ora");
                }
                salvaButton.setEnabled(false);
                visualizzaButton.setEnabled(true);
                rifiutaButton.setEnabled(false);
                visualizzaUtenteButton.setEnabled(false);
                try {
                    controller.setEsitoFalse(contenuto,dataCreazione,oraCreazione);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con il settaggio");
                }
                textPaneModificato.setText("");
                textPaneOriginale.setText("");
                contenuto=null;
                controller.testo.frasiTesto.clear();
                controller.modificaTesto.utente=null;
            }
        });

        visualizzaUtenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VisualizzaUtente visualizzaUtente = new VisualizzaUtente(frame,controller);
                    visualizzaUtente.mostraFinestra();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con il visual dell'utente");
                }
            }
        });
        visualizzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contenuto = null;
                String titolo = null;
                int flag=0;
                controller.testo.frasiTesto.clear();
                try {
                    risultati=setTestoModificato();
                    if(controller.modificaTesto.utente != null)
                    {
                        controller.setUtenteRichiedente(controller.modificaTesto.getUtente());
                        visualizzaUtenteButton.setEnabled(true);
                    }
                    titolo=getTitolo(risultati);
                    setTestoOriginale(titolo); // mi setto anche il testo originale
                    //mi setto il testo modificato e allo stesso tempo mi faccio ritornare il titolo
                    //del testo originale a cui si riferiva la modifica cosi vado a settare il testo originale
                } catch (Exception exception)
                {
                    flag=1;
                    try {
                        if(controller.getTestiNonLetti(controller.getNomeUtente()) == 0){
                            JOptionPane.showMessageDialog(null,"Non ci sono testi da guardare");
                            visualizzaButton.setEnabled(false);
                            salvaButton.setEnabled(false);
                            rifiutaButton.setEnabled(false);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,"Errore");
                    }
                    try {
                        if(controller.getTestiNonLetti(controller.getNomeUtente()) > 0) {
                            JOptionPane.showMessageDialog(null, "Errore nel settare il testo modificato");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,"Errore");
                    }
                }
                if(flag == 0) {
                    salvaButton.setEnabled(true);
                    rifiutaButton.setEnabled(true);
                    visualizzaButton.setEnabled(false);
                }
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nascondiFinestra();
            }
        });
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaButton.setEnabled(false);
                visualizzaButton.setEnabled(true);
                rifiutaButton.setEnabled(false);
                visualizzaUtenteButton.setEnabled(false);
                try {
                    //hash map per mapparmi le frasiLinkate con la pagina di destinazione del collegamento
                    controller.setFrasiLinkate(getTitolo(risultati),frasiLinkateTestoOriginale);
                    for (String s: frasiLinkateTestoOriginale) {
                        hashMap.put(s,controller.getPaginaDestinazione(getTitolo(risultati),s));
                        //System.out.println(""+s+","+controller.getPaginaDestinazione(getTitolo(risultati),s));
                    }
                    controller.setEsito(getContenuto(risultati),dataCreazione,oraCreazione);
                    for (Frase f: controller.testo.getFrasiTesto()) {
                        f.stampaFrase(f);
                    }
                    controller.addFrasi(controller.testo.getFrasiTesto(),frasiLinkateTestoOriginale,hashMap);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con il settaggio dell'esito");
                }
                textPaneModificato.setText("");
                textPaneOriginale.setText("");
                controller.testo.frasiTesto.clear();
                controller.modificaTesto.utente=null;
            }
        });
    }

    /**
     * Mostra finestra.
     */
    public void mostraFinestra()
    {
        frame.setVisible(true);
    }

    /**
     * Nascondi finestra.
     */
    public void nascondiFinestra()
    {
        frame.setVisible(false);
    }

    /**
     * Metodo che mi confronta una stringa con un arraylist.
     *
     * @param s     La stringa
     * @param frasi Le frasi da confrontare
     * @return un boolean che indica se la parola è stata trovata
     */
    public static boolean isEqual(String s,ArrayList<String> frasi)
    {
        for (String parola: frasi) {
            if(s.equals(parola))
            {
                frasi.remove(parola);
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo che restituisce il contenuto dei risultati.
     *
     * @param risultati the risultati
     * @return Una stringa che indica il contenuto
     */
    public String getContenuto(List<String> risultati)
    {
        return risultati.get(0);
    }

    /**
     * Metodo che restituisce il titolo dei risultati.
     *
     * @param risultati I risultati
     * @return Una stringa che indica titolo
     */
    public String getTitolo(List<String> risultati)
    {
        return risultati.get(1);
    }

    /**
     * Metodo che mi setta testo modificato, cioè la proposta dell'utente
     * @return Una list string che contiene come primo elemento il contenuto della modifica
     * come secondo il titolo a cui è riferito e come terzo l'autore che ha richiesto la modifica
     */
    public List<String> setTestoModificato() {
        List<String> risultati = new ArrayList<>();
        try {
            risultati = controller.getContenutiNonLetti(controller.getNomeUtente());
            contenuto = getContenuto(risultati); // qui abbiamo il contenuto però senza frasi linkate
            //dobbiamo aggiungerle le vado a prendere sotto in frasiLinkate...
            // le frasi del contenuto
            String titolo = getTitolo(risultati);
            String nomeutente = getAutoreRichiedente(risultati);
            controller.setUtenteRichiedente(nomeutente);


            dataCreazione = controller.getDataCreazione(contenuto, titolo);
            oraCreazione = controller.getOraCreazione(contenuto, titolo);
            ArrayList<String> frasiLinkateTestoOriginale = (ArrayList<String>) controller.getFrasiLinkate(titolo);
            ArrayList<String> frasiLinkateTestoOriginaleAppoggio = new ArrayList<>();
            riempiAppoggio(frasiLinkateTestoOriginale);
            controller.makeFrasi(contenuto);

            StringBuilder risultatoFinale = new StringBuilder();
            risultatoFinale.append("<html>");
            ArrayList<Frase>frases=controller.generateHTMLwithLINK(frasiLinkateTestoOriginale,risultatoFinale);
            //controller.testo.aggiungiFrasi(frases);
            risultatoFinale.append("</html>");
            String testoHTML = risultatoFinale.toString();
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            textPaneModificato.setEditorKit(kit);
            textPaneModificato.setDocument(doc);
            textPaneModificato.setText(testoHTML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return risultati;
    }

    /**
     * Mi setta il  testo originale quello a cui è riferito la modifica.
     *
     * @param titolo     Il titolo del testo
     * @throws SQLException the sql exception
     */
    public void setTestoOriginale(String titolo) throws SQLException {
            ArrayList<String> contenuto= (ArrayList<String>) controller.getContenutoPagina(titolo);
            // in questo array list ho le frasi della pagina originale
            //senza link adesso aggiungo i link a questa pagina per visualizzarla con i link nel panel
            ArrayList<String> frasiLinkateTestoOriginale = (ArrayList<String>) controller.getFrasiLinkate(titolo);
            StringBuilder risultato = new StringBuilder();
            risultato.append("<html>");
            for (String s: contenuto) {
                if(sonoUguali(s,frasiLinkateTestoOriginale))
                {
                    risultato.append("<a href=\"\">").append(s).append("</a> ");
                }
                else
                {
                    risultato.append(s).append(" ");
                }
            }
            risultato.append("</html>");
            String testoHTML = risultato.toString();
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            textPaneOriginale.setEditorKit(kit);
            textPaneOriginale.setDocument(doc);
            textPaneOriginale.setText(testoHTML);
    }

    /**
     * Metodo che riempe le frasiLinkate del testo originale
     *
     * @param frasi Le frasi che andremo a riempire.
     */
    public void riempiAppoggio(ArrayList<String> frasi)
    {
        controller.pagina.frasiLinkate.clear();;
        for (String s:frasi) {
            if(s.contains(" ") || s.contains("-") || s.contains("'"))
            {
                String[] parole = s.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");;
                for (String parola: parole) {
                    //frasiSenzaSpazi.add(parola);
                    controller.pagina.addFrasiLinkate(parola);
                }
            }
            else
            {
                controller.pagina.addFrasiLinkate(s);
            }
        }
    }

    /**
     * Metodo che mi confronta una frase con l'array list e vede se è uguale ad una delle frasi
     * contenuta in esse.
     *
     * @param frase La frase da confrontare
     * @param frasi  Le frasi con cui si vogliono confrontare
     * @return the boolean
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

    /**
     * Mi restituisce l' autore richiedente della modifica.
     *
     * @param risultati  risultati del metodo che setta il testo modificato
     * @return una stringa che contiene l'autore richiedente della modifica
     */
    public String getAutoreRichiedente(List<String> risultati)
    {
            return risultati.get(2);
    }
}

