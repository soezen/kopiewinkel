insert into GebruikerTypes (Naam, Standaard)
values ('Beheerder', 1);
insert into GebruikerTypes (Naam, Standaard)
values ('Leerkracht', 0);

insert into Gebruikers (Naam, Status, GebruikerType)
values ('Eline', 1, 1);

insert into PrijsTypes (Naam)
values ('Standaard');

insert into OpdrachtTypes (Naam, PrijsType, Van)
values ('Standaard', 1, '2012-01-01');

insert into Opdrachten (Opdrachtgever, OpdrachtType, Bestand, Aantal, AanmaakDatum, Status)
values (1, 1, 'bestand.txt', 30, '2012-02-12', 2);
insert into Opdrachten (Opdrachtgever, OpdrachtType, Bestand, Aantal, AanmaakDatum, Status)
values (1, 1, 'bestand.txt', 31, '2012-02-12', 1);
insert into Opdrachten (Opdrachtgever, OpdrachtType, Bestand, Aantal, AanmaakDatum, Status)
values (1, 1, 'bestand.txt', 32, '2012-02-12', 3);
insert into Opdrachten (Opdrachtgever, OpdrachtType, Bestand, Aantal, AanmaakDatum, Status)
values (1, 1, 'bestand.txt', 33, '2012-02-12', 4);

insert into Rechten (Gebruiker, Constrained) 
values (1, 1);