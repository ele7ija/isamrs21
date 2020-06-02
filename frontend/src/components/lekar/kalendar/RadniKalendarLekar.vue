<template>
<div>
  <h1 class="display-2">Moj kalendar</h1>
  <KalendarPrikaz :events="this.posete" :today="this.today"/>
</div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import KalendarPrikaz from './KalendarPrikaz'
export default {
  components:{
    KalendarPrikaz,
  },
  data (){
    return {
      lekaroviPacijenti: [],
      posete: [],
      today: undefined,
    }
  },

  computed: {
    ...mapGetters({
      pacijenti: "pacijenti/getPacijenti",
    }),
  },
  created(){
    this.init();
  },
  methods: {
    ...mapActions({
      fetchPacijenti: 'pacijenti/loadPacijenti',
    }),

    async init(){
      await this.fetchPacijenti();
      for ( var pacijent of this.pacijenti){
        /*UCITATI SAMO POSETE KOD TOG LEKARA. 
        PRODJI KROZ SVE PACIJENTE I UCITAJ POSETE SAMO KOD IZABRANOG LEKARA
        simuliraj setovanje odabranog pacijenta da bi mogla da se pozove checkLekarovPacijentMetoda
        zatim proveri da li pacijent pripada tom lekaru
        ako pripada dodaj njegove posete u listu poseta.
        */
        this.$store.commit("pacijenti/setOdabraniPacijent", pacijent, {root: true});
        await this.$store.dispatch("pacijenti/checkLekarovPacijent",{root: true});
        var isLekarovPacijent = this.$store.getters["pacijenti/isLekarovPacijent"];
        if( isLekarovPacijent){
          this.lekaroviPacijenti.push(pacijent);
          for (var poseta of pacijent.zdravstveniKarton.posete){
            console.log(poseta);
            this.posete.push({
              name: poseta.pregled.tipPregleda.naziv,
              start: this.formatDate(poseta.pregled.pocetakPregleda),
              end: this.formatDate(poseta.pregled.krajPregleda),
              ime: `${pacijent.ime} ${pacijent.prezime}`,
              posetaId: poseta.id,
              pacijentObject: pacijent,
              opis: poseta.opis,
              color: "blue",
            })
          }
        }
      }  
      
      //setuj trenutni datum
      var a = new Date();
      this.today = `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`;
    },

    formatDate (datum) {
      var a = new Date(datum);
      a = this.$utility.handleTimeZone(a);
      return `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`;
    },
  }
}
</script>

<style>

</style>