<template>
  <v-data-table
    :headers="headers"
    :items="preglediSale.pregledi"
    :search="search"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-toolbar-title>Kalendar pregleda sale: {{preglediSale.sala.oznaka}}</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-subheader>Crvenom bojom su označeni pregledi zbog kojih sala nije slobodna za datum iz upita</v-subheader>
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
    <template v-slot:item="{ item }">
        <tr :class="{
              'red lighten-3': ((item.pocetak_date.getTime() <= preglediSale.pocetak.getTime() && 
                                item.kraj_date.getTime() >= preglediSale.pocetak.getTime()) || 
                                (preglediSale.pocetak.getTime() <= item.pocetak_date.getTime() && 
                                preglediSale.kraj.getTime() >= item.pocetak_date.getTime()))
              }">
          <td>{{item.datum}}</td>
          <td>{{item.pocetak}}</td>
          <td>{{item.kraj}}</td>
          <td>{{item.lekar}}</td>
          <td>{{item.tipPregleda}}</td>
          <td>{{item.konacnaCena}}</td>
          <td>{{item.pacijent}}</td>
        </tr>
      </template>
  </v-data-table>
</template>

<script>
export default {
  name: 'PreglediSale',
  props: ['preglediSale'],
  data: function(){
    return {
      search: '',
      headers: [
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
          text: 'Cena sa popustom',
          value: 'konacnaCena',
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