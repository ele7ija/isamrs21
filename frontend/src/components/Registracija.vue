<template>
  <v-container fluid><v-row align='center' justify='center'>
  <v-col :cols="4">
    <v-card>
    <v-card-title>Registracija</v-card-title>
    <v-card-text>
    <v-form ref='loginForm' v-model='valid'>
      <v-text-field
        v-model='ime'
        :counter='20'
        :rules='imePravila'
        label='Ime'
        required>
      </v-text-field>
      <v-text-field
        v-model='prezime'
        :counter='30'
        :rules='prezimePravila'
        label='Prezime'
        required>
      </v-text-field>
      <v-autocomplete
        ref="drzava"
        v-model="drzava"
        :rules="drzavaPravila"
        :items="drzave"
        label="Drzava"
        required
      ></v-autocomplete>
      <v-text-field
        v-model='grad'
        :rules='gradPravila'
        label='Grad'
        required>
      </v-text-field>
      <v-text-field
        v-model='adresa'
        :counter='100'
        :rules='adresaPravila'
        label='Adresa'
        required>
      </v-text-field>
      <v-text-field
        v-model='brojTelefona'
        :counter='15'
        :rules='brojTelefonaPravila'
        label='Broj telefona'
        required>
      </v-text-field>
      <v-text-field
        v-model='jbo'
        :rules='jboPravila'
        label='Jedinstveni broj osiguranika'
        required>
      </v-text-field>
      <v-divider></v-divider>
      <v-text-field
        v-model='email'
        :rules='emailPravila'
        label='E-mail'
        required>
      </v-text-field>
      <v-text-field
        v-model='lozinka'
        :append-icon="prikaziLozinku ? 'mdi-eye' : 'mdi-eye-off'"
        :type="prikaziLozinku ? 'text' : 'password'"
        @click:append="prikaziLozinku = !prikaziLozinku"
        :rules='lozinkaPravila'
        label='Lozinka'
        required>
      </v-text-field>
      <v-text-field
        v-model='ponovljenaLozinka'
        :type="'password'"
        :rules='ponovljenaLozinkaPravila'
        label='Ponovljena lozinka'
        required>
      </v-text-field>
      <v-btn
        :disabled="!valid"
        color="success"
        class="mr-4"
        @click="registruj"
      >
        Registruj se
      </v-btn>
      <v-btn
        color="error"
        class="mr-4"
        @click="resetujFormu"
      >
        Resetuj formu
      </v-btn>
    </v-form>
    </v-card-text>
    </v-card>
  </v-col>
  <!-- <v-col class='md-5'></v-col> -->
  </v-row></v-container>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  name: 'Registracija',
  data: function() {
    return {
      valid: true,
      ime: '',
      imePravila: [
        v => !!v || 'Ime je obavezno',
        v => (v && v.length <= 20) || 'Ime ima najviše 20 karaktera'
      ],
      prezime: '',
      prezimePravila: [
        v => !!v || 'Prezime je obavezno',
        v => (v && v.length <= 20) || 'Prezime ima najviše 30 karaktera'
      ],
      drzave: ['Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Anguilla', 'Antigua &amp; Barbuda', 'Argentina', 'Armenia', 'Aruba', 'Australia', 'Austria', 'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados', 'Belarus', 'Belgium', 'Belize', 'Benin', 'Bermuda', 'Bhutan', 'Bolivia', 'Bosnia &amp; Herzegovina', 'Botswana', 'Brazil', 'British Virgin Islands', 'Brunei', 'Bulgaria', 'Burkina Faso', 'Burundi', 'Cambodia', 'Cameroon', 'Cape Verde', 'Cayman Islands', 'Chad', 'Chile', 'China', 'Colombia', 'Congo', 'Cook Islands', 'Costa Rica', 'Cote D Ivoire', 'Croatia', 'Cruise Ship', 'Cuba', 'Cyprus', 'Czech Republic', 'Denmark', 'Djibouti', 'Dominica', 'Dominican Republic', 'Ecuador', 'Egypt', 'El Salvador', 'Equatorial Guinea', 'Estonia', 'Ethiopia', 'Falkland Islands', 'Faroe Islands', 'Fiji', 'Finland', 'France', 'French Polynesia', 'French West Indies', 'Gabon', 'Gambia', 'Georgia', 'Germany', 'Ghana', 'Gibraltar', 'Greece', 'Greenland', 'Grenada', 'Guam', 'Guatemala', 'Guernsey', 'Guinea', 'Guinea Bissau', 'Guyana', 'Haiti', 'Honduras', 'Hong Kong', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran', 'Iraq', 'Ireland', 'Isle of Man', 'Israel', 'Italy', 'Jamaica', 'Japan', 'Jersey', 'Jordan', 'Kazakhstan', 'Kenya', 'Kuwait', 'Kyrgyz Republic', 'Laos', 'Latvia', 'Lebanon', 'Lesotho', 'Liberia', 'Libya', 'Liechtenstein', 'Lithuania', 'Luxembourg', 'Macau', 'Macedonia', 'Madagascar', 'Malawi', 'Malaysia', 'Maldives', 'Mali', 'Malta', 'Mauritania', 'Mauritius', 'Mexico', 'Moldova', 'Monaco', 'Mongolia', 'Montenegro', 'Montserrat', 'Morocco', 'Mozambique', 'Namibia', 'Nepal', 'Netherlands', 'Netherlands Antilles', 'New Caledonia', 'New Zealand', 'Nicaragua', 'Niger', 'Nigeria', 'Norway', 'Oman', 'Pakistan', 'Palestine', 'Panama', 'Papua New Guinea', 'Paraguay', 'Peru', 'Philippines', 'Poland', 'Portugal', 'Puerto Rico', 'Qatar', 'Reunion', 'Romania', 'Russia', 'Rwanda', 'Saint Pierre &amp; Miquelon', 'Samoa', 'San Marino', 'Satellite', 'Saudi Arabia', 'Senegal', 'Serbia', 'Seychelles', 'Sierra Leone', 'Singapore', 'Slovakia', 'Slovenia', 'South Africa', 'South Korea', 'Spain', 'Sri Lanka', 'St Kitts &amp; Nevis', 'St Lucia', 'St Vincent', 'St. Lucia', 'Sudan', 'Suriname', 'Swaziland', 'Sweden', 'Switzerland', 'Syria', 'Taiwan', 'Tajikistan', 'Tanzania', 'Thailand', "Timor L'Este", 'Togo', 'Tonga', 'Trinidad &amp; Tobago', 'Tunisia', 'Turkey', 'Turkmenistan', 'Turks &amp; Caicos', 'Uganda', 'Ukraine', 'United Arab Emirates', 'United Kingdom', 'United States', 'Uruguay', 'Uzbekistan', 'Venezuela', 'Vietnam', 'Virgin Islands (US)', 'Yemen', 'Zambia', 'Zimbabwe'],
      drzava: '',
      drzavaPravila: [
        v => !!v || 'Drzava je obavezna'
      ],
      grad: '',
      gradPravila: [
      ],
      adresa: '',
      adresaPravila: [
        v => (v && v.length <= 100) || 'Adresa ima najviše 100 karaktera'

      ],
      brojTelefona: '',
      brojTelefonaPravila: [
        v => !!v || 'Broj telefona je obavezan',
        v => (v && v.length <= 15) || 'Broj telefona ima najviše 15 karaktera'
      ],
      jbo: '',
      jboPravila: [
        v => !!v || 'JBO je obavezan',
      ],
      email: '',
      emailPravila: [
        v => !!v || 'E-mail je obavezan',
        v => /.+@.+\..+/.test(v) || 'E-mail mora biti ispravan',
      ],
      lozinka: '',
      prikaziLozinku: false,
      lozinkaPravila: [
        v => !!v || 'Lozinka je obavezna',
        // (v && v.length <= 8) || 'Lozinka mora biti duza od 8 cifara',
      ],
      ponovljenaLozinka: '',
      ponovljenaLozinkaPravila: [
        v => !!v || 'Lozinka je obavezna',
        v => v===this.lozinka || 'Lozinke moraju biti iste',
        // (v && v.length <= 8) || 'Lozinka mora biti duza od 8 cifara',
      ],
    }
  },
  computed: {
    ...mapState('korisnici', [
      'registrovanKorisnik'
    ])
  },
  methods: {
    resetujFormu: function() {
      this.$refs.loginForm.reset();
    },
    ...mapActions({
      registrujKorisnika: 'korisnici/registrujKorisnika',
      podnesiZahtev: 'zahteviZaRegistraciju/podnesiZahtev'
    }),
    registruj: async function(){
      let korisnik = {
        ime: this.ime,
        prezime: this.prezime,
        drzava: this.drzava,
        grad: this.grad,
        adresa: this.adresa,
        brojTelefona: this.brojTelefona,
        jbo: this.jbo,
        email: this.email,
        sifra: this.lozinka
      }
      await this.registrujKorisnika(korisnik);
      let zahtev = {
        datumPodnosenja: new Date(),
        pacijent: this.registrovanKorisnik,
        odobren: false
      }
      this.podnesiZahtev(zahtev)
      setTimeout( () => this.$router.push('/login'), 1000)
    }
  }
}
</script>

<style>

</style>