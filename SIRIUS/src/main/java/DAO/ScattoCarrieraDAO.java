package DAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti gli scatti di carriera presenti in un qualsiasi database.
 */
public interface ScattoCarrieraDAO {

    //CUD
    /**
     * Aggiunge un nuovo scatto di carriera, effettuato dal dipendente a tempo indeterminato specificato, al database.
     *
     * @param tipoScatto    {@link String}      Il tipo di scatto di carriera.
     * @param matricola     {@link String}      La matricola del dipendente che ha effettuato lo scatto di carriera.
     * @param data          {@link LocalDate}   La data in cui è stato effettuato lo scatto di carriera.
     */
    void addScattoCarriera(String tipoScatto, String matricola, LocalDate data);

    /**
     * Rimuove uno scatto di carriera di tipo middle o senior, effettuato dal dipendente a tempo indeterminato specificato, dal database.
     *
     * @param tipoScatto    {@link String}      Il tipo di scatto di carriera da rimuovere, scelto tra middle o senior.
     * @param matricola     {@link String}      La matricola del dipendente che ha effettuato lo scatto da rimuovere.
     */
    void removeScattoCarriera(String tipoScatto, String matricola);

    /**
     * Rimuove tutti gli scatti di carriera, effettuati da un dipendente a tempo indeterminato specifico, dal database.
     *
     * @param matricola     {@link String}      La matricola del dipendente del quale si intendono rimuovere gli scatti di carriera.
     */
    void removeAllScattiCarrieraDipendente(String matricola);

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
    void updateScattoCarriera(String vecchioTipoScatto, String vecchiaMatricola, LocalDate vecchiaData, String tipoScatto, String matricola, LocalDate data);

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
    void obtainScattiCarriera(ArrayList<String> tipiScatto, ArrayList<String> stringDipendenti, ArrayList<LocalDate> date) throws SQLException;

    /**
     * Ottiene tutti gli scatti di carriera, effettuati da dipendenti a tempo indeterminato, dal database.
     *
     * @return {@link ArrayList}    Una lista di informazioni rappresentanti tutti gli scatti di carriera.
     */
    ArrayList<String[]> getScattiCarriera();

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
