package Model;

import java.time.LocalDate;

/**
 * La classe ”Scatto di carriera” rappresenta gli scatti di carriera di un
 * dipendente. In particolare lo scatto puo' essere di quattro tipi:
 * 1. Scatto da junior a middle, indicato con ”Middle”
 * 2. Scatto da middle a senior, indicato con ”Senior”
 * 3. Scatto da non dirigente a dirigente, che indichiamo con la
 * denominazione ”Promosso a dirigente”, non vincolato all’anzianita'
 * 4. Scatto da dirigente a non dirigente, che indichiamo con la
 * denominazione ”Rimosso da dirigente”, non vincolato all’anzianita'.
 * Oltre al tipo di scatto avremo anche la data in cui e' avvenuto lo scatto
 * per una determinata matricola.
 * */
public class ScattoCarriera {

    //Rappresenta la data in cui viene effettuato lo scatto di carriera
    private LocalDate data;

    //Rappresenta il tipo di scatto di carriera
    private TipoScatto tipoScatto;

    //Associazione: rappresenta il dipendente che ha compiuto lo scatto di carriera
    private DipendenteIndeterminato dipendente;

    // COSTRUTTORE
    /**
     * Costruisce un nuovo oggetto "ScattoCarriera" con le infromazioni fornite.
     *
     * @param data       {@link LocalDate}                  La data in cui viene effettuato lo scatto di carriera.
     * @param tipoScatto {@link TipoScatto}                 Il tipo di scatto di carriera, tra Middle, Senior, Promosso_a_dirigente e Rimosso_da_dirigente.
     * @param dipendente {@link DipendenteIndeterminato}    Il dipendente che ha compiuto lo scatto di carriera.
     */
    public ScattoCarriera(LocalDate data, TipoScatto tipoScatto, DipendenteIndeterminato dipendente) {
        this.data = data;
        this.tipoScatto = tipoScatto;

        //Associazione (scatto effettuato)
        this.dipendente = dipendente;
    }

    // SETTERS
    /**
     * Imposta la data di uno scatto di carriera.
     *
     * @param data {@link LocalDate}    La data in cui viene effettuato lo scatto di carriera.
     */
    public void setData (LocalDate data) {
        this.data = data;
    }

    /**
     * Imposta il tipo di scatto di carriera, tra Middle, Senior, Promosso_a_dirigente e Rimosso_da_dirigente.
     *
     * @param tipoScatto {@link TipoScatto} Il tipo di scatto di carriera.
     */
    public void setTipoScatto(TipoScatto tipoScatto) {
        this.tipoScatto = tipoScatto;
    }

    //Associazione: Imposta quale dipendente ha compiuto lo scatto
    /**
     * Imposta quale dipendente ha compiuto lo scatto di carriera.
     *
     * @param dipendente {@link DipendenteIndeterminato} Il dipendente che ha compiuto lo scatto di carriera.
     */
    public void setDipendente(DipendenteIndeterminato dipendente) {
        this.dipendente = dipendente;
    }



    // GETTERS
    /**
     * Recupera la data di uno scatto di carriera.
     *
     * @return {@link LocalDate}   La data in cui viene effettuato lo scatto di carriera.
     */
    public LocalDate getData(){
        return data;
    }

    /**
     * Recupera il tipo di scatto di carriera, tra Middle, Senior, Promosso_a_dirigente e Rimosso_da_dirigente.
     *
     * @return {@link TipoScatto}   Il tipo di scatto di carriera.
     */
    public TipoScatto getTipoScatto(){
        return tipoScatto;
    }

    /**
     * Recupera il dipendente che ha compiuto lo scatto di carriera.
     *
     * @return {@link DipendenteIndeterminato}   Il dipendente che ha compiuto lo scatto di carriera.
     */
    public DipendenteIndeterminato getDipendente(){
        return dipendente;
    }

    // METODI
    //OVERRIDE DI METODI EREDITATI
    /**
     * Sovrascrittura del metodo "equals" di object, che permette di effettuare un confronto tra un oggetto di tipo "ScattoCarriera" ed un oggetto di tipo "Object".
     * Il risultato e' calcolato confrontando l'uguaglianza dei campi "tipoScatto", "data" e "dipendente".
     *
     * @param obj   {@link Object}  Rappresenta l'oggetto con cui l'istanza di "ScattoCarriera" verra' confrontata.
     *
     * @return  True se l'oggetto passato come parametro e' di tipo "ScattoCarriera" e i campi sono uguali all'istanza di scatto di carriera considerata.
     * */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof ScattoCarriera))
            return false;

        ScattoCarriera scatto = (ScattoCarriera) obj;

        if (!scatto.getTipoScatto().equals(this.getTipoScatto()))    return false;
        if (!scatto.getData().equals(this.getData()))                return false;
        if (!scatto.getDipendente().equals(this.getDipendente()))    return false;

        return true;
    }

    /**
     * Sovrascrittura del metodo "toString" di object, che permette di fornire una descrizione completa dello scatto di carriera invocante.
     *
     * @return {@link String}   Descrizione testuale di tutti i campi dell'oggetto di tipo "ScattoCarriera" considerato.
     * */
    @Override
    public String toString() {
        return String.format("Dipendente: %s %s%nMatricola: %s%nData: %s%nTipo: %s",
                dipendente.getNome(), dipendente.getCognome(), dipendente.getMatricola(), data, tipoScatto.toString());
    }
}