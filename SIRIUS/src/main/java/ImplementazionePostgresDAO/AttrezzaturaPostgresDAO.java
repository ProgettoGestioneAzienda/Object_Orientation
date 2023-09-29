package ImplementazionePostgresDAO;

import DAO.AttrezzaturaDAO;
import Database.ConnessioneDatabase;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia AttrezzaturaDAO e fornisce metodi per la manipolazione di dati riguardanti le attrezzature recuperate dal database Postgres.
 */
public class AttrezzaturaPostgresDAO implements AttrezzaturaDAO {
    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public AttrezzaturaPostgresDAO(){

        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    //CUD

    /**
     * Aggiunge un'attrezzatura, acquistata da un progetto, al database.
     *
     * @param descrizione   {@link String}      La descrizione dell'attrezzatura.
     * @param costo         {@link BigDecimal}  Il costo dell'attrezzatura.
     * @param cup           {@link String}      Il CUP unico del progetto proprietario dell'attrezzatura.
     * @param nomeLab       {@link String}      Il nome del laboratorio dove potra' risiedere l'attrezzatura (può essere null).
     */
    @Override
    public void addAttrezzatura(String descrizione, BigDecimal costo, String cup, String nomeLab) {

        String query = "INSERT INTO azienda.ATTREZZATURA(Descrizione, nomeLab, CUP, Costo) VALUES (?, ?, ?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, descrizione);
            preparedStatement.setString(2, nomeLab);
            preparedStatement.setString(3, cup);
            preparedStatement.setBigDecimal(4, costo);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    /**
     * Aggiorna un'attrezzatura nel database.
     *
     * @param idAttrezzatura    {@link String}      Identificativo corrente dell'attrezzatura da aggiornare.
     * @param descrizione       {@link String}      La nuova descrizione dell'attrezzatura da aggiornare.
     * @param costo             {@link BigDecimal}  Il nuovo costo dell'attrezzatura da aggiornare.
     * @param cup               {@link String}      Il nuovo CUP unico del progetto proprietario da aggiornare.
     * @param nomeLab           {@link String}      Il nuovo nome unico del laboratorio in cui risiede l'attrezzatura da aggiornare (può essere null).
     */
    @Override
    public void updateAttrezzatura(Integer idAttrezzatura, String descrizione, BigDecimal costo, String cup, String nomeLab) {

        String query = "UPDATE azienda.ATTREZZATURA SET Descrizione = ?, Costo = ?, nomeLab = ?, CUP = ? WHERE idAttrezzatura = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, descrizione);
            preparedStatement.setBigDecimal(2, costo);
            preparedStatement.setString(3, nomeLab);
            preparedStatement.setString(4, cup);

            preparedStatement.setInt(5, idAttrezzatura);

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
     * Ottiene le informazioni sulle attrezzature dal database e le inserisce in liste separate.
     *
     * @param descrizioni       {@link ArrayList<String>}       Una lista vuota in cui verranno inserite le descrizioni delle attrezzature.
     * @param costi             {@link ArrayList<BigDecimal>}   Una lista vuota in cui verranno inseriti i costi delle attrezzature.
     * @param stringProgetti    {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i nomi dei progetti proprietari delle attrezzature.
     * @param stringLaboratori  {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i nomi dei laboratori in cui potranno risiedere le attrezzature. Può contenere valori nulli.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainAttrezzature(ArrayList<Integer> idAttrezzature, ArrayList<String> descrizioni, ArrayList<BigDecimal> costi, ArrayList<String> stringProgetti, ArrayList<String> stringLaboratori) throws SQLException{

        String query = "SELECT * FROM azienda.ATTREZZATURA";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {

                idAttrezzature.add(result.getInt("idAttrezzatura"));
                descrizioni.add(result.getString("Descrizione"));
                costi.add(result.getBigDecimal("Costo"));
                stringProgetti.add(result.getString("Cup"));

                if (result.getString("NomeLab") == null)
                    stringLaboratori.add(null);
                else
                    stringLaboratori.add(result.getString("NomeLab"));

            }

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            throw new SQLException();
        }
    }

    /**
     * Ottiene tutte le attrezzature dal database e restituisce una matrice di stringhe.
     *
     * @return  {@link ArrayList<String[]>} Una lista di informazioni rappresentanti tutte le attrezzature nel database.
     */
    @Override
    public ArrayList<String[]> getAttrezzature() {

        String query = "SELECT * FROM azienda.ATTREZZATURA";

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