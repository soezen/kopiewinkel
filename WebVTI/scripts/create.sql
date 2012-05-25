create table Criteria (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Eigenaar INT(8) NOT NULL,
  Tabel VARCHAR(50) NOT NULL,
  PRIMARY KEY(ID)
);
alter table Criteria AUTO_INCREMENT = 14000000;

create table OptieTypes (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Omschrijving VARCHAR(255) NOT NULL,
  Max INT(2) NOT NULL DEFAULT -1,
  Min INT(2) NOT NULL DEFAULT 0,
  Van DATE NOT NULL,
  Tot DATE,
  Status INT(1),
  Volgorde INT(3),
  Criteria INT(8),
  CHECK (Min >= 0),
  CHECK (Max >= Min),
  CHECK (Tot > Van),
  CHECK (Status in (1, 2, 3)),
  CHECK (Volgorde > 0),
  UNIQUE (Naam),
  UNIQUE (Volgorde),
  PRIMARY KEY (ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table OptieTypes AUTO_INCREMENT =4000000;

create table Opties (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Omschrijving VARCHAR(255) NOT NULL,
  Van DATE NOT NULL,
  Tot DATE,
  Status INT(1) NOT NULL,
  Volgorde INT(3) NOT NULL,
  OptieType INT(8) NOT NULL,
  Criteria INT(8),
  CHECK (Status in (1, 2, 3)),  
  CHECK (Tot > Van),
  CHECK (Volgorde > 0),
  UNIQUE (OptieType, Naam),
  UNIQUE (Volgorde),
  PRIMARY KEY (ID),
  FOREIGN KEY (OptieType) REFERENCES OptieTypes(ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Opties AUTO_INCREMENT =5000000;

create table Condities (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Commentaar VARCHAR(300),
  Conditie1 INT(8),
  Operator INT(1),
  Conditie2 INT(8),
  Expressie INT(8),
  Criteria INT(8),
  CHECK (Conditie2 is not null or expressie is not null),
  CHECK (Operator in (1, 2, 3)),
  UNIQUE (Naam),
  PRIMARY KEY (ID),
  FOREIGN KEY (Conditie1) REFERENCES Condities(ID),
  FOREIGN KEY (Conditie2) REFERENCES Condities(ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Condities AUTO_INCREMENT =10000000;

create table Constraints (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Type TINYINT(1) NOT NULL,
  Field1 INT(8) NOT NULL,
  Field2 INT(8) NOT NULL,
  Standaard TINYINT(1),
  Criteria INT(8),
  PRIMARY KEY (ID),
  CHECK (Type in (1, 2, 3, 4)),
  CHECK (Standaard in (0, 1)),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Constraints AUTO_INCREMENT =7000000;

create table Doelgroepen (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Graad INT(1),
  Schooljaar INT(4) NOT NULL,
  Aantal INT(4) NOT NULL,
  Criteria INT(8),
  PRIMARY KEY (ID),
  CHECK (Graad in (1, 2, 3)),
  CHECK (Schooljaar > 1990),
  CHECK (Aantal > 0),
  UNIQUE (Schooljaar, Graad, Naam),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Doelgroepen AUTO_INCREMENT =2000000;

create table GebruikerTypes (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Standaard INT(1) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (Naam),
  CHECK (Standaard in (0, 1))
);
alter table GebruikerTypes AUTO_INCREMENT =11000000;

create table Gebruikers (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Wachtwoord VARCHAR(50),
  Email VARCHAR(100),
  LastLogon DATETIME,
  Status TINYINT(1) NOT NULL,
  GebruikerType INT(8) NOT NULL,
  Criteria INT(8),
  PRIMARY KEY (ID),
  UNIQUE (Naam),
  CHECK (Status in (0, 1)),
  FOREIGN KEY (GebruikerType) REFERENCES GebruikerTypes(ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID) ON DELETE SET NULL
);
alter table Gebruikers AUTO_INCREMENT=1000000;

alter table Criteria
add constraint FOREIGN KEY (Eigenaar) REFERENCES Gebruikers(ID);

create table InputVelden (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Type TINYINT(1) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (Naam, Type),
  CHECK (Type in (1, 2, 3, 4))
);
alter tabel InputVelden AUTO_INCREMENT =12000000;

create table PrijsTypes (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  UNIQUE (Naam),
  PRIMARY KEY (ID)
);

create table OpdrachtTypes (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Naam VARCHAR(50) NOT NULL,
  Omschrijving VARCHAR(255) NOT NULL,
  PrijsType INT(8) NOT NULL,
  Van Date NOT NULL,
  Tot Date,
  Criteria INT(8),
  PRIMARY KEY (ID),
  UNIQUE (Naam),
  FOREIGN KEY (PrijsType) REFERENCES PrijsTypes(ID), 
  CHECK (Tot > Van)
);

create table Opdrachten (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Opdrachtgever INT(8) NOT NULL,
  Uitvoerder INT(8),
  OpdrachtType INT(8) NOT NULL,
  Bestand VARCHAR(255) NOT NULL,
  Aantal INT(4) NOT NULL,
  AanmaakDatum DATETIME NOT NULL,
  PrintDatum DATETIME,
  Status INT(1) NOT NULL,
  Commentaar VARCHAR(255),
  Criteria INT(8),
  PRIMARY KEY (ID),
  FOREIGN KEY (Opdrachtgever) REFERENCES Gebruikers(ID),
  FOREIGN KEY (OpdrachtType) REFERENCES OpdrachtTypes(ID),
  FOREIGN KEY (Uitvoerder) REFERENCES Gebruikers(ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID),
  CHECK (Aantal > 0),
  CHECK (PrintDatum >= AanmaakDatum),
  CHECK (Status in (1, 2, 3, 4, 5))
);

create table InputWaarden (
  Opdracht INT(8) NOT NULL,
  InputVeld INT(8) NOT NULL,
  Waarde VARCHAR(50) NOT NULL,
  PRIMARY KEY (Opdracht, InputVeld),
  FOREIGN KEY (Opdracht) REFERENCES Opdrachten(ID),
  FOREIGN KEY (InputVeld) REFERENCES InputVelden(ID)
);

create table LayoutProperties (
  Gebruiker INT(8) NOT NULL,
  OptieType INT(8) NOT NULL,
  Zichtbaar INT(1) NOT NULL,
  Locatie INT(1) NOT NULL,
  Selectie INT(1) NOT NULL,
  Weergave INT(1) NOT NULL,
  PRIMARY KEY (Gebruiker, OptieType),
  CHECK (Zichtbaar in (1, 2, 3)),
  CHECK (Locatie in (1, 2, 3, 4)),
  CHECK (Selectie in (1, 2, 3)),
  CHECK (Weergave in (1, 2, 3, 4)),
  FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
  FOREIGN KEY (OptieType) REFERENCES OptieTypes(ID)
);

create table OpdrachtDoelgroepen (
  Opdracht INT(8) NOT NULL,
  Doelgroep INT(8) NOT NULL,
  PRIMARY KEY (Opdracht, Doelgroep),
  FOREIGN KEY (Opdracht) REFERENCES Opdrachten(ID),
  FOREIGN KEY (Doelgroep) REFERENCES Doelgroepen(ID)
);

create table OpdrachtOpties (
  Optie INT(8) NOT NULL,
  Opdracht INT(8) NOT NULL,
  PRIMARY KEY (Optie, Opdracht),
  FOREIGN KEY (Optie) REFERENCES Opties(ID),
  FOREIGN KEY (Opdracht) REFERENCES Opdrachten(ID)
);

create table OpdrachtTypeInput (
  OpdrachtType INT(8) NOT NULL,
  InputVeld INT(8) NOT NULL,
  Volgorde INT(2) NOT NULL,
  Verplicht TINYINT(1) NOT NULL,
  PRIMARY KEY (OpdrachtType, InputVeld),
  UNIQUE (Volgorde),
  FOREIGN KEY (OpdrachtType) REFERENCES OpdrachtTypes(ID),
  FOREIGN KEY (InputVeld) REFERENCES InputVelden(ID),
  CHECK (Volgorde > 0),
  CHECK (Verplicht in (0, 1))
);

create table Prijzen (
  ID INT(8) NOT NULL,
  Van Date NOT NULL,
  Tot Date,
  Bedrag DECIMAL(9,3) NOT NULL,
  PrijsType INT(8) NOT NULL,
  Conditie INT(8),
  Eenheid TINYINT(1) NOT NULL,
  Type TINYINT(1) NOT NULL,
  Criteria INT(8),
  PRIMARY KEY (ID),
  CHECK (Tot > Van),
  CHECK (Bedrag >= 0),
  CHECK (Eenheid in (1, 2, 3)),
  CHECK (Type in (1, 2, 3)),
  FOREIGN KEY (PrijsType) REFERENCES PrijsTypes(ID), 
  FOREIGN KEY (Conditie) REFERENCES Condities(ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID)
);

create table Rechten (
  Gebruiker INT(8) NOT NULL,
  Constrained INT(8) NOT NULL,
  PRIMARY KEY (Gebruiker, Constrained)
);

create table Toewijzingen (
  Opdrachtgever INT(8) NOT NULL,
  Doelgroep INT(8) NOT NULL,
  PRIMARY KEY (Opdrachtgever, Doelgroep),
  FOREIGN KEY (Opdrachtgever) REFERENCES Gebruikers(ID),
  FOREIGN KEY (Doelgroep) REFERENCES Doelgroepen(ID)
);

create table Aanvragen (
  ID INT(8) NOT NULL AUTO_INCREMENT,
  Aanvrager INT(8) NOT NULL,
  Type TINYINT(1) NOT NULL,
  Status TINYINT(1) NOT NULL,
  AanmaakDatum DATETIME NOT NULL,
  AfwerkDatum DATETIME,
  Commentaar VARCHAR(255),
  Veld VARCHAR(100),
  CorrectieWaarde VARCHAR(100),
  Bestemmelingen VARCHAR(200),
  Criteria INT(8),
  PRIMARY KEY (ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID),
  FOREIGN KEY (Aanvrager) REFERENCES Gebruikers(ID),
  CHECK (Type in (1, 2, 3, 4)),
  CHECK (Status in (1, 2, 3, 4, 5, 6)),
  CHECK (AfwerkDatum >= AanmaakDatum),
  CHECK (Commentaar is null and Veld is null)
);

create table Meldingen (
  Gebruiker INT(8) NOT NULL,
  Volgt INT(8) NOT NULL,
  GewijzigdOp DATETIME NOT NULL,
  Email TINYINT(1) NOT NULL,
  PRIMARY KEY (Gebruiker, Volgt),
  FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
  CHECK (Email in (0, 1))
);

create table StartPagina (
  Gebruiker INT(8) NOT NULL,
  Criteria INT(8) NOT NULL,
  PRIMARY KEY (Gebruiker, Criteria),
  FOREIGN KEY (Gebruiker) REFERENCES Gebruikers(ID),
  FOREIGN KEY (Criteria) REFERENCES Criteria(ID)
);
