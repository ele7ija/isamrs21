<template>
  <div>
    <v-container fluid>
      <div class="ml-2 mr-2">
        <TabelaPregleda :data="_slobodniPregledi" :title="'Slobodni pregledi lekara'" class="mt-n2"></TabelaPregleda>
        <TabelaPregleda :data="_rezervisaniPregledi" :title="'Rezervisani pregledi lekara'" class="mt-7"></TabelaPregleda>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import TabelaPregleda from "./TabelaPregleda";
export default {
  name: "PreglediLekar",
  props: ["idLekara"],
  components: {
    TabelaPregleda
  },
  computed: {
    ...mapGetters({
      preglediKlinike: 'preglediAdmin/getPreglediKlinike',
      osobljeKlinike: 'osoblje/getMedicinskoOsoblje',
      tipoviPregledaKlinike: 'tipoviPregleda/getTipoviPregleda',
      saleKlinike: 'sale/getSale'
    }),
    _preglediMapped: function(){
      try{
        let temp = this.preglediKlinike.map(x => {
          let osoblje = this.osobljeKlinike.filter(y => y.id == x.lekar.id)[0];
          let tipPregleda = this.tipoviPregledaKlinike.filter(y => y.id == x.tipPregleda.id)[0];
          let sala = this.saleKlinike.filter(y => y.id == x.sala.id)[0];
          let retval = {
            id: x.id,
            datum: new Date(x.pocetakPregleda).toLocaleDateString(),
            pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
            kraj: new Date(x.krajPregleda).toLocaleTimeString(),
            lekar: `${osoblje.ime} ${osoblje.prezime}`,
            lekarId: osoblje.id,
            tipPregleda: tipPregleda.naziv,
            sala: sala.oznaka,
            cena: parseInt(x.cena, 10),
            popust: x.popust,
            konacnaCena: parseInt(x.konacnaCena, 10),
            poseta: x.poseta //potrebno za dalju filtraciju slobodnih i rezervisanih pregleda
          };
          if(retval.poseta != null){
            retval.pacijent = `${x.poseta.zdravstveniKarton.pacijent.ime} ${x.poseta.zdravstveniKarton.pacijent.prezime}`
          }
          return retval;
        });
        return temp.filter(x => x.lekarId == this.idLekara);
      }catch{
        console.log("Ups");
        return [];
      }
     
    },
    _slobodniPregledi: function(){
      return this._preglediMapped.filter(x => x.poseta == null);
    },
    _rezervisaniPregledi: function(){
      return this._preglediMapped.filter(x => x.poseta != null);
    },

  },
  created(){
    this.fetchSaleKlinike();
    this.fetchPreglediKlinike();
  },
  methods: {
    ...mapActions({
      fetchPreglediKlinike: 'preglediAdmin/fetchPreglediKlinike',
      fetchSaleKlinike: 'sale/loadSale',
    }),
  }

}
</script>

<style>

</style>