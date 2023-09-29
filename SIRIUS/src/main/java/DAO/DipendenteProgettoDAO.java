package DAO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti i dipendenti a progetto presenti in un qualsiasi database.
 */
public interface DipendenteProgettoDAO {

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
    void addDipendenteProgetto(String nome, String cognome, String codFiscale, String matricola, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza, String cup, BigDecimal costo);

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
    void updateDipendenteProgetto(String vecchiaMatricola, String nome, String cognome, String codFiscale, String matricola, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate scadenza, String cup, BigDecimal costo);

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
    void updateDatiAnagraficiDipendente(String vecchioCodFiscale, String nome, String cognome, String codFiscale, String indirizzo, LocalDate dataNascita);

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
    void obtainDipendentiProgetto(ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> codFiscali,
                                         ArrayList<String> matricole, ArrayList<String> indirizzi,
                                         ArrayList<LocalDate> dateNascita, ArrayList<LocalDate> dateAssunzioni, ArrayList<LocalDate> scadenze,
                                         ArrayList<BigDecimal> costiContratti, ArrayList<String> progetti) throws SQLException;

    /**
     * Ottiene i dati di tutti i dipendenti a progetto ingaggiati da progetti dall'archivio dei dipendenti.
     *
     * @return {@link ArrayList}   Una lista di informazioni rappresentanti i dati dei dipendenti.
     */
    ArrayList<String[]> getDipendentiProgetto();

    //Metodo utilizzato per convertire un resultSet in matrice
    /**
     * Converte un ResultSet in una matrice di stringhe, costituita dai dati recuperati mediante una query.
     *
     * @param metaData      {@link ResultSetMetaData}       Il meta-data del ResultSet.
     * @param result        {@link ResultSet}               Il ResultSet contenente i dati.
     * @return              {@link ArrayList}               Una matrice di stringhe contenente i dati ottenuti dalla query.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati nel ResultSet.
     */
    ArrayList<String[]> createMatrix(ResultSetMetaData metaData, ResultSet result) throws SQLException;
}
