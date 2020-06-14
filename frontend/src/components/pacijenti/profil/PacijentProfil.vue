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
      locationKey: 100,


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
    ]),
    _lokacija(){
      return `${this.pacijent.adresa}, ${this.pacijent.grad}, ${this.pacijent.drzava}`;
    }
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

    getAddressData(data){
      this.pacijent.adresa = data.route;
      if(data.street_number)
        this.pacijent.adresa += " " + data.street_number;
      this.pacijent.grad = data.locality;
      this.pacijent.drzava = data.country;
    },

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