package ImplementazionePostgresDAO;

import DAO.AfferireDAO;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia AfferireDAO e fornisce metodi per la manipolazione di dati riguardanti le afferenze tra dipendenti indeterminati e laboratori dal database Postgres.
 */
public class AfferirePostgresDAO implements AfferireDAO {
    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede l'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public AfferirePostgresDAO(){

        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


    //CUD

    /**
     * Aggiunge un'afferenza di un dipendente indeterminato ad un laboratorio nell'azienda.
     *
     * @param matricola {@link String}  La matricola del dipendente da associare.
     * @param nomeLab   {@link String}  Il nome del laboratorio da associare.
     */
    @Override
    public void addAfferenza(String matricola, String nomeLab) {

        String query = "INSERT INTO azienda.AFFERIRE(Matricola, nomeLab) VALUES (?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nomeLab);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Rimuove un'afferenza di un dipendente indeterminato ad un laboratorio dall'azienda.
     *
     * @param matricola {@link String}  La matricola del dipendente di cui rimuovere l'associazione.
     * @param nomeLab   {@link String}  Il nome del laboratorio di cui rimuovere l'associazione.
     */
    @Override
    public void removeAfferenza (String matricola, String nomeLab) {

        String query = "DELETE FROM azienda.AFFERIRE WHERE Matricola = ? AND nomelab = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nomeLab);

            preparedStatement.executeUpdate();

        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna un'afferenza di un dipendente indeterminato ad un laboratorio nell'azienda.
     *
     * @param vecchiaMatricola  {@link String}  La matricola corrente del dipendente da aggiornare.
     * @param vecchioNomeLab    {@link String}  Il nome corrente del laboratorio da aggiornare.
     * @param matricola         {@link String}  La nuova matricola del dipendente da aggiornare.
     * @param nomeLab           {@link String}  Il nuovo nome del laboratorio da aggiornare.
     */
    @Override
    public void updateAfferenza(String vecchiaMatricola, String vecchioNomeLab, String matricola, String nomeLab) {

        String query = "UPDATE azienda.AFFERIRE SET Matricola = ?, nomeLab = ? WHERE Matricola = ? AND nomelab = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nomeLab);

            preparedStatement.setString(3, vecchiaMatricola);
            preparedStatement.setString(4, vecchioNomeLab);

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
     * Ottiene tutte le afferenze tra dipendenti indeterminati e laboratori nell'azienda.
     *
     * @param dipendenti    {@link ArrayList<String>}   ArrayList in cui verranno inserite le matricole dei dipendenti associati.
     * @param laboratori    {@link ArrayList<String>}   ArrayList in cui verranno inseriti i nomi dei laboratori associati.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainAfferenze(ArrayList<String> dipendenti, ArrayList<String> laboratori) throws SQLException{

        String query = "SELECT * FROM azienda.AFFERIRE";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while(result.next()){

                dipendenti.add(result.getString("Matricola"));
                laboratori.add(result.getString("NomeLab"));
            }

            result.close();

        }catch (SQLException sqlException){
            throw new SQLException();
        }
    }

    /**
     * Restituisce, sottoforma di matrice di stringhe, tutte le afferenze tra dipendenti indeterminati e laboratori registrate nell'azienda.
     *
     * @return  {@link ArrayList<String[]>}  Un ArrayList di array di stringhe, rappresentante tutte le afferenze. Ogni array contiene la matricola e il nome del laboratorio.
     */
    @Override
    public ArrayList<String[]> getAfferenze() {

        String query = "SELECT * FROM azienda.AFFERIRE";

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


    //Metodo utilizzato per convertire un resultSet in matrice

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
