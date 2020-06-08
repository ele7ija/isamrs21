<template>
<div>
  <v-data-table
    :headers="headers"
    :items="getNeovereniRecepti"
    :search="searchNeovereni"
    class="elevation-1 mx-4"
    item-key="poseta.id"
    show-expand  >
    <template v-slot:top>
      <v-toolbar flat color="error lighten-5">
        <v-toolbar-title> Neovereni Recepti</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>       
        <!-- search bar  -->
        <v-text-field
          v-model="searchNeovereni"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
      </v-toolbar>
    </template>

    <!-- lekovi idu na expaned -->
    <template v-slot:expanded-item="{ headers, item }">
      <td :colspan="headers.length" style="padding:15px " class="grey--text ">
      <v-data-table
        :headers="headersLekovi"
        :items="item.poseta.lekovi"
        hide-default-footer        
        class="elevation-2 blue lighten-5 ">
      </v-data-table>
      </td>
    </template>

    <template v-slot:item.actions ="{ item }" >
      <v-btn dark class=" mr-2 blue" @click="overi(item)"> 
        Overi
        <v-icon right >mdi-check</v-icon>
      </v-btn>
    </template>
  </v-data-table>

  <v-divider class="my-4" inset vertical></v-divider>

  <v-data-table
    :headers="headers"
    :items="getOvereniRecepti"
    :search="searchOvereni"
    class="elevation-1 mx-4"
    show-expand  >
    <template v-slot:top>
      <v-toolbar flat color="green lighten-5">
        <v-toolbar-title> Overeni Recepti</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>       
        <!-- search bar -->
        <v-text-field
          v-model="searchOvereni"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
      </v-toolbar>
    </template>

    <!-- lekovi idu na expaned -->
    <template v-slot:expanded-item="{ headers, item }">
      <td :colspan="headers.length" style="padding:15px " class="grey--text ">
      <v-data-table
        :headers="headersLekovi"
        :items="item.poseta.lekovi"
        hide-default-footer        
        class="elevation-2 blue lighten-5 ">
      </v-data-table>
      </td>
    </template>
  </v-data-table>
</div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  data(){
    return {
      searchNeovereni: '',
      searchOvereni: '',
      headers: [
        {
          text: 'id',
          value: 'id'
        },
        {
          text: 'Ime',
          value: 'poseta.zdravstveniKarton.pacijent.ime',
          sortable: true,
        },
        {
          text: 'Prezime',
          value: 'poseta.zdravstveniKarton.pacijent.prezime',
          sortable: true,
        },
        {
          text: 'Dijagnoza',
          value: 'poseta.bolest.naziv',
          sortable: true,
        },
        {
          text: 'Lekovi',
          value: 'data-table-expand',
          sortable: false,
        },
        {
          text: 'Akcije',
          value: 'actions',
          sortable: false,
          align: "end",
        }
      ],
      headersLekovi: [
        {
          text: 'Šifra',
          value: 'sifra',
          sortable: false,
        },
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: false,
        },
      ],

    }
  },

  computed: {
    ...mapGetters({
      getOvereniRecepti: 'recepti/getOvereniRecepti',
      getNeovereniRecepti: 'recepti/getNeovereniRecepti',
    })
  },

  created() {
    this.fetchRecepti();
  },

  methods:{
    ...mapActions({
      fetchRecepti: 'recepti/loadRecepti',
      overiRecept:  'recepti/overiRecept',
    }),

    overi(item){
      var recept = {
        id: item.id,
        overen: true,
      }
      this.overiRecept(recept);
    }

  }
}
</script>

<style>

</style>