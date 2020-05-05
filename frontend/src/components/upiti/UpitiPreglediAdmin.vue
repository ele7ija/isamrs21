<template>
  <div>
    <v-container fluid>
      <div class="ml-5 mr-5">
        <TabelaNeobradjenihUpita :all="_neobradjeniUpiti" class="mt-5" @accept="accept" @reject="reject"></TabelaNeobradjenihUpita>
        <TabelaObradjenihUpita :all="_obradjeniUpiti" class="mt-5" @deleteItem="deleteItem"></TabelaObradjenihUpita>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import TabelaNeobradjenihUpita from './TabelaNeobradjenihUpita';
import TabelaObradjenihUpita from './TabelaObradjenihUpita';
export default {
  name: "UpitiPreglediAdmin",
  components: {
    TabelaNeobradjenihUpita,
    TabelaObradjenihUpita
  },
  computed:{
    ...mapGetters({
      upiti: "upitiPreglediAdmin/getUpiti"
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
          sala: x.pregled ? x.pregled.sala.oznaka : null,
          pregled: x.pregled
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
          sala: x.pregled ? x.pregled.sala.oznaka : null,
          pregled: x.pregled
        };
      });
    }
  },
  created(){
    this.fetchUpiti();
  },
  methods: {
    ...mapActions({
      fetchUpiti: "upitiPreglediAdmin/loadUpiti",
      obradiAdmin: "upitiPreglediAdmin/obradiAdmin",
    }),
    accept(item){
      //SAMO za UPIT ZA UNAPRED DEFINISAN PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      updatedItem.adminObradio = true;
      updatedItem.odobren = true;

      //PUT zahtev za accept
      this.obradiAdmin(updatedItem);
    },
    reject(item){
      //SAMO za UPIT ZA UNAPRED DEFINISAN PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      updatedItem.adminObradio = true;
      updatedItem.odobren = false;

      //PUT zahtev za reject
      this.obradiAdmin(updatedItem);
    },
    deleteItem(item){
      //TODO: brisanje upita na serveru(samo ako je pacijent obradio upit)
      //voditi racuna da se obrise i originalni upit ukoliko je item zapravo izmenjeniUpit
      return item;
    }
  }
}
</script>

<style>

</style>