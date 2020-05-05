<template>
  <div>
    <v-container fluid>
      <div class="ml-5 mr-5">
        <TabelaNeobradjenihUpita :all="_neobradjeniUpiti" class="mt-5" @accept="accept" @reject="reject"></TabelaNeobradjenihUpita>
        <TabelaObradjenihUpita :all="_obradjeniUpiti" class="mt-5" @deleteItem="deleteItem"></TabelaObradjenihUpita>
      </div>
    </v-container>
    <v-snackbar
      v-model="snackbar"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import TabelaNeobradjenihUpita from './TabelaNeobradjenihUpita';
import TabelaObradjenihUpita from './TabelaObradjenihUpita';
export default {
  name: "UpitiPreglediAdmin",
  data: function(){
    return {
      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null
    };
  },
  components: {
    TabelaNeobradjenihUpita,
    TabelaObradjenihUpita
  },
  
  computed:{
    ...mapGetters({
      upiti: "upitiPreglediAdmin/getUpiti",
      noviPregled: "preglediAdmin/getNoviPregled"
    }),
    _neobradjeniUpiti: function(){
      let temp = this.upiti.filter(x => !x.adminObradio);
      return temp.map(x => {
        return {
          id: x.id,
          pacijent: `${x.pacijent.ime} ${x.pacijent.prezime}`,
          datum: new Date(x.pocetakPregleda).toLocaleDateString(),
          pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
          kraj: new Date(x.krajPregleda).toLocaleTimeString(),
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          tipPregleda: x.tipPregleda.naziv,
          sala: x.unapredDefinisaniPregled ? x.unapredDefinisaniPregled.sala.oznaka : null,
          pregled: x.unapredDefinisaniPregled
        };
      });
    },
    _obradjeniUpiti: function(){
      let temp = this.upiti.filter(x => x.adminObradio && x.izmenjeniPregled == null);
      return temp.map(x => {
        return {
          id: x.id,
          pacijent: `${x.pacijent.ime} ${x.pacijent.prezime}`,
          datum: new Date(x.pocetakPregleda).toLocaleDateString(),
          pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
          kraj: new Date(x.krajPregleda).toLocaleTimeString(),
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          tipPregleda: x.tipPregleda.naziv,
          sala: x.unapredDefinisaniPregled ? x.unapredDefinisaniPregled.sala.oznaka : null,
          pregled: x.unapredDefinisaniPregled,
          odobren: x.odobren,
          pacijentObradio: x.pacijentObradio,
          potvrdjen: x.potvrdjen
        };
      });
    }

  },
  created(){
    this.fetchPregledi();
    this.fetchUpiti();
  },
  methods: {
    ...mapActions({
      fetchUpiti: "upitiPreglediAdmin/loadUpiti",
      fetchPregledi: "preglediAdmin/fetchPreglediKlinike",
      obradiAdmin: "upitiPreglediAdmin/obradiAdmin",
      deleteUpit: "upitiPreglediAdmin/deleteUpit"
    }),
    accept(item){
      //SAMO za UPIT ZA UNAPRED DEFINISAN PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      let obj = {
        id: updatedItem.id,
        adminObradio: true,
        odobren: true,
        unapredDefinisaniPregled: {
          id: updatedItem.unapredDefinisaniPregled ? updatedItem.unapredDefinisaniPregled.id : this.noviPregled.id
        }
      }

      //PUT zahtev za accept
      this.obradiAdmin(obj);
      this.refreshTables();
    },
    reject(item){
      //SAMO za UPIT ZA UNAPRED DEFINISAN PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      let obj = {
        id: updatedItem.id,
        adminObradio: true,
        odobren: false,
        unapredDefinisaniPregled: {
          id: updatedItem.unapredDefinisaniPregled ? updatedItem.unapredDefinisaniPregled.id : this.noviPregled.id
        }
      }

      //PUT zahtev za reject
      this.obradiAdmin(obj);
      this.refreshTables();
    },
    deleteItem(item){
      //TODO: brisanje upita na serveru(samo ako je pacijent obradio upit)
      //voditi racuna da se obrise i originalni upit ukoliko je item zapravo izmenjeniUpit
      this.deleteUpit(item.id).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    },

    refreshTables(){
      this._neobradjeniUpiti;
      this._obradjeniUpiti
    }

  }
}
</script>

<style>

</style>