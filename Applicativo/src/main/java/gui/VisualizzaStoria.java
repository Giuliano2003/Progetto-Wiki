package gui;
import controller.Controller;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * La classe Visualizza storia che mi permette di visualizzare la storia di una pagina.
 */
public class VisualizzaStoria {

    /**
     * Il Frame corrente.
     */
    public JFrame frame;

    /**
     * Il panel dove va inserita la storia.
     */
    public JEditorPane editorPane;

    /**
     * Le frasi linkate della pagina dove vado a visualizzare la storia.
     */
    ArrayList<String> frasiLinkate = new ArrayList<>();


    /**
     * Il Controller che mi permette di risalire alla storia della pagina.
     */
    public Controller controller = new Controller();

    /**
     * Hash map che mantiene i collegamenti fra le frasi e le pagine
     */
    public HashMap<String,String> hashMap = new HashMap<>();

    /**
     * Instanzia a new Visualizza storia.
     *
     * @param framechiamante Il framechiamante
     * @param controller     Il controller
     * @throws SQLException the sql exception
     */
    public VisualizzaStoria(JFrame framechiamante, Controller controller) throws SQLException {
        this.controller=controller;
        frame = new JFrame("Visualizza Storia");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                framechiamante.setEnabled(true);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        // Testo in HTML
        String htmlText = "<html><body>" +
                "<h1 style='font-size: 36px; color: #333;'>Storia Pagina</h1>" +
                "<p style='font-size: 18px; color: #666;'>Questa è la storia della pagina:</p>" +
                "</textarea>" +
                "</body></html>";

        editorPane.setText(htmlText);

        //adesso dalla tabella cronologia_testi andremo a prendere la storia dei testi passati
        ArrayList<String> frasi = new ArrayList<>();
        ArrayList<String> paginaDestinazione = new ArrayList<>();
        ArrayList<String> autoreModifica = new ArrayList<>();
        initStoriaTesto(frasi,paginaDestinazione,autoreModifica);
        ArrayList<String>frasiPagina = new ArrayList<>();
        ArrayList<String>frasiPaginaLinkate = new ArrayList<>();
        controller.setFrasiPagina(controller.getTitolo(),frasiPagina,frasiPaginaLinkate);
        controller.setFrasi(frasi);
        controller.setFrasiLink(frasi);
        for (int k=0;k<frasiPagina.size();k++)
        {
            if(frasiPaginaLinkate.get(k) != null)
            {
                String parola=frasiPagina.get(k);
                if(parola.contains(" ") || parola.contains("-") || parola.contains("'")){
                    String[] parole = parola.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");
                    for (String s: parole) {
                        hashMap.put(s,frasiPaginaLinkate.get(k));
                    }
                }
                else {
                    hashMap.put(frasiPagina.get(k),frasiPaginaLinkate.get(k));
                }
            }
            else {
                hashMap.put(frasiPagina.get(k), frasiPaginaLinkate.get(k));
            }
        }

        JScrollPane scrollPane = new JScrollPane(editorPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        editorPane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JEditorPane source = (JEditorPane) e.getSource();
                int offset = source.viewToModel(e.getPoint());// Ottieni la posizione del clic nel documento
                String paginaDestinazione = null;
                // Ottieni la parola a partire dalla posizione del clic
                try {
                    int start = Utilities.getWordStart(source, offset);
                    int end = Utilities.getWordEnd(source, offset);
                    String parolaCliccata = source.getText(start, end - start);
                    paginaDestinazione= hashMap.get(parolaCliccata);
                    if(paginaDestinazione != null) {
                        controller.setPagina(paginaDestinazione);
                        controller.attivaFlag();
                        CercaPagina cercaPagina = new CercaPagina(frame, controller);
                        frame.setEnabled(false);
                        cercaPagina.mostraFinestra();
                    }
                } catch (BadLocationException e1)
                {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.add(mainPanel);
    }

    /**
     * Mostra finestra.
     */
    public void mostraFinestra() {
        frame.setVisible(true);
    }

    /**
     * Nascondi finestra.
     *
     * @param framechiamante Il framechiamante
     */
    public void nascondiFinestra(JFrame framechiamante) {
        frame.setVisible(false);
        framechiamante.setEnabled(true);
    }

    /**
     * Metodo che inizializza la storia del testo storia testo.
     *
     * @param frasi              Le frasi del testo
     * @param paginaDestinazione Le frasiLinkate del testo
     * @param autoreModifica     L' autore  della modifica
     * @throws SQLException the sql exception
     */
    public void initStoriaTesto(ArrayList<String> frasi, ArrayList<String> paginaDestinazione,ArrayList<String> autoreModifica) throws SQLException {
        ArrayList<java.sql.Date> datasql=new ArrayList<>();
        ArrayList<Time> orasql= new ArrayList<>();
        String titolopagina = controller.getTitolo();
        controller.setPagina(titolopagina);
        controller.setTesto();
        controller.setCronologiaTesto();
        controller.setCronologiaTesti(frasi,paginaDestinazione,autoreModifica,datasql,orasql);
        String autorePagina = controller.getAutorePagina(controller.getTitolo());
        int size = Math.min(Math.min(frasi.size(), datasql.size()), Math.min(orasql.size(), paginaDestinazione.size()));
        // Testo HTML da aggiungere
        StringBuilder htmlText = new StringBuilder("<html><body style='background-color: #F5F5F5;'>");
        htmlText.append("<h1 style='font-size: 36px; color: #333;'>Storia del testo : ").append(controller.getTitolo()).append("</h1>");
        //htmlText.append("<h1 style='font-size: 30px; color: #333;'>Il Testo Non Ha Subito Cambiamenti !</h1>");
        java.sql.Date lastDate = null;
        Time lastTime = null;
        // qui setto la pagina con la modifica
        ArrayList<String> pagineLinkate = new ArrayList<>();
        Time time = controller.getOraCreazione(controller.getTitolo());
        Date date = controller.getDataCreazione(controller.getTitolo());
        htmlText.append("<h1 style='font-size: 15px; color: #333;'>La pagina è stata creata il ").append(" ").append(date).append(" ").append("Alle ore ").append(" ").append(time).append("</h1>");
            ArrayList<String> frasiPagina = new ArrayList<>();
            ArrayList<String> frasiPaginaLinkate = new ArrayList<>();
            controller.setFrasiPagina(controller.getTitolo(),frasiPagina,frasiPaginaLinkate);
            htmlText.append("<h1 style='font-size: 20px; color: #333;'>E il suo contenuto attuale è:").append("</h1>");
            int z=0;
        htmlText.append("<h1 style='font-size: 13px; color: #334;'>");
        for (String s: frasiPagina) {
            String link = frasiPaginaLinkate.get(z);
            if (link != null) {
                htmlText.append("<a href=\"").append(link).append("\" style='color: #0066CC; text-decoration: none;'>").append(" ").append(s).append(" ").append("</a>");
            } else {
                htmlText.append(s);
            }
            z++;
            // Se non è l'ultimo elemento con la stessa data e ora, aggiungi uno spazio
        }
        htmlText.append("</h1>");
        // qui quella passata
        for (int i = 0; i < size; i++) {
            java.sql.Date datacsql = datasql.get(i);
            Time oracsql = orasql.get(i);
            String contenuto = controller.getContenutoPagina(titolopagina, datacsql, oracsql);
            controller.setFrasiLinkate(titolopagina, pagineLinkate);
            for (String s : pagineLinkate) {
                if (s.contains(" ") || s.contains("-") || s.contains("'")) {
                    String[] parole = s.split("\\s+|(?<=\\p{Punct})|(?=\\p{Punct})");
                    for (String s1 : parole) {
                        frasiLinkate.add(s1);
                    }
                } else {
                    frasiLinkate.add(s);
                }
            }

            makeFrasi(contenuto);
            // Se la data o l'ora corrente sono diverse dall'ultima data e ora visualizzate, inserisci un nuovo paragrafo con la nuova data e ora
            if (lastDate == null || !datacsql.equals(lastDate) || !oracsql.equals(lastTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String formattedDateTime = sdf.format(oracsql);
                htmlText.append("<p style='font-size: 14px; color: #333; font-family: Arial, sans-serif;'>").append("In Data ").append(datacsql).append(" E Ora ").append(formattedDateTime).append(" ").append("Ha Subito Una Modifica").append(" ").append("</p>");
                lastDate = datacsql;
                lastTime = oracsql;
            }
            // Aggiungi il contenuto della pagina
            htmlText.append("<p style='font-size: 12px; color: #444; font-family: Arial, sans-serif;'>");
            int j = i;
            htmlText.append("Contenuto Della Pagina Precedente :");
            htmlText.append("</p>");
            htmlText.append("<p style='font-size: 12px; color: #444; font-family: Arial, sans-serif;'>");
            while (j < size && datacsql.equals(datasql.get(j)) && oracsql.equals(orasql.get(j))) {
                String frase = frasi.get(j);
                String link = paginaDestinazione.get(j);
                if (link != null) {
                    htmlText.append("<a href=\"").append(link).append("\" style='color: #0066CC; text-decoration: none;'>").append(frase).append(" ").append("</a>");
                } else {
                    htmlText.append(frase);
                }
                j++;
                // Se non è l'ultimo elemento con la stessa data e ora, aggiungi uno spazio
                if (j < size && datacsql.equals(datasql.get(j)) && oracsql.equals(orasql.get(j))) {
                    htmlText.append(" ");
                }
            }
            i = j - 1;
            htmlText.append("</p>");
            htmlText.append("<p style='font-size: 12px; color: #333; font-family: Arial, sans-serif;'>");
            if(autoreModifica.get(i).equals(autorePagina))
            {
                htmlText.append("Modifica Effettuata Dall'Autore Della Pagina: ").append(autoreModifica.get(i)).append(" ");
                htmlText.append("</p>");
            }
            else {
                htmlText.append("Modifica Effettuata Da : ").append(autoreModifica.get(i));
                htmlText.append("</p>");
            }
            htmlText.append("<p style='font-size: 12px; color: #333; font-family: Arial, sans-serif;'> La modifica è stata :");
            controller.generateHTML(pagineLinkate,htmlText);

// Aggiungi una nuova riga per separare questa sezione di codice da quella successiva
            htmlText.append("</p>");
            htmlText.append("<p></p>");
        }
        htmlText.append("</body></html>");
        editorPane.setText(htmlText.toString());
    }
    /**
     * Metodo che costruisce le frasi dal testo in input.
     *
     * @param testoInput Il testo in input
     */
    public void makeFrasi(String testoInput) {
        controller.pagina.frasiLinkate.clear();
        for (String s: frasiLinkate) {
            controller.pagina.addFrasiLinkate(s);
        }
        controller.makeFrasi(testoInput);

    }

}


