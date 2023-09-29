package ImplementazionePostgresDAO;

import DAO.ProgettoDAO;
import Database.ConnessioneDatabase;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia ProgettoDAO e fornisce metodi per la manipolazione di dati dei progetti presenti nel database Postgres.
 */
public class ProgettoPostgresDAO implements ProgettoDAO {

    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public ProgettoPostgresDAO(){

        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


    //CUD
    /**
     * Aggiunge un nuovo progetto al database.
     *
     * @param nome                 {@link String}       Il nome unico del progetto.
     * @param cup                  {@link String}       Il CUP unico del progetto.
     * @param budget               {@link BigDecimal}   Il budget del progetto.
     * @param dataInizio           {@link LocalDate}    La data di inizio del progetto.
     * @param dataFine             {@link LocalDate}    La data di fine del progetto (può essere nulla se non è stata definita).
     * @param referenteScientifico {@link String}       La matricola del referente scientifico del progetto.
     * @param responsabile         {@link String}       La matricola del responsabile del progetto.
     */
    @Override
    public void addProgetto(String nome, String cup, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine, String referenteScientifico, String responsabile) {

        String query = "INSERT INTO azienda.PROGETTO(CUP, Nome, dataInizio, dataFine, Budget, Referente_Scientifico, Responsabile) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, cup);
            preparedStatement.setString(2, nome);
            preparedStatement.setDate(3, Date.valueOf(dataInizio));

            if (dataFine == null)
                preparedStatement.setDate(4, null);
            else
                preparedStatement.setDate(4, Date.valueOf(dataFine));

            preparedStatement.setBigDecimal(5, budget);
            preparedStatement.setString(6, referenteScientifico);
            preparedStatement.setString(7, responsabile);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna le informazioni di un progetto nel database.
     *
     * @param vecchioCup                {@link String}      Il codice unico corrente del progetto da aggiornare.
     * @param nome                      {@link String}      Il nuovo nome unico del progetto da aggiornare.
     * @param cup                       {@link String}      Il nuovo CUP unico del progetto da aggiornare.
     * @param budget                    {@link BigDecimal}  Il nuovo budget del progetto da aggiornare.
     * @param dataInizio                {@link LocalDate}   La nuova data di inizio del progetto da aggiornare.
     * @param dataFine                  {@link LocalDate}   La nuova data di fine del progetto da aggiornare (può essere null se non è stata definita).
     * @param referenteScientifico      {@link String}      La matricola del nuovo referente scientifico del progetto da aggiornare.
     * @param responsabile              {@link String}      La matricola del nuovo responsabile del progetto da aggiornare.
     */
    @Override
    public void updateProgetto (String vecchioCup, String nome, String cup, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine, String referenteScientifico, String responsabile) {

        String query = "UPDATE azienda.PROGETTO SET CUP = ?, Nome = ?, dataInizio = ?, dataFine = ?, Budget = ?, Referente_Scientifico = ?, Responsabile = ? WHERE CUP = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, cup);
            preparedStatement.setString(2, nome);
            preparedStatement.setDate(3, Date.valueOf(dataInizio));

            if (dataFine == null)
                preparedStatement.setDate(4, null);
            else
                preparedStatement.setDate(4, Date.valueOf(dataFine));

            preparedStatement.setBigDecimal(5, budget);
            preparedStatement.setString(6, referenteScientifico);
            preparedStatement.setString(7, responsabile);

            preparedStatement.setString(8, vecchioCup);

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
     * Recupera informazioni sui progetti dal database e le inserisce in liste separate.
     *
     * @param nomi                          {@link ArrayList<String>}       Una lista in cui inserire i nomi unici dei progetti da leggere.
     * @param cups                          {@link ArrayList<String>}       Una lista in cui inserire i CUP unici dei progetti da leggere.
     * @param budgets                       {@link ArrayList<BigDecimal>}   Una lista in cui inserire i budget dei progetti da leggere.
     * @param dateInizio                    {@link ArrayList<LocalDate>}    Una lista in cui inserire le date di inizio dei progetti da leggere.
     * @param dateFine                      {@link ArrayList<LocalDate>}    Una lista in cui inserire le date di fine dei progetti da leggere(può contenere valori nulli).
     * @param stringReferentiScientifici    {@link ArrayList<String>}       Una lista in cui inserire i referenti scientifici dei progetti da leggere.
     * @param stringResponsabili            {@link ArrayList<String>}       Una lista in cui inserire i responsabili dei progetti da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainProgetti(ArrayList<String> nomi, ArrayList<String> cups, ArrayList<BigDecimal> budgets, ArrayList<LocalDate> dateInizio,
                               ArrayList<LocalDate> dateFine, ArrayList<String> stringReferentiScientifici, ArrayList<String> stringResponsabili) throws SQLException{

        String query = "SELECT * FROM azienda.PROGETTO";

        try{

            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while(result.next()){

                nomi.add(result.getString("Nome"));
                cups.add(result.getString("Cup"));
                budgets.add(result.getBigDecimal("Budget"));
                dateInizio.add(result.getDate("DataInizio").toLocalDate());

                if (result.getDate("DataFine") == null)
                    dateFine.add(null);
                else
                    dateFine.add(result.getDate("DataFine").toLocalDate());

                stringReferentiScientifici.add(result.getString("Referente_scientifico"));
                stringResponsabili.add(result.getString("Responsabile"));
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
     * Recupera informazioni sui progetti dal database.
     *
     * @return {@link ArrayList}   Una lista di informazioni rappresentanti le informazioni sui progetti.
     */
    @Override
    public ArrayList<String[]> getProgetti() {

        String query = "SELECT CupProgetto, Nome, DataInizio, DataFine, Budget, Referente_scientifico, Responsabile, SUM(DISTINCT(costo)) CostoAttrezzature, SUM(DISTINCT(costoDip)) CostoDip " +
                        "FROM (azienda.PROGETTO AS PROGETTO(cupProgetto, nome, dataInizio, dataFine, budget, referente_scientifico, responsabile) " +
                        "LEFT JOIN azienda.DIP_PROGETTO AS DIP_PROGETTO(matricola, nomeDip, cognome, codFiscale, indirizzo, dataNascita, dataAssunzione, costoDip, scadenza, cupDip) " +
                        "ON cupProgetto = cupDip) LEFT JOIN azienda.ATTREZZATURA AS ATT ON cupProgetto = ATT.cup " +
                        "GROUP BY cupProgetto";

        try{
            preparedStatement = connessione.prepareStatement(query);

            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo le istanze aperte
            result.close();
            connessione.close();

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Recupera informazioni sui progetti non terminati dal database.
     *
     * @return {@link ArrayList}    Una lista di informazioni rappresentanti i progetti non terminati.
     */
    @Override
    public ArrayList<String[]> getProgettiNonTerminati() {

        String query = "SELECT * FROM azienda.PROGETTO WHERE DataFine is null OR dataFine >= CURRENT_DATE";

        try{
            preparedStatement = connessione.prepareStatement(query);

            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo le istanze aperte
            result.close();
            connessione.close();

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Ottiene il costo totale delle attrezzature acquistate da un progetto specifico.
     *
     * @param progetto  {@link String}      Il CUP unico  del progetto di cui si desidera calcolare il costo totale delle attrezzature acquistate.
     * @return          {@link BigDecimal}  Il costo totale delle attrezzature acquistate dal progetto o null se non e' presente nessun acquisto.
     */
    @Override
    public BigDecimal getCostoTotaleAttrezzature(String progetto){

        BigDecimal costoTotaleAttrezzature = null;

        String query = "SELECT Cup, SUM(Costo) CostoAttrezzature " +
                        "FROM azienda.ATTREZZATURA " +
                        "WHERE Cup = ? " +
                        "GROUP BY Cup";


        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, progetto);

            result = preparedStatement.executeQuery();

            while(result.next())
                costoTotaleAttrezzature = result.getBigDecimal("CostoAttrezzature");

            //Chiudo le istanze aperte
            result.close();
            connessione.close();

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return costoTotaleAttrezzature;
    }

    /**
     * Ottiene il costo totale dei dipendenti a progetto ingaggiati da un progetto specifico.
     *
     * @param progetto {@link String}       Il CUP unico del progetto di cui si vuole calcolare il costo totale dei dipendenti a progetto ingaggiati.
     * @return         {@link BigDecimal}   Il costo totale dei dipendenti a progetto ingaggiati dal progetto o null se non sono presenti ingaggi.
     */
    @Override
    public BigDecimal getCostoTotaleDipendentiProgetto(String progetto){

        BigDecimal costoTotaleDipendentiProgetto = null;

        String query = "SELECT Cup, SUM(Costo) CostoDipendenti " +
                        "FROM azienda.DIP_PROGETTO " +
                        "WHERE Cup = ? " +
                        "GROUP BY Cup";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, progetto);

            result = preparedStatement.executeQuery();

            while(result.next())
                costoTotaleDipendentiProgetto = result.getBigDecimal("CostoDipendenti");

            //Chiudo le istanze aperte
            result.close();
            connessione.close();

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return costoTotaleDipendentiProgetto;
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

            //gli ultimi due campi saranno destinati ad impostare i costi a 0
            for (int j = 1; j < columns; j++) {

                if (result.getString(j) == null && (j == columns - 2 || j == columns - 1))
                    str[j] = "0";
                else
                    str[j] = result.getString(j);
            }

            resultMatrix.add(str);
        }

        return resultMatrix;
    }
}
