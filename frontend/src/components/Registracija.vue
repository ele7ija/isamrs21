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
      <vuetify-google-autocomplete
        id='map2'
        ref='lokacija'
        :key="locationKey"
        append-icon='mdi-map-marker'
        v-bind:disable='true'
        placeholder="lokacija"
        :value="_lokacija"
        v-on:placechanged="getAddressData"
        :options="{fields: ['geometry', 'address_components', 
          'formatted_address']}"
      >
      </vuetify-google-autocomplete>
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
      locationKey: 100,

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
      drzava: '',
      grad: '',
      adresa: '',
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
    ]),
    _lokacija(){
      if(!this.adresa || !this.grad || !this.drzava)
        return '';
      return `${this.adresa}, ${this.grad}, ${this.drzava}`;
    }
  },
  methods: {
    resetujFormu: function() {
      this.$refs.loginForm.reset();
      this.locationKey += 1;
    },
    ...mapActions({
      registrujKorisnika: 'korisnici/registrujKorisnika',
      podnesiZahtev: 'zahteviZaRegistraciju/podnesiZahtev'
    }),

    getAddressData(data){
      this.adresa = data.route;
      if(data.street_number)
        this.adresa += " " + data.street_number;
      this.grad = data.locality;
      this.drzava = data.country;
    },

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