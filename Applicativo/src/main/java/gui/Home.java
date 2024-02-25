package gui;
import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

/**
 * The type Home.
 */
public class Home {

    /**
     * Il frame della pagina
     */
    private static JFrame frame;
    /**
     * Il pannello principale dove inseriremo tutti i bottoni
     */

    private static JPanel mainPanel;

    /**
     * The constant accessoLabel.
     */
    public static JLabel accessoLabel;

    /**
     * L'istanza controller che ci serverà a comunicare con il database e il controller
     */

    private static Controller controller = new Controller();

    /**
     * Stringa costante per il testo html accesso non effettuato.
     */

    private static final String ACTION_1 = "Arial";

    /**
     * La label che iniziale che da il benvenuto all'utente.
     */
    public static JLabel welcomeLabel = new JLabel("Benvenuto su Progetto-Wiki !");


    /**
     * Il punto iniziale dell'applicazione.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        /**
         * Inizializzazione del frame settando la sua grandezza
         */
        frame = new JFrame("Wiki");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        /**
         * Settiamo il background di colore bianco.
         */
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Titolo al centro
        welcomeLabel.setFont(new Font(ACTION_1, Font.BOLD, 40));
        welcomeLabel.setHorizontalAlignment(welcomeLabel.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        /**
         * Logica che viene utilizzata per visualizzare al centro Accesso non effettuato.
         * si aggiunge un mouse listener facendo cosi che se ci clicchiamo sopra apre la pagina
         * visualizza statistiche in modo tale che ci visualizzi le stastiche dell'utente
         */
        accessoLabel = new JLabel("<html><u>Accesso non effettuato</u></html>");
        accessoLabel.setFont(new Font(ACTION_1, Font.BOLD, 20));
        accessoLabel.setHorizontalAlignment(JLabel.CENTER);
        accessoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        accessoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VisualizzaStatistiche visualizzaStatistiche = null;
                try {
                    if(controller.utente != null) {
                        visualizzaStatistiche = new VisualizzaStatistiche(frame, controller);
                        if (visualizzaStatistiche == null) {
                            JOptionPane.showMessageDialog(null, "Errore");
                        } else {
                            visualizzaStatistiche.mostraFinestra();
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Registati o Loggati");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Errore con la visual delle notifiche");
                }
                catch (NullPointerException E)
                {
                    E.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Registrati O Loggati !");
                }
            }
        });
        mainPanel.add(accessoLabel, BorderLayout.CENTER);


        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        /**
         * Aggiunga dei vari pulsanti al pannello main per far navigare l'utente.
         */

        addButtonToPanel(buttonPanel, "Cerca Pagina", e -> {
            CercaPagina cercaPagina = new CercaPagina(frame, controller);
            frame.setEnabled(false);
            cercaPagina.mostraFinestra();
        });

        addButtonToPanel(buttonPanel, "Crea Pagina", e -> {
            try {
                CreaPagina creaPagina1 = new CreaPagina(frame, controller);
                creaPagina1.mostraFinestra();
            } catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(null, "Registrati o Loggati Prima");
                e1.printStackTrace();
            }
        });

        addButtonToPanel(buttonPanel, "Registrati", e -> {
            Registrazione registrazione = new Registrazione(frame, controller);
            registrazione.mostraFinestra();
            frame.setEnabled(false);
        });

        addButtonToPanel(buttonPanel, "Entra", e -> {
            LoginPage loginPage = new LoginPage(frame, controller);
            loginPage.mostraFinestra();
            frame.setEnabled(false);
        });

        addButtonToPanel(buttonPanel, "Controlla Notifiche", e -> {
            if(controller.getUtente() != null) {
                VisualizzaNotifiche visualizzaNotifiche = new VisualizzaNotifiche(frame, controller);
                visualizzaNotifiche.mostraFinestra();
                frame.setEnabled(false);
            }
            else {
                JOptionPane.showMessageDialog(null,"Registrati O Loggati !");
            }
        });
        addButtonToPanel(buttonPanel, "Logout", e -> {
            // Azione da eseguire quando viene premuto il pulsante "Logout"
            controller.logOutUtente(); // Esegui il logout
            accessoLabel.setText("<html><u>Accesso non effettuato</u></html>"); // Aggiorna il testo del label di accesso
        });

        addButtonToPanel(buttonPanel, "Cancella Le Tue Pagine", e -> {
            if(controller.utente!=null) {
                CancellaPagina cancellaPagina = new CancellaPagina(frame, controller);
                cancellaPagina.mostraFinestra();
                frame.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null,"Registrati O Loggati Prima !");
            }
        });

        addButtonToPanel(buttonPanel, "Storia Pagine", e -> {
            if (controller.getUtente() == null) {
                JOptionPane.showMessageDialog(frame, "Registrati o Loggati Prima");
            } else {
                StoriaPagine storiaPagine = new StoriaPagine(frame, controller);
                frame.setEnabled(false);
                storiaPagine.mostraFinestra();
            }
        });

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Metodo per aggiungere i bottoni al pannelo principale.
     */
    private static void addButtonToPanel(JPanel panel, String buttonText, ActionListener listener) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font(ACTION_1, Font.PLAIN, 16));
        button.addActionListener(listener);
        panel.add(button);
    }
}
