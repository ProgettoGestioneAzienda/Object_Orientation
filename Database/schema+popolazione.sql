--CANCELLAZIONE VECCHIO SCHEMA
DROP SCHEMA IF EXISTS azienda CASCADE;

--------------------------------------------------------------------------------------------------------------------------------------------------------------
--CREAZIONE SCHEMA

CREATE SCHEMA azienda AUTHORIZATION postgres;

--------------------------------------------------------------------------------------------------------------------------------------------------------------
--CREAZIONE DOMINI

--CREO UN GENERICO DOMINIO "STRING" PER CAMPI VARCHAR SENZA ESIGENZE SPECIFICHE
CREATE DOMAIN azienda.STRING AS VARCHAR(30);

--CREO UN DOMINIO "EURO" PER CAMPI DI CIFRE DECIMALI CHE RAPPRESENTANO UN COSTO
CREATE DOMAIN azienda.EURO AS NUMERIC(20, 2) --Va da -(10^18)+0.01 a (10^18)-0.01 e arrotonda per difetto da 0.001 a 0.004, il resto per eccesso
    CONSTRAINT POSITIVE_EURO
        CHECK (VALUE >= 0);

--AGGIUNGO IL VINCOLO CHE UNA MATRICOLA DEVE ESSERE OBBLIGATORIAMENTE DI 8 CARATTERI, RINOMINANDOLA CON UNA LABEL
CREATE DOMAIN azienda.MATRICOLA AS VARCHAR(8)
    CONSTRAINT DOM_MATRICOLA_CHECK_LENGTH
        CHECK (LENGTH(VALUE) = 8);

--AGGIUNGO IL VINCOLO CHE IL CUP DEVE ESSERE OBBLIGATORIAMENTE DI 15 CARATTERI, RINOMINANDOLO CON UNA LABEL
CREATE DOMAIN azienda.CUP AS VARCHAR(15)
    CONSTRAINT DOM_CUP_CHECK_LENGTH
        CHECK (LENGTH(VALUE) = 15)
    CONSTRAINT DOM_CUP_CHECK_ALPHANUMERIC
        CHECK (VALUE ~ '[[:alnum:]]{15}');

--AGGIUNGO IL VINCOLO CHE UN CODICE FISCALE DEVE ESSERE OBBLIGATORIAMENTE DI 16 CARATTERI, RINOMINANDOLA CON UNA LABEL
CREATE DOMAIN azienda.CODFISCALE AS VARCHAR(16)
    CONSTRAINT DOM_CODFISCALE_CHECK_LENGTH
        CHECK (LENGTH(VALUE) = 16)
    CONSTRAINT DOM_CODFISCALE_CHECK_ALPHANUMERIC
        CHECK (VALUE ~ '[[:alnum:]]{16}');

--IL TIPO DI UN DIPENDENTE DEVE ESSERE OBBLIGATORIAMENTE UNO DI QUESTI
CREATE DOMAIN azienda.TIPO_DIPENDENTE AS VARCHAR(6)
    CONSTRAINT DOM_TIPO_DIPENDENTE_CHECK_JMS
        CHECK (UPPER(VALUE) IN ('JUNIOR', 'MIDDLE', 'SENIOR'));

--IL TIPO DI SCATTO DEVE ESSERE OBBLIGATORIAMENTE UNO DI QUESTI.
--Lo scatto da Junior a Middle è chiamato "Middle", lo scatto da Middle a Senior è chiamato "Senior" e lo scatto a Dirigente è chiamato "Dirigente"
CREATE DOMAIN azienda.TIPO_SCATTO AS VARCHAR(20)
    CONSTRAINT DOM_TIPO_SCATTO_CHECK_MSD
        CHECK (UPPER(VALUE) IN ('MIDDLE', 'SENIOR', 'PROMOSSO_A_DIRIGENTE', 'RIMOSSO_DA_DIRIGENTE'));



--------------------------------------------------------------------------------------------------------------------------------------------------------------
--CREAZIONE TABELLE

CREATE TABLE azienda.DIP_INDETERMINATO(

    Matricola azienda.MATRICOLA NOT NULL,
    Tipo azienda.TIPO_DIPENDENTE NOT NULL DEFAULT 'Junior',
    Nome azienda.STRING NOT NULL,
    Cognome azienda.STRING NOT NULL,
    codFiscale azienda.CODFISCALE NOT NULL,
    Indirizzo VARCHAR(100),
    dataNascita DATE NOT NULL,
    dataAssunzione DATE NOT NULL,
    dataFine DATE,
    Dirigente BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_dip_indeterminato PRIMARY KEY (Matricola),
        --Non posso assumere un dipendente non ancora nato o licenziare un dipendente non ancora assunto
    CONSTRAINT check_ordine_date_di CHECK (dataNascita < dataAssunzione AND dataAssunzione <= dataFine)
);

CREATE TABLE azienda.SCATTO_CARRIERA(

    Matricola azienda.MATRICOLA NOT NULL,
    Tipo azienda.TIPO_SCATTO NOT NULL,
    Data DATE NOT NULL,

    CONSTRAINT pk_scatto_carriera PRIMARY KEY (Matricola, Tipo, Data),
    CONSTRAINT fk_matricola_scatto_carriera FOREIGN KEY (Matricola)
        REFERENCES azienda.DIP_INDETERMINATO(Matricola)
        ON DELETE CASCADE	ON UPDATE CASCADE
);

CREATE TABLE azienda.LABORATORIO(

    Nome azienda.STRING NOT NULL,
    Topic azienda.STRING NOT NULL,
    Responsabile_Scientifico azienda.MATRICOLA NOT NULL,

    CONSTRAINT pk_laboratorio PRIMARY KEY (Nome),
    CONSTRAINT fk_responsabile_scientifico_laboratorio FOREIGN KEY (Responsabile_Scientifico)
        REFERENCES azienda.DIP_INDETERMINATO(Matricola)
        ON DELETE NO ACTION		ON UPDATE CASCADE

);

