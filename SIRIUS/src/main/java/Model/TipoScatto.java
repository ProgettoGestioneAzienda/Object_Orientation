package Model;

/**
 * L'enum TipoScatto rappresenta i diversi tipi di scatto di carriera che un dipendente
 * può subire all'interno di un'organizzazione. I possibili valori sono Middle, Senior,
 * Promosso_a_dirigente e Rimosso_da_dirigente.
 */
public enum TipoScatto {

    /**
     * Rappresenta uno scatto di carriera di tipo Middle.
     */
    Middle,

    /**
     * Rappresenta uno scatto di carriera di tipo Senior.
     */
    Senior,

    /**
     * Rappresenta uno scatto di carriera in cui un dipendente viene promosso a un ruolo di dirigente.
     */
    Promosso_a_dirigente,

    /**
     * Rappresenta uno scatto di carriera in cui un dipendente viene rimosso da un ruolo di dirigente.
     */
    Rimosso_da_dirigente;
}