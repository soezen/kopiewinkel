insert into gebruikertypes (id, naam, standaard) values (11000001, 'Leerkrachten',  0);
insert into gebruikertypes (id, naam, standaard) values (11000002, 'Beheerders',    1);
insert into gebruikertypes (id, naam, standaard) values (11000003, 'Administratie', 0);

insert into gebruikers (id, naam, email, status, gebruikertype) values (1000001, 'Anne Saelens',          'rogge.suzan@gmail.com', 1, 11000001);
insert into gebruikers (id, naam, email, status, gebruikertype) values (1000002, 'Monique Lefebvre',      'rogge.suzan@gmail.com', 1, 11000002);
insert into gebruikers (id, naam, email, status, gebruikertype) values (1000003, 'Guillaume Buysschaert', 'rogge.suzan@gmail.com', 1, 11000003);

insert into inputvelden (id, naam, type) values (12000001, 'Opdrachtgever', 4);
insert into inputvelden (id, naam, type) values (12000002, 'Bestand',       4);
insert into inputvelden (id, naam, type) values (12000003, 'Aantal',        4);
insert into inputvelden (id, naam, type) values (12000004, 'Commentaar',    4);
insert into inputvelden (id, naam, type) values (12000005, 'Prijs',         4);

insert into doelgroepen (id, naam, graad, schooljaar, aantal) values (2000001, '1ste jaar Mechanica', 1, 2011, 23);
insert into doelgroepen (id, naam, graad, schooljaar, aantal) values (2000002, '1ste jaar Kunst',     1, 2011, 12);

insert into toewijzingen (gebruiker, doelgroep) values (11000001, 2000001);

insert into prijstypes (id, Naam) values (9000001, 'Standaard');
insert into prijstypes (id, Naam) values (9000002, 'Gratis');

insert into opdrachttypes (id, naam, prijstype, van, omschrijving) values (6000001, 'Standaard',      9000001, '2012-01-01', 'Standaard invulbon voor leerkrachten');
insert into opdrachttypes (id, naam, prijstype, van, omschrijving) values (6000002, 'Administratie',  9000002, '2012-01-01', 'Invulbon voor administratieve opdrachten, hoeft niet aangerekend worden');
insert into opdrachttypes (id, naam, prijstype, van, omschrijving) values (6000003, 'Prive Opdracht', 9000001, '2012-01-01', 'Opdrachten voor privé, deze moeten betaald worden door de opdrachtgever');

insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000001, 12000002, 1, 1, 1);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000001, 12000003, 1, 1, 2);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000001, 12000005, 1, 0, 3);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000001, 12000004, 0, 1, 4);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000002, 12000001, 1, 1, 1);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000002, 12000002, 1, 1, 2);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000002, 12000003, 1, 1, 3);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000002, 12000004, 0, 1, 4);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000003, 12000002, 1, 1, 1);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000003, 12000003, 1, 1, 2);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000003, 12000004, 0, 1, 4);
insert into opdrachttypeinput (opdrachttype, inputveld, verplicht, wijzigbaar, volgorde) values (6000003, 12000005, 1, 0, 3);

insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000001, 'Druktype',      1, 1, '2012-01-01', 1, 1, 'hoe een pagina gedrukt moet worden');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000002, 'Kleur',         1, 0, '2012-01-01', 1, 2, 'welke kleur de pagina''s moeten hebben');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000003, 'Gewicht',       1, 0, '2012-01-01', 1, 3, 'welk gewicht een pagina heeft');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000004, 'Formaat',       1, 0, '2012-01-01', 1, 4, 'de grootte van de pagina''s');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000005, 'Nieten',        1, 0, '2012-01-01', 1, 5, 'hoe de pagina''s aan elkaar geniet moeten worden');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000006, 'Andere',       -1, 0, '2012-01-01', 1, 6, 'andere mogelijke opties');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000007, 'Kleur Kaft',    1, 0, '2012-01-01', 1, 7, 'kleur van de kaft');
insert into optietypes (id, naam, max, min, van, status, volgorde, omschrijving) values(4000008, 'Gewicht Kaft',  1, 0, '2012-01-01', 1, 8, 'gewicht van de kaft');

insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000001, 'Recto',          '2012-01-01', 1, 1, 4000001, 'pagina''s enkel aan de voorkant bedrukken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000002, 'Recto Verso',    '2012-01-01', 1, 2, 4000001, 'pagina''s aan beide kanten bedrukken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000003, 'Blauw',          '2012-01-01', 1, 1, 4000002, 'blauwe pagina''s gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000004, 'Rood',           '2012-01-01', 1, 2, 4000002, 'rode pagina''s gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000005, 'Geel',           '2012-01-01', 1, 3, 4000002, 'gele pagina''s gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000006, '120gr',          '2012-01-01', 1, 1, 4000003, 'pagina''s van 120 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000007, '160gr',          '2012-01-01', 1, 2, 4000003, 'pagina''s van 160 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000008, '170gr',          '2012-01-01', 1, 3, 4000003, 'pagina''s van 170 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000009, '200gr',          '2012-01-01', 1, 4, 4000003, 'pagina''s van 200 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000010, 'A3',             '2012-01-01', 1, 1, 4000004, 'A3 pagina''s gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000011, 'Linkerboven',    '2012-01-01', 1, 1, 4000005, 'pagina''s in de linker bovenhoek bij elkaar nieten');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000012, 'Tweemaal links', '2012-01-01', 1, 2, 4000005, 'pagina''s aan de linkerkant bij elkaar nieten met 2 nietjes');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000013, 'Plooi boekje',   '2012-01-01', 1, 3, 4000005, 'boekje nieten in de plooi');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000014, 'Boekje',         '2012-01-01', 1, 1, 4000006, 'Drukken als een boekje');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000015, 'Perforeren',     '2012-01-01', 1, 2, 4000006, 'pagina''s perforeren aan de linkerkant');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000016, 'Transparant',    '2012-01-01', 1, 3, 4000006, 'drukken op transparanten (het is dus niet mogelijk om RV te drukken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000017, 'Kaft',           '2012-01-01', 1, 4, 4000006, 'Kaft toevoegen aan de kopie (voor en achterkant): kaft kleur en gewicht moeten ook opgegeven worden');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000018, 'Inbinden',       '2012-01-01', 1, 5, 4000006, 'Bundel inbinden');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000019, 'Blauw',          '2012-01-01', 1, 1, 4000007, 'blauwe kaft gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000020, 'Rood',           '2012-01-01', 1, 2, 4000007, 'rode kaft gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000021, 'Geel',           '2012-01-01', 1, 3, 4000007, 'gele kaft gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000022, '120gr',          '2012-01-01', 1, 1, 4000008, 'kaft van 120 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000023, '160gr',          '2012-01-01', 1, 2, 4000008, 'kaft van 160 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000024, '170gr',          '2012-01-01', 1, 3, 4000008, 'kaft van 170 gram gebruiken');
insert into opties (id, naam, van, status, volgorde, optietype, omschrijving) values (5000025, '200gr',          '2012-01-01', 1, 4, 4000008, 'kaft van 200 gram gebruiken');

insert into condities (id, Naam, expressie) values (10000001, 'A3 geselecteerd',        '500000010');
insert into condities (id, Naam, expressie) values (10000002, 'opdracht zonder opties', 'OPTIES=[500000001]');

insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000001, '2012-01-01', 0.01,   9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000002, '2012-01-01', 0.02,   9000001, 10000003, PAGINA, OPTIETYPE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000003, '2012-01-01', 0.013,  9000001, 10000002, PAGINA, OPTIETYPE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000004, '2012-01-01', 0.02,   9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000005, '2012-01-01', 0.005,  9000001, null,     PAGINA, OPTIETYPE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000006, '2012-01-01', 0.015,  9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000007, '2012-01-01', 0,      9000001, 10000001, PAGINA, FORMULE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000008, '2012-01-01', 0,      9000001, 10000001, PAGINA, FORMULE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000009, '2012-01-01', 0,      9000001, 10000001, PAGINA, FORMULE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000010, '2012-01-01', 0.1,    9000001, 10000001, PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000011, '2012-01-01', 0.04,   9000001, null,     KOPIE,  OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000012, '2012-01-01', 0.01,   9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000013, '2012-01-01', 0.62,   9000001, null,     KOPIE,  OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000014, '2012-01-01', 0.477,  9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000015, '2012-01-01', 0.022,  9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000016, '2012-01-01', 0.022,  9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000017, '2012-01-01', 0.04,   9000001, null,     PAGINA, OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000018, '2012-01-01', 0.01,   9000001, null,     KOPIE,  OPTIETYPE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000019, '2012-01-01', 0.03,   9000001, null,     KOPIE,  OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000020, '2012-01-01', 0.044,  9000001, null,     KOPIE,  OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000021, '2012-01-01', 0.044,  9000001, null,     KOPIE,  OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000022, '2012-01-01', 0.08,   9000001, null,     KOPIE,  OPTIE);
insert into prijzen (id, van, bedrag, prijstype, conditie, eenheid, type) values (8000023, '2012-01-01', 0,      9000001, null,     PAGINA, FORMULE);