CREATE TABLE azienda.AFFERIRE(

    Matricola azienda.MATRICOLA NOT NULL,
    nomeLab azienda.STRING NOT NULL,

    CONSTRAINT pk_afferire PRIMARY KEY (Matricola, nomeLab),
    CONSTRAINT fk_matricola_afferire FOREIGN KEY (Matricola)
        REFERENCES azienda.DIP_INDETERMINATO(Matricola)
        ON DELETE CASCADE	ON UPDATE CASCADE,
    CONSTRAINT fk_nome_afferire FOREIGN KEY (nomeLab)
        REFERENCES azienda.LABORATORIO(Nome)
        ON DELETE CASCADE	ON UPDATE CASCADE
);

CREATE TABLE azienda.PROGETTO(

    CUP azienda.CUP NOT NULL,
    Nome azienda.STRING NOT NULL,
    dataInizio DATE NOT NULL,
    dataFine DATE,
    Budget azienda.EURO NOT NULL,
    Referente_Scientifico azienda.MATRICOLA NOT NULL,
    Responsabile azienda.MATRICOLA NOT NULL,

    CONSTRAINT pk_progetto PRIMARY KEY (CUP),
    CONSTRAINT fk_referente_scientifico_progetto FOREIGN KEY (Referente_Scientifico)
        REFERENCES azienda.DIP_INDETERMINATO(Matricola)
        ON DELETE NO ACTION		ON UPDATE CASCADE,
    CONSTRAINT fk_responsabile_progetto FOREIGN KEY (Responsabile)
        REFERENCES azienda.dip_indeterminato(Matricola)
        ON DELETE NO ACTION		ON UPDATE CASCADE,
    CONSTRAINT nome_progetto_unico UNIQUE(Nome),
    CONSTRAINT check_positive_budget CHECK (Budget > 0), --Nella nostra azienda non possiamo lavorare a progetti con fondi minori o uguali a 0
    CONSTRAINT check_date_fine_inizio CHECK (dataInizio <= dataFine) --La data di inizio non può avvenire dopo la data di fine
);

CREATE TABLE azienda.LAVORARE(

    CUP azienda.CUP NOT NULL,
    nomeLab azienda.STRING NOT NULL,

    CONSTRAINT pk_lavorare PRIMARY KEY (CUP, nomeLab),
    CONSTRAINT fk_cup_lavorare FOREIGN KEY (CUP)
        REFERENCES azienda.PROGETTO(CUP)
        ON DELETE CASCADE	ON UPDATE CASCADE,
    CONSTRAINT fk_nome_lavorare FOREIGN KEY (nomeLab)
        REFERENCES azienda.LABORATORIO(Nome)
        ON DELETE CASCADE	ON UPDATE CASCADE
);

CREATE TABLE azienda.ATTREZZATURA(

    idAttrezzatura SERIAL NOT NULL,
    Descrizione VARCHAR(256) NOT NULL,
    Costo azienda.EURO NOT NULL,
    nomeLab azienda.STRING,
    CUP azienda.CUP NOT NULL,

    CONSTRAINT pk_attrezzatura PRIMARY KEY (idAttrezzatura),
    CONSTRAINT fk_nome_attrezzatura FOREIGN KEY (nomeLab)
        REFERENCES azienda.LABORATORIO(Nome)
/*Nel caso in cui cancellassimo un laboratorio, avremmo comunque intatto lo storico degli acquisti fatto. Dato che le attrezzature sono acquistate per un
progetto è giustificata l'azione ON DELETE SET NULL dato che abbiamo contemplato la possibilità di avere ATTREZZATURE non
possedute da nessuno.*/
        ON DELETE SET NULL		ON UPDATE CASCADE,
    CONSTRAINT fk_cup_attrezzatura FOREIGN KEY (CUP)
        REFERENCES azienda.progetto(CUP)
        ON DELETE CASCADE	ON UPDATE CASCADE
);

CREATE TABLE azienda.DIP_PROGETTO(

    Matricola azienda.Matricola NOT NULL,
    Nome azienda.STRING NOT NULL,
    Cognome azienda.STRING NOT NULL,
    codFiscale azienda.CODFISCALE NOT NULL,
    Indirizzo VARCHAR(100),
    dataNascita DATE NOT NULL,
    dataAssunzione DATE NOT NULL,
    Costo azienda.EURO NOT NULL,
    Scadenza DATE NOT NULL,
    CUP azienda.CUP NOT NULL,

    CONSTRAINT pk_dip_progetto PRIMARY KEY (Matricola),
    CONSTRAINT fk_cup_dip_progetto FOREIGN KEY (CUP)
        REFERENCES	azienda.progetto(CUP)
        ON DELETE CASCADE	ON UPDATE CASCADE,
    --Non posso assumere un dipendente non ancora nato o licenziare un dipendente non ancora assunto
    CONSTRAINT check_ordine_date_dp CHECK (dataNascita < dataAssunzione AND dataAssunzione <= Scadenza)
);

