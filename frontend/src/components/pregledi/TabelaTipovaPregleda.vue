<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="data"
      :search="search"
      show-select
      single-select
      class="elevation-1"
      @item-selected="rowSelect"
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Tipovi Pregleda</v-toolbar-title>
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
  name: "TabelaTipovaPregleda",
  props: ["index"],
  data: function(){
    return{
      search: "",
      headers: [
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: true,

        },
        {
          text: 'Opis',
          value: 'opis',
          sortable: false
        }
      ]
    };
  },
  computed: {
    ...mapGetters({
      data: "tipoviPregleda/getTipoviPregleda"
    })
  },
  methods: {
    rowSelect: function ({item, value}) {
      if(value){
        this.$store.commit("pregledDialog/setTipPregleda", item);
        let obj = {
          index: this.index,
          done: true,
        }
        this.$emit('changeStatus', obj)
      }else{
        this.$store.commit("pregledDialog/setTipPregleda", null);
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