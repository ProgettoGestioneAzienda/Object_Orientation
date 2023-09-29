package ImplementazionePostgresDAO;

import DAO.LaboratorioDAO;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia LaboratorioDAO e fornisce metodi per la manipolazione di dati riguardanti laboratori presenti nel database Postgres.
 */
public class LaboratorioPostgresDAO implements LaboratorioDAO{

    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public LaboratorioPostgresDAO(){

        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


    //CUD
    /**
     * Aggiunge un nuovo laboratorio al database.
     *
     * @param nome                              {@link String}  Il nome unico del laboratorio.
     * @param topic                             {@link String}  Il topic o campo di ricerca del laboratorio.
     * @param matricolaResponsabileScientifico  {@link String}  La matricola del responsabile scientifico del laboratorio.
     */
    @Override
    public void addLaboratorio (String nome, String topic, String matricolaResponsabileScientifico) {

        String query = "INSERT INTO azienda.LABORATORIO(Nome, Topic, Responsabile_Scientifico) VALUES (?, ?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, matricolaResponsabileScientifico);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Rimuove un laboratorio dal database in base al nome specificato.
     *
     * @param nome  {@link String}  Il nome unico del laboratorio da rimuovere.
     */
    @Override
    public void removeLaboratorio (String nome) {

        String query = "DELETE FROM azienda.LABORATORIO WHERE Nome = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, nome);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna i dettagli di un laboratorio esistente nel database.
     *
     * @param vecchioNome                       {@link String}  Il nome unico corrente del laboratorio da aggiornare.
     * @param nome                              {@link String}  Il nuovo nome unico del laboratorio da aggiornare.
     * @param topic                             {@link String}  Il nuovo topic o campo di ricerca del laboratorio da aggiornare.
     * @param matricolaResponsabileScientifico  {@link String}  La matricola del nuovo responsabile scientifico del laboratorio da aggiornare.
     */
    @Override
    public void updateLaboratorio (String vecchioNome, String nome, String topic, String matricolaResponsabileScientifico) {

        String query = "UPDATE azienda.LABORATORIO SET Nome = ?, Topic = ?, Responsabile_Scientifico = ? WHERE Nome = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, matricolaResponsabileScientifico);

            preparedStatement.setString(4, vecchioNome);

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
     * Ottiene informazioni sui laboratori nel database, inclusi i nomi, i topics e i responsabili scientifici.
     *
     * @param nomi                          {@link ArrayList<String>}  Una lista in cui verranno aggiunti i nomi unici dei laboratori da leggere.
     * @param topics                        {@link ArrayList<String>}  Una lista in cui verranno aggiunti i topics dei laboratori da leggere.
     * @param stringResponsabiliScientifici {@link ArrayList<String>}  Una lista in cui verranno aggiunti i nomi dei responsabili scientifici dei laboratori da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainLaboratori(ArrayList<String> nomi, ArrayList<String> topics, ArrayList<String> stringResponsabiliScientifici) throws SQLException{

        String query = "SELECT * FROM azienda.LABORATORIO";

        try{

            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {

                nomi.add(result.getString("Nome"));
                topics.add(result.getString("Topic"));
                stringResponsabiliScientifici.add(result.getString("Responsabile_scientifico"));

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
     * Ottiene una lista di laboratori che non lavorano attualmente per il progetto specificato.
     *
     * @param progettoCup   {@link String}  Il CUP unico del progetto per cui cercare laboratori candidati.
     *
     * @return {@link ArrayList}   Una lista di laboratori candidati a lavorare per il progetto specificato.
     */
    @Override
    public ArrayList<String[]> getLaboratoriCandidati(String progettoCup) {

        String query = "SELECT * " +
                "FROM azienda.LABORATORIO " +
                "WHERE Nome NOT IN (SELECT nomeLab FROM azienda.LAVORARE WHERE cup = ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, progettoCup);

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
     * Ottiene una lista di laboratori che attualmente lavorano sul progetto specifico.
     *
     * @param progettoCup   {@link String}  Il CUP unico del progetto per cui cercare laboratori lavoranti.
     *
     * @return {@link ArrayList}   Una lista di informazioni sui laboratori che stanno lavorando atturalmente sul progetto specifico.
     */
    @Override
    public ArrayList<String[]> getLaboratoriLavoranti(String progettoCup) {

        String query = "SELECT * " +
                        "FROM azienda.LABORATORIO " +
                        "WHERE Nome IN (SELECT nomeLab FROM azienda.LAVORARE WHERE cup = ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, progettoCup);

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
     * Ottiene informazioni sui laboratori nel database, inclusi i nomi, i topics, i responsabili scientifici e il numero di afferenti.
     *
     * @return {@link ArrayList}    Una lista di informazioni sui laboratori, con i rispettivi nomi, topics, responsabili scientifici e il numero di afferenti.
     */
    @Override
    public ArrayList<String[]> getLaboratori() {

        String query = "SELECT Nome, Topic, Responsabile_scientifico, COUNT(*) nAfferenti " +
                "FROM azienda.LABORATORIO LEFT JOIN azienda.AFFERIRE ON Nome = nomeLab "
                + "GROUP BY Nome";

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
