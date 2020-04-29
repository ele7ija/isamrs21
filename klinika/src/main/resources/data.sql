insert into authority values(1, 'pacijent');
insert into authority values(2, 'admin-klinickog-centra');
insert into authority values(3, 'admin-klinike');
insert into authority values(4, 'lekar');
insert into authority values(5, 'medicinska-sestra');

insert into klinika (id, naziv, adresa) values(1, 'Dimitrije Banjac', 'Zmaj Ognjena Vuka 1, Novi Sad, Srbija');
insert into klinika (id, naziv, adresa) values(2, 'Dejan Cirjakovic', 'Zmaja od Nocaja 2, Beograd, Srbija');

-- Administratori klinike
insert into korisnik (type, klinika_admina_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 1, 'admin.admin@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 1, 0x01);
insert into korisnik (type, klinika_admina_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 2, 'admin2.admin2@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 4, 0x01);
insert into korisnik (type, klinika_admina_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 1, 'admin3.admin3@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 5, 0x01);
-- Pacijenti
insert into korisnik (type, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('PA', 'millan.djuric@hotmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Milan', 'Djuric', '1', 'Serbia', 'Novi Sad', 2, 2, 0x01);
-- Lekari
insert into korisnik (type, klinika_id, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('LE', 1, 'lekar@gmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Miodrag', 'Lazic', '1', 'Serbia', 'Nis', 3, 3, 0x01);

insert into user_authority values(1,3);
insert into user_authority values(4,3);
insert into user_authority values(5,3);
insert into user_authority values(2,1);
insert into user_authority values(3,4);

insert into sala (oznaka, klinika_id) values ('Glavna operaciona', 1);
insert into sala (oznaka, klinika_id) values ('Sporedna operaciona', 1);
insert into sala (oznaka, klinika_id) values ('Opsta', 2);

insert into tip_pregleda (naziv, opis, klinika_id) values ('Operacija srca', 'Operacija na otvorenom srcu', 1);
insert into tip_pregleda (naziv, opis, klinika_id) values ('Operacija kuka', 'Operacija kuka miofacijalna embrionalna gonoreja', 1);
insert into tip_pregleda (naziv, opis, klinika_id) values ('Pregled kicme', 'Opsti pregled kicme', 2);

insert into pregled (klinika_id, lekar_id, sala_id, tip_pregleda_id) values (1, 3, 1, 1);