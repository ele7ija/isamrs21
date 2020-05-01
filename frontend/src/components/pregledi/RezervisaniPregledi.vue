<template>
  <v-data-table
    :headers="headers"
    :items="data"
    :search="search"
    class="elevation-1"
    >
    <template v-slot:top>
      <v-toolbar flat color="blue lighten-3">
        <v-toolbar-title>Rezervisani pregledi unutar klinike</v-toolbar-title>
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
</template>

<script>
export default {
  name: "PreglediAdmin",
  props: ["data"],
  data: function(){
    return {
      search: '',
      headers: [
        {
          text: 'Datum',
          value: 'datum',
          sortable: true

        },
        { 
          text: 'Pocetak',
          value: 'pocetak',
          sortable: true
        },
        { 
          text: 'Kraj',
          value: 'kraj',
          sortable: true
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
          text: 'Cena',
          value: 'cena',
          sortable: true
        },
        { 
          text: 'Sala',
          value: 'sala',
          sortable: true
        },
        { 
          text: 'Pacijent',
          value: 'pacijent',
          sortable: true
        }
      ]
    };
  }
}
</script>

<style>

</style>