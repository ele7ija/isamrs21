insert into authority values(1, 'pacijent');
insert into authority values(2, 'admin-klinickog-centra');
insert into authority values(3, 'admin-klinike');
insert into authority values(4, 'lekar');
insert into authority values(5, 'medicinska-sestra');

insert into klinika (id, naziv, adresa, grad, drzava, opis, slika) values(1, 'Dimitrije Banjac', 'Zmaj Ognjena Vuka 1', 'Novi Sad', 'Srbija', 'Donec hendrerit enim nisl, ut rutrum elit pellentesque nec. Ut sit amet porta nunc. Phasellus accumsan lacinia neque finibus lobortis. Praesent elementum, tortor vel varius egestas, eros turpis aliquet risus, sed tempus nulla felis quis diam. Praesent aliquam risus eu tincidunt volutpat. Integer eu purus nec odio sodales pulvinar. Proin imperdiet, orci vitae ornare congue, odio ante vestibulum dui, a blandit lectus erat eu diam. Nam consectetur a nisl eu gravida. Integer vestibulum mi eu nibh congue, nec sollicitudin mi bibendum.', 'https://r-cf.bstatic.com/images/hotel/max1024x768/115/115202004.jpg');
insert into klinika (id, naziv, adresa, grad, drzava, opis, slika) values(2, 'Dejan Cirjakovic', 'Zmaja od Nocaja 2', 'Beograd', 'Srbija', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eleifend, mi a rhoncus feugiat, ligula nunc lobortis turpis, hendrerit pharetra justo arcu vel est. Vestibulum lacus diam, accumsan pharetra enim id, placerat imperdiet dolor. Aliquam egestas, nulla at scelerisque dignissim, est purus molestie risus, fringilla varius sem velit nec ipsum. Mauris sagittis eros vel mi blandit, egestas mattis tellus commodo. Vivamus sollicitudin tellus nec pulvinar aliquam. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Mauris elit velit, pellentesque quis feugiat sit amet, euismod id enim. Phasellus non nibh congue, mollis lorem id, molestie velit. Proin elit mauris, gravida congue tempor a, malesuada et ligula. Aliquam accumsan dignissim eros ac pretium. Vivamus eu auctor ex, nec facilisis purus.', 'https://dijagonala.com/wp-content/uploads/2018/05/urgentni-IMG_6784-1024x576.jpg');
insert into klinika (id, naziv, adresa, grad, drzava, opis, slika) values(3, 'Nikola Skoric', 'Jovana Jovanovića Zmaja 23', 'Nis', 'Srbija', 'Donec hendrerit enim nisl, ut rutrum elit pellentesque nec. Ut sit amet porta nunc. Phasellus accumsan lacinia neque finibus lobortis. Praesent elementum, tortor vel varius egestas, eros turpis aliquet risus, sed tempus nulla felis quis diam. Praesent aliquam risus eu tincidunt volutpat. Integer eu purus nec odio sodales pulvinar. Proin imperdiet, orci vitae ornare congue, odio ante vestibulum dui, a blandit lectus erat eu diam. Nam consectetur a nisl eu gravida. Integer vestibulum mi eu nibh congue, nec sollicitudin mi bibendum.', 'https://scontent.fbeg2-1.fna.fbcdn.net/v/t1.0-9/36643301_1884396104952556_7864005404382986240_o.jpg?_nc_cat=108&_nc_sid=e3f864&_nc_eui2=AeHkmjqdAWTuXVFMcy2alv0rmM-29hO5qwGYz7b2E7mrATrRbs8irlPGbMC7k8k0mBq7_-NoHIwiG6--klr3j6V-&_nc_oc=AQlA8Oy6p7rMWVI3H49XF9e5Gb7YRMm8e_zaucgr9KbFP1FNjROFmVjOGKzQ0LH9iro&_nc_ht=scontent.fbeg2-1.fna&oh=19ef6fbdca154c9e949bc4c4ab404d51&oe=5ED61946');
--insert into klinika (id, naziv, adresa, grad, drzava, opis, slika) values(4, 'Stojce Stoleski', 'Zmaj Jovina 15', 'Novi Sad', 'Srbija', 'Donec hendrerit enim nisl, ut rutrum elit pellentesque nec. Ut sit amet porta nunc. Phasellus accumsan lacinia neque finibus lobortis. Praesent elementum, tortor vel varius egestas, eros turpis aliquet risus, sed tempus nulla felis quis diam. Praesent aliquam risus eu tincidunt volutpat. Integer eu purus nec odio sodales pulvinar. Proin imperdiet, orci vitae ornare congue, odio ante vestibulum dui, a blandit lectus erat eu diam. Nam consectetur a nisl eu gravida. Integer vestibulum mi eu nibh congue, nec sollicitudin mi bibendum.', 'http://vma.mod.gov.rs/vma3.jpg');
--insert into klinika (id, naziv, adresa, grad, drzava, opis, slika) values(5, 'Stojce Stoleski', 'Zmaja od Šipova 1', 'Sombor', 'Srbija', 'Donec hendrerit enim nisl, ut rutrum elit pellentesque nec. Ut sit amet porta nunc. Phasellus accumsan lacinia neque finibus lobortis. Praesent elementum, tortor vel varius egestas, eros turpis aliquet risus, sed tempus nulla felis quis diam. Praesent aliquam risus eu tincidunt volutpat. Integer eu purus nec odio sodales pulvinar. Proin imperdiet, orci vitae ornare congue, odio ante vestibulum dui, a blandit lectus erat eu diam. Nam consectetur a nisl eu gravida. Integer vestibulum mi eu nibh congue, nec sollicitudin mi bibendum.', 'https://ikvbv.ns.ac.rs/old/images/Vesti/2016/Instituti-kamenica.jpg');

-- Administratori klinickog centra
insert into korisnik (type, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AC', 'admincentra@gmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin', 'admin', '1', 'Serbia', 'Novi Sad', 1, 7, 0x01);

-- Administratori klinike
insert into korisnik (type, klinika_admina_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 1, 'admin.admin@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 1, 0x01);
insert into korisnik (type, klinika_admina_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 2, 'admin2.admin2@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 4, 0x01);
insert into korisnik (type, klinika_admina_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 1, 'admin3.admin3@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 5, 0x01);
-- Pacijenti
insert into korisnik (type, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('PA', 'millan.djuric@hotmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Milan', 'Djuric', '1', 'Serbia', 'Novi Sad', 2, 2, 0x01);
-- Lekari
insert into korisnik (type, klinika_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled, broj_specijalizacija) values ('LE', 1, 'lekar@gmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Miodrag', 'Lazic', '1', 'Serbia', 'Nis', 3, 3, 0x01, 2);
insert into korisnik (type, klinika_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled, broj_specijalizacija) values ('LE', 2, 'lekar2@gmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Marko', 'Mitrovic', '1', 'Serbia', 'Novi Sad', 6, 6, 0x01, 1);
insert into korisnik (type, klinika_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled, broj_specijalizacija) values ('LE', 3, 'lekar3@gmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Ognjen', 'Peric', '1', 'Serbia', 'Novi Sad', 8, 8, 0x01, 2);

-- sablon: values (korisnik_id, sifra_entiteta)
insert into user_authority values(1,3);
insert into user_authority values(4,3);
insert into user_authority values(5,3);
insert into user_authority values(2,1);
insert into user_authority values(3,4);
insert into user_authority values(6,4);
insert into user_authority values(7,2);

insert into sala (id, oznaka, klinika_id) values (1, 'Glavna kardioloska', 1);
insert into sala (id, oznaka, klinika_id) values (2, 'Sporedna kardioloska', 1);
insert into sala (id, oznaka, klinika_id) values (3, 'Opsta', 2);
insert into sala (id, oznaka, klinika_id) values (4, 'Pulmologija 1', 3);
insert into sala (id, oznaka, klinika_id) values (5, 'Pulmologija 2', 3);

insert into cenovnik(id, iznosUDinarima, naziv, klinika_id) values(1, 1000, 'Stavka 1', 1);
insert into cenovnik(id, iznosUDinarima, naziv, klinika_id) values(2, 5000, 'Stavka 2', 2);
insert into cenovnik(id, iznosUDinarima, naziv, klinika_id) values(3, 3000, 'Stavka 3', 3);


insert into tip_pregleda (id, naziv, opis, klinika_id, cenovnik_id, trajanje_minuti, vrsta) values (1, 'Kardiološki pregled', 'Opšti pregled srca i krvnih sudova', 1, 1, 60, 'pregled');
insert into tip_pregleda (id, naziv, opis, klinika_id, cenovnik_id, trajanje_minuti, vrsta) values (2, 'Kardiološko testiranje', 'Opšte testiranje srca i krvnih sudova', 1, 1, 75, 'pregled');
insert into tip_pregleda (id, naziv, opis, klinika_id, cenovnik_id, trajanje_minuti, vrsta) values (3, 'Gastroenterološki pregled', 'Opšti pregled sistema za varenje', 2, 2, 90, 'pregled');
insert into tip_pregleda (id, naziv, opis, klinika_id, cenovnik_id, trajanje_minuti, vrsta) values (4, 'Pulmološki pregled', 'Opšti pregled pluća', 3, 3, 100, 'pregled');
insert into tip_pregleda (id, naziv, opis, klinika_id, cenovnik_id, trajanje_minuti, vrsta) values (5, 'Pulmološko testiranje', 'Opšte testiranje pluća', 3, 3, 50, 'pregled');

insert into specijalnost_lekara values(1, 3);
insert into specijalnost_lekara values(2, 3);
insert into specijalnost_lekara values(3, 6);
insert into specijalnost_lekara values(4, 8);
insert into specijalnost_lekara values(5, 8);

insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (1, 3, 1, 1, STR_TO_DATE('28/05/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('28/05/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (1, 3, 1, 1, STR_TO_DATE('01/06/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('01/06/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (1, 3, 2, 2, STR_TO_DATE('29/05/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('29/05/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (1, 3, 2, 2, STR_TO_DATE('02/06/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('02/06/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (2, 6, 3, 3, STR_TO_DATE('01/06/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('01/06/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (2, 6, 3, 3, STR_TO_DATE('03/06/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('03/06/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (3, 8, 4, 4, STR_TO_DATE('28/05/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('28/05/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (3, 8, 4, 4, STR_TO_DATE('01/06/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('01/06/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (3, 8, 5, 5, STR_TO_DATE('29/05/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('29/05/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);
insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id, pocetak_pregleda, kraj_pregleda, cena, popust, konacna_cena) values (3, 8, 5, 5, STR_TO_DATE('02/06/2020 12:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('02/06/2020 13:00:00','%d/%m/%Y %H:%i:%s'), 1000, 0, 1000);

insert into upit_za_pregled values(1,0x0,STR_TO_DATE('23/05/2020 9:00:00','%d/%m/%Y %H:%i:%s'),STR_TO_DATE('23/05/2020 13:00:00','%d/%m/%Y %H:%i:%s'),0x0,'bla',0x0,STR_TO_DATE('23/05/2020 12:00:00','%d/%m/%Y %H:%i:%s'),0x0,0,null,1,3,2,1,null);
insert into upit_za_pregled values(2,0x0,STR_TO_DATE('23/05/2020 9:00:00','%d/%m/%Y %H:%i:%s'),STR_TO_DATE('28/05/2020 12:50:00','%d/%m/%Y %H:%i:%s'),0x0,'bla',0x0,STR_TO_DATE('28/05/2020 12:20:00','%d/%m/%Y %H:%i:%s'),0x0,0,null,1,3,2,1,null);
insert into upit_za_pregled values(3,0x0,STR_TO_DATE('23/05/2020 9:00:00','%d/%m/%Y %H:%i:%s'),STR_TO_DATE('28/05/2020 12:50:00','%d/%m/%Y %H:%i:%s'),0x0,'bla',0x0,STR_TO_DATE('28/05/2020 11:20:00','%d/%m/%Y %H:%i:%s'),0x0,0,null,1,3,2,1,null);