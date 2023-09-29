package ImplementazionePostgresDAO;

import DAO.DipendenteProgettoDAO;
import Database.ConnessioneDatabase;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Questa classe implementa l'interfaccia DipendenteProgettoDAO e fornisce metodi per la manipolazione di dati riguardanti i dipendenti a progetto recuperati dal database Postgres.
 */
public class DipendenteProgettoPostgresDAO implements DipendenteProgettoDAO {
    private Connection connessione;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet result = null;
    private ArrayList<String[]> resultMatrix = null;

    /**
     * Costruttore della classe che richiede un'istanza di connessione al database Postgres tramite la classe ConnessioneDatabase.
     */
    public DipendenteProgettoPostgresDAO(){
        try{
            connessione = ConnessioneDatabase.getInstance().getConnection();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


    //CUD
    /**
     * Aggiunge un dipendente a progetto ingaggiato da un progetto all'archivio dei dipendenti.
     *
     * @param nome              {@link String}      Il nome del dipendente.
     * @param cognome           {@link String}      Il cognome del dipendente.
     * @param codFiscale        {@link String}      Il codice fiscale del dipendente.
     * @param matricola         {@link String}      La matricola del dipendente.
     * @param indirizzo         {@link String}      L'indirizzo del dipendente. Può essere null se non specificato.
     * @param dataNascita       {@link LocalDate}   La data di nascita del dipendente.
     * @param dataAssunzione    {@link LocalDate}   La data di assunzione del dipendente.
     * @param scadenza          {@link LocalDate}   La data di scadenza del contratto del dipendente.
     * @param cup               {@link String}      Il CUP unico del progetto che ingaggia il dipendente.
     * @param costo             {@link BigDecimal}  Il costo del contratto del dipendente.
     */
    @Override
    public void addDipendenteProgetto (String nome, String cognome, String codFiscale, String matricola, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza, String cup, BigDecimal costo) {

        String query = "INSERT INTO azienda.DIP_PROGETTO(Matricola, Nome, Cognome, codfiscale, Indirizzo, dataNascita, dataAssunzione, Scadenza, CUP, Costo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, cognome);
            preparedStatement.setString(4, codFiscale);

            if (indirizzo == null)
                preparedStatement.setString(5, null);
            else
                preparedStatement.setString(5, indirizzo);

            preparedStatement.setDate(6, Date.valueOf(dataNascita));
            preparedStatement.setDate(7, Date.valueOf(dataAssunzione));
            preparedStatement.setDate(8, Date.valueOf(scadenza));
            preparedStatement.setString(9, cup);
            preparedStatement.setBigDecimal(10, costo);

            preparedStatement.executeUpdate();

            //Chiudo la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    /**
     * Aggiorna i dati di un dipendente a progetto ingaggiato da un progetto nell'archivio dei dipendenti.
     *
     * @param vecchiaMatricola  {@link String}      La matricola corrente del dipendente da aggiornare.
     * @param nome              {@link String}      Il nuovo nome del dipendente da aggiornare.
     * @param cognome           {@link String}      Il nuovo cognome del dipendente da aggiornare.
     * @param codFiscale        {@link String}      Il nuovo codice fiscale del dipendente da aggiornare.
     * @param matricola         {@link String}      La nuova matricola del dipendente da aggiornare.
     * @param indirizzo         {@link String}      Il nuovo indirizzo del dipendente da aggiornare. Può essere nullo.
     * @param dataNascita       {@link LocalDate}   La nuova data di nascita del dipendente da aggiornare.
     * @param dataAssunzione    {@link LocalDate}   La nuova data di assunzione del dipendente da aggiornare.
     * @param scadenza          {@link LocalDate}   La nuova data di scadenza del contratto del dipendente da aggiornare.
     * @param cup               {@link String}      Il CUP unico del nuovo progetto da cui il dipendente da aggiornare è stato ingaggiato.
     * @param costo             {@link BigDecimal}  Il nuovo costo del contratto del dipendente da aggiornare.
     */
    @Override
    public void updateDipendenteProgetto (String vecchiaMatricola, String nome, String cognome, String codFiscale, String matricola, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza, String cup, BigDecimal costo) {

        String query = "UPDATE azienda.DIP_PROGETTO SET Matricola = ?, Nome = ?, Cognome = ?, codFiscale = ?, Indirizzo = ?, dataNascita = ?, dataAssunzione = ?, Scadenza = ?, CUP = ?, Costo = ? WHERE Matricola = ?";

        try{
            preparedStatement = connessione.prepareStatement(query);

            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, cognome);
            preparedStatement.setString(4, codFiscale);

            if (indirizzo == null)
                preparedStatement.setString(5, null);
            else
                preparedStatement.setString(5, indirizzo);

            preparedStatement.setDate(6, Date.valueOf(dataNascita));
            preparedStatement.setDate(7, Date.valueOf(dataAssunzione));
            preparedStatement.setDate(8, Date.valueOf(scadenza));
            preparedStatement.setString(9, cup);
            preparedStatement.setBigDecimal(10, costo);

            preparedStatement.setString(11, vecchiaMatricola);

            preparedStatement.executeUpdate();

            // Si chiude la connessione
            connessione.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    /**
     * Aggiorna i dati anagrafici di un dipendente a progetto nel database.
     *
     * @param vecchioCodFiscale {@link String}      Il codice fiscale corrente del dipendente da aggiornare.
     * @param nome              {@link String}      Il nuovo nome del dipendente da aggiornare.
     * @param cognome           {@link String}      Il nuovo cognome del dipendente da aggiornare.
     * @param codFiscale        {@link String}      Il nuovo codice fiscale del dipendente da aggiornare.
     * @param indirizzo         {@link String}      Il nuovo indirizzo del dipendente da aggiornare (può essere null).
     * @param dataNascita       {@link LocalDate}   La nuova data di nascita del dipendente da aggiornare.
     */
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

    //QUERY

    /**
     * Recupera i dati di tutti i dipendenti a progetto ingaggiati da progetti dall'archivio dei dipendenti.
     *
     * @param nomi             {@link ArrayList<String>}        Una lista in cui verranno inseriti i nomi dei dipendenti da leggere.
     * @param cognomi          {@link ArrayList<String>}        Una lista in cui verranno inseriti i cognomi dei dipendenti da leggere.
     * @param codFiscali       {@link ArrayList<String>}        Una lista in cui verranno inseriti i codici fiscali dei dipendenti da leggere.
     * @param matricole        {@link ArrayList<String>}        Una lista in cui verranno inserite le matricole dei dipendenti da leggere.
     * @param indirizzi        {@link ArrayList<String>}        Una lista in cui verranno inseriti gli indirizzi dei dipendenti da leggere. Possono essere nulli.
     * @param dateNascita      {@link ArrayList<LocalDate>}     Una lista in cui verranno inserite le date di nascita dei dipendenti da leggere.
     * @param dateAssunzioni   {@link ArrayList<LocalDate>}     Una lista in cui verranno inserite le date di assunzione dei dipendenti da leggere.
     * @param scadenze         {@link ArrayList<LocalDate>}     Una lista in cui verranno inserite le date di scadenza dei contratti dei dipendenti da leggere.
     * @param costiContratti   {@link ArrayList<BigDecimal>}    Una lista in cui verranno inseriti i costi dei contratti dei dipendenti da leggere.
     * @param progetti         {@link ArrayList<String>}        Una lista in cui verranno inseriti i CUP unici dei progetti che ingaggiano da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    @Override
    public void obtainDipendentiProgetto(ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> codFiscali,
                                         ArrayList<String> matricole, ArrayList<String> indirizzi,
                                         ArrayList<LocalDate> dateNascita, ArrayList<LocalDate> dateAssunzioni, ArrayList<LocalDate> scadenze,
                                         ArrayList<BigDecimal> costiContratti, ArrayList<String> progetti) throws SQLException{

        String query = "SELECT * FROM azienda.DIP_PROGETTO";

        try{
            preparedStatement = connessione.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {

                nomi.add(result.getString("Nome"));
                cognomi.add(result.getString("Cognome"));
                codFiscali.add(result.getString("CodFiscale"));
                matricole.add(result.getString("Matricola"));

                //lavorazione di indirizzo
                String indirizzo = result.getString("Indirizzo");
                if (indirizzo == null)
                    indirizzi.add(null);
                else indirizzi.add(indirizzo);

                dateNascita.add(result.getDate("DataNascita").toLocalDate());
                dateAssunzioni.add(result.getDate("DataAssunzione").toLocalDate());
                scadenze.add(result.getDate("Scadenza").toLocalDate());
                costiContratti.add(result.getBigDecimal("Costo"));
                progetti.add(result.getString("Cup"));

            }

            //Chiudo le istanze aperte
            result.close();
            connessione.close();
        }
        catch (SQLException sqlException){
            throw new SQLException();
        }
    }

    /**
     * Ottiene i dati di tutti i dipendenti a progetto ingaggiati da progetti dall'archivio dei dipendenti.
     *
     * @return {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti.
     */
    @Override
    public ArrayList<String[]> getDipendentiProgetto() {

        resultMatrix = new ArrayList<>();
        String query = "SELECT * FROM azienda.DIP_PROGETTO";

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