INSERT INTO azienda.DIP_INDETERMINATO(Matricola, Tipo, Nome, Cognome, codFiscale, Indirizzo, dataNascita, dataAssunzione, dataFine, Dirigente) VALUES
('KD0001PP', 'Junior', 'Paolo', 'Rossi', 'RSSPLA76E18F839B', 'Via Medina, 5, 80133 Napoli NA', '18/05/1976', '16/03/2021', NULL, 'False'),
('AZ0156OL', 'Middle', 'Giustino', 'Tomasetti', 'TMSGTN83P08I158R', 'Via Legnano, 18, 71016 San Severo FG', '08/09/1983', '25/09/2018', NULL, 'False'),
('KP1500XD', 'Middle', 'Livia', 'Donatoni', 'DNTLVI00S53F205H', NULL, '13/11/2000', '30/09/2018', '11/12/2021', 'False'),
('NG3020OP', 'Senior', 'Oreste', 'Zamorani', 'ZMRRST85E15L120W', 'Via Olmata, 106, 04019 Terracina LT', '15/05/1970', '19/12/1990', NULL, 'False'),
('DS0155CC', 'Junior', 'Liliana', 'Coppola', 'CPPLLN65E56G482Y', 'Via Fernando Francesco d''Avalos, 9/C, 65126 Pescara PE', '16/05/1965', '15/07/1988', '05/05/1990', 'False'),
('BH0100FI', 'Senior', 'Daniele', 'Comolli', 'CMLDNL90E08L219M', NULL, '08/05/1990', '28/09/2010', NULL, 'False'),
('OD9234CC', 'Senior', 'Federico', 'Galeati', 'GLTFRC80R17E506C', 'Via Guglielmo Paladini, 19, 73100 Lecce LE', '17/10/1980', '17/10/2010', NULL, 'False'),
('EE2214XN', 'Junior', 'Fabio', 'Brancaccio', 'BRNFBA85M05H501N', 'Via dei Marrucini, 6, 00185 Roma RM', '05/08/1985', '09/09/2015', '18/09/2015', 'False'),
('CF2901KK', 'Junior', 'Claudia', 'Broggini', 'BRGCLD94A54E506J', 'Via di Leuca, 34, 73100 Lecce LE', '14/01/1994', '12/11/2022', NULL, 'False'),
('FE2901KK', 'Middle', 'Ugo', 'Corradi', 'CRRGUO89D12D612B', 'Via della Cernaia, 10, 50129 Firenze FI', '12/04/1989', '05/02/2011', '19/02/2016', 'False'),
('AL3910ZO', 'Middle', 'Ugo', 'Corradi', 'CRRGUO89D12D612B', 'Via della Cernaia, 10, 50129 Firenze FI', '12/04/1989', '25/02/2016', '21/09/2022', 'False'),
('AL3910ZP', 'Middle', 'Liliana', 'Coppola', 'CPPLLN92L52G786A', 'Via Taranto, 158, 75025 Policoro MT', '12/07/1992', '30/05/2017', NULL, 'False'),
('IP2100GF', 'Senior', 'Gianmarco', 'De Simone', 'DSMGMR02B11F205H', NULL, '11/12/1995', '04/05/2012', NULL, 'False'),
('FE2901BK', 'Junior', 'Gianni', 'Boitani', 'BTNGNN78R19H199O', 'Via Giacomo Battuzzi, 49, 48123 Ravenna RA', '19/10/1978', '12/04/2021', NULL, 'False'),
('OF6749NN', 'Junior', 'Livia', 'Donatoni', 'DNTLVI00S53F205H', NULL, '13/11/2000', '25/05/2022', NULL, 'False'),

('2DAA74B5', 'Junior', 'Valentina', 'Piscopo', 'PSCVNT02E47F839L', 'Strada Valentini 388', '07-05-2002', '25/06/2021', NULL, FALSE),
('B8B7C93B', 'Middle', 'Simone', 'Scisciola', 'SCSSMN99B04F839G', 'Rotonda Riva 243', '04/02/1999', '07/03/2019', NULL, FALSE),
('5B3966D5', 'Senior', 'Luca', 'Piccolo', 'PCCLCU01E07F839S', 'Rotonda Miriana 549', '07/05/2001', '06/03/2015', NULL, FALSE),
('F6C4CE53', 'Junior', 'Gabriele', 'D''Annunzio', 'DNNGRL03C12H501W', 'Borgo Colombo 3', '12/03/1903', '26/04/1942', '30/11/1944', FALSE),
('38463E9A', 'Middle', 'Roberto', 'Saiello', 'SLLRRT90S21F839G', 'Contrada Costa 877', '21/11/1990', '04/03/2016', '03/07/2020', FALSE),
('DA49BE9E', 'Senior', 'Rosario', 'Saiello', 'SLLRSR87S21F839I', 'Strada Benedetti 6', '21/11/1987', '04/03/2015', NULL, FALSE),
('D45FB369', 'Junior', 'Giovanni', 'Spoleto', 'SPLGNN98E11F839B', 'Strada Carbone 4', '11/05/1998', '26/04/2022', NULL, FALSE),
('446988A8', 'Middle', 'Isaac', 'Newton', 'SCINTN11A04H221U', 'Via Grasso 421', '4/06/1911', '16/08/1945', '30/08/1950', FALSE),
('A7443DBC', 'Senior', 'Nikola', 'Tesla', 'NKLTSL61L10A001V', 'Via Nunzia 98', '10/07/1961', '23/05/1984', NULL, FALSE),
('C7EA6634', 'Junior', 'Giuseppe', 'Garibaldi', 'GSPGBL35L04H149G', 'Strada Romeo 3', '4/07/1935', '20/07/1959', '24/09/1961', FALSE),
('6C47A92F', 'Middle', 'Charles', 'Darwin', 'CHRDWN84B12D449U', 'Contrada Giorgio 86', '12/02/1984', '05/07/2017', NULL, FALSE),
('FDDB4ED2', 'Senior', 'Alan', 'Turing', 'LNATNG90H23L021U', 'Rotonda Bellini 4', '23/06/1990', '26/06/2014', NULL, FALSE),
('59F5DB2A', 'Junior', 'Niccolò', 'Copernico', 'NCLCRN77S08D455E', 'Piazza Brigitta 9', '19/02/1977', '14/04/2005', '05/12/2007', FALSE),
('4BCB4FA4', 'Middle', 'Ada', 'Lovelace', 'DAALLC93T50Z300Y', 'Incrocio Gelsomina 64', '10/12/1993', '03/06/2017', NULL, FALSE),
('443FCE35', 'Senior', 'Albert', 'Gonzalez', 'LBRGZL81T08Z227Z', 'Piazza Neri 9', '08/12/1981', '28/01/2003', NULL, FALSE),

