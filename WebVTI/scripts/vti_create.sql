create table GebruikerTypes (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Standaard TINYINT(1) NOT NULL,
	PRIMARY KEY (ID),
	UNIQUE (Naam),
  CHECK (ID < 12000000)
);
alter table GebruikerTypes AUTO_INCREMENT = 11000000;

create table Criteria (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Eigenaar INT(8) NOT NULL,
	Tabel VARCHAR(50) NOT NULL,
	PRIMARY KEY (ID),
	UNIQUE (Naam, Eigenaar),
  CHECK (ID < 15000000)
);
alter table Criteria AUTO_INCREMENT = 14000000;

create table Leerlingen (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  StartDatum DATE NOT NULL,
  EindDatum DATE,
  Criteria INT(8),
  PRIMARY KEY (ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL,
  CHECK (ID < 17000000),
  CHECK ((StartDatum < EindDatum) or (EindDatum is null))
);
alter table Leerlingen AUTO_INCREMENT = 16000000;

create table Gebruikers (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Wachtwoord VARCHAR(50),
	Email VARCHAR(75),
	Status TINYINT(1) NOT NULL,
	LaatstIngelogd DATETIME,
	GebruikerType INT(8) NOT NULL,
	Criteria INT(8),
	PRIMARY KEY (ID),
	UNIQUE (Naam),
	CHECK (ID < 2000000),
	FOREIGN KEY (GebruikerType) REFERENCES GebruikerTypes(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Gebruikers AUTO_INCREMENT = 1000000;
alter table Criteria add constraint `FK-CriteriaEigenaar` FOREIGN KEY (Eigenaar) REFERENCES Gebruikers(ID);

create table Doelgroepen (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(75) NOT NULL,
	Graad INT(1),
	Criteria INT(8),
	PRIMARY KEY (ID),
	UNIQUE (Graad, Naam),
	CHECK (Graad in (1, 2, 3, 4, 0)),
	CHECK (ID < 3000000),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Doelgroepen AUTO_INCREMENT = 2000000;

create table DoelgroepLeerlingen (
  Leerling INT(8) NOT NULL,
  Doelgroep INT(8) NOT NULL,
  Groep VARCHAR(10) NOT NULL,
  Schooljaar INT(4) NOT NULL,
  PRIMARY KEY (Leerling, Doelgroep, Schooljaar),
  FOREIGN KEY (Leerling) REFERENCES Leerlingen(ID),
  FOREIGN KEY (Doelgroep) REFERENCES Doelgroepen(ID)
);

create table PrijsTypes (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	PRIMARY KEY (ID), 
	UNIQUE (Naam),
  CHECK (ID < 10000000)
);
alter table PrijsTypes AUTO_INCREMENT = 9000000;

create table OpdrachtTypes (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Omschrijving VARCHAR(255) NOT NULL,
	PrijsType INT(8) NOT NULL,
	Van DATE NOT NULL,
	Tot DATE,
	Criteria INT(8),
	PRIMARY KEY (ID),
	CHECK (Tot > Van or Tot is null),
  CHECK (ID < 7000000),
	FOREIGN KEY (PrijsType) REFERENCES PrijsTypes(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table OpdrachtTypes AUTO_INCREMENT = 6000000;

create table Opdrachten (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Opdrachtgever INT(8) NOT NULL,
	AanmaakDatum DATETIME NOT NULL,
	OpdrachtType INT(8) NOT NULL,
	Bestand VARCHAR(200) NOT NULL,
	Aantal INT(3) NOT NULL,
	Status VARCHAR(20) NOT NULL,
	PrintDatum DATETIME,
	Uitvoerder INT(8),
	Commentaar VARCHAR(255),
	Criteria INT(8),
	PRIMARY KEY (ID),
	CHECK (Aantal > 0),
	CHECK (Status in ('AANGEVRAAGD', 'IN_BEHANDELING', 'AFGEWERKT', 'GEWEIGERD', 'DATA_TEKORT')),
	CHECK (PrintDatum is null or PrintDatum > AanmaakDatum),
	CHECK (ID < 4000000),
	FOREIGN KEY (OpdrachtGever) REFERENCES Gebruikers(ID),
	FOREIGN KEY (OpdrachtType) REFERENCES OpdrachtTypes(ID),
	FOREIGN KEY (Uitvoerder) REFERENCES Gebruikers(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Opdrachten AUTO_INCREMENT = 3000000;

create table OptieTypes (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Omschrijving VARCHAR(255) NOT NULL,
	Van DATE NOT NULL,
	Tot DATE,
	Min INT(2) NOT NULL,
	Max INT(2) NOT NULL,
	Status VARCHAR(20) NOT NULL,
	Volgorde INT(2) NOT NULL,
	Criteria INT(8),
	PRIMARY KEY (ID),
	CHECK (Tot > Van or Tot is null),
	CHECK (Max >= Min or Max = -1),
	CHECK (Min >= 0),
	CHECK (Status in ('HUIDIG', 'VORIG', 'NIEUW')),
	CHECK (Volgorde >= 0),
	CHECK (ID < 5000000),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table OptieTypes AUTO_INCREMENT = 4000000;

create table OpdrachtTypeOpties (
  OpdrachtType INT(8) NOT NULL,
  OptieType INT(8) NOT NULL,
  PRIMARY KEY (OpdrachtType, OptieType),
  FOREIGN KEY (OpdrachtType) REFERENCES OpdrachtTypes(ID),
  FOREIGN KEY (OptieType) REFERENCES OptieTypes(ID)
);

create table Opties (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Omschrijving VARCHAR(255) NOT NULL,
	OptieType INT(8) NOT NULL,
	Van DATE NOT NULL,
	Tot DATE,
	Status VARCHAR(20) NOT NULL,
	Volgorde INT(2) NOT NULL,
	Criteria INT(8),
	PRIMARY KEY (ID),
	CHECK (Van < Tot or Tot is null),
	CHECK (Volgorde >= 0),
	CHECK (Status in ('HUIDIG', 'VORIG', 'NIEUW')),
	CHECK (ID < 6000000),
	FOREIGN KEY (OptieType) REFERENCES OptieTypes(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Opties AUTO_INCREMENT = 5000000;

create table OpdrachtOpties (
	Opdracht INT(8) NOT NULL,
	Optie INT(8) NOT NULL,
	PRIMARY KEY (Opdracht, Optie),
	FOREIGN KEY (Opdracht) REFERENCES Opdrachten(ID),
	FOREIGN KEY (Optie) REFERENCES Opties(ID)
);
create table Toewijzingen (
	Gebruiker INT(8) NOT NULL,
	Doelgroep INT(8) NOT NULL,
  Groep INT(1),
	PRIMARY KEY (Gebruiker, Doelgroep),
	FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
	FOREIGN KEY (Doelgroep) REFERENCES Doelgroepen(ID)
);
create table OpdrachtDoelgroepen (
	Opdracht INT(8) NOT NULL,
	Doelgroep INT(8) NOT NULL,
	PRIMARY KEY (Opdracht, Doelgroep),
	FOREIGN KEY (Opdracht) REFERENCES Opdrachten(ID),
	FOREIGN KEY (Doelgroep) REFERENCES Doelgroepen(ID)
);

create table Constraints (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Type VARCHAR(20) NOT NULL,
	Constrainer INT(8) NOT NULL,
	Constrained VARCHAR(50) NOT NULL,
	Standaard TINYINT(1) NOT NULL,
  Wederkerig TINYINT(1) NOT NULL,
	Criteria INT(8),
	PRIMARY KEY (ID),
	CHECK (Type in ('VERPLICHT', 'VERBIEDT', 'PRIJS', 'FORMULE')),
	CHECK ((Constrainer >= 4000000 and Constrainer < 6000000 and Type in (1, 2, 3)) 
		or (Constrainer >= 8000000 and Constrainer < 9000000 and Type = 4)),
	CHECK ((Constrained >= 4000000 and Constrained < 6000000 and Type in (1, 2)) 
		or (Constrained >= 8000000 and Constrained < 9000000 and Type = 3)
		or (Type = 4)),
  CHECK ((Type = 'VERBIEDT' and Wederkerig) or Type != 'VERBIEDT'),
  CHECK (ID < 8000000),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Constraints AUTO_INCREMENT = 7000000;

create table Condities (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Commentaar VARCHAR(255),
	Linker INT(8),
	Connector VARCHAR(20),
	Rechter INT(8),
	Expressie VARCHAR(100),
	Criteria INT(8),
	PRIMARY KEY (ID),
	UNIQUE (Naam),
        CHECK (ID < 11000000),
	CHECK (Connector in ('AND', 'OR', 'NOT')), 
	FOREIGN KEY (Linker) REFERENCES Condities(ID),
	FOREIGN KEY (Rechter) REFERENCES Condities(ID)
);
alter table Condities AUTO_INCREMENT = 10000000;

create table Prijzen (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	PrijsType INT(8) NOT NULL,
	Conditie INT(8),
	Bedrag DECIMAL(9,3) NOT NULL,
	Eenheid VARCHAR(20) NOT NULL,
	Type VARCHAR(20) NOT NULL,
	Van DATE NOT NULL,
	Tot DATE,
	Criteria INT(8),
	PRIMARY KEY (ID),
	CHECK (Bedrag >= 0),
        CHECK (ID < 9000000),
	CHECK (Eenheid in ('KOPIE', 'PAGINA', 'OPDRACHT')),
	CHECK (Type in ('OPTIE', 'OPTIETYPE', 'OPDRACHT', 'FORMULE')),
	CHECK (Van < Tot or Tot is null),
	FOREIGN KEY (PrijsType) REFERENCES PrijsTypes(ID),
	FOREIGN KEY (Conditie) REFERENCES Condities(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Prijzen AUTO_INCREMENT = 8000000;

create table LayoutProperties (
	Gebruiker INT(8) NOT NULL,
	OptieType INT(8) NOT NULL,
	Zichtbaar INT(1) NOT NULL,
	Locatie INT(1) NOT NULL,
	Selectie INT(1) NOT NULL,
	Weergave INT(1) NOT NULL,
	PRIMARY KEY (Gebruiker, OptieType),
	FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
	FOREIGN KEY (OptieType) REFERENCES OptieTypes(ID), 
	CHECK (Zichtbaar in ('ALTIJD', 'VERPLICHT', 'NOOIT')),
	CHECK (Locatie in ('APART', 'VERPLICHT', 'BEIDE', 'NERGENS')),
	CHECK (Selectie in ('AUTOMATISCH', 'HANDMATIG', 'BEIDE')),
	CHECK (Weergave in ('RADIO', 'CHECK', 'LIJST', 'TABEL'))
);

create table Rechten (
	Gebruiker INT(8) NOT NULL,
	Constrained INT(8) NOT NULL,
	PRIMARY KEY (Gebruiker, Constrained),
	CHECK ((Gebruiker >= 1000000 and Gebruiker < 2000000) 
		or (Gebruiker >= 11000000 and Gebruiker < 12000000)),
	CHECK ((Constrained >= 4000000 and Constrained < 7000000)
		or (Constrained >= 15000000 and Constrained < 16000000)
		or (Constrained >= 12000000 and Constrained < 13000000))
);

create table InputVelden (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Type VARCHAR(20) NOT NULL,
  Min INT(3),
  Max INT(3),
	PRIMARY KEY (ID),
	UNIQUE (Naam, Type),
	CHECK (Type in ('DATUM', 'TEKST', 'GETAL', 'VAST', 'EMAIL')),
        CHECK (ID < 13000000),
  CHECK (Max >= Min or Max is null or Min is null)
);
alter table InputVelden AUTO_INCREMENT = 12000000;

create table OpdrachtTypeInput (
	InputVeld INT(8) NOT NULL,
	OpdrachtType INT(8) NOT NULL,
	Verplicht TINYINT(1) NOT NULL,
	Wijzigbaar TINYINT(1) NOT NULL,
	Zichtbaar TINYINT(1) NOT NULL,
	Volgorde INT(2) NOT NULL,
	PRIMARY KEY (InputVeld, OpdrachtType),
	UNIQUE (OpdrachtType, Volgorde),
        FOREIGN KEY (InputVeld) REFERENCES InputVelden(ID),
	FOREIGN KEY (OpdrachtType) REFERENCES OpdrachtTypes(ID),
	CHECK (Volgorde >= 0)
);

create table InputWaarden (
	InputVeld INT(8) NOT NULL,
	Opdracht INT(8) NOT NULL,
	Waarde VARCHAR(100) NOT NULL,
	PRIMARY KEY (InputVeld, Opdracht),
	FOREIGN KEY (InputVeld) REFERENCES InputVelden(ID),
	FOREIGN KEY (Opdracht) REFERENCES Opdrachten(ID)
);

create table Aanvragen (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Eigenaar INT(8) NOT NULL,
	Type VARCHAR(20) NOT NULL,
	Status VARCHAR(20) NOT NULL,
	AanmaakDatum DATETIME NOT NULL,
	AfwerkDatum DATETIME,
	Commentaar VARCHAR(255),
	Veld INT(8),
	CorrectieWaarde VARCHAR(255),
	Bestemmelingen VARCHAR(200) NOT NULL,
	Criteria INT(8),
	PRIMARY KEY (ID),
  CHECK (ID < 14000000),
	CHECK (Type in ('RECHT', 'CORRECTIE', 'INFO', 'BUG')),
	CHECK (Status in ('NIEUW', 'GELEZEN', 'AFGEWERKT', 'IN_BEHANDELING', 'GEWEIGERD', 'GEANNULEERD')),
	CHECK (AfwerkDatum > AanmaakDatum or AfwerkDatum is null),
	CHECK (Commentaar is not null or Veld is not null),
	FOREIGN KEY (Eigenaar) REFERENCES Gebruikers(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID)
);
alter table Aanvragen AUTO_INCREMENT = 13000000;

create table Meldingen (
	Gebruiker INT(8) NOT NULL,
	Volgt INT(8) NOT NULL,
	Gewijzigd DATETIME NOT NULL,
	Email TINYINT(1) NOT NULL,
	PRIMARY KEY (Gebruiker, Volgt),
	FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
	CHECK ((Volgt >= 13000000 and Volgt < 14000000) 
		or (Volgt >= 3000000 and Volgt < 4000000))
);

create table StartPagina (
	Gebruiker INT(8) NOT NULL,
	Criteria INT(8) NOT NULL,
	PRIMARY KEY (Gebruiker, Criteria),
	FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
	FOREIGN KEY (Criteria) REFERENCES Criteria(ID)
);

create table MenuItems (
	ID INT(8) NOT NULL AUTO_INCREMENT,
	Naam VARCHAR(50) NOT NULL,
	Link VARCHAR(255) NOT NULL,
	Extern TINYINT(1) NOT NULL,
	Volgorde INT(2) NOT NULL,
	PRIMARY KEY (ID),
  CHECK (ID < 16000000),
	CHECK (Volgorde >= 0)
);
alter table MenuItems AUTO_INCREMENT = 15000000;
