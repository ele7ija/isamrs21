insert into authority values(1, 'pacijent');
insert into authority values(2, 'admin-klinickog-centra');
insert into authority values(3, 'admin-klinike');
insert into authority values(4, 'lekar');
insert into authority values(5, 'medicinska-sestra');

insert into korisnik (type, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('AK', 'admin.admin@yahoo.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'admin_klinike', 'admin_klinike', '1', 'Serbia', 'Novi Sad', 1, 1, 0x01);
insert into korisnik (type, email, sifra, ime, prezime, broj_telefona, drzava, grad, jbo, id, enabled) values ('PA', 'millan.djuric@hotmail.com', '$2a$10$cVG9lUTjhiB2j8HqrSk6B.PifdUlXCPZ6IDOIq.LrltVTXWSuVRqS', 'Milan', 'Djuric', '1', 'Serbia', 'Novi Sad', 1, 2, 0x01);

insert into user_authority values(1,3);
insert into user_authority values(2,1);

insert into klinika values(1);