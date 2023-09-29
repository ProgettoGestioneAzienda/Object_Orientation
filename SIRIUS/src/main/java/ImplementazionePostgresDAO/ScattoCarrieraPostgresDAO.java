package ImplementazionePostgresDAO;

import DAO.ScattoCarrieraDAO;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia ScattoCarrieraDAO e fornisce metodi per la manipolazione di dati degli scatti di carriera dal database Postgres.
 */
public class ScattoCarrieraPostgresDAO implements ScattoCarrieraDAO {

    private Connection connessione;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ResultSetMetaData metaData = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public ScattoCarrieraPostgresDAO(){
        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //CUD
    /**
     * Aggiunge un nuovo scatto di carriera, effettuato dal dipendente a tempo indeterminato specificato, al database.
     *
     * @param tipoScatto    {@link String}      Il tipo di scatto di carriera.
     * @param matricola     {@link String}      La matricola del dipendente che ha effettuato lo scatto di carriera.
     * @param data          {@link LocalDate}   La data in cui è stato effettuato lo scatto di carriera.
     */
    @Override
    public void addScattoCarriera(String tipoScatto, String matricola, LocalDate data) {

        String query = "INSERT INTO azienda.SCATTO_CARRIERA (matricola, tipo, data) VALUES (?, ?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, tipoScatto);
            preparedStatement.setDate(3, Date.valueOf(data));

            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    /**
     * Rimuove uno scatto di carriera di tipo middle o senior, effettuato dal dipendente a tempo indeterminato specificato, dal database.
     *
     * @param tipoScatto    {@link String}      Il tipo di scatto di carriera da rimuovere, scelto tra middle o senior.
     * @param matricola     {@link String}      La matricola del dipendente che ha effettuato lo scatto da rimuovere.
     */
    @Override
    public void removeScattoCarriera(String tipoScatto, String matricola) {

        String query = "DELETE FROM azienda.SCATTO_CARRIERA WHERE matricola = ? AND tipo = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, tipoScatto);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    /**
     * Rimuove tutti gli scatti di carriera, effettuati da un dipendente a tempo indeterminato specifico, dal database.
     *
     * @param matricola     {@link String}      La matricola del dipendente del quale si intendono rimuovere gli scatti di carriera.
     */
    @Override
    public void removeAllScattiCarrieraDipendente(String matricola){

        String query = "DELETE FROM azienda.SCATTO_CARRIERA WHERE matricola = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna uno scatto di carriera, di un dipendente a tempo indeterminato specificato, esistente nel database.
     *
     * @param vecchioTipoScatto     {@link String}          Il tipo corrente di scatto di carriera da aggiornare.
     * @param vecchiaMatricola      {@link String}          La matricola corrente del dipendente che ha effettuato lo scatto da aggiornare.
     * @param vecchiaData           {@link LocalDate}       La data corrente in cui è avvenuto lo scatto di carriera da aggiornare.
     * @param tipoScatto            {@link String}          Il nuovo tipo di scatto di carriera da aggiornare.
     * @param matricola             {@link String}          La nuova matricola del dipendente associata allo scatto da aggiornare.
     * @param data                  {@link LocalDate}       La nuova data in cui è avvenuto lo scatto di carriera da aggiornare.
     */
    @Override
    public void updateScattoCarriera (String vecchioTipoScatto, String vecchiaMatricola, LocalDate vecchiaData, String tipoScatto, String matricola, LocalDate data) {

        String query = "UPDATE azienda.SCATTO_CARRIERA SET matricola = ?, tipo = ?, data = ? WHERE matricola = ? AND tipo = ? AND data = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            //dati per l'aggiornamento
            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, tipoScatto);
            preparedStatement.setDate(3, Date.valueOf(data));

            //dati da aggiornare
            preparedStatement.setString(4, vecchiaMatricola);
            preparedStatement.setString(5, vecchioTipoScatto);
            preparedStatement.setDate(6, Date.valueOf(vecchiaData));

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    //QUERY
    /**
     * Ottiene tutti gli scatti di carriera, effettuati da un dipendente a tempo indeterminato, dal database, e li inserisce nelle liste specificate.
     *
     * @param tipiScatto            {@link ArrayList<String>}       Lista in cui verranno inseriti i tipi di scatto da leggere.
     * @param stringDipendenti      {@link ArrayList<String>}       Lista in cui verranno inserite le matricole dei dipendenti che hanno effettuato gli scatti da leggere.
     * @param date                  {@link ArrayList<String>}       Lista in cui verranno inserite le date degli scatti di carriera da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainScattiCarriera(ArrayList<String> tipiScatto, ArrayList<String> stringDipendenti, ArrayList<LocalDate> date) throws SQLException{

        String query = "SELECT * FROM azienda.SCATTO_CARRIERA";

        try {

            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while(result.next()){

                tipiScatto.add(result.getString("Tipo"));
                stringDipendenti.add(result.getString("Matricola"));
                date.add(result.getDate("Data").toLocalDate());
            }

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
            connessione.close();

        }catch (SQLException sqlException){
            throw new SQLException();
        }
    }

    /**
     * Ottiene tutti gli scatti di carriera, effettuati da dipendenti a tempo indeterminato, dal database.
     *
     * @return {@link ArrayList}    Una lista di informazioni rappresentanti tutti gli scatti di carriera.
     */
    @Override
    public ArrayList<String[]> getScattiCarriera() {

        String query = "SELECT * FROM azienda.SCATTO_CARRIERA";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo le istanze aperte
            result.close();
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Converte un ResultSet in una matrice di stringhe, costituita dai dati recuperati mediante una query.
     *
     * @param metaData      {@link ResultSetMetaData}       Il meta-data del ResultSet.
     * @param result        {@link ResultSet}               Il ResultSet contenente i dati.
     * @return              {@link ArrayList}               Una matrice di stringhe contenente i dati ottenuti dalla query.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati nel ResultSet.
     */
    @Override
    public ArrayList<String[]> createMatrix(ResultSetMetaData metaData, ResultSet result) throws SQLException {

        resultMatrix = new ArrayList<>();

        int columns = metaData.getColumnCount() + 1;

        while (result.next()) {

            String[] str = new String[columns];

            str[0] = Integer.toString(columns);

            for (int j = 1; j < columns; j++) {
                str[j] = result.getString(j);
            }

            resultMatrix.add(str);
        }

        return resultMatrix;
    }
}
