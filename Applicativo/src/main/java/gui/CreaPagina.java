package gui;
import controller.Controller;
import model.Frase;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * La classe Crea pagina che gestisce la creazione delle pagine all'utente.
 */
public class CreaPagina {
    /**
     * Il frame della pagina CreaPagina.
     */
    public static JFrame frame;

    private String testoInput = "";

    /**
     * Testo dove inserire i temi all'inizio vuoto.
     */
    private String textTemi = "";


    /**
     * Il primo panel
     */

    private JPanel panel1;

    /**
     * Label dove inseriamo il titolo
     */

    private JLabel titoloLabel;

    /**
     * TextField dove inseriamo il titolo della pagina
     */

    private JTextField titoloPaginaTextField;

    /**
     * TextField dove inseriamo la frase che vogliamo collegare
     */

    private JTextField fraseCollegamentoTextField;

    /**
     * TextField dove inseriamo la pagina di destinazione che vogliamo dare alla pagina
     */

    private JTextField paginaDestinazioneTextField;
    /**
     * Il textPane dove andremo a inserire il testo
     */
    public JTextPane contenutoPaginaTextArea = new JTextPane();
    /**
     * Pulsante per il salvataggio
     */
    private JButton salvaButton;
    /**
     * Pulsante per annullare
     */
    private JButton annullaButton;
    /**
     * Pulsante per tornare indietro
     */
    private JButton indietroButton;
    /**
     * Pulsante per creare il collegamento
     */
    private JButton creaCollegamentoButton;
    /**
     * Il Flag annullamento.
     */
    int flagAnnullamento = 0;
    /**
     * I temi della pagina.
     */
    public ArrayList<String> temiPagina = new ArrayList<>();
   /**
     * Frasi sottolineate appoggio una lista per supportare l'array che contiene le frasiInserite.
     */
    public ArrayList<String> frasiSottolineateAppoggio = new ArrayList<>();
    /**;
     * La Hash map mantiene le corrispondenze fra le frasi e le pagine di destinazione.
     */
    HashMap<String,String> hashMap = new HashMap<>();

    /**
     * contenuto della pagina inizialmente vuoto,serve per non far perdere i progressi all'utente quando scrive
     */
    String contenuto = "";
    /**
     * Titolo della pagina inizialmente vuoto,serve per non far perdere i progressi all'utente quando scrive
     */

    String titoloPagina = "";

    /**
     * * fraseDaLinkare inizialmente vuoto,serve per non far perdere i progressi all'utente quando scrive
     */
    String fraseDaLinkare = "";
    /**
     * * paginaDestinazione inizialmente vuoto,serve per non far perdere i progressi all'utente quando scrive
     */

    String paginaDestinazione = "";

