<template>
  <h1>kalendar</h1>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  data (){
    return {
      lekaroviPacijenti: [],
      events: [],
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
      //TODO: UCITATI SAMO POSETE KOD TOG LEKARA. 
      //PRODJI KROZ SVE PACIJENTE I UCITAJ POSETE SAMO KOD IZABRANOG LEKARA
    }),

    async init(){
      await this.fetchPacijenti();
      for ( var pacijent of this.pacijenti){
        /*UCITATI SAMO POSETE KOD TOG LEKARA. 
        PRODJI KROZ SVE PACIJENTE I UCITAJ POSETE SAMO KOD IZABRANOG LEKARA
        simuliraj setovanje odabranog pacijenta da bi mogla da se pozove checkLekarovPacijentMetoda
        zatim proveri da li pacijent pripada tom lekaru
        ako pripada dodaj njegove posete u listu eventova.
        */
        this.$store.commit("pacijenti/setOdabraniPacijent", pacijent, {root: true});
        await this.$store.dispatch("pacijenti/checkLekarovPacijent",{root: true});
        var isLekarovPacijent = this.$store.getters["pacijenti/isLekarovPacijent"];
        if( isLekarovPacijent){
          this.lekaroviPacijenti.push(pacijent);
          for (var poseta of pacijent.zdravstveniKarton.posete){
            console.log("pocetak pregleda: ",poseta.pregled);
            this.events.push({
              name: poseta.pregled.tipPregleda.naziv,
              start: poseta.pregled.pocetakPregleda,
              end: poseta.pregled.krajPregleda,
              details: `${pacijent.ime}  ${pacijent.prezime}`,
            })
          }
        }
      }
      console.log(this.events);      
      //TODO: dodaj eventove u kalendar

    }
  }
}
</script>

<style>

</style>