('X3KJ7B5D', 'Junior', 'Berenice', 'Fanucci', 'BRNFCC73P55B354I', 'Via Pisanelli, 117, 00011 Roma RM', '15/09/1973', '12/10/2022', NULL, FALSE), 		--no scatto
('Y9LQ8T2R', 'Middle', 'Alma', 'Rizzo', 'RZZLMA81R57E715O', 'Via Firenze, 80, 06015 Perugia PG', '17/10/1981', '03/03/2020', NULL, FALSE), 				--scatto middle
('H6MNB4VC', 'Middle', 'Ruggero', 'Folliero', 'FLLRGR82A21D643A', 'Via Foria, 89, 92015 Agrigento AG', '21/01/1982', '05/07/2019', NULL, FALSE),        --scatto middle
('G1PZ2X4F', 'Junior', 'Benedetta', 'Trevisan', 'TRVBDT91P62F205X', 'Via del Caggio, 96, 70896 Bari BA', '22/09/1991', '07/05/2022', NULL, FALSE),      --no scatto
('C9VH2J8N', 'Senior', 'Lamberto', 'Sabbatini', 'SBBLBR63L03H703X', 'Viale delle Province, 111, 81057 Caserta CA', '03/07/1963', '23/02/2012', NULL, FALSE),		--scatto middle scatto senior FactoryIoT
('K4S6D7F9', 'Senior', 'Filippo', 'Russo', 'RSSFPP75A24C242H', 'Via San Pietro Ad Aram, 3, 85067 Potenza PZ', '24/01/1975', '15/02/2016', NULL, FALSE),			--scatto middle scatto senior
('Q2WY7U5E', 'Middle', 'Palmiro', 'Napolitano', 'NPLPMR57R10E040Z', 'Via Catullo, 27, 72018 Brindisi BR', '10/10/1986', '03/08/2018', '13/12/2022', FALSE),		--licenziato middle
('N5B8J3M7', 'Junior', 'Claudia', 'Lucchese', 'LCCCLD92E55F839O', 'Via Nicola Spaventa, 74, 91040 Trapani TP', '15/05/1992', '19/12/2022', NULL, FALSE),		--no scatto
('P6L2K8T4', 'Middle', 'Doroteo', 'Udinese', 'DNSDRT82T21L049G', 'Via Piave, 101, 97098 Ragusa RG', '21/12/1982', '14/01/2018', NULL, FALSE),					--scatto middle
('Z1X5C2V9', 'Junior', 'Nella', 'Romano', 'RMNNLL74D53A662E', 'Via Palermo, 38, 95032 Catania CT', '13/04/1974', '05/04/2021', NULL, FALSE),					--no scatto
('F8R6G3J7', 'Senior', 'Giuseppe', 'Lo Duca', 'LDCGPP68L08H501Y', 'Via Pasquale Scura, 46, 98045 Messina ME', '08/07/1968', '21/10/2010', NULL, FALSE),			--scatto middle scatto senior TechLab dirigente
('T5K9M7H1', 'Senior', 'Piero', 'Angelo', 'NGLPRI64C08L049L', 'Via Galileo Ferraris, 31, 02015 Rieti RI', '08/03/1964', '18/05/2011', NULL, FALSE),				--scatto middle scatto senior DataWorks
('U2N6B4L8', 'Senior', 'Monica', 'Pirozzi', 'PRZMNC59L48D612U', 'Piazza Principe Umberto, 107, 11028 Aosta AO', '08/07/1959', '05/03/2011', '18/09/2019', FALSE), --licenziato scatto senior
('V9C5F2P7', 'Middle', 'Costantino', 'Bruno', 'CSTBRN02A05F257E', 'Piazza Bovio, 79, 17043 Savona SV', '05/01/2002', '05/03/2020', NULL, FALSE), 			--scatto Middl
('W1Q8Z2X4', 'Senior', 'Gaspare', 'Buccho', 'BCCGPR83L31F839G', 'Via Galileo Ferraris, 32, 10013 Torino TO', '31/07/1983', '21/05/2010', NULL, FALSE),      --senior dirigente
('Z8MH9F2E', 'Junior', 'Gabriele', 'Russo', 'RSSGBR80L27B345E', 'Via Casoria, 23, 00132 Roma RM', '20/05/1980', '01/11/2022', NULL, FALSE),
('W5KL6H9Z', 'Middle', 'Simone', 'Ferrari', 'FRRSMN73A23L219E', 'Via dei Serpenti, 43, 00184 Roma RM', '07/06/1973', '01/03/2019', NULL, FALSE),
('F4J9X7K2', 'Senior', 'Jolanda', 'Greco', 'GRCJND77R56I452S', 'Via Croce Rossa, 100, 07052 Sassari SS', '16/10/1977', '13/11/2011', NULL, FALSE),
('T8M3B2N6', 'Senior', 'Remo', 'Romani', 'RMNRME61D18H473W', 'Piazza Trieste e Trento, 4, 12050 Roddino CN', '18/04/1961', '17/03/2009', NULL, FALSE),
('L6P9R2V7', 'Senior', 'Antonio', 'Cremonesi', 'CRMNTN67A09L682H', 'Via Lagrange, 134, 21100 Varese VA', '09/01/1967', '11/05/2012', NULL, FALSE);