    /**
     * Instanzia una  Crea pagina.
     *
     * @param frameChiamante Il frame chiamante della home
     * @param controller     Il controller
     */
    CreaPagina(JFrame frameChiamante, Controller controller) {

        frame = new JFrame("Pagina Iniziale");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                frameChiamante.setEnabled(true);
                frameChiamante.setVisible(true);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.setSize(1200, 800);

        panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

        frame.setContentPane(panel1);
        titoloLabel = new JLabel("Scrivi La Tua Pagina " + controller.utente.username);
        titoloLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel1.add(titoloLabel);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titoloPaginaTextField = new JTextField("Titolo della Pagina");
        titoloPaginaTextField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(titoloPaginaTextField);
        contenutoPaginaTextArea = new JTextPane();
        contenutoPaginaTextArea.setText("Contenuto Della Pagina");
        contenutoPaginaTextArea.setPreferredSize(new Dimension(600, 400));  // Dimensione personalizzata// Dimensione personalizzata
        JScrollPane scrollPane = new JScrollPane(contenutoPaginaTextArea);
        inputPanel.add(scrollPane);
        salvaButton = new JButton("Salva");
        annullaButton = new JButton("Annulla");
        indietroButton = new JButton("Torna Indietro");
        fraseCollegamentoTextField = new JTextField("Inserisci La Frase Da Linkare");
        paginaDestinazioneTextField = new JTextField("Inserisci La Pagina di Destinazione");
        creaCollegamentoButton = new JButton("Crea Collegamento");
        JTextField temiPaginaTextField = new JTextField("Inserisci i temi della pagina separati da virgole");
        inputPanel.add(temiPaginaTextField);

        //logica per mantenere i temi quando l'utente perde il focus

        temiPaginaTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                temiPaginaTextField.setText(textTemi);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textTemi=temiPaginaTextField.getText();
                temiPaginaTextField.setText(textTemi);
            }
        });
        contenutoPaginaTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                contenuto = contenutoPaginaTextArea.getText();
                contenutoPaginaTextArea.setText(contenuto);
            }

            @Override
            public void focusGained(FocusEvent e) {
                contenutoPaginaTextArea.setText(contenuto);
            }
        });

        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testoInput = contenutoPaginaTextArea.getText();
                controller.makeFrasi(testoInput);
                String temi = temiPaginaTextField.getText();
                String [] temiDivisi = temi.split(",");
                for (String s: temiDivisi) {
                    try {
                        controller.addTemi(s);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,"Errore inserimento temi");
                    }
                    temiPagina.add(s);
                }
                String titolo = titoloPaginaTextField.getText();
                String nomeAutore = controller.utente.username;
                try {
                    controller.addPagina(titolo,nomeAutore);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con l'inserimento della pagina,inserisci un'altro titolo");
                }
                try{
                    controller.addFrasi(controller.testo.frasiTesto,frasiSottolineateAppoggio,hashMap);
                }catch (Exception e1)
                {
                    JOptionPane.showMessageDialog(null,"Errore con l'inserimento delle frasi");
                }
            }
        });
        titoloPaginaTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                titoloPaginaTextField.setText(titoloPagina);
            }

            @Override
            public void focusLost(FocusEvent e) {
                titoloPagina=titoloPaginaTextField.getText();
                titoloPaginaTextField.setText(titoloPagina);
            }
        });

        fraseCollegamentoTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                fraseCollegamentoTextField.setText(fraseDaLinkare);
            }

            @Override
            public void focusLost(FocusEvent e) {
                fraseDaLinkare=fraseCollegamentoTextField.getText();
                fraseCollegamentoTextField.setText(fraseDaLinkare);
            }

        });

        paginaDestinazioneTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                paginaDestinazioneTextField.setText(paginaDestinazione);
            }

            @Override
            public void focusLost(FocusEvent e) {
                paginaDestinazione=paginaDestinazioneTextField.getText();
                paginaDestinazioneTextField.setText(paginaDestinazione);
            }
        });

        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aggiungi qui la logica per annullare le modifiche
                titoloPaginaTextField.setText("");
                contenutoPaginaTextArea.setText("");
                flagAnnullamento=1;
            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frameChiamante.setVisible(true);
                frameChiamante.setEnabled(true);
                frame.setVisible(false);
            }
        });


        creaCollegamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String collegamento = fraseCollegamentoTextField.getText();
                try {
                    if(!controller.checkPaginaDestinazione(paginaDestinazioneTextField.getText()))
                    {
                        JOptionPane.showMessageDialog(null,"Non Esiste La Pagina di destinazione del collegamento");
                    }
                    else
                    {
                        hashMap.put(collegamento,paginaDestinazioneTextField.getText());
                        frasiSottolineateAppoggio.add(collegamento);
                        if(collegamento.contains(" ") || collegamento.contains("-") || collegamento.contains("'")){
                            String[] parole = collegamento.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");
                            for (String s: parole) {
                                controller.pagina.addFrasiLinkate(s);
                                //frasiSottolineate.add(s);
                                aggiungiEffettoCliccabile();
                            }
                        }
                        else {
                            //frasiSottolineate.add(fraseCollegamentoTextField.getText());
                            controller.pagina.addFrasiLinkate(fraseCollegamentoTextField.getText());
                        }
                        aggiungiEffettoCliccabile();
                    }
                }catch (Exception e2)
                {
                    e2.printStackTrace();
                }

            }
        });

        inputPanel.add(salvaButton);
        inputPanel.add(annullaButton);
        inputPanel.add(indietroButton);
        inputPanel.add(fraseCollegamentoTextField);
        inputPanel.add(paginaDestinazioneTextField);
        inputPanel.add(creaCollegamentoButton);
        panel1.add(inputPanel);
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
     * Metodo per far visualizzare all'utente la parola in blu quando la linka
     */

    private void aggiungiEffettoCliccabile() {
        String contenuto = contenutoPaginaTextArea.getText();
        String fraseCliccabile = fraseCollegamentoTextField.getText();
        if (contenuto.contains(fraseCliccabile)) {
            StyledDocument styledDocument = contenutoPaginaTextArea.getStyledDocument();
            SimpleAttributeSet attributi = new SimpleAttributeSet();
            StyleConstants.setUnderline(attributi, true);
            StyleConstants.setForeground(attributi, Color.BLUE);
            int start = contenuto.indexOf(fraseCliccabile);
            int end = start + fraseCliccabile.length();
            styledDocument.setCharacterAttributes(start, end - start, attributi, false);
        } else {
            JOptionPane.showMessageDialog(frame, "La frase cliccabile non è presente nel testo.");
        }
    }

    /**
     * Metodo per confrontare una stringa con un array e se trova una corrispondenza nella lista frasi
     * rimuove quell'elemento.
     *
     * @param s     La stringa che voglio confrontare
     * @param frasi L'array con cui voglio confrontare la mia stringa
     * @return un boolean che indica che si è trovata la stringa o meno
     */
    public static boolean isEqual(String s,ArrayList<String>frasi)
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
}

