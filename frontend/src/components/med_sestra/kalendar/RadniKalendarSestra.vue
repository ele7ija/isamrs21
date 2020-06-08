<template>
<div>
  <h1 class="display-1 blue--text text--darken-3 pa-3">Moj kalendar</h1>
  <KalendarPrikaz :events="this.godisnjiOdmori" :today="this.today"/>
</div>
</template>

<script>
import { mapActions } from 'vuex';
import KalendarPrikaz from './KalendarPrikaz'
export default {
  components:{
    KalendarPrikaz,
  },
  data (){
    return {
      lekaroviPacijenti: [],
      godisnjiOdmori: [],
      today: undefined,
    }
  },

  created(){
    this.init();
  },
  methods: {
    ...mapActions({
      fetchZahteviZaGodisnji: 'zahteviZaGodisnjiOsoblje/fetchAllZahtevi',
    }),

    async init(){
      await this.loadGodisnjiOdmor();
      //setuj trenutni datum
      var a = new Date();
      this.today = `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`;
    },


    async loadGodisnjiOdmor(){
      await this.fetchZahteviZaGodisnji();
      var godisnji = this.$store.getters["zahteviZaGodisnjiOsoblje/getMojiOdobreniZahtevi"];
      console.log(godisnji);
      for(var g of godisnji){
        this.godisnjiOdmori.push({
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