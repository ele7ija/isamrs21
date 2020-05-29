<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="transformedData"
      :search="search"
      show-select
      single-select
      class="elevation-1"
      @item-selected="rowSelect"
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Lekari</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
          
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-toolbar>
      </template>
    </v-data-table>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: "TabelaLekaraPrelgedi",
  props: ["index", "currentIndex"],
  data: function(){
    return{
      search: "",
      headers: [
        {
          text: 'Ime',
          value: 'ime',
          sortable: true,

        },
        {
          text: 'Prezime',
          value: 'prezime',
          sortable: true
        },
        {
          text: 'Email',
          value: 'email',
          sortable: true
        }
      ]
    };
  },
  computed: {
    ...mapGetters({
      odabraniTipPregleda: "pregledDialog/getTipPregleda",
      pocetak: "pregledDialog/getPocetak",
      kraj: "pregledDialog/getKraj",
      data: "osoblje/getMedicinskoOsoblje",
      pregledi: "preglediAdmin/getPreglediKlinike"
    }),
    transformedData: function(){
      if(this.currentIndex == this.index)
        return this.data.filter(x => 
          x.pozicija == "lekar" &&
          x.tipovi_pregleda.filter(y => y.id == this.odabraniTipPregleda.id).length != 0 &&
          x.pregledi.filter(y => { //provera da li lekar ima termin pregleda u ovom terminu
            let pregled = this.pregledi.filter(z => z.id == y.id)[0];
            let start = pregled.pocetakPregleda;
            let end = pregled.krajPregleda;
            let start2 = new Date(this.pocetak);
            let end2 = new Date(this.kraj);
            return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
          }).length == 0 &&
          x.dodatneOperacije.filter(y => { //provera da li lekar ima dodatnu operaciju u ovom terminu
            let pregled = this.pregledi.filter(z => z.id == y.id)[0];
            let start = pregled.pocetakPregleda;
            let end = pregled.krajPregleda;
            let start2 = new Date(this.pocetak);
            let end2 = new Date(this.kraj);
            return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
          }).length == 0 &&
          x.radniKalendar.zahteviZaGodisnjiOdmor.filter(y => { //provera da li je lekar na odsustvu u ovom terminu
            if(!y.odobreno)
              return false;
            let start = this.$utility.handleTimeZone(new Date(y.prviDanGodisnjeg));
            let end = this.$utility.handleTimeZone(new Date(y.poslednjiDanGodisnjeg));
            let start2 = new Date(this.pocetak);
            start2.setHours(0,0,0,0);
            let end2 = new Date(this.kraj);
            end2.setHours(0,0,0,0);
            return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
          }).length == 0
          );
      else
        return [];
    }
  },
  methods: {
    rowSelect: function ({item, value}) {
      if(value){
        this.$store.commit("pregledDialog/setLekar", item);
        let obj = {
          index: this.index,
          done: true,
        }
        this.$emit('changeStatus', obj)
      }else{
        this.$store.commit("pregledDialog/setLekar", null);
        let obj = {
          index: this.index,
          done: false,
        }
        this.$emit('changeStatus', obj)
      }
    }
  }
}
</script>

<style>

</style>