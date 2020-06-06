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
      fetchZahteviZaGodisnji: 'zahteviZaGodisnjiOsoblje/fetchMojiZahtevi',
    }),

    async init(){
      await this.loadPosete();
      await this.loadGodisnjiOdmor();
      //setuj trenutni datum
      var a = new Date();
      this.today = `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`;
    },

    async loadPosete(){
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
    },

    async loadGodisnjiOdmor(){
      await this.fetchZahteviZaGodisnji();
      var godisnji = this.$store.getters["zahteviZaGodisnjiOsoblje/getNeobradjeniZahteviZaGodisnji"]
      for(var g of godisnji){
        console.log(g);
        this.posete.push({
          name: "Odsustvo",
          start: this.formatDate(g.prviDanGodisnjeg),
          end: getPoslednjiDanGodisnjeg(g.poslednjiDanGodisnjeg),
          opis:"godisnji odmor",
          color: "pink",
        })
      }

      function getPoslednjiDanGodisnjeg(poslednjiDanGodisnjeg){
        var a = new Date(poslednjiDanGodisnjeg);
        a.setHours(23);
        a.setMinutes(59);
        return  `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`;
      }
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