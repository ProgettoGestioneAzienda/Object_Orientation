package Database;

import java.sql.*;

/**
 * Questa classe gestisce la connessione a un database PostgreSQL utilizzando il pattern Singleton.
 * Solo un'istanza di questa classe è consentita per garantire che ci sia una sola connessione al database
 * in tutto l'applicativo.
 *
 * La classe fornisce un metodo statico {@link #getInstance()} per ottenere un'istanza dell'oggetto di connessione al database.
 * Una volta ottenuta un'istanza di questa classe, è possibile ottenere la connessione al database tramite
 * il metodo {@link #getConnection()} per eseguire query e interagire con il database.
 *
 * @see Connection
 * @see DriverManager
 */
public class ConnessioneDatabase {

    private static ConnessioneDatabase instance;
    private Connection connection = null;
    private String userName = "postgres";
    private String password = "password";
    private String url = "jdbc:postgresql://localhost:5432/azienda";

    private String driver = "org.postgresql.Driver";



    //COSTRUTTORE
    /**
     * Costruttore privato della classe. Questo costruttore si occupa di stabilire la connessione al database
     * utilizzando le credenziali specificate come attributi privati. Se il driver o la connessione falliscono,
     * vengono gestite le eccezioni corrispondenti.
     *
     */
    private ConnessioneDatabase() {

        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
        }
        catch (ClassNotFoundException classNotFoundException){
            System.out.println("Database Driver non trovato!");
            classNotFoundException.printStackTrace();
        }
        catch (SQLException sqlException){
            System.out.println("Connessione non riuscita!");
            sqlException.printStackTrace();
        }
    }


    //METODI
    /* Singleton pattern: costruttore privato accessibile solo all'interno della classe stessa (da questo metodo).
     * Serve per impedire di poter creare piu' connessioni al database in qualunque parte del codice (ne deve essere solo una).
     * Le altre classi potranno utilizzare solo questo metodo per richiedere un riferimento all'oggetto della connessione o crearne una
     * nuova se non esiste gia'
     */

    /**
     * Restituisce un'istanza unica di questa classe. Se non esiste già un'istanza o se l'istanza
     * esistente è stata chiusa, viene creata una nuova istanza.
     *
     * @return un'istanza di ConnessioneDatabase
     * @throws SQLException se la creazione di una nuova connessione fallisce.
     */
    public static ConnessioneDatabase getInstance() throws SQLException{

        if (instance == null)
            instance = new ConnessioneDatabase();
        else if (instance.getConnection().isClosed()) {
            instance = new ConnessioneDatabase();
        }

        return instance;
    }

    //Restituisce la connessione al database tramite la quale poter effettuare query
    /**
     * Restituisce la connessione al database attualmente aperta. Questa connessione può essere utilizzata
     * per eseguire query e interagire con il database.
     *
     * @return l'oggetto Connection per il database.
     */
    public Connection getConnection(){
        return connection;
    }
}