insert into constraints (id, type, field1, field2, default) values (7000001, 2, 5000002, '5000016', 0);
insert into constraints (id, type, field1, field2, default) values (7000002, 1, 5000007, '4000002', 0);
insert into constraints (id, type, field1, field2, default) values (7000003, 2, 5000008, '4000002', 0);
insert into constraints (id, type, field1, field2, default) values (7000004, 2, 5000009, '4000002', 0);
insert into constraints (id, type, field1, field2, default) values (7000005, 1, 5000013, '5000014', 0);
insert into constraints (id, type, field1, field2, default) values (7000006, 2, 5000016, '5000002', 0);
insert into constraints (id, type, field1, field2, default) values (7000007, 1, 5000017, '4000007', 0);
insert into constraints (id, type, field1, field2, default) values (7000008, 1, 5000017, '4000008', 0);
insert into constraints (id, type, field1, field2, default) values (7000009, 1, 5000019, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000010, 1, 5000020, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000011, 1, 5000021, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000012, 1, 5000022, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000013, 1, 5000023, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000014, 1, 5000024, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000015, 1, 5000025, '5000017', 0);
insert into constraints (id, type, field1, field2, default) values (7000016, 3, 5000001, '8000001', 1);
insert into constraints (id, type, field1, field2, default) values (7000017, 3, 5000002, '8000004', 1);
insert into constraints (id, type, field1, field2, default) values (7000018, 3, 4000002, '8000005', 1);
insert into constraints (id, type, field1, field2, default) values (7000019, 3, 5000006, '8000006', 1);
insert into constraints (id, type, field1, field2, default) values (7000020, 3, 5000008, '8000015', 1);
insert into constraints (id, type, field1, field2, default) values (7000021, 3, 5000007, '8000016', 1);
insert into constraints (id, type, field1, field2, default) values (7000022, 3, 5000009, '8000017', 1);
insert into constraints (id, type, field1, field2, default) values (7000023, 3, 5000017, '8000011', 1);
insert into constraints (id, type, field1, field2, default) values (7000024, 3, 5000010, '8000012', 1);
insert into constraints (id, type, field1, field2, default) values (7000025, 3, 5000018, '8000013', 1);
insert into constraints (id, type, field1, field2, default) values (7000026, 3, 5000016, '8000014', 1);
insert into constraints (id, type, field1, field2, default) values (7000027, 3, 4000007, '8000018', 1);
insert into constraints (id, type, field1, field2, default) values (7000028, 3, 5000022, '8000019', 1);
insert into constraints (id, type, field1, field2, default) values (7000029, 3, 5000023, '8000020', 1);
insert into constraints (id, type, field1, field2, default) values (7000030, 3, 5000024, '8000021', 1);
insert into constraints (id, type, field1, field2, default) values (7000031, 3, 5000025, '8000022', 1);
insert into constraints (id, type, field1, field2, default) values (7000032, 4, 8000007, 'DEFAULT*(3+2/(DEFAULT*1000))', 0);
insert into constraints (id, type, field1, field2, default) values (7000033, 4, 8000008, 'DEFAULT*(3+8/(DEFAUlT*1000))', 0);
insert into constraints (id, type, field1, field2, default) values (7000034, 4, 8000009, 'DEFAULT*(3+8/(DEFAULT*1000))', 0);
insert into constraints (id, type, field1, field2, default) values (7000035, 3, 4000001, '8000002', 1);
insert into constraints (id, type, field1, field2, default) values (7000036, 3, 4000001, '8000003', 0);
insert into constraints (id, type, field1, field2, default) values (7000037, 3, 4000002, '8000023', 0);
insert into constraints (id, type, field1, field2, default) values (7000038, 4, 8000023, '2*DEFAULT', 0);
insert into constraints (id, type, field1, field2, default) values (7000039, 3, 5000006, '8000007', 0);
insert into constraints (id, type, field1, field2, default) values (7000040, 3, 5000007, '8000008', 0);
insert into constraints (id, type, field1, field2, default) values (7000041, 3, 5000008, '8000009', 0);
insert into constraints (id, type, field1, field2, default) values (7000042, 3, 5000009, '8000010', 0);

insert into opdrachten (