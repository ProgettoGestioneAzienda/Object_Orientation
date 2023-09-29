package DAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti i dipendenti indeterminati presenti in un qualsiasi database.
 */
public interface DipendenteIndeterminatoDAO {

    // CUDA
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
    void addDipendenteIndeterminato(String nome, String cognome, String codFiscale, String matricola, String tipoDipendente, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate dataFine, boolean dirigente);

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
    void updateDipendenteIndeterminato(String vecchiaMatricola, String nome, String cognome, String codFiscale, String matricola, String tipoDipendente, String indirizzo, LocalDate dataNascita, LocalDate dataAssunzione, LocalDate dataFine, boolean dirigente);

    /**
     * Aggiorna lo stato dirigenziale del dipendente a tempo indeterminato specificato.
     *
     * @param matricola {@link String}  La matricola corrente del dipendente indeterminato.
     * @param dirigente {@link String}  Il nuovo stato dirigenziale che si desidera impostare.
     */
    void updateStatoDirigente(String matricola, boolean dirigente);

    /**
     * Aggiorna il tipo del dipendente a tempo indeterminato specificato.
     *
     * @param matricola          {@link String} La matricola corrente del dipendente indeterminato.
     * @param tipoDipendente     {@link String} Il nuovo tipo in aggiornamento.
     */
    void updateTipoDipendente(String matricola, String tipoDipendente);

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
    void updateDatiAnagraficiDipendente(String vecchioCodFiscale, String nome, String cognome, String codFiscale, String indirizzo, LocalDate dataNascita);

    /**
     * Aggiorna la data di assunzione del dipendente a tempo indeterminato specificato.
     *
     * @param matricola             {@link String}      La matricola corrente del dipendente indeterminato da aggiornare.
     * @param nuovaDataAssunzione   {@link LocalDate}   La nuova data assunzione del dipendente indeterminato da aggiornare.
     * */
    void updateDataAssunzione(String matricola, LocalDate nuovaDataAssunzione);

    // QUERY
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
    void obtainDipendentiIndeterminati(ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> codFiscali,
                                       ArrayList<String> matricole, ArrayList<String> tipiDipendente, ArrayList<String> indirizzi,
                                       ArrayList<LocalDate> dateNascita, ArrayList<LocalDate> dateAssunzioni, ArrayList<LocalDate> dateFine, ArrayList<Boolean> dirigenti) throws SQLException;

    /**
     * Ottiene tutti i dipendenti a tempo indeterminato dal database e restituisce una matrice di stringhe.
     *
     * @return  {@link ArrayList} Una lista di informazioni rappresentanti tutti i dipendenti nel database.
     */
    ArrayList<String[]> getDipendentiIndeterminati();

    /**
     * Recupera tutti i dipendenti a tempo indeterminato senior candidati al ruolo di responsabile scientifico per un laboratorio.
     *
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al ruolo specificato.
     */
    ArrayList<String[]> getAllDipendentiCandidatiResponsabileScientifico();

    /**
     * Recupera tutti i dipendenti a tempo indeterminato senior candidati al ruolo di referente scientifico per un progetto.
     *
     * @param dataInizioProgetto {@link LocalDate} La data di inizio del progetto.
     * @param dataFineProgetto   {@link LocalDate} La data di fine del progetto.
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al ruolo specificato.
     */
    ArrayList<String[]> getAllDipendentiCandidatiReferenteScientifico(LocalDate dataInizioProgetto, LocalDate dataFineProgetto);

    /**
     * Recupera tutti i dipendenti a tempo indeterminato dirigenti candidati al ruolo di responsabile per un progetto.
     *
     * @param dataInizioProgetto {@link LocalDate} La data di inizio del progetto.
     * @param dataFineProgetto   {@link LocalDate} La data di fine del progetto.
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al ruolo specificato.
     */
    ArrayList<String[]> getAllDipendentiCandidatiDirigente(LocalDate dataInizioProgetto, LocalDate dataFineProgetto);

    /**
     * Recupera tutti i dipendenti a tempo indeterminato che, se facessero il tipo di scatto passato in input, non andrebbero in uno stato illegale.
     *
     * @param tipo  {@link String}  Il tipo di scatto considerato.
     *
     * @return  {@link ArrayList}  Una lista di informazioni rappresentanti i dati dei dipendenti candidati al tipo di scatto specificato.
     */
    ArrayList<String[]> getDipendentiCandidatiScatto(String tipo);

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
