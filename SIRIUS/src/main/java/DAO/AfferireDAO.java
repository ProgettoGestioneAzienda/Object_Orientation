package DAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti le afferenze tra dipendenti indeterminati e laboratori presenti in un qualsiasi database.
 */
public interface AfferireDAO {

    //CUD
    /**
     * Aggiunge un'afferenza di un dipendente indeterminato ad un laboratorio nell'azienda.
     *
     * @param matricola {@link String}  La matricola del dipendente da associare.
     * @param nomeLab   {@link String}  Il nome del laboratorio da associare.
     */
    void addAfferenza(String matricola, String nomeLab);

    /**
     * Rimuove un'afferenza di un dipendente indeterminato ad un laboratorio dall'azienda.
     *
     * @param matricola {@link String}  La matricola del dipendente di cui rimuovere l'associazione.
     * @param nomeLab   {@link String}  Il nome del laboratorio di cui rimuovere l'associazione.
     */
    void removeAfferenza(String matricola, String nomeLab);

    /**
     * Aggiorna un'afferenza di un dipendente indeterminato ad un laboratorio nell'azienda.
     *
     * @param vecchiaMatricola  {@link String}  La matricola corrente del dipendente da aggiornare.
     * @param vecchioNomeLab    {@link String}  Il nome corrente del laboratorio da aggiornare.
     * @param matricola         {@link String}  La nuova matricola del dipendente da aggiornare.
     * @param nomeLab           {@link String}  Il nuovo nome del laboratorio da aggiornare.
     */
    void updateAfferenza(String vecchiaMatricola, String vecchioNomeLab, String matricola, String nomeLab);

    //QUERY
    /**
     * Ottiene tutte le afferenze tra dipendenti indeterminati e laboratori nell'azienda.
     *
     * @param dipendenti    {@link ArrayList<String>}   ArrayList in cui verranno inserite le matricole dei dipendenti associati.
     * @param laboratori    {@link ArrayList<String>}   ArrayList in cui verranno inseriti i nomi dei laboratori associati.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    void obtainAfferenze(ArrayList<String> dipendenti, ArrayList<String> laboratori) throws SQLException;

    /**
     * Restituisce, sottoforma di matrice di stringhe, tutte le afferenze tra dipendenti indeterminati e laboratori registrate nell'azienda.
     *
     * @return  {@link ArrayList}  Un ArrayList di array di stringhe, rappresentante tutte le afferenze. Ogni array contiene la matricola e il nome del laboratorio.
     */
    ArrayList<String[]> getAfferenze();

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
