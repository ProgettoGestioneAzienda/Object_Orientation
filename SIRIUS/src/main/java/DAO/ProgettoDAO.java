package DAO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti i progetti presenti in un qualsiasi database.
 */
public interface ProgettoDAO {

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
    void addProgetto (String nome, String cup, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine, String referenteScientifico, String responsabile);

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
    void updateProgetto (String vecchioCup, String nome, String cup, BigDecimal budget, LocalDate dataInizio, LocalDate dataFine, String referenteScientifico, String responsabile);

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
    void obtainProgetti(ArrayList<String> nomi, ArrayList<String> cups, ArrayList<BigDecimal> budgets, ArrayList<LocalDate> dateInizio,
                        ArrayList<LocalDate> dateFine, ArrayList<String> stringReferentiScientifici, ArrayList<String> stringResponsabili) throws SQLException;

    /**
     * Recupera informazioni sui progetti dal database.
     *
     * @return {@link ArrayList}   Una lista di informazioni rappresentanti le informazioni sui progetti.
     */
    ArrayList<String[]> getProgetti();

    /**
     * Recupera informazioni sui progetti non terminati dal database.
     *
     * @return {@link ArrayList}    Una lista di informazioni rappresentanti i progetti non terminati.
     */
    ArrayList<String[]> getProgettiNonTerminati();

    /**
     * Ottiene il costo totale delle attrezzature acquistate da un progetto specifico.
     *
     * @param progetto  {@link String}      Il CUP unico  del progetto di cui si desidera calcolare il costo totale delle attrezzature acquistate.
     * @return          {@link BigDecimal}  Il costo totale delle attrezzature acquistate dal progetto o null se non e' presente nessun acquisto.
     */
    BigDecimal getCostoTotaleAttrezzature(String progetto);

    /**
     * Ottiene il costo totale dei dipendenti a progetto ingaggiati da un progetto specifico.
     *
     * @param progetto {@link String}       Il CUP unico del progetto di cui si vuole calcolare il costo totale dei dipendenti a progetto ingaggiati.
     * @return         {@link BigDecimal}   Il costo totale dei dipendenti a progetto ingaggiati dal progetto o null se non sono presenti ingaggi.
     */
    BigDecimal getCostoTotaleDipendentiProgetto(String progetto);

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
