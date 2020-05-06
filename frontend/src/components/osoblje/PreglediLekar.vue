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
            poseta: x.poseta, //potrebno za dalju filtraciju slobodnih i rezervisanih pregleda
            upiti: x.upiti
          };
          let pacijent = this.getPacijent(x);
          if(pacijent != null){
            retval.pacijent = pacijent;
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
      return this._preglediMapped.filter(x => this.isSlobodan(x));
    },
    _rezervisaniPregledi: function(){
      return this._preglediMapped.filter(x => this._slobodniPregledi.filter(y => y.id == x.id).length == 0);
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
    isSlobodan(x){
      if(x.poseta != null)
        return false;
      for(let upit of x.upiti){
        if(upit.potvrdjen){
          return false;
        }
        if(upit.odobren && !upit.pacijentObradio){
          return false;
        }
      }
      return true;
    },

    getPacijent(x){
      /*
      x - instanca pregleda
      returns - ime i prezime pacijentaukoliko je pregled rezervisan, null inace 
      */
      if(x.poseta != null)
        return `${x.poseta.zdravstveniKarton.pacijent.ime} ${x.poseta.zdravstveniKarton.pacijent.prezime}`;
      for(let upit of x.upiti){
        if(upit.potvrdjen){
          return `${upit.pacijent.ime} ${upit.pacijent.prezime}`;
        }
        if(upit.odobren && !upit.pacijentObradio){
          return `${upit.pacijent.ime} ${upit.pacijent.prezime}`;
        }
      }
      return null;
    }
  }

}
</script>

<style>

</style>