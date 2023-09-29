package ImplementazionePostgresDAO;

import Database.ConnessioneDatabase;
import DAO.DipendenteIndeterminatoDAO;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia DipendenteIndeterminatoDAO e fornisce metodi per la manipolazione di dati riguardanti i dipendenti indeterminati presenti nel database Postgres.
 */
public class DipendenteIndeterminatoPostgresDAO implements DipendenteIndeterminatoDAO {
    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public DipendenteIndeterminatoPostgresDAO() {

        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


    //CUD
    /**
     * Aggiunge un dipendente a tempo indeterminato al database.
     *
     * @param nome              {@link String}      Il nome del dipendente.
     * @param cognome           {@link String}      Il cognome del dipendente.
     * @param codFiscale        {@link String}      Il codice fiscale del dipendente.
     * @param matricola         {@link String}      La matricola del dipendente.
     * @param tipoDipendente    {@link String}      Il tipo di dipendente (Junior, Middle, Senior).
     * @param indirizzo         {@link String}      L'indirizzo del dipendente. Può essere nullo.
     * @param dataNascita       {@link LocalDate}   La data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}   La data di assunzione del dipendente.
     * @param dataFine          {@link LocalDate}   La data di fine del dipendente. Può essere nulla.
     * @param dirigente                             True se il dipendente è un dirigente, altrimenti False.
     */
    @Override
    public void addDipendenteIndeterminato (String nome, String cognome, String codFiscale, String matricola, String tipoDipendente, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate dataFine, boolean dirigente){

        String query = "INSERT INTO azienda.DIP_INDETERMINATO(Matricola, Tipo, Nome, Cognome, CodFiscale, Indirizzo, DataNascita, DataAssunzione, DataFine, Dirigente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, tipoDipendente);
            preparedStatement.setString(3, nome);
            preparedStatement.setString(4, cognome);
            preparedStatement.setString(5, codFiscale);

            if(indirizzo == null)
                preparedStatement.setString(6, null);
            else
                preparedStatement.setString(6, indirizzo);

            preparedStatement.setDate(7, Date.valueOf(dataNascita));
            preparedStatement.setDate(8, Date.valueOf(dataAssunzione));

            if (dataFine == null)
                preparedStatement.setDate(9, null);
            else
                preparedStatement.setDate(9, Date.valueOf(dataFine));

            preparedStatement.setBoolean(10, dirigente);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna le informazioni di un dipendente a tempo indeterminato nel database.
     *
     * @param vecchiaMatricola  {@link String}      La matricola corrente del dipendente da aggiornare.
     * @param nome              {@link String}      Il nuovo nome del dipendente da aggiornare.
     * @param cognome           {@link String}      Il nuovo cognome del dipendente da aggiornare.
     * @param codFiscale        {@link String}      Il nuovo codice fiscale del dipendente da aggiornare.
     * @param matricola         {@link String}      La nuova matricola del dipendente da aggiornare.
     * @param tipoDipendente    {@link String}      Il nuovo tipo di dipendente (Junior, Middle, Senior).
     * @param indirizzo         {@link String}      Il nuovo indirizzo del dipendente da aggiornare. Può essere nullo.
     * @param dataNascita       {@link LocalDate}   La nuova data di nascita del dipendente da aggiornare.
     * @param dataAssunzione    {@link LocalDate}   La nuova data di assunzione del dipendente da aggiornare.
     * @param dataFine          {@link LocalDate}   La nuova data di fine del dipendente da aggiornare. Può essere nulla.
     * @param dirigente                             True se il dipendente da aggiornare è un dirigente, altrimenti False.
     */
    @Override
    public void updateDipendenteIndeterminato (String vecchiaMatricola, String nome, String cognome, String codFiscale, String matricola, String tipoDipendente, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate dataFine, boolean dirigente) {

        String query = "UPDATE azienda.DIP_INDETERMINATO SET Matricola = ?, Tipo = ?, Nome = ?, Cognome = ?, codFiscale = ?, Indirizzo = ?, dataNascita = ?, dataAssunzione = ?, dataFine = ?, Dirigente = ? WHERE Matricola = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, tipoDipendente);
            preparedStatement.setString(3, nome);
            preparedStatement.setString(4, cognome);
            preparedStatement.setString(5, codFiscale);

            if (indirizzo == null)
                preparedStatement.setString(6, null);
            else
                preparedStatement.setString(6, indirizzo);

            preparedStatement.setDate(7, Date.valueOf(dataNascita));
            preparedStatement.setDate(8, Date.valueOf(dataAssunzione));

            if (dataFine == null)
                preparedStatement.setDate(9, null);
            else
                preparedStatement.setDate(9, Date.valueOf(dataFine));

            preparedStatement.setBoolean(10, dirigente);

            preparedStatement.setString(11, vecchiaMatricola);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna lo stato dirigenziale del dipendente a tempo indeterminato specificato.
     *
     * @param matricola {@link String}  La matricola corrente del dipendente indeterminato.
     * @param dirigente {@link String}  Il nuovo stato dirigenziale che si desidera impostare.
     */
    @Override
    public void updateStatoDirigente(String matricola, boolean dirigente){

        String query = "UPDATE azienda.DIP_INDETERMINATO SET Dirigente = ? WHERE Matricola = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setBoolean(1, dirigente);
            preparedStatement.setString(2, matricola);

            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    /**
    * Aggiorna il tipo del dipendente a tempo indeterminato specificato.
     *
     * @param matricola          {@link String} La matricola corrente del dipendente indeterminato.
     * @param tipoDipendente     {@link String} Il nuovo tipo in aggiornamento.
    */
    @Override
    public void updateTipoDipendente(String matricola, String tipoDipendente){

        String query = "UPDATE azienda.DIP_INDETERMINATO SET Tipo = ? WHERE Matricola = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, tipoDipendente);
            preparedStatement.setString(2, matricola);

            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna i dati anagrafici del dipendente a tempo indeterminato specificato.
     *
     * @param vecchioCodFiscale {@link String}      Il codice fiscale corrente del dipendente da aggiornare.
     * @param nome              {@link String}      Il nuovo nome del dipendente indeterminato da aggiornare.
     * @param cognome           {@link String}      Il nuovo cognome del dipendente indeterminato da aggiornare.
     * @param codFiscale        {@link String}      Il nuovo codice fiscale del dipendente indeterminato da aggiornare.
     * @param indirizzo         {@link String}      Il nuovo indirizzo del dipendente indeterminato da aggiornare.
     * @param dataNascita       {@link LocalDate}   La nuova data di nascita del dipendente indeterminato da aggiornare.
     * */
    @Override
    public void updateDatiAnagraficiDipendente(String vecchioCodFiscale, String nome, String cognome, String codFiscale, String indirizzo, LocalDate dataNascita){

        String query = "UPDATE azienda.DIP_INDETERMINATO SET Nome = ?, Cognome = ?, CodFiscale = ?, Indirizzo = ?, DataNascita = ? WHERE CodFiscale = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cognome);
            preparedStatement.setString(3, codFiscale);

            if (indirizzo == null)
                preparedStatement.setString(4, null);
            else
                preparedStatement.setString(4, indirizzo);

            preparedStatement.setDate(5, Date.valueOf(dataNascita));

            preparedStatement.setString(6, vecchioCodFiscale);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna la data di assunzione del dipendente a tempo indeterminato specificato.
     *
     * @param matricola             {@link String}      La matricola corrente del dipendente indeterminato da aggiornare.
     * @param nuovaDataAssunzione   {@link LocalDate}   La nuova data assunzione del dipendente indeterminato da aggiornare.
     * */
    @Override
    public void updateDataAssunzione(String matricola, LocalDate nuovaDataAssunzione){

        String query = "UPDATE azienda.DIP_INDETERMINATO SET DataAssunzione = ? WHERE Matricola = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setDate(1, Date.valueOf(nuovaDataAssunzione));
            preparedStatement.setString(2, matricola);

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
     * Ottiene le informazioni sui dipendenti a tempo indeterminato dal database e le inserisce in liste separate.
     *
     * @param nomi              {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i nomi dei dipendenti indeterminati da leggere.
     * @param cognomi           {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i cognomi dei dipendenti indeterminati da leggere.
     * @param codFiscali        {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i codici fiscali dei dipendenti indeterminati da leggere.
     * @param matricole         {@link ArrayList<String>}       Una lista vuota in cui verranno inserite le matricole dei dipendenti indeterminati da leggere.
     * @param tipiDipendente    {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i tipi di dipendente dei dipendenti indeterminati da leggere.
     * @param indirizzi         {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti gli indirizzi dei dipendenti indeterminati da leggere. Può contenere valori nulli.
     * @param dateNascita       {@link ArrayList<LocalDate>}    Una lista vuota in cui verranno inserite le date di nascita dei dipendenti indeterminati da leggere.
     * @param dateAssunzioni    {@link ArrayList<LocalDate>}    Una lista vuota in cui verranno inserite le date di assunzione dei dipendenti indeterminati. da leggere
     * @param dateFine          {@link ArrayList<LocalDate>}    Una lista vuota in cui verranno inserite le date di fine dell'impiego dei dipendenti indeterminati da leggere. Può contenere valori nulli.
     * @param dirigenti         {@link ArrayList}               Una lista vuota in cui verranno inseriti i flag che indicano se un dipendente da leggere è dirigente o meno.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainDipendentiIndeterminati(ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> codFiscali,
                                              ArrayList<String> matricole, ArrayList<String> tipiDipendente, ArrayList<String> indirizzi,
                                              ArrayList<LocalDate> dateNascita, ArrayList<LocalDate> dateAssunzioni, ArrayList<LocalDate> dateFine, ArrayList<Boolean> dirigenti) throws SQLException {

        String query = "SELECT * FROM azienda.DIP_INDETERMINATO";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();


            while (result.next()) {

                nomi.add(result.getString("Nome"));
                cognomi.add(result.getString("Cognome"));
                codFiscali.add(result.getString("CodFiscale"));
                matricole.add(result.getString("Matricola"));
                tipiDipendente.add(result.getString("Tipo"));

                //lavorazione di indirizzo
                String indirizzo = result.getString("Indirizzo");
                if (indirizzo == null)
                    indirizzi.add(null);
                else indirizzi.add(indirizzo);

                dateNascita.add(result.getDate("DataNascita").toLocalDate());
                dateAssunzioni.add(result.getDate("DataAssunzione").toLocalDate());

                //lavorazione di dataFine
                Date dataFine = result.getDate("DataFine");
                if (dataFine == null)
                    dateFine.add(null);
                else dateFine.add(dataFine.toLocalDate());

                dirigenti.add(result.getBoolean("Dirigente"));

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
     * Ottiene tutti i dipendenti a tempo indeterminato dal database e restituisce una matrice di stringhe.
     *
     * @return  {@link ArrayList} Una lista di informazioni rappresentanti tutti i dipendenti nel database.
     */
    @Override
    public ArrayList<String[]> getDipendentiIndeterminati() {

        String query = "SELECT * FROM azienda.DIP_INDETERMINATO";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Recupera tutti i dipendenti a tempo indeterminato senior candidati al ruolo di responsabile scientifico per un laboratorio.
     *
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al ruolo specificato.
     */
    @Override
    public ArrayList<String[]> getAllDipendentiCandidatiResponsabileScientifico() {

        String query = "SELECT * FROM azienda.DIP_INDETERMINATO WHERE UPPER(Tipo) = 'SENIOR' AND dataFine is null";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
            connessione.close();

        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Recupera tutti i dipendenti a tempo indeterminato senior candidati al ruolo di referente scientifico per un progetto.
     *
     * @param dataInizioProgetto {@link LocalDate} La data di inizio del progetto.
     * @param dataFineProgetto {@link LocalDate} La data di fine del progetto.
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al ruolo specificato.
     */
    @Override
    public ArrayList<String[]> getAllDipendentiCandidatiReferenteScientifico(LocalDate dataInizioProgetto, LocalDate dataFineProgetto) {

        String query;
        boolean dataFineProgettoIsPresent;

        if (dataFineProgetto == null){
            query = "SELECT * FROM azienda.DIP_INDETERMINATO WHERE (dataAssunzione + INTERVAL '7 years' <= ?) AND dataFine is null";
            dataFineProgettoIsPresent = false;
        } else {
            query = "SELECT * FROM azienda.DIP_INDETERMINATO WHERE (dataAssunzione + INTERVAL '7 years' <= ?) AND (dataFine IS NULL OR (? <= dataFine AND CURRENT_DATE > dataFine))";
            dataFineProgettoIsPresent = true;
        }

        try{
            preparedStatement = connessione.prepareStatement(query);

            if (dataFineProgettoIsPresent) {

                preparedStatement.setDate(1, Date.valueOf(dataInizioProgetto));
                preparedStatement.setDate(2, Date.valueOf(dataFineProgetto));

            } else {
                preparedStatement.setDate(1, Date.valueOf(dataInizioProgetto));
            }

            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
            connessione.close();

        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Recupera tutti i dipendenti a tempo indeterminato dirigenti candidati al ruolo di responsabile per un progetto.
     * @param dataInizioProgetto {@link LocalDate} La data di inizio del progetto.
     * @param dataFineProgetto {@link LocalDate} La data di fine del progetto.
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al ruolo specificato.
     */
    @Override
    public ArrayList<String[]> getAllDipendentiCandidatiDirigente(LocalDate dataInizioProgetto, LocalDate dataFineProgetto) {

        String query;
        boolean dataFineProgettoIsPresent;

        if (dataFineProgetto == null){
            query = "SELECT * FROM azienda.DIP_INDETERMINATO WHERE dirigente = true AND (dataAssunzione <= ?) AND dataFine is null";
            dataFineProgettoIsPresent = false;
        } else {
            query = "SELECT * FROM azienda.DIP_INDETERMINATO WHERE dirigente = true AND (dataAssunzione <= ?) AND (dataFine IS NULL OR (? <= dataFine AND CURRENT_DATE > dataFine))";
            dataFineProgettoIsPresent = true;
        }

        try{
            preparedStatement = connessione.prepareStatement(query);

            if (dataFineProgettoIsPresent) {

                preparedStatement.setDate(1, Date.valueOf(dataInizioProgetto));
                preparedStatement.setDate(2, Date.valueOf(dataFineProgetto));
            } else
                preparedStatement.setDate(1, Date.valueOf(dataInizioProgetto));

            result = preparedStatement.executeQuery();

            //Converto in matrice
            metaData = result.getMetaData();
            resultMatrix = createMatrix(metaData, result);

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
            connessione.close();

        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return resultMatrix;
    }

    /**
     * Recupera tutti i dipendenti a tempo indeterminato che, se facessero il tipo di scatto passato in input, non andrebbero in uno stato illegale.
     *
     * @param tipo  {@link String}  Il tipo di scatto considerato.
     *
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al tipo di scatto specificato.
     */
    @Override
    public ArrayList<String[]> getDipendentiCandidatiScatto(String tipo) {

        try{
            if(tipo.equals("Middle") || tipo.equals("Senior")) {

                preparedStatement = connessione.prepareStatement("SELECT * FROM azienda.DIP_INDETERMINATO WHERE UPPER(Tipo) = ?");

                preparedStatement.setString(1, tipo.toUpperCase());

            } else if (tipo.equals("Promosso_a_dirigente") || tipo.equals("Rimosso_da_dirigente")) {

                preparedStatement = connessione.prepareStatement("SELECT * FROM azienda.DIP_INDETERMINATO WHERE Dirigente = ?");

                if (tipo.equals("Promosso_a_dirigente"))
                    preparedStatement.setBoolean(1, false);
                else
                    preparedStatement.setBoolean(1, true);
            }

            result = preparedStatement.executeQuery();
            metaData = result.getMetaData();

            resultMatrix = createMatrix(metaData, result);

            //Chiudo il resultSet
            result.close();

            //Chiudo la connessione
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
