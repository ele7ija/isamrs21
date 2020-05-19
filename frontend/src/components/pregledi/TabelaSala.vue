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
          <v-toolbar-title>Sale</v-toolbar-title>
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
  name: "TabelaLekara",
  props: ["index", "currentIndex"],
  data: function(){
    return{
      search: "",
      headers: [
        {
          text: 'Oznaka',
          value: 'oznaka',
          sortable: true,

        }
      ]
    };
  },
  computed: {
    ...mapGetters({
      odabraniTipPregleda: "pregledDialog/getTipPregleda",
      pocetak: "pregledDialog/getPocetak",
      kraj: "pregledDialog/getKraj",
      data: "sale/getSale",
      pregledi: "preglediAdmin/getPreglediKlinike"
    }),
    transformedData: function(){
      if(this.currentIndex == this.index)
        return this.data.filter(x => 
          x.pregledi.filter(y => {
            let pregled = this.pregledi.filter(z => z.id == y.id)[0]; //zbog dobre vremenske zone
            let start = pregled.pocetakPregleda;
            let end = pregled.krajPregleda;
            let start2 = this.pocetak;
            let end2 = this.kraj;
            return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
          }).length == 0);
      else
        return [];
    }
  },
  methods: {
    rowSelect: function ({item, value}) {
      if(value){
        this.$store.commit("pregledDialog/setSala", item);
        let obj = {
          index: this.index,
          done: true,
        }
        this.$emit('changeStatus', obj)
      }else{
        this.$store.commit("pregledDialog/setSala", null);
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