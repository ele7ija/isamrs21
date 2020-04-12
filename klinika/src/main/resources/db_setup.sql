create database klinicki_centar;
create user 'springuser'@'%' identified by 'springpassword';
grant all on klinicki_centar.* to 'springuser'@'%';