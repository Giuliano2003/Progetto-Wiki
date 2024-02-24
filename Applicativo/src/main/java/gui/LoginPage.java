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

/**
 * La classe Login page che da all'utente la possibilita di fare il login.
 */
public class LoginPage {

    private static JFrame frame;

    /**
     * L'istanza controller che ci permette di interrogare il database e vedere se i dati inseriti
     * sono coerenti con quelli nel database.
     */
    public static Controller controller = new Controller();

    /**
     * Instanziamo una loginPage.
     *
     * @param frameChiamante Il frame chiamante della pagina Home
     * @param controller     Il controller
     */
    public LoginPage(JFrame frameChiamante, Controller controller) {
        this.controller=controller;
        frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
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

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel titleLabel = new JLabel("Benvenuto! Effettua il Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 5);
        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel, gbc);

        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel, gbc);

        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        /**
         * Bottone per il login.
         */
        JButton loginButton = new JButton("Login");

        /**
         * Action Listener per implementare la logica di quando clicchiamo sul bottone.
         */
        loginButton.addActionListener(new ActionListener() {
            /**
             * Quando l'utente clicca sul bottone si vanno a prendere l'username e la password inserita
             * nei textField e vengono passate alla funzione che prende restituisce true se le credenziali
             * erano giuste,false altrimenti.Successivamente viene settato l'utente nel controller.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isValidCredentials(usernameField.getText(), passwordField.getText())) {
                        controller.setUtente(usernameField.getText());
                        JOptionPane.showMessageDialog(frame, "Login Successful");
                        Home.accessoLabel.setText("Accesso Effettuato come " + controller.getNomeUtente());
                        frame.dispose(); // Chiudi la finestra di login dopo il login
                    } else {
                        JOptionPane.showMessageDialog(frame, "Login Failed. Invalid credentials.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Errore nel login");
                }
                // Puliamo i campi dopo il tentativo di login
                usernameField.setText("");
                passwordField.setText("");
            }
        });
        panel.add(loginButton, gbc);

        gbc.gridx++;
        /**
         * Pulsante per tornare indietro.
         */
        JButton backButton = new JButton("Indietro");
        backButton.addActionListener(new ActionListener() {

            /**
             * Quando clicchiamo sul pulsante indietro chiudiamo la finestra corrente e riattivamo la scorsa.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Chiudi la finestra di login
                frameChiamante.setEnabled(true); // Riattiva la finestra chiamante
            }
        });
        panel.add(backButton, gbc);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Metodo che si prende cura del login dell'utente , richiamo l'apposito metodo nel controller e
     * restituendo true se tutto va a buon fine,altrimenti false
     * @return un boolean che indica lo stato della registrazione
     */
    private static boolean isValidCredentials(String username, String password) throws SQLException {
        int flag = controller.loginUtente(username, password);
        if(flag == 1)
        {
            return true;// return true se flag = 1
        }
        return false;
    }

    /**
     * Mostra finestra.
     */
    public void mostraFinestra() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    /**
     * Nascondi finestra.
     *
     * @param frameChiamante Il frame chiamante
     */
    public void nascondiFinestra(JFrame frameChiamante) {

        /**
         * Metodo che permette di settare il framechiamante di nuovo cliccabile e di settare la visibilità
         * del frame corrente a false.
         */
                frameChiamante.setEnabled(true);
                frame.setVisible(false);
    }
}


