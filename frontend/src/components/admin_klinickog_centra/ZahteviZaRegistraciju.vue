<template>
<div>



  <v-data-table
    :headers="headers"
    :items="getAll"
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

      <!-- akcija prihvati -->
      <v-dialog v-model="dialogOnPrihvati" width="400">
        <template v-slot:activator="{ on }">
          <v-btn v-on="on" class="ma-2" elevation=1  color="success lighten-1" small >
            Prihvati
            <v-icon right>mdi-check</v-icon>
          </v-btn>
        </template>

        <v-card>
          <v-card-text class="pa-2">
            Da li ste sigurni da zelite da prihvatite korisnika 
            {{item.pacijent.ime}} {{item.pacijent.prezime}}?
          </v-card-text>


          <v-divider></v-divider>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="success" small @click="prihvati(item)">
              Prihvati
            </v-btn>
            <v-btn color="error lighten-1" small @click="dialogOnPrihvati=false">
              Nazad
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- akcija odbij -->
      <v-dialog v-model="dialogOnOdbij" width="500">
        <template v-slot:activator="{ on }">
          <v-btn v-on="on" class="ma-2" elevation=1  color="error lighten-1" small>
            Odbij 
          <v-icon right>mdi-close</v-icon>
          </v-btn>
        </template>

        <v-form v-model="isFormValid">
          <v-card>
            <v-card-title>
              <span class="headline">
              Odbij zahtev korisniku: 
              {{item.pacijent.ime}} {{item.pacijent.prezime}}
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

            <!-- akcije -->
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="success" small  @click="odbij(item)" :disabled="!isFormValid">
              Pošalji
            </v-btn>
            <v-btn color="error lighten-1" small @click.stop="dialogOnOdbij=false">
              Nazad
            </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-dialog>
    </template> 

  </v-data-table>
  
  <!-- snackbarovi -->
  <v-snackbar
  :timeout=0
  v-model="mailSending">
    Slanje mejla je u toku

    <v-progress-circular
    indeterminate
    color="info"
    ></v-progress-circular>
  </v-snackbar>

  <v-snackbar
  :timeout=4000
  v-model="mailSent"
  color="success">
    Mejl je uspešno poslat

    <v-btn
    outlined
    small
    @click="mailSent = false">
    Zatvori
    </v-btn>
  </v-snackbar>
  
  <v-snackbar
  :timeout=4000
  v-model="mailNotSent"
  color="error">
    Mejl nije uspešno poslat

    <v-btn
    outlined
    small
    @click="mailNotSent = false">
    Zatvori
    </v-btn>
  </v-snackbar>


</div>
</template>

<script>
import {mapGetters, mapActions, } from 'vuex';

export default {
  name: "ZahteviZaRegistraciju",
  data: function (){
    return {
      search: '',
      dialogOnPrihvati: false,
      dialogOnOdbij: false,
      isFormValid: true,
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
        getAll: 'zahteviZaRegistraciju/getAllZahtevi',
        getMailSending: 'zahteviZaRegistraciju/getMailSending',
        getMailSent: 'zahteviZaRegistraciju/getMailSent',
        getMailNotSent: 'zahteviZaRegistraciju/getMailNotSent',
      }
    ),

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

    prihvati (item){
      this.dialogOnPrihvati = false;
      this.prihvatiZahtev(item);
    },
    odbij (item){
      //zatvori dijalog
      this.dialogOnOdbij = false;
      //upali snackbar promeni na true
      this.snackbarSending = false;

      //posalji objekat na bek
      this.zahtev.id = item.id;
      this.zahtev.datumOdobrenja = new Date();
      //this.adminOdobrio =
      this.zahtev.prihvacen = false;
      this.odbijZahtev(this.zahtev);
    },
  }
}
</script>

<style>
</style>