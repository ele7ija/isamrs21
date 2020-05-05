<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="all"
      :search="search"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="id"
      show-expand
      class="elevation-1"
      >
      <template v-slot:top>
        <v-toolbar flat color="blue lighten-3">
          <v-toolbar-title>Obradjeni upiti za preglede</v-toolbar-title>
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
      <template v-slot:item.actions="{ item }">
        <v-icon
          small
          class="mr-2"
          v-if="item.pregled!=null"
          @click="deleteItem(item)"
        >
          mdi-delete
        </v-icon>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <p class="text-center mt-2 mb-n4 pb-n4">Jos neki podaci o ovom pregledu</p>
          <br>
          Cena: {{item.pregled.cena}}
          Popust: {{item.pregled.popust}}
          KonacnaCena: {{item.pregled.konacnaCena}}
          Obradjen od strane pacijenta (preduslov za brisanje ovog upita): {{item.pacijentObradio}}
        </td>
      </template>
    </v-data-table>
  </div>
</template>

<script>
export default {
  name: "TabelaObradjenihUpita",
  props: ["all"],
  data: function(){
    return {
      search: '',
      expanded: [],
      singleExpand: true,
      headers: [
        {
          text: 'Pacijent',
          value: 'pacijent',
          sortable: true

        },
        {
          text: 'Datum',
          value: 'datum',
          sortable: false
        },
        { 
          text: 'Pocetak',
          value: 'pocetak',
          sortable: false
        },
        { 
          text: 'Kraj',
          value: 'kraj',
          sortable: false
        },
        { 
          text: 'Lekar',
          value: 'lekar',
          sortable: true
        },
        { 
          text: 'Tip Pregleda',
          value: 'tipPregleda',
          sortable: true
        },
        { 
          text: 'Sala',
          value: 'sala',
          sortable: true
        },
        { 
          text: 'Akcije',
          value: 'actions',
          sortable: false,
        },
        {
          text: 'Detalji',
          value: 'data-table-expand'
        }
      ]
    };
  },
  methods: {
    deleteItem(item){
      this.$emit("deleteItem", item);
    },
    
  }
}
</script>

<style>

</style>