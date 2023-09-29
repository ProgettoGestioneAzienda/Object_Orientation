package DAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti le istanze di lavoro tra laboratori e progetti presenti in un qualsiasi database.
 */
public interface LavorareDAO {

    //CUD
    /**
     * Aggiunge una nuova istanza di lavoro tra un progetto ed un laboratorio.
     *
     * @param cup               {@link String}      Il CUP del progetto.
     * @param nomeLaboratorio   {@link String}      Il nome del laboratorio.
     */
    void addLavorare(String cup, String nomeLaboratorio);

    /**
     * Rimuove un'istanza di lavoro tra un progetto e un laboratorio.
     *
     * @param cup               {@link String}  Il CUP del progetto da specificare per la rimozione.
     * @param nomeLaboratorio   {@link String}  Il nome del laboratorio da specificare per la rimozione.
     */
    void removeLavorare(String cup, String nomeLaboratorio);

    /**
     * Aggiorna un'istanza di lavoro tra un progetto e un laboratorio.
     *
     * @param vecchioCup                {@link String}  Il CUP attuale del progetto da aggiornare.
     * @param vecchioNomeLaboratorio    {@link String}  Il nome attuale del laboratorio da aggiornare.
     * @param cup                       {@link String}  Il nuovo CUP del progetto.
     * @param nomeLaboratorio           {@link String}  Il nuovo nome del laboratorio.
     */
    void updateLavorare(String vecchioCup, String vecchioNomeLaboratorio, String cup, String nomeLaboratorio);

    //QUERY
    /**
     * Ottiene tutte le istanze di lavoro dal database e le inserisce nelle liste specificate.
     *
     * @param progetti      {@link String}  Una lista in cui verranno inseriti i CUP dei progetti da leggere.
     * @param laboratori    {@link String}  Una lista in cui verranno inseriti i nomi dei laboratori da leggere.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    void obtainLavorare(ArrayList<String> progetti, ArrayList<String> laboratori) throws SQLException;

    /**
     * Ottiene tutte le istanze di lavoro correnti dal database.
     *
     * @return {@link ArrayList}   Una lista di informazioni rappresentanti le istanze di lavoro tra progetti e laboratori.
     */
    ArrayList<String[]> getLavorare();

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
