package gui;
import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *Classe usata per garantire che gli utenti che usano l'applicativo possano registrarsi
 */

public class Registrazione {
    /**
     * Frame della pagina.
     * */

            private JFrame frame;
    /**
     * Istanza di controller che garantisce che i dati che ha inserito l'utente siano coerenti,completi e
     * in linea con le politiche del db
     */

            public Controller controller = new Controller();

    /**
     *
     * @param frameChiamante il frame della pagina Home che richiama questa pagina
     * @param controller il controller
     */
    public Registrazione(JFrame frameChiamante, Controller controller) {
                //creazione del frame
                this.controller=controller;
                frame = new JFrame("Registrazione");
                frame.setSize(400, 300);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
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
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        //creazione del campo utente

        // Etichette e campi di testo per utente
        JLabel userLabel = new JLabel("Utente:");
        JTextField userField = new JTextField();
        mainPanel.add(userLabel);
        mainPanel.add(userField);

        //creazione del campo password
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        //Creazione del campo email

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        // Pulsante di registrazione
        JButton registerButton = new JButton("Registrati");
        JButton indietroButton = new JButton("Indietro");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //metodo che si occupa di inserire l'utente nel db
                    controller.addUtente(userField.getText(),passwordField.getText(),emailField.getText());
                    Home.accessoLabel.setText("Accesso Effettuato come : " + controller.utente.username);
                    JOptionPane.showMessageDialog(null,"Registrazione Effettuata");
                    frameChiamante.setEnabled(true);
                    frame.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore inserimento Utente");
                }

            }
        });
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setEnabled(true);
                nascondiFinestra();
            }
        });

        // Aggiungi il pulsante al pannello principale
        mainPanel.add(registerButton);
        mainPanel.add(indietroButton);

        frame.add(mainPanel);
    }

    /**
     * Metodo per mostare la finestra
     */
    public void mostraFinestra()
            {
        frame.setVisible(true);
            }

    /**
     * metodo per nascondere la finestra
     */
    public void nascondiFinestra()
            {frame.setVisible(false);
            }

}

