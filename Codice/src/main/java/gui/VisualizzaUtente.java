package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * La classe Visualizza utente che permette di vedere le stastistiche dell'utente in fase di accettazione.
 */
public class VisualizzaUtente {

    /**
     * Il Frame corrente.
     */
    public JFrame frame;

    /**
     * L'editor pane dove verrano visualizati i risultati.
     */
    public JEditorPane editorPane;

    /**
     * il controller che in questo caso servirà a recuperare dati dell'utente dal database
     */

    private Controller controller;

    /**
     * Il font che useremo.
     */
    String testoFont = "<p style='font-size: 14px; color: #333; font-family: Arial, sans-serif;'>";

    /**
     * Instantiates a new Visualizza utente.
     *
     * @param frameChiamante Il frame chiamante
     * @param controller     Il controller
     * @throws SQLException the sql exception
     */
    public VisualizzaUtente(JFrame frameChiamante, Controller controller) throws SQLException {
        frame = new JFrame("Visualizza Le Statistiche Di : "+controller.getUsernameRichiedente());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frameChiamante.setEnabled(true);
                frameChiamante.setVisible(true);
            }
        });
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        int pagineCreate=controller.getPagineCreate(controller.getUsernameRichiedente());
        int numeroModifiche = controller.getNumeroModifiche(controller.getUsernameRichiedente());
        String rango = controller.getRango(controller.getUsernameRichiedente());
        editorPane.setContentType("text/html");
        StringBuilder htmlText = new StringBuilder("<html><body style='background-color: #F5F5F5;'>");
        htmlText.append("<h1 style='font-size: 36px; color: #333;'>Statistiche Dell'Utente : ").append(controller.getUsernameRichiedente()).append("</h1>");
        htmlText.append(testoFont).append("Ha Creato : ").append(pagineCreate).append(" Pagine").append("</p>");
        htmlText.append(testoFont).append("Ha Effettuato : ").append(numeroModifiche).append(" Modifiche").append("</p>");
        htmlText.append(testoFont).append("è Rango : ").append(rango).append("</p>");
        editorPane.setText(htmlText.toString());
        JScrollPane scrollPane = new JScrollPane(editorPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

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
     *
     * @param framechiamante the framechiamante
     */
    public void nascondiFinestra(JFrame framechiamante)
    {frame.setVisible(false);
        framechiamante.setEnabled(true);}
}
