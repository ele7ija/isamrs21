<template>
  <v-container fluid><v-row align='center' justify='center'>
  <v-col :cols="4">
    <v-card>
    <v-card-title class='red lighten-3'>Izmena profila</v-card-title>
    <v-card-text>
    <h3 v-if='pacijent==null'>Pacijent se učitava...</h3>
    <v-form ref='form' v-model='valid' v-if='pacijent!=null'>
      <v-text-field
        v-model='pacijent.ime'
        class='mt-4'
        :counter='20'
        :rules='imePravila'
        label='Ime'
        required>
      </v-text-field>
      <v-text-field
        v-model='pacijent.prezime'
        :counter='30'
        :rules='prezimePravila'
        label='Prezime'
        required>
      </v-text-field>
      <v-autocomplete
        ref="drzava"
        v-model="pacijent.drzava"
        :rules="drzavaPravila"
        :items="drzave"
        label="Drzava"
        required
      ></v-autocomplete>
      <v-text-field
        v-model='pacijent.grad'
        :rules='gradPravila'
        label='Grad'
        required>
      </v-text-field>
      <v-text-field
        v-model='pacijent.adresa'
        :counter='100'
        :rules='adresaPravila'
        label='Adresa'
        required>
      </v-text-field>
      <v-text-field
        v-model='pacijent.brojTelefona'
        :counter='15'
        :rules='brojTelefonaPravila'
        label='Broj telefona'
        required>
      </v-text-field>
      <v-text-field
        v-model='pacijent.jbo'
        :rules='jboPravila'
        class='mb-3'
        label='Jedinstveni broj osiguranika'
        hint="Nemate pravo da menjate JBO"
        :persistent-hint='true'
        disabled>
      </v-text-field>
      <v-text-field
        v-model='pacijent.email'
        :rules='emailPravila'
        class='mb-3'
        label='E-mail'
        hint="Nemate pravo da menjate E-mail"
        :persistent-hint='true'
        disabled>
      </v-text-field>
      <v-text-field
        v-model='novalozinka'
        :append-icon="prikaziLozinku ? 'mdi-eye' : 'mdi-eye-off'"
        :type="prikaziLozinku ? 'text' : 'password'"
        @click:append="prikaziLozinku = !prikaziLozinku"
        :rules='lozinkaPravila'
        label='Nova lozinka'
        autocomplete="new-password"
        required>
      </v-text-field>
      <v-text-field
        v-model='ponovljenalozinka'
        :type="'password'"
        :rules='ponovljenaLozinkaPravila'
        label='Ponovljena lozinka'
        required>
      </v-text-field>
      <v-btn
        :disabled="!valid"
        color="success"
        class="mr-4"
        @click="izmeni"
      >
        Izmeni profil
      </v-btn>
      <v-btn
        color="error"
        class="mr-4"
        @click="$router.go(-1)"
      >
        Odustani
      </v-btn>
    </v-form>
    </v-card-text>
    </v-card>
  </v-col>
  </v-row>
  <v-snackbar
      v-model="snackbarErr"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbarErr = false"
      >
        Close
      </v-btn>
    </v-snackbar>
    <v-snackbar
      v-model="snackbarSucc"
      :timeout="snackbarTimeout"
      color="green darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbarSucc = false"
      >
        Close
      </v-btn>
    </v-snackbar></v-container>
</template>