INSERT INTO azienda.SCATTO_CARRIERA(Matricola, Tipo, Data) VALUES
('IP2100GF', 'Promosso_a_dirigente', '30/12/2023'),
('AZ0156OL', 'Promosso_a_dirigente', '17/10/2022'),
('BH0100FI', 'Promosso_a_dirigente', '24/09/2015'),
('NG3020OP', 'Promosso_a_dirigente', '18/06/2007'),
('NG3020OP', 'Promosso_a_dirigente', '01/08/2009'),
('NG3020OP', 'Promosso_a_dirigente', '16/12/2015'),
('OD9234CC', 'Promosso_a_dirigente', '12/11/2014'),
('OD9234CC', 'Promosso_a_dirigente', '11/04/2020'),
('AL3910ZO', 'Promosso_a_dirigente', '08/06/2021'),
('DS0155CC', 'Promosso_a_dirigente', '15/02/1990'),

('2DAA74B5', 'Promosso_a_dirigente', '26/06/2022'),
('FDDB4ED2', 'Promosso_a_dirigente', '26/06/2020'),
('4BCB4FA4', 'Promosso_a_dirigente', '03/06/2018'),
('DA49BE9E', 'Promosso_a_dirigente', '16/08/2021'),
('F6C4CE53', 'Promosso_a_dirigente', '18/10/1942'),
('446988A8', 'Promosso_a_dirigente', '16/01/1948'),
('C7EA6634', 'Promosso_a_dirigente', '20/07/1960'),

('W1Q8Z2X4', 'Promosso_a_dirigente', '28/01/2019'),
('C9VH2J8N', 'Promosso_a_dirigente', '12/05/2019'),
('F8R6G3J7', 'Promosso_a_dirigente', '20/02/2020'),
('C9VH2J8N', 'Rimosso_da_dirigente', '24/03/2020'),
('K4S6D7F9', 'Promosso_a_dirigente', '20/02/2023');


INSERT INTO azienda.LABORATORIO(Nome, Topic, Responsabile_Scientifico) VALUES
('Ganular', 'Telecomunicazioni', 'NG3020OP'),
('Aptos', 'Intelligenza Artificiale', 'BH0100FI'),
('Aj Logics', 'Intelligenza Artificiale', 'BH0100FI'),
('Kupfert', 'Web Design', 'IP2100GF'),

('Laboratorio Sigma', 'App mobili', '5B3966D5'),
('Ricerche Omega', 'Web development', 'DA49BE9E'),
('Istituto Tecnologico Alpha', 'Machine learning', 'A7443DBC'),
('Studio Delta', 'Cybersecurity', 'FDDB4ED2'),
('Sperimentazione Gamma', 'Programmazione', '443FCE35'),
('Fondazione Epsilon', 'Design digitale', '443FCE35'),
('Dipartimento Zeta', 'Robotics', 'DA49BE9E'),

('FactoryIoT', 'Internet delle Cose (IoT)', 'C9VH2J8N'),
('CodeLab', 'Sviluppo software', 'C9VH2J8N'),
('TechLab', 'Ricerca e sviluppo', 'K4S6D7F9'),
('TrustChain', 'Blockchain', 'F8R6G3J7'),
('DataWorks', 'Data science', 'T5K9M7H1'),
('TestZone', 'Testing qualità software', 'W1Q8Z2X4'),
('UserXperience', 'Interfacce utente', 'K4S6D7F9'),
('QuantumLab', 'Quantum Computing', 'W1Q8Z2X4');

INSERT INTO azienda.AFFERIRE(Matricola, nomeLab) VALUES
('AZ0156OL', 'Kupfert'),
('FE2901BK', 'Aptos'),
('AZ0156OL', 'Aj Logics'),
('CF2901KK', 'Aj Logics'),
('FE2901BK', 'Aj Logics'),
('AL3910ZP', 'Aj Logics'),
('BH0100FI', 'Ganular'),
('IP2100GF', 'Ganular'),

('2DAA74B5', 'Sperimentazione Gamma'),
('B8B7C93B', 'Ricerche Omega'),
('D45FB369', 'Istituto Tecnologico Alpha'),
('6C47A92F', 'Dipartimento Zeta'),
('4BCB4FA4', 'Istituto Tecnologico Alpha'),
('6C47A92F', 'Studio Delta'),
('2DAA74B5', 'Fondazione Epsilon'),
('B8B7C93B', 'Fondazione Epsilon'),

('X3KJ7B5D', 'FactoryIoT'),
('G1PZ2X4F', 'FactoryIoT'),
('G1PZ2X4F', 'TestZone'),
('N5B8J3M7', 'TestZone'),
('N5B8J3M7', 'UserXperience'),
('Z1X5C2V9', 'FactoryIoT'),
('Z8MH9F2E', 'FactoryIoT'),
('Z8MH9F2E', 'TrustChain'),
('Z8MH9F2E', 'TestZone'),
('Y9LQ8T2R', 'TechLab'),
('Y9LQ8T2R', 'DataWorks'),
('H6MNB4VC', 'DataWorks'),
('H6MNB4VC', 'QuantumLab'),
('P6L2K8T4', 'FactoryIoT'),
('P6L2K8T4', 'TestZone'),
('P6L2K8T4', 'TrustChain'),
('V9C5F2P7', 'QuantumLab'),
('W5KL6H9Z', 'TestZone'),
('W5KL6H9Z', 'UserXperience'),
('C9VH2J8N', 'QuantumLab'),
('C9VH2J8N', 'TechLab'),
('K4S6D7F9', 'QuantumLab'),
('F8R6G3J7', 'DataWorks'),
('T5K9M7H1', 'FactoryIoT'),
('T5K9M7H1', 'TrustChain'),
('T5K9M7H1', 'TestZone'),
('W1Q8Z2X4', 'TechLab'),
('F4J9X7K2', 'DataWorks'),
('T8M3B2N6', 'QuantumLab'),
('T8M3B2N6', 'TechLab'),
('L6P9R2V7', 'CodeLab'),
('L6P9R2V7', 'UserXperience');

