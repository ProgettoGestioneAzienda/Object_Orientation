package DAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti i laboratori presenti in un qualsiasi database.
 */
public interface LaboratorioDAO {

    //CUD
    /**
     * Aggiunge un nuovo laboratorio al database.
     *
     * @param nome                              {@link String}  Il nome unico del laboratorio.
     * @param topic                             {@link String}  Il topic o campo di ricerca del laboratorio.
     * @param matricolaResponsabileScientifico  {@link String}  La matricola del responsabile scientifico del laboratorio.
     */
    void addLaboratorio (String nome, String topic, String matricolaResponsabileScientifico);

    /**
     * Rimuove un laboratorio dal database in base al nome specificato.
     *
     * @param nome  {@link String}  Il nome unico del laboratorio da rimuovere.
     */
    void removeLaboratorio (String nome);

    /**
     * Aggiorna i dettagli di un laboratorio esistente nel database.
     *
     * @param vecchioNome                       {@link String}  Il nome unico corrente del laboratorio da aggiornare.
     * @param nome                              {@link String}  Il nuovo nome unico del laboratorio da aggiornare.
     * @param topic                             {@link String}  Il nuovo topic o campo di ricerca del laboratorio da aggiornare.
     * @param matricolaResponsabileScientifico  {@link String}  La matricola del nuovo responsabile scientifico del laboratorio da aggiornare.
     */
    void updateLaboratorio (String vecchioNome, String nome, String topic, String matricolaResponsabileScientifico);

    // QUERY
    /**
     * Ottiene informazioni sui laboratori nel database, inclusi i nomi, i topics e i responsabili scientifici.
     *
     * @param nomi                          {@link ArrayList<String>}  Una lista in cui verranno aggiunti i nomi unici dei laboratori da leggere.
     * @param topics                        {@link ArrayList<String>}  Una lista in cui verranno aggiunti i topics dei laboratori da leggere.
     * @param stringResponsabiliScientifici {@link ArrayList<String>}  Una lista in cui verranno aggiunti i nomi dei responsabili scientifici dei laboratori da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    void obtainLaboratori(ArrayList<String> nomi, ArrayList<String> topics, ArrayList<String> stringResponsabiliScientifici) throws SQLException;

    /**
     * Ottiene una lista di laboratori che non lavorano attualmente per il progetto specificato.
     *
     * @param progettoCup   {@link String}  Il CUP unico del progetto per cui cercare laboratori candidati.
     *
     * @return {@link ArrayList}   Una lista di laboratori candidati a lavorare per il progetto specificato.
     */
    ArrayList<String[]> getLaboratoriCandidati(String progettoCup);

    /**
     * Ottiene una lista di laboratori che attualmente lavorano sul progetto specifico.
     *
     * @param progettoCup   {@link String}  Il CUP unico del progetto per cui cercare laboratori lavoranti.
     *
     * @return {@link ArrayList}   Una lista di informazioni sui laboratori che stanno lavorando atturalmente sul progetto specifico.
     */
    ArrayList<String[]> getLaboratoriLavoranti(String progettoCup);

    /**
     * Ottiene informazioni sui laboratori nel database, inclusi i nomi, i topics, i responsabili scientifici e il numero di afferenti.
     *
     * @return {@link ArrayList}    Una lista di informazioni sui laboratori, con i rispettivi nomi, topics, responsabili scientifici e il numero di afferenti.
     */
    ArrayList<String[]> getLaboratori();

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
