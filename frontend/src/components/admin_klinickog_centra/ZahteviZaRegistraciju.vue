<template>
<div>

  <v-data-table
    :headers="headers"
    :items="_getAll"
    :search="search"
    class="elevation-1" 
    >
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-toolbar-title>Zahtevi za registraciju</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        
        <!-- search bar  -->
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>

      </v-toolbar>
    </template>

    <!-- akcije prihvati i odbij -->
    <template v-slot:item.actions="{ item }">
          <v-btn @click="prihvatiDialog(item)" class="ma-2" elevation=1  color="success lighten-1" small >
            Prihvati
          <v-icon right>mdi-check</v-icon>
          </v-btn>       
          <v-btn @click="odbijDialog(item)" class="ma-2" elevation=1  color="error lighten-1" small>
            Odbij 
          <v-icon right>mdi-close</v-icon>
          </v-btn>
    </template> 

  </v-data-table>
  
  <!-- snackbarovi -->
  <v-snackbar
    :timeout=0
    v-model="obradaKrenula"
  >
    Obrada zahteva je u toku
    <v-progress-circular
    indeterminate
    color="info"
    ></v-progress-circular>
  </v-snackbar>

  <v-snackbar
    :timeout=4000
    v-model="obradaZavrsena"
    :color="color"
  >
    {{text}}
    <v-btn
      outlined
      small
      @click="obradaZavrsena = false"
    >
    Zatvori
    </v-btn>
  </v-snackbar>

  <!-- dijalozi -->
  <!-- dialog prihvati -->
  <!-- retain-focus="false" mora jer vueitfy ima neki bag -->
  <v-dialog v-model="dialogOnPrihvati" width="400" > 
    <v-card>
      <v-card-text class="pa-2">
        Da li ste sigurni da zelite da prihvatite korisnika {{punoIme}}?
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="success" small @click="prihvati">
          Prihvati
        </v-btn>
        <v-btn color="error lighten-1" small @click="dialogOnPrihvati=false">
          Nazad
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- dialog odbij -->
  <v-dialog v-model="dialogOnOdbij" width="500" >
    <v-form v-model="isFormValid" >
      <v-card>
        <v-card-title>
          <span class="headline">
          Odbij zahtev korisniku: {{punoIme}}
          </span>
        </v-card-title>

        <v-card-text>
          <v-container>
            <v-textarea
            outlined
            label="razlog odbijanja" 
            hint="Neregistrovanom korisniku obrazložiti zašto je odbijen."
            :rules=razlogRule
            v-model="zahtev.tekst"
            ></v-textarea>
          </v-container>
        </v-card-text>    
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="success" small  @click="odbij" :disabled="!isFormValid">
          Pošalji
        </v-btn>
        <v-btn color="error lighten-1" small @click.stop="dialogOnOdbij=false">
          Nazad
        </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>

</div>
</template>

<script>
import {mapGetters, mapActions, } from 'vuex';

export default {
  name: "ZahteviZaRegistraciju",
  data: function (){
    return {
      color: null,
      text: null,
      obradaKrenula: false,
      obradaZavrsena: false,

      search: '',
      dialogOnPrihvati: false,
      dialogOnOdbij: false,
      isFormValid: true,
      selectedPacijent: {
        ime: '',
        prezime: '',
      },
      zahtev: {
        tekst: 'Vaš zahtev se odbija jer..',
        id: undefined,
        datumOdobrenja: undefined,
        adminOdobrio: undefined,
        prihvacen: undefined,
      },
      headers: [
        {
          text: 'Ime',
          value: 'pacijent.ime',
          sortable: true,
        },
        {
          text: "Prezime",
          value: 'pacijent.prezime',
          sortable: true,
        },
        {
          text: "Email",
          value: 'pacijent.email',
          sortable: true,
        },
        {
          text: "Datum Podnošenja",
          value: 'datumString',
          sortable: true,
        },
        {
          text: "Vreme Podnošenja",
          value: 'vremeString',
          sortable: true,
        },
        { 
          text: 'Actions',
          value: 'actions',
          sortable: false,
          align: 'end'
        }
      ],
      //rule
      razlogRule: [
        v => !!v || 'Razlog odbijanja zahteva je obavezan',
        v => (v && v.length <= 1000) || 'Razlog ima najviše 1000 karaktera'
      ],

    }
  },
  computed: {
    ...mapGetters(
      {
        getAll: 'zahteviZaRegistraciju/getAllZahtevi'
      }
    ),

    _getAll(){
      if(!this.getAll)
        return [];
      else{
        return this.getAll.filter(x => !x.odobren);
      }
    },

    punoIme: function(){
      return this.selectedPacijent.ime + ' ' + this.selectedPacijent.prezime;
    },
    //za snackbarove polja
    mailSending: {
      get () {return this.getMailSending},
      set (bool) { this.setMailSending(bool) }
    },
    mailSent: {
      get () {return this.getMailSent},
      set (bool) {this.setMailSending(bool)}
    },
    mailNotSent: {
      get () {return this.getMailNotSent},
      set (bool) {this.setMailNotSent(bool)}
    }
  },

  created(){
    this.fetchData();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'zahteviZaRegistraciju/fetchAllZahtevi',
        prihvatiZahtev: 'zahteviZaRegistraciju/prihvatiZahtev',
        odbijZahtev: 'zahteviZaRegistraciju/odbijZahtev',
        setMailSending: 'zahteviZaRegistraciju/setMailSending',
        setMailSent: 'zahteviZaRegistraciju/setMailSent',
        setMailNotSent: 'zahteviZaRegistraciju/setMailNotSent'

      }
    ),

    prihvatiDialog(item){
      this.selectedPacijent.ime = item.pacijent.ime;
      this.selectedPacijent.prezime = item.pacijent.prezime;
      this.selectedPacijent.id = item.pacijent.id;
      this.dialogOnPrihvati = true;
    },
    odbijDialog(item){
      this.selectedPacijent.ime = item.pacijent.ime;
      this.selectedPacijent.prezime = item.pacijent.prezime;
      this.selectedPacijent.id = item.pacijent.id;
      this.dialogOnOdbij = true;
    },
    prihvati (){
      //zatvori dijalog
      this.dialogOnPrihvati = false;
      this.zahtev.id = this.selectedPacijent.id;
      this.zahtev.datumOdobrenja = new Date();
      this.zahtev.odobren = true;
      this.obradaKrenula = true;
      this.prihvatiZahtev(this.zahtev).then(
        (message) => {
          this.obradaKrenula = false;
          this.text = message;
          this.color = "#66BB6A";
          this.obradaZavrsena = true;
        },
        (message) => {
          this.obradaKrenula = false;
          this.text = message;
          this.color = "#EF5350";
          this.obradaZavrsena = true;
        },
      );
    },
    odbij (){
      //zatvori dijalog
      this.dialogOnOdbij = false;
      //posalji objekat na bek
      this.zahtev.id = this.selectedPacijent.id;
      this.zahtev.datumOdobrenja = new Date();
      this.zahtev.odobren = false;
      this.obradaKrenula = true;
      this.odbijZahtev(this.zahtev).then(
        (message) => {
          this.obradaKrenula = false;
          this.text = message;
          this.color = "#66BB6A";
          this.obradaZavrsena = true;
        },
        (message) => {
          this.obradaKrenula = false;
          this.text = message;
          this.color = "#EF5350";
          this.obradaZavrsena = true;
        },
      );
    },
  }
}
</script>

<style>
</style>