INSERT INTO azienda.PROGETTO(CUP, Nome, dataInizio, dataFine, Budget, Referente_Scientifico, Responsabile) VALUES
('J63G16029530490', 'Tricolouris', '21/12/2020', '23/05/2024', 1350, 'IP2100GF', 'IP2100GF'),
('O25H61393843815', 'Photonics', '12/10/1999', '12/10/2000', 7000, 'NG3020OP', 'NG3020OP'),
('N50E60862030706', 'Fairytale', '05/06/2025', NULL, 2500, 'NG3020OP', 'BH0100FI'),
('N13G09807134097', 'Trident', '30/09/2019', NULL, 850, 'BH0100FI', 'AZ0156OL'),

('Y0Z0A1B1C1D1E1F', 'Rivoluzione verde', '2022-10-01', '2023-09-30', 100000, 'A7443DBC', '2DAA74B5'),
('A001B0002C00035', 'Progetto di sviluppo software', '2022-10-01', NULL, 130000, 'A7443DBC', '2DAA74B5'),
('I0J0K0L0M0N0O0P', 'Smart City', '2022-01-01', '2023-06-30', 250000, 'A7443DBC', 'FDDB4ED2'),
('Q0R0S0T0U0V0W0X', 'Intelligenza artificiale', ' 2022-07-01', '2023-01-15', 200000, '443FCE35', '4BCB4FA4'),
('E000F000G000H00', 'Innovazione digitale', '2023-01-01', NULL, 150000, 'DA49BE9E', 'DA49BE9E'),

('OUNH7DFJ8T1M2KP', 'ResourceWise', '05/02/2020', '10/01/2024', 450000, 'F4J9X7K2', 'W1Q8Z2X4'),
('CQYR9VXW5A6E1HZ', 'SecurityShield', '24/04/2020', '23/07/2024', 485000, 'T8M3B2N6', 'F8R6G3J7'),
('P3K7D5F8L4S9J6G', 'InvoicesChain', '11/12/2021', '17/10/2025', 430000, 'F4J9X7K2', 'W1Q8Z2X4'),
('N1X4Z8B5G7V6H2J', 'AIfinity', '23/03/2022', '07/05/2025', 550000, 'T8M3B2N6', 'F8R6G3J7'),
('M5S8D4F9K2L1G7H', 'AutoTest', '15/05/2019', '25/07/2023', 430000, 'L6P9R2V7', 'K4S6D7F9'),
('3N7D6G9X2R8K1S4', 'SmartShop', '16/05/2019', '15/01/2023', 300000, 'L6P9R2V7', 'K4S6D7F9');

INSERT INTO azienda.LAVORARE(CUP, nomeLab) VALUES
('J63G16029530490', 'Ganular'),
('J63G16029530490', 'Kupfert'),
('J63G16029530490', 'Aj Logics'),
('N50E60862030706', 'Kupfert'),
('N50E60862030706', 'Aptos'),
('N50E60862030706', 'Ganular'),
('N13G09807134097', 'Ganular'),

('Y0Z0A1B1C1D1E1F', 'Laboratorio Sigma'),
('Y0Z0A1B1C1D1E1F', 'Ricerche Omega'),
('Y0Z0A1B1C1D1E1F', 'Studio Delta'),
('A001B0002C00035', 'Sperimentazione Gamma'),
('A001B0002C00035', 'Fondazione Epsilon'),
('I0J0K0L0M0N0O0P', 'Laboratorio Sigma'),
('I0J0K0L0M0N0O0P', 'Sperimentazione Gamma'),
('I0J0K0L0M0N0O0P', 'Istituto Tecnologico Alpha'),
('Q0R0S0T0U0V0W0X', 'Dipartimento Zeta'),
('Q0R0S0T0U0V0W0X', 'Studio Delta'),
('E000F000G000H00', 'Istituto Tecnologico Alpha'),
('E000F000G000H00', 'Ricerche Omega'),

('OUNH7DFJ8T1M2KP', 'FactoryIoT'),
('OUNH7DFJ8T1M2KP', 'DataWorks'),
('OUNH7DFJ8T1M2KP', 'UserXperience'),
('CQYR9VXW5A6E1HZ', 'QuantumLab'),
('CQYR9VXW5A6E1HZ', 'TechLab'),
('P3K7D5F8L4S9J6G', 'TrustChain'),
('P3K7D5F8L4S9J6G', 'DataWorks'),
('P3K7D5F8L4S9J6G', 'CodeLab'),
('N1X4Z8B5G7V6H2J', 'TechLab'),
('N1X4Z8B5G7V6H2J', 'TrustChain'),
('N1X4Z8B5G7V6H2J', 'QuantumLab'),
('M5S8D4F9K2L1G7H', 'TestZone'),
('M5S8D4F9K2L1G7H', 'UserXperience'),
('M5S8D4F9K2L1G7H', 'CodeLab'),
('3N7D6G9X2R8K1S4', 'CodeLab'),
('3N7D6G9X2R8K1S4', 'UserXperience'),
('3N7D6G9X2R8K1S4', 'FactoryIoT');

INSERT INTO azienda.ATTREZZATURA(Descrizione, nomeLab, CUP, Costo) VALUES
('Stampante 3D', 'Aptos', 'O25H61393843815', 800),
('Computer portatile', 'Ganular', 'N50E60862030706', 500),
('Computer portatile', 'Ganular', 'N50E60862030706', 500),
('Proiettore', 'Ganular', 'N13G09807134097', 350),
('mBot Robot programmabile', 'Aptos', 'N50E60862030706', 150),
('Stampante 3D', 'Aptos', 'O25H61393843815', 800),
('Monitor 32''''', 'Kupfert', 'J63G16029530490', 200),
('Monitor 48''''', 'Kupfert', 'J63G16029530490', 375),
('Smartphone', 'Kupfert', 'J63G16029530490', 80),
('Stampante 3D', 'Aj Logics', 'O25H61393843815', 800),
('Computer portatile', 'Aptos', 'O25H61393843815', 750),
('Parte di ricambio per "SoftBank Robotics Pepper Robot"', 'Kupfert', 'N50E60862030706', 100),

('Stampante 3D professionale', 'Laboratorio Sigma', 'Y0Z0A1B1C1D1E1F', 5000),
('Microscopio elettronico', 'Ricerche Omega', 'Y0Z0A1B1C1D1E1F', 2000),
('Centrifuga professionale', 'Studio Delta', 'Y0Z0A1B1C1D1E1F', 13000),
('Stampante 3D avanzata', 'Sperimentazione Gamma', 'A001B0002C00035', 3000),
('Sistema di analisi proteomica', 'Fondazione Epsilon', 'A001B0002C00035', 500),
('Sequenziatore di nuova generazione', 'Fondazione Epsilon', 'A001B0002C00035', 3000),
('Sistema di imaging avanzato', 'Laboratorio Sigma', 'I0J0K0L0M0N0O0P', 100),
('Attrezzatura per la cultura cellulare', 'Istituto Tecnologico Alpha', 'I0J0K0L0M0N0O0P', 1900),
('Sistema di rifrazione', 'Dipartimento Zeta', 'Q0R0S0T0U0V0W0X', 3500),
('Calcolatore quantistico', 'Studio Delta', 'Q0R0S0T0U0V0W0X', 6000),
('Stampante 3D ultra avanzata', 'Studio Delta', 'Q0R0S0T0U0V0W0X', 300),
('Sistema di microscopia avanzato', 'Istituto Tecnologico Alpha', 'E000F000G000H00', 5000),
('Sistema di fermentazione', 'Ricerche Omega', 'E000F000G000H00', 20000),

('PC Desktop', 'FactoryIoT', 'OUNH7DFJ8T1M2KP', 3200),
('Monitor 32 inch.', 'FactoryIoT', 'OUNH7DFJ8T1M2KP', 350),
('Sensore', 'FactoryIoT', 'OUNH7DFJ8T1M2KP', 210),
('Sensore', 'FactoryIoT', 'OUNH7DFJ8T1M2KP', 560),
('Microcontroller M1', 'FactoryIoT', 'OUNH7DFJ8T1M2KP', 270),
('PC Portatile', 'FactoryIoT', '3N7D6G9X2R8K1S4', 1560),
('Proiettore', 'FactoryIoT', '3N7D6G9X2R8K1S4', 150),
('Microfono', 'FactoryIoT', '3N7D6G9X2R8K1S4', 40),
('Microcontroller M2', 'FactoryIoT', '3N7D6G9X2R8K1S4', 320),
('Modulo di rete', 'DataWorks', 'OUNH7DFJ8T1M2KP', 120),
('Server', 'DataWorks', 'OUNH7DFJ8T1M2KP', 1350),
('Server', 'DataWorks', 'OUNH7DFJ8T1M2KP', 870),
('PC ad alte prestazioni', 'DataWorks', 'OUNH7DFJ8T1M2KP', 5679),
('PC ad alte prestazioni', 'DataWorks', 'P3K7D5F8L4S9J6G', 7660),
('Server', 'DataWorks', 'P3K7D5F8L4S9J6G', 532),
('PC Portatile', 'DataWorks', 'P3K7D5F8L4S9J6G', 870),
('PC Portatile', 'UserXperience', 'OUNH7DFJ8T1M2KP', 1265),
('Adobe XD copia fisica', 'UserXperience', 'OUNH7DFJ8T1M2KP', 130),
('Eye Tracker', 'UserXperience', 'OUNH7DFJ8T1M2KP', 540),
('PC Portatile', 'UserXperience', 'M5S8D4F9K2L1G7H', 953),
('Tavoletta grafica', 'UserXperience', 'M5S8D4F9K2L1G7H', 220),
('PC Portatile', 'UserXperience', '3N7D6G9X2R8K1S4', 1230),
('Videocamera', 'UserXperience', '3N7D6G9X2R8K1S4', 250),
('Tavoletta grafica', 'UserXperience', '3N7D6G9X2R8K1S4', 220),
('PC quantistico', 'QuantumLab', 'CQYR9VXW5A6E1HZ', 5340),
('Strumento di visualizzazione', 'QuantumLab', 'CQYR9VXW5A6E1HZ', 3679),
('PC quantistico', 'QuantumLab', 'N1X4Z8B5G7V6H2J', 8760),
('Simulatore quantistico', 'QuantumLab', 'N1X4Z8B5G7V6H2J', 4500),
('Magnetometro', 'QuantumLab', 'N1X4Z8B5G7V6H2J', 1350),
('PC ad alte prestazioni', 'TechLab', 'CQYR9VXW5A6E1HZ', 6740),
('Monitor 43 inch.', 'TechLab', 'CQYR9VXW5A6E1HZ', 650),
('Misuratore onde elettromagnetiche', 'TechLab', 'CQYR9VXW5A6E1HZ', 1455),
('Criptatore Hardware', 'TechLab', 'CQYR9VXW5A6E1HZ', 3650),
('Scanner', 'TechLab', 'N1X4Z8B5G7V6H2J', 2700),
('Robot specializzato', 'TechLab', 'N1X4Z8B5G7V6H2J', 6000),
('PC portatile', 'CodeLab', 'P3K7D5F8L4S9J6G', 1200),
('PC Dekstop', 'CodeLab', 'M5S8D4F9K2L1G7H', 3500),
('Monitor 24 inch.', 'CodeLab', 'M5S8D4F9K2L1G7H', 370),
('Lavagna', 'CodeLab', 'M5S8D4F9K2L1G7H', 200),
('PC portatile', 'CodeLab', '3N7D6G9X2R8K1S4', 950),
('Proiettore', 'CodeLab', '3N7D6G9X2R8K1S4', 250),
('Microfono', 'CodeLab', '3N7D6G9X2R8K1S4', 150),
('PC Desktop', 'TestZone', 'M5S8D4F9K2L1G7H', 3560),
('Monitor 42 inch.', 'TestZone', 'M5S8D4F9K2L1G7H', 660),
('Smartphone', 'TestZone', 'M5S8D4F9K2L1G7H', 550),
('Misuratore di prestazioni', 'TestZone', 'M5S8D4F9K2L1G7H', 1350),
('Analizzatore del traffico di rete', 'TestZone', 'M5S8D4F9K2L1G7H', 3360),
('Generatore di segnali', 'TestZone', 'M5S8D4F9K2L1G7H', 4320);

INSERT INTO azienda.DIP_PROGETTO(Matricola, Nome, Cognome, codfiscale, Indirizzo, dataNascita, dataAssunzione, Scadenza, CUP, Costo) VALUES
('FP2190PL', 'Fabio', 'Brancaccio', 'BRNFBA85M05H501N', 'Via dei Marrucini, 6, 00185 Roma RM', '05/08/1985', '28/07/2020', '28/12/2022', 'J63G16029530490', 125),
('GP4291SO', 'Fabio', 'Brancaccio', 'BRNFBA85M05H501N', 'Via dei Marrucini, 6, 00185 Roma RM', '05/08/1985', '29/12/2022', '10/01/2023', 'J63G16029530490', 200),
('CF1944AF', 'Claudia', 'Romiti', 'RMTCLD75M50L736F', NULL, '10/08/1975', '12/03/2013', '05/11/2013', 'N13G09807134097', 250),
('HO1034OK', 'Maria', 'Pepe', 'PPEMRA00D54B519B', 'Corso Vittorio Emanuele II, 51, 86100 Campobasso CB', '14/04/2000', '09/05/2024', '12/11/2026', 'N50E60862030706', 300),
('AL1230KF', 'Gianluigi', 'Liguori', 'LGRGLG00P08B519P', 'Via Fossi, 16, 86010 Mirabello Sannitico CB', '08/09/2000', '09/05/2024', '12/11/2026', 'N50E60862030706', 300),

('O9A7EMAR', 'Alain', 'Aspect', 'LNASCT87H15Z110E', 'chemin Baron 9', '15/06/1987', '2022-10-01', '2023-09-30', 'Y0Z0A1B1C1D1E1F', 5000),
('F7I6BWVU', 'David', 'Baltimore', 'DVDBTM98D07Z512P', 'Rotonda Jelena 4', '07/03/1998', '2022-10-01', '2023-09-30', 'A001B0002C00035', 5000),
('BTNV7YB3', 'Allen', 'Bard', 'LLNBRD93T18B193E', 'Price Drive 162', '18/12/1993', '2022-01-01', '2023-06-30', 'I0J0K0L0M0N0O0P', 5000),
('NZ38YMEH', 'Gordon', 'Moore', 'GRDMRO99A03C388B', 'Strada Bianchi 12', '03/01/1999', '2022-07-01', '2023-01-15', 'Q0R0S0T0U0V0W0X', 5000),
('DN8OEE8P', 'Shinya', 'Yamanaka', 'SHNYNK00P04Z221Y', 'Shat Ngon Estate', '04/09/2000', '2023-01-01', '2023-06-30', 'E000F000G000H00', 5000),

('K7H2F8L4', 'Valeria', 'Milani', 'MLNVLR70D19F205E', 'Via Foria, 19, 20021 Milano MI', '19/04/1970', '19/01/2020', '05/01/2024', 'OUNH7DFJ8T1M2KP', 120000),
('E3R9G5N1', 'Italo', 'Mazzi', 'MZZTLI71B23F839Q', 'Via Piave, 107, 80131 Napoli NA', '23/02/1971', '20/04/2020', '10/07/2024', 'CQYR9VXW5A6E1HZ', 145442),
('T4V7A6Z9', 'Sara', 'Esposito', 'SPSSRA85D28A271Z', 'Via Solfatara, 56, 60125 Ancona AN', '28/04/1985', '01/12/2021', '14/10/2025', 'P3K7D5F8L4S9J6G', 165350),
('Q1D8B6K2', 'Vincenzo', 'Trevisano', 'TRVVCN76H10D969R', 'Piazza della Repubblica, 140, 16121 Genova GE', '10/06/1976', '13/03/2022', '01/05/2025', 'N1X4Z8B5G7V6H2J', 115000),
('J9X6C3Y2', 'Giancarlo', 'Gallo', 'GLLGCR82D04G273Q', 'Via Piccinni, 88, 90131 Palermo PA', '04/04/1982', '27/04/2018', '20/07/2023', 'M5S8D4F9K2L1G7H', 133500),
('M2P5F7R1', 'Elio', 'Romano', 'RMNLEI85H30L219V', 'Via Alessandro Manzoni, 70, 10143 Torino TO', '30/06/1985', '28/04/2019', '10/01/2023', '3N7D6G9X2R8K1S4', 111500); --scaduto