package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Connessione database.
 */
public class ConnessioneDatabase {
    private static ConnessioneDatabase istance;

    /**
     * The Connection.
     */
    public Connection connection = null;

    /**
     * The Nome.
     */
    public String nome = "postgres";

    /**
     * The Password.
     */
    public String password = "Dragonball";

    /**
     * The Url.
     */
    String Url = "jdbc:postgresql://localhost:5432/ProgettoWiki";

    private String driver = "org.postgresql.Driver";
    private ConnessioneDatabase() throws SQLException
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(Url,nome,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     * @throws SQLException the sql exception
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (istance == null) {
            istance = new ConnessioneDatabase();
        }
        else if (istance.connection.isClosed()) {
            istance = new ConnessioneDatabase();
        }
        return istance;
    }

    /**
     * Gets istance.
     *
     * @return the istance
     */
    public static ConnessioneDatabase getIstance() {
        return istance;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}
