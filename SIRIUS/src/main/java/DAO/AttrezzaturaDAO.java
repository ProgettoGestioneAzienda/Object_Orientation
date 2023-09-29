package DAO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia che fornisce metodi per la manipolazione di dati riguardanti le attrezzature presenti in un qualsiasi database.
 */
public interface AttrezzaturaDAO {

    //CUD
    /**
     * Aggiunge un'attrezzatura, acquistata da un progetto, al database.
     *
     * @param descrizione   {@link String}      La descrizione dell'attrezzatura.
     * @param costo         {@link BigDecimal}  Il costo dell'attrezzatura.
     * @param cup           {@link String}      Il CUP unico del progetto proprietario dell'attrezzatura.
     * @param nomeLab       {@link String}      Il nome del laboratorio dove potra' risiedere l'attrezzatura (può essere null).
     */
    void addAttrezzatura (String descrizione, BigDecimal costo, String cup, String nomeLab);

    /**
     * Aggiorna un'attrezzatura nel database.
     *
     * @param idAttrezzatura    {@link String}      Identificativo corrente dell'attrezzatura da aggiornare.
     * @param descrizione       {@link String}      La nuova descrizione dell'attrezzatura da aggiornare.
     * @param costo             {@link BigDecimal}  Il nuovo costo dell'attrezzatura da aggiornare.
     * @param cup               {@link String}      Il nuovo CUP unico del progetto proprietario da aggiornare.
     * @param nomeLab           {@link String}      Il nuovo nome unico del laboratorio in cui risiede l'attrezzatura da aggiornare (può essere null).
     */
    void updateAttrezzatura(Integer idAttrezzatura, String descrizione, BigDecimal costo, String cup, String nomeLab);

    //QUERY
    /**
     * Ottiene le informazioni sulle attrezzature dal database e le inserisce in liste separate.
     *
     * @param descrizioni       {@link ArrayList<String>}       Una lista vuota in cui verranno inserite le descrizioni delle attrezzature.
     * @param costi             {@link ArrayList<BigDecimal>}   Una lista vuota in cui verranno inseriti i costi delle attrezzature.
     * @param stringProgetti    {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i nomi dei progetti proprietari delle attrezzature.
     * @param stringLaboratori  {@link ArrayList<String>}       Una lista vuota in cui verranno inseriti i nomi dei laboratori in cui potranno risiedere le attrezzature. Può contenere valori nulli.
     *
     * @throws SQLException Viene lanciata un'eccezione nel caso la definizione delle tabelle del database non e' uguale a quella prevista.
     */
    void obtainAttrezzature(ArrayList<Integer> idAttrezzature, ArrayList<String> descrizioni, ArrayList<BigDecimal> costi, ArrayList<String> stringProgetti, ArrayList<String> stringLaboratori) throws SQLException;

    /**
     * Ottiene tutte le attrezzature dal database e restituisce una matrice di stringhe.
     *
     * @return  {@link ArrayList} Una lista di informazioni rappresentanti tutte le attrezzature nel database.
     */
    ArrayList<String[]> getAttrezzature();

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
