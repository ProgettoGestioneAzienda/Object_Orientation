package ImplementazionePostgresDAO;

import DAO.LavorareDAO;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia LavorareDAO e fornisce metodi per la manipolazione di dati riguardanti le istanze di lavoro presenti nel database Postgres.
 */
public class LavorarePostgresDAO implements LavorareDAO {
    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public LavorarePostgresDAO(){

        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    //CUD
    /**
     * Aggiunge una nuova istanza di lavoro tra un progetto ed un laboratorio.
     *
     * @param cup               {@link String}      Il CUP del progetto.
     * @param nomeLaboratorio   {@link String}      Il nome del laboratorio.
     */
    @Override
    public void addLavorare (String cup, String nomeLaboratorio) {

        String query = "INSERT INTO azienda.LAVORARE(CUP, nomeLab) VALUES (?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, cup);
            preparedStatement.setString(2, nomeLaboratorio);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Rimuove un'istanza di lavoro tra un progetto e un laboratorio.
     *
     * @param cup               {@link String}  Il CUP del progetto da specificare per la rimozione.
     * @param nomeLaboratorio   {@link String}  Il nome del laboratorio da specificare per la rimozione.
     */
    @Override
    public void removeLavorare (String cup, String nomeLaboratorio) {

        String query = "DELETE FROM azienda.LAVORARE WHERE CUP = ? AND nomeLab = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, cup);
            preparedStatement.setString(2, nomeLaboratorio);

            preparedStatement.executeUpdate();

        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna un'istanza di lavoro tra un progetto e un laboratorio.
     *
     * @param vecchioCup                {@link String}  Il CUP attuale del progetto da aggiornare.
     * @param vecchioNomeLaboratorio    {@link String}  Il nome attuale del laboratorio da aggiornare.
     * @param cup                       {@link String}  Il nuovo CUP del progetto.
     * @param nomeLaboratorio           {@link String}  Il nuovo nome del laboratorio.
     */
    @Override
    public void updateLavorare (String vecchioCup, String vecchioNomeLaboratorio, String cup, String nomeLaboratorio) {

        String query = "UPDATE azienda.LAVORARE SET CUP = ?, nomeLab = ? WHERE CUP = ? AND nomeLab = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, cup);
            preparedStatement.setString(2, nomeLaboratorio);

            preparedStatement.setString(3, vecchioCup);
            preparedStatement.setString(4, vecchioNomeLaboratorio);

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
     * Ottiene tutte le istanze di lavoro dal database e le inserisce nelle liste specificate.
     *
     * @param progetti      {@link String}  Una lista in cui verranno inseriti i CUP dei progetti da leggere.
     * @param laboratori    {@link String}  Una lista in cui verranno inseriti i nomi dei laboratori da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainLavorare(ArrayList<String> progetti, ArrayList<String> laboratori) throws SQLException{

        String query = "SELECT * FROM azienda.LAVORARE";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while(result.next()){

                progetti.add(result.getString("Cup"));
                laboratori.add(result.getString("NomeLab"));
            }

            result.close();

        }catch (SQLException sqlException){
            throw new SQLException();
        }
    }

    /**
     * Ottiene tutte le istanze di lavoro correnti dal database.
     *
     * @return {@link ArrayList}   Una lista di informazioni rappresentanti le istanze di lavoro tra progetti e laboratori.
     */
    @Override
    public ArrayList<String[]> getLavorare() {

        String query = "SELECT * FROM azienda.LAVORARE";

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
