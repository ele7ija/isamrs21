<template>
  <div>
    <v-container fluid>
      <div class="ml-5 mr-5">
        <SlobodniPregledi :data="_slobodniPregledi" class="mt-5"></SlobodniPregledi>
        <RezervisaniPregledi :data="_rezervisaniPregledi" class="mt-10"></RezervisaniPregledi>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import RezervisaniPregledi from "@/components/pregledi/RezervisaniPregledi";
import SlobodniPregledi from "@/components/pregledi/SlobodniPregledi";
export default {
  name: "PreglediMain",
  components: {
    SlobodniPregledi,
    RezervisaniPregledi
  },
  computed: {
    ...mapGetters({
      preglediKlinike: 'preglediAdmin/getPreglediKlinike',
      osobljeKlinike: 'osoblje/getMedicinskoOsoblje',
      tipoviPregledaKlinike: 'tipoviPregleda/getTipoviPregleda',
      saleKlinike: 'sale/getSale'
    }),
    _preglediMapped: function(){
      return this.preglediKlinike.map(x => {
        let osoblje = this.osobljeKlinike.filter(y => y.id == x.lekar.id)[0];
        let tipPregleda = this.tipoviPregledaKlinike.filter(y => y.id == x.tipPregleda.id)[0];
        let sala = this.saleKlinike.filter(y => y.id == x.sala.id)[0];
        let retval = {
          id: x.id,
          datum: new Date(x.pocetakPregleda).toLocaleDateString(),
          pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
          kraj: new Date(x.krajPregleda).toLocaleTimeString(),
          lekar: `${osoblje.ime} ${osoblje.prezime}`,
          tipPregleda: tipPregleda.naziv,
          sala: sala.oznaka,
          cena: x.cena,
          poseta: x.poseta //potrebno za dalju filtraciju slobodnih i rezervisanih pregleda
        };
        if(retval.poseta != null){
          retval.pacijent = `${x.poseta.zdravstveniKarton.pacijent.ime} ${x.poseta.zdravstveniKarton.pacijent.prezime}`
        }
        return retval;
      });
    },
    _slobodniPregledi: function(){
      return this._preglediMapped.filter(x => x.poseta == null);
    },
    _rezervisaniPregledi: function(){
      return this._preglediMapped.filter(x => x.poseta != null);
    },

  },
  created(){
    this.fetchOsobljeKlinike();
    this.fetchSaleKlinike();
    this.fetchTipoviPregledaKlinike();
    this.fetchPreglediKlinike();
  },
  methods: {
    ...mapActions({
      fetchPreglediKlinike: 'preglediAdmin/fetchPreglediKlinike',
      fetchOsobljeKlinike: 'osoblje/loadMedicinskoOsoblje',
      fetchSaleKlinike: 'sale/loadSale',
      fetchTipoviPregledaKlinike: 'tipoviPregleda/loadTipoviPregleda',
      fetchPacijenti: 'pacijenti/loadTipoviPregleda',
    }),
  }

}
</script>

<style>

</style>