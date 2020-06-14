# Klinike
Projekat tima 21 iz predmeta ISA i MRS - letnji semestar 2020.

Link ka aplikaciji: https://klinike.herokuapp.com/

### Autori:
|     Ime i prezime    	|   Indeks   	| github      	|
|--------------------	|:----------:	|-------------:	|
|      Milan Đurić     	| SW 12-2017 	| [```djuricmilan```](github.com/djuricmilan) 	|
| Aleksandar Vujinović 	| SW 46-2017 	| [```alekalekalek```](github.com/alekalekalek)|
|     Bojan Popržen    	| SW 16-2017 	| [```ele7ija```](github.com/ele7ija)    	|


# Pokretanje projekta

U ovoj sekciji ćemo opisati kako se projekat pokreće u produkciji. Ukoliko se projekat pokreće za development, server koji opslužuje frontend bi se pokrenuo odvojeno i korisnik bi posetio adresu na kojoj on opslužuje sadržaj (u našem slučaju localhost:8081). 

Pokretanje projekta u produkciji se sastoji iz dva dela:
1. Pokretanje Sistema za upravljenje bazom podataka (SUBP)
2. Pokretanje aplikativnog servera

## 1. Pokretanje SUBP-a

Izabrani SUBP za ovaj projekat je MySQL. Slede koraci za njegovo pokretanje:
- Preuzeti i instalirati MySQL server i inicijalno ga pokrenuti
- Izvršiti SQL skriptu [db_setup.sql](./klinika/src/main/resources/db_setup.sql). Nakon ovog koraka, kreiraće se baza podataka i default korisnik koji će biti korišćeni pri povezivanju Spring aplikacije sa bazom. Ta podešavanja su već navedena u aplication.properties datoteci.

## 2. Pokretanje aplikativnog servera

Pokretanje aplikativnog servera podrazumeva kompajliranje Java koda i njegovo pokretanje u JVM izvršnom okruženju.

- Preuzeti maven
- Pozicionirati se na root ovog projekta i izvršiti maven naredbu: 
> mvn clean install

Dva glavna koraka ove naredbe su kompajliranje frontend i backend koda, a međukoraci su preuzimanje modula od kojih zavise, kopiranje izgrađenog frontend koda (html/js/css) u direktorijum odakle se servira statički sadržaj bekend servera; potom kompajliranje Java koda bekenda i izgradnja .jar arhive. Svi ovi koraci su dirigovani sadržajem pom.xml fajlova u direktorijumima root, frontend i klinika.

- Pozicionirati se u <tt>./klinika/target</tt> i pokrenuti build-ovan aplikativni server naredbom:
> java -jar klinika-0.0.1-SNAPSHOT.jar

- Posetiti aplikaciju na adresi: 
> http://localhost:8080