<script>
import pacijentAPI from '@/api/pacijenti';
import { mapState, mapMutations } from 'vuex';
import korisnikAPI from '@/api/korisnici';
export default {
  name: 'PacijentProfil',
  data: function() {
    return {
      valid: true,
      pacijent: null,
      pacijent_backup: null,
      novalozinka: '',
      ponovljenalozinka: '',
      imePravila: [
        v => !!v || 'Ime je obavezno',
        v => (v && v.length <= 20) || 'Ime ima najviše 20 karaktera'
      ],
      prezimePravila: [
        v => !!v || 'Prezime je obavezno',
        v => (v && v.length <= 20) || 'Prezime ima najviše 30 karaktera'
      ],
      drzave: ['Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Anguilla', 'Antigua &amp; Barbuda', 'Argentina', 'Armenia', 'Aruba', 'Australia', 'Austria', 'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados', 'Belarus', 'Belgium', 'Belize', 'Benin', 'Bermuda', 'Bhutan', 'Bolivia', 'Bosnia &amp; Herzegovina', 'Botswana', 'Brazil', 'British Virgin Islands', 'Brunei', 'Bulgaria', 'Burkina Faso', 'Burundi', 'Cambodia', 'Cameroon', 'Cape Verde', 'Cayman Islands', 'Chad', 'Chile', 'China', 'Colombia', 'Congo', 'Cook Islands', 'Costa Rica', 'Cote D Ivoire', 'Croatia', 'Cruise Ship', 'Cuba', 'Cyprus', 'Czech Republic', 'Denmark', 'Djibouti', 'Dominica', 'Dominican Republic', 'Ecuador', 'Egypt', 'El Salvador', 'Equatorial Guinea', 'Estonia', 'Ethiopia', 'Falkland Islands', 'Faroe Islands', 'Fiji', 'Finland', 'France', 'French Polynesia', 'French West Indies', 'Gabon', 'Gambia', 'Georgia', 'Germany', 'Ghana', 'Gibraltar', 'Greece', 'Greenland', 'Grenada', 'Guam', 'Guatemala', 'Guernsey', 'Guinea', 'Guinea Bissau', 'Guyana', 'Haiti', 'Honduras', 'Hong Kong', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran', 'Iraq', 'Ireland', 'Isle of Man', 'Israel', 'Italy', 'Jamaica', 'Japan', 'Jersey', 'Jordan', 'Kazakhstan', 'Kenya', 'Kuwait', 'Kyrgyz Republic', 'Laos', 'Latvia', 'Lebanon', 'Lesotho', 'Liberia', 'Libya', 'Liechtenstein', 'Lithuania', 'Luxembourg', 'Macau', 'Macedonia', 'Madagascar', 'Malawi', 'Malaysia', 'Maldives', 'Mali', 'Malta', 'Mauritania', 'Mauritius', 'Mexico', 'Moldova', 'Monaco', 'Mongolia', 'Montenegro', 'Montserrat', 'Morocco', 'Mozambique', 'Namibia', 'Nepal', 'Netherlands', 'Netherlands Antilles', 'New Caledonia', 'New Zealand', 'Nicaragua', 'Niger', 'Nigeria', 'Norway', 'Oman', 'Pakistan', 'Palestine', 'Panama', 'Papua New Guinea', 'Paraguay', 'Peru', 'Philippines', 'Poland', 'Portugal', 'Puerto Rico', 'Qatar', 'Reunion', 'Romania', 'Russia', 'Rwanda', 'Saint Pierre &amp; Miquelon', 'Samoa', 'San Marino', 'Satellite', 'Saudi Arabia', 'Senegal', 'Serbia', 'Seychelles', 'Sierra Leone', 'Singapore', 'Slovakia', 'Slovenia', 'South Africa', 'South Korea', 'Spain', 'Sri Lanka', 'St Kitts &amp; Nevis', 'St Lucia', 'St Vincent', 'St. Lucia', 'Sudan', 'Suriname', 'Swaziland', 'Sweden', 'Switzerland', 'Syria', 'Taiwan', 'Tajikistan', 'Tanzania', 'Thailand', "Timor L'Este", 'Togo', 'Tonga', 'Trinidad &amp; Tobago', 'Tunisia', 'Turkey', 'Turkmenistan', 'Turks &amp; Caicos', 'Uganda', 'Ukraine', 'United Arab Emirates', 'United Kingdom', 'United States', 'Uruguay', 'Uzbekistan', 'Venezuela', 'Vietnam', 'Virgin Islands (US)', 'Yemen', 'Zambia', 'Zimbabwe'],
      drzavaPravila: [
        v => !!v || 'Drzava je obavezna'
      ],
      gradPravila: [
      ],
      adresaPravila: [
        // v => (v && v.length <= 100) || 'Adresa ima najviše 100 karaktera'
      ],
      brojTelefonaPravila: [
        v => !!v || 'Broj telefona je obavezan',
        v => (v && v.length <= 15) || 'Broj telefona ima najviše 15 karaktera'
      ],
      jboPravila: [
        v => !!v || 'JBO je obavezan',
      ],
      emailPravila: [
        v => !!v || 'E-mail je obavezan',
        v => /.+@.+\..+/.test(v) || 'E-mail mora biti ispravan',
      ],
      prikaziLozinku: false,
      lozinkaPravila: [
        // v => !!v || 'Lozinka je obavezna',
        // (v && v.length <= 8) || 'Lozinka mora biti duza od 8 cifara',
      ],
      ponovljenaLozinkaPravila: [
        // v => !!v || 'Lozinka je obavezna',
        v => v===this.novalozinka || 'Lozinke moraju biti iste',
        // (v && v.length <= 8) || 'Lozinka mora biti duza od 8 cifara',
      ],
      snackbarErr: false,
      snackbarSucc: false,
      snackbarTimeout: 2000,
      snackbarText: null,
    }
  },
  computed: {
    ...mapState('korisnici', [
      '_korisnik',
      'korisnik',
    ])
  },
  methods: {
    ...mapMutations('korisnici', [
      'mutateKorisniciArray',
      'setNewSifra'
    ]),
    ...mapMutations('profil', [
      'setProfil',
      '_setCopy'
    ]),
    dobavi: function(email) {
      pacijentAPI.getPacijent(email)
      .then(({data}) => {
        this.pacijent = data;
        this.pacijent_backup = data;
      })
    },
    izmeni: function() {
      if (this.novalozinka!=this.pacijent_backup.sifra && this.novalozinka!=='') {
        this.pacijent.sifra = this.novalozinka;
      }
      let profil = JSON.parse(JSON.stringify(this.pacijent));
      let poslednjaSifra = this.pacijent_backup.password;
      let role = this.korisnik.role;
      korisnikAPI.updateProfil({
        profil, 
        poslednjaSifra,
        role
      })
      .then(({data:{result,success, message}}) => {
        if (success){
          this.setProfil(result);
          this._setCopy(JSON.parse(JSON.stringify(result)));
          this.mutateKorisniciArray(result)
          if(this.novalozinka != '')
            this.setNewSifra(this.novalozinka);
          this.snackbarText = message;
          setTimeout( () => this.$router.push('/pacijent/home'), 1000);
          this.snackbarSucc = true;
        }
        else {
          this.snackbarText = message;
          this.snackbarErr = true;
        }
      })
    }
  },
  mounted() {
    this.dobavi(this._korisnik.username)
  }
}
</script>

<style>

</style>