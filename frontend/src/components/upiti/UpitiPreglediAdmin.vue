<template>
  <div>
    <v-container fluid>
      <div class="ml-5 mr-5">
        <TabelaNeobradjenihUpita :all="_neobradjeniUpiti" class="mt-5" @accept="accept" @reject="reject" @acceptCustom="acceptCustom" :key="key1"></TabelaNeobradjenihUpita>
        <TabelaObradjenihUpita :all="_obradjeniUpiti" class="mt-5" @deleteItem="deleteItem" :key="key2"></TabelaObradjenihUpita>
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
      key1: 0,
      key2: 2,
      snackbar: false,
      snackbarTimeout: 5000,
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
      let temp = this.upiti.filter(x => !x.adminObradio && x.izmenjeniPregled == null);
      return temp.map(x => {
        return {
          id: x.id,
          version: x.version,
          pacijent: `${x.pacijent.ime} ${x.pacijent.prezime}`,
          datum: new Date(x.pocetakPregleda).toLocaleDateString(),
          pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
          kraj: new Date(x.krajPregleda).toLocaleTimeString(),
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          tipPregleda: x.tipPregleda.naziv,
          sala: x.sala ? x.sala.oznaka : null,
          pregled: x.unapredDefinisaniPregled,
        };
      });
    },
    _obradjeniUpiti: function(){
      let temp = this.upiti.filter(x => x.adminObradio && x.izmenjeniPregled == null);
      return temp.map(x => {
        return {
          id: x.id,
          version: x.version,
          pacijent: `${x.pacijent.ime} ${x.pacijent.prezime}`,
          datum: new Date(x.pocetakPregleda).toLocaleDateString(),
          pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
          kraj: new Date(x.krajPregleda).toLocaleTimeString(),
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          tipPregleda: x.tipPregleda.naziv,
          sala: x.sala ? x.sala.oznaka : null,
          pregled: x.unapredDefinisaniPregled,
          odobren: x.odobren,
          pacijentObradio: x.pacijentObradio,
          potvrdjen: x.potvrdjen,
          _odobren: x.odobren ? "da" : "ne"
        };
      });
    }

  },
  created(){
    this.fetchPregledi();
    this.fetchSale();
    this.fetchOsoblje();
    this.fetchUpiti();
    this.$store.commit("salaFilter/reset", {}, {root:true});
  },
  methods: {
    ...mapActions({
      fetchUpiti: "upitiPreglediAdmin/loadUpiti",
      fetchSale: "sale/loadSale",
      fetchOsoblje: "osoblje/loadMedicinskoOsoblje",
      fetchPregledi: "preglediAdmin/fetchPreglediKlinike",
      obradiAdmin: "upitiPreglediAdmin/obradiAdmin",
      obradiAdminCustom: "upitiPreglediAdmin/obradiAdminCustom",
      deleteUpit: "upitiPreglediAdmin/deleteUpit"
    }),
    accept(item){
      //SAMO za UPIT ZA UNAPRED DEFINISAN PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      let obj = {
        id: updatedItem.id,
        version: updatedItem.version,
        adminObradio: true,
        odobren: true,
        unapredDefinisaniPregled: {
          id: updatedItem.unapredDefinisaniPregled ? updatedItem.unapredDefinisaniPregled.id : this.noviPregled.id
        }
      }

      //PUT zahtev za accept
      this.obradiAdmin(obj).then(null, (error) => {
        this.snackbar = true;
        this.snackbarText = error;
      });
      this.refreshTables();
    },

    acceptCustom({upit, cena, popust, konacnaCena, dodatniLekari}){
      //SAMO ZA CUSTOM PREGLED
      //prvo dodaje pregled, obradjuje upit i definise novi upit ako je to potrebno
      upit.unapredDefinisaniPregled = {
        cena,
        popust,
        konacnaCena,
        dodatniLekari
      };
      this.obradiAdminCustom(upit).then(
        null, (error) => {
          this.snackbar = true;
          this.snackbarText = error;
        }
      );
      
      this.refreshTables();
    },

    reject(item){
      //SAMO za UPIT ZA UNAPRED DEFINISAN PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      let obj = {
        id: updatedItem.id,
        version: updatedItem.version,
        adminObradio: true,
        odobren: false,
      }
      if(updatedItem.unapredDefinisaniPregled){
        obj.unapredDefinisaniPregled = {
          id: updatedItem.unapredDefinisaniPregled.id
        };
      }
      //PUT zahtev za reject
      this.obradiAdmin(obj).then(null, (error) => {
        this.snackbar = true;
        this.snackbarText = error;
      });
      this.refreshTables();
    },
    deleteItem(item){
      this.deleteUpit({idUpita: item.id, version: item.version}).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    },

    refreshTables(){
      this.key1++;
      this.key2++;
    }

  }
}
</script>

<style>

</style>