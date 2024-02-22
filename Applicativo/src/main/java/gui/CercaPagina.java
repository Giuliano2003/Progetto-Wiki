package gui;
import model.Pagina;
import model.Testo;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import controller.Controller;

/**
 * Classe che mi permette di cercare le pagine presenti nel database.
 */
public class CercaPagina {

    /**
     * frame della pagina corrente
     */
    private JFrame frameCerca;
    /**
     * Barra di ricerca dove l'utente può cercare le pagine
     */

    private JTextField searchBar;

    /**
     * Pulsante di ricerca
     */

    private JButton searchButton;

    /**
     * Pulsante per andare indietro
     */


    private JButton indietroButton;
    /**
     * I Collegamenti della pagina, contiene le frasi che hanno pagina destinazione diversa da null.
     */
    ArrayList<String> collegamentiPagina = new ArrayList<>();
    /**
     * Le Frasi della pagina.
     */
    ArrayList<String> frasiPagina = new ArrayList<>();
    /**
     * The Hash map.
     */
    HashMap<String,String> hashMap = new HashMap<>();

    /**
     * Il tasto per modificar eil testo
     */

    private JButton modificaButton;

    /**
     *L'area dove verranno mostrati i risultati
     */

    private JTextPane risultatiTextArea;

    /**
     * Il controller che in questo caso serve a recuperare le pagine e settare i link
     */

    private Controller controller = new Controller();

    /**
     * Instanzio a Cerca pagina.
     *
     * @param frameChiamante Il frame chiamante ,la home
     * @param controller     Il controller
     */
    CercaPagina(JFrame frameChiamante, Controller controller) {

        // Creazione della finestra CercaPagine
        frameCerca = new JFrame("Cerca Pagine");
        frameCerca.setSize(1000, 700);
        frameCerca.setDefaultCloseOperation(frameCerca.DISPOSE_ON_CLOSE);
        frameCerca.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frameChiamante.setEnabled(true);
            }
        });

        frameCerca.setLayout(new BorderLayout());
        // Creazione del pannello superiore con la barra di ricerca
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        searchBar = new JTextField(20);
        searchButton = new JButton("Cerca");
        indietroButton = new JButton("Indietro");
        modificaButton = new JButton("Modifica Testo");
        topPanel.add(searchBar);
        topPanel.add(searchButton);
        topPanel.add(indietroButton);
        topPanel.add(modificaButton);
        modificaButton.setVisible(false);


        // Creazione dell'area di testo per visualizzare i risultati
        risultatiTextArea = new JTextPane();
        risultatiTextArea.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane(risultatiTextArea);


        frameCerca.add(topPanel, BorderLayout.NORTH);
        frameCerca.add(scrollPane, BorderLayout.CENTER);


        searchButton.addActionListener(new ActionListener() {
            //Gli utenti generici del sistema potranno cercare una pagina e il sistema mostrerà la versione corrente del
            //testo e i collegamenti.
            @Override
            public void actionPerformed(ActionEvent e)
            {
                collegamentiPagina.clear();
                String titoloPaginaDaCercare = searchBar.getText();
                try
                {
                    controller.setFrasiLinkate(titoloPaginaDaCercare, collegamentiPagina);
                    //il metodo setFrasiLinkate mi riempe frasiLinkateTestoOriginale con tutti i collegamenti, della pagina che stiamo cercando
                    for (String s: collegamentiPagina)
                    {
                        hashMap.put(s,controller.getPaginaDestinazione(titoloPaginaDaCercare,s));
                        //getPaginaDestinazione mi ritorna la pagina destinazione del collegamento corrente(s)
                    }
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,"Errore con la ricerca !");
                }
                //parolePaginaDaCercare sono le frasi della pagina
                frasiPagina = new ArrayList<>();
                StringBuilder risultatoFinale = new StringBuilder("<html>");
                try
                {
                    controller.cercaPagina(titoloPaginaDaCercare, frasiPagina);
                    //il metodo cercaPagina mi riempe parolePaginaDaCercare con le frasi del testo e mi crea
                    // un istanza attiva di pagina con la pagina  del titolo che ho cercato

                    //parolePaginaDaCercare dovvrebbe essere chiamato piu frasiPaginaDaCercare

                    ////adesso mi scorro le frasi e vedo quale di queste hanno dei collegamenti
                    //se il collegamento/link è != null allora
                    // sottolineo la frase all'interno del testo (pk ricorda il collegamento è una frase)

                    for (String parola: frasiPagina)
                    {
                        String link = controller.ottieniCollegamentoParola(titoloPaginaDaCercare,parola);
                        //link è la pag_destinazione della frase corrente(parola) della pagina cercata
                        if(link != null)
                        {
                            risultatoFinale.append("<a href=\"").append(link).append("\">").append(parola).append("</a> ");
                        }
                        else
                        {
                            risultatoFinale.append(parola).append(" ");
                        }
                    }
                    risultatoFinale.append("</html>");
                    risultatiTextArea.setText(String.valueOf(risultatoFinale));
                    modificaButton.setVisible(true);
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,"Errore nella ricerca");
                }
            }
        });
        /*Quindi  risultatiTextarea sara riempito da tutte le frasi del testo che abbiamo cercato.
        Si puo modificare(si rende attivo il bottone)
        parolePagineDaCercare è un arraylist composto da tutte le frasi del testo. le frasi ricorda sono anche i collegamenti
        frasiLinkateTestoOriginale conterra i collegamenti della pagina cercata
        Il controller ha un istanza attiva di pagina(quella che ho cercato)*/

        risultatiTextArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTextPane source = (JTextPane) e.getSource();
                int offset = source.viewToModel(e.getPoint());// Ottieni la posizione del clic nel documento
                String paginaDestinazione = null;
                // Ottieni la parola a partire dalla posizione del clic
                try
                {
                    int start = Utilities.getWordStart(source, offset);
                    int end = Utilities.getWordEnd(source, offset);
                    String parolaCliccata = source.getText(start, end - start);
                    //chiavi avra tutti i collegamenti della pagina cercata
                    ArrayList<String> chiavi = new ArrayList<>(hashMap.keySet());
                    for (String s: chiavi)
                    {
                        if(s!=null)
                        {
                            if (s.contains(parolaCliccata)) paginaDestinazione = hashMap.get(s);

                        }
                    }
                    //se ho cliccato una parola che è un collegamento,(quindi una frase),allora mi salvo la pagina destinazione
                    //di quel collegamento
                    //creo la nuova pagina che sara quella della pag destinazione,
                    //MA ATTENZIONE NON HA PIU UN TESTO DOVRAI POI CREARLO!!!!!!!!!!!!!!
                    //creo l istanza attiva per il controller
                    //Pagina pagina = new Pagina(paginaDestinazione);
                    //creo l istanza attiva per il controller
                    controller.setPagina(paginaDestinazione);
                    //se clicco un collegamento si riempe paginaDestinazione e faccio setTestoModificato
                    //se pagDestinazione è null non
                    // deve fare niente di tutto cio
                    if(paginaDestinazione != null)
                    {
                        setTestoModificato(paginaDestinazione);
                        searchBar.setText(controller.getTitolo());
                    }
                }
                catch (BadLocationException ex)
                {
                    JOptionPane.showMessageDialog(null, "Clicca Meglio !");
                } catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,"Errore");
                }
            }


            @Override
            public void mousePressed(MouseEvent e) {
                    //override
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                    //override
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                        //override
            }

            @Override
            public void mouseExited(MouseEvent e) {
                        //override
            }
        });
        //Qui quindi io esco sempre con una nuova istanza di Pagina, che non ha più un testo. l istanza Testo di Pagina è vuota

        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frasiPagina.clear();
                ArrayList<String> paginedestinazione = new ArrayList<>();
                ArrayList<String> frasiLinkate = new ArrayList<>(hashMap.keySet());
                //se stai navigando senza esserti loggato o registrato
                if (controller.getUtente() == null || controller.getUtente().getNomeUtente() == null)
                {
                    JOptionPane.showMessageDialog(null, "Registrati");
                }
                else
                {
                    try
                    {
                        //Questo if è stato fatto per dare continuità tra le pagine.
                        //Nel momento in cui tu cerchi la pagina avrai tutto di quella pagina: riga 135
                        //Se la pagina e poi clicchi un collegamento di questa pagina,
                        //Nel momento in cui tu clicchi avrai una nuova Pagina pero senza Testo
                        //e quindi dovrai ricrearlo, cio che faccio nell if
                        if(controller.pagina.getTesto() == null)
                        {
                            controller.setFrasiLinkate(controller.getTitolo(),frasiLinkate);
                            controller.setFrasiPagina(controller.getTitolo(), frasiPagina,paginedestinazione);
                            controller.setTesto(frasiPagina);
                            controller.setPagina(controller.getTitolo(),controller.getTesto());
                            /*
                            Testo testo = new Testo(frasiPagina);
                            Pagina pagina = new Pagina(controller.getTitolo(), testo);
                            controller.setPagina(pagina);

                             */
                            controller.setFrasiLinkate(frasiLinkate);
                            //imposta i nuovi collegamenti che ho creato come quelli attivi del controller
                        }
                    }
                    catch (SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null,"Errore con il settaggio delle frasi !");
                    }
                    ModificaPagina modificaPagina = null;
                    try
                    {
                        modificaPagina = new ModificaPagina(frameCerca, controller);
                    }
                    catch (SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null,"Errore con la modifica");
                    }
                    if(modificaPagina != null) {
                        modificaPagina.mostraFinestra();
                        frameCerca.setEnabled(false);
                    }
                }

            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nascondiFinestra(frameChiamante);
                frameChiamante.setVisible(true);
            }
        });
    }

    /**
     * Mostra finestra.
     */
    public void mostraFinestra()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frameCerca.setVisible(true);
            }
        });
    }

    /**
     * Nascondi finestra.
     *
     * @param frameChiamante the frame chiamante
     */
    public void nascondiFinestra(JFrame frameChiamante)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frameChiamante.setEnabled(true);
                frameCerca.setVisible(false);
            }
        });
    }

    /**
     * Sets testo modificato.
     *
     * @param paginaDestinazione the pagina destinazione
     * @throws SQLException the sql exception
     */
    public void  setTestoModificato(String paginaDestinazione) throws SQLException
    {
        risultatiTextArea.setText("");
        collegamentiPagina.clear();
        frasiPagina.clear();
        hashMap.clear();
        //praticamente ho resettato tutte le proprietà della pagina che ho cercato, pk adesso sono su quella
        // del collegamento
        controller.setFrasiPagina(paginaDestinazione, frasiPagina, collegamentiPagina);
        //setFrasiPagina mi riempe parolePaginaDaCercare con le frasi del testo,
        // e mi riempe frasiLinkateTestoOriginale con i collegamenti,( della pagina passata come parametro)

        //inizio a scrivere la nuova pagina
        StringBuilder risultato = new StringBuilder("<html>");
        ArrayList<String> appoggio = new ArrayList<>();
        int i=0;
        for (String s: frasiPagina)
        {
            appoggio.add(collegamentiPagina.get(i));
            String link = collegamentiPagina.get(i);//frasiLinkateTestoOriginale ha le pagine destinazione
            if(link != null)//se la parola corrente ha una pagina destinazione, la sottolineo
            {
                hashMap.put(s,link);
                risultato.append("<a href=\"").append(link).append("\">").append(s).append("</a> ");
            }
            else risultato.append(s).append(" ");
            i++;
        }
        risultato.append("</html>");
        risultatiTextArea.setText(String.valueOf(risultato));
        modificaButton.setVisible(true);
    }
    /*da questo metodo esco che ho riempito con la nuova pagina questi:
     1)risultatiTextArea
     2)frasiLinkateTestoOriginale
     3)parolePaginaDaCercare
     4)hashMap.clear
     ma non inserisco un testo alla pagina nuova*/
}
