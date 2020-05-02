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
  name: "TabelaLekara",
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
      data: "osoblje/getMedicinskoOsoblje"
    }),
    transformedData: function(){
      if(this.currentIndex == this.index)
        return this.data.filter(x => 
          x.pozicija == "lekar" &&
          x.tipovi_pregleda.filter(y => y.id == this.odabraniTipPregleda.id).length != 0 &&
          x.pregledi.filter(y => {
            let start = new Date(y.pocetakPregleda);
            let end = new Date(y.krajPregleda);
            return (start.getTime() <= this.pocetak.getTime() && end.getTime() >= this.pocetak.getTime()) ||
              (this.pocetak.getTime() <= start.getTime() && this.kraj.getTime() >= start.getTime())
          }).length == 0);
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