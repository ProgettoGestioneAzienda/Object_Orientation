<a name="readme-top"></a>
# ProgettoObjectOrientation

## Introduzione

<table>
<tr>
<td>

Questo progetto permette l'utilizzo di un applicativo sviluppato tramite il linguaggio di programmazione orientato ad oggetti Java. L'applicaivo consente agli utenti di visualizzare i dati presenti nelle varie aree, di aggiungere ulteriori dati e di apportare modifiche ad essi. In particolare, è basato sul sistema di gestione di personale e progetti di un’azienda.

Questo applicativo è progettato per gestire le informazioni relative al personale dell'azienda, inclusi i dati anagrafici dei dipendenti, i loro ruoli, i laboratori a cui partecipano e il loro stato occupazionale. Inoltre, il tutto è stato progettato per supportare la gestione dei progetti, compresi i dettagli del progetto, da quali laboratori sono lavorati e gli acquisti fatti per esso.

</td>
</tr>
</table>

## Creazione database e gestione utenti con privilegi

Di seguito viene descritta la procedura per la creazione di un nuovo database e la gestione degli utenti con i relativi privilegi. Per creare un nuovo utente con password, è possibile utilizzare il comando SQL "CREATE USER" seguito dal nome dell'utente e dalla password assegnata.

```
CREATE USER nomeUtente WITH PASSWORD 'passwordUtente';   
```

Per creare un nuovo database, è possibile utilizzare il comando SQL "CREATE DATABASE" seguito dal nome del nuovo database.

```
CREATE DATABASE GestioneAzienda;   
```

Qualora si desideri fornire i permessi di utilizzo in un determinato schema compreso nel database, è possibile utilizzare il comando SQL "GRANT USAGE" seguito dal nome dello schema e dal nome dell'utente a cui si vogliono fornire i permessi.

```
GRANT USAGE ON Azienda TO nome_user;
```

Per garantire tutti i diritti ed i privilegi al nuovo utente sul nuovo database, è possibile utilizzare il comando SQL "GRANT ALL" seguito dal nome del database e dal nome dell'utente. 

```
GRANT ALL ON DATABASE GestioneAzienda TO nomeUtente;
```

Il nuovo utente ora possiede tutti i diritti ed i privilegi su GestioneAzienda, ma non ne è ancora l'ADMIN. Per assegnare il ruolo di ADMIN al nuovo utente, è possibile utilizzare il comando SQL "ALTER DATABASE OWNER" seguito dal nome del database e dal nome dell'utente.

```
ALTER DATABASE GestioneAzienda OWNER TO nomeUtente;
```

## Configurazione del database

Per configurare il database, creato mediato PostgresSQL, su cui si basa l'applicativo è possibile riferirsi al codice che riporta lo [schema e la popolazione](https://github.com/ProgettoGestioneAzienda/Object_Orientation/blob/main/Database/schema%2Bpopolazione.sql) di esso. Avendo a disposizione il databse sarà consentito l'accesso all'applicativo con dati già presenti in esso.

## Installazione applicativo

Sarà possibile la compilazione dell'applicativo facendo riferimento al [file contenente le istruzioni](https://github.com/ProgettoGestioneAzienda/Object_Orientation/tree/main/SIRIUS/src/main/java). 

## Informazioni sull'applicativo

É consigliato fare riferimento alla [documentazione in LaTeX](https://github.com/ProgettoGestioneAzienda/Object_Orientation/blob/main/Sistema%20di%20gestione%20di%20personale%20e%20progetti%20-%20Progetto%20Object%20Orientation.pdf) per ulteriori informazioni e per la visualizzazione dei diagrammi, invece fare riferimento alla [documentazione in JavaDoc](https://raw.githack.com/ProgettoGestioneAzienda/Object_Orientation/main/DocumentazioneJavaDoc/index.html) per ulteriori specifiche riguardanti il codice dell'applicativo.

<p align="right">(<a href="#readme-top">torna all'inizio</a>)</p>
