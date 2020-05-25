<template>
  <v-data-table
    :headers="headers"
    :items="pregledi._pregledi"
    :search="search"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-toolbar-title>{{pregledi.title}}</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-subheader>{{pregledi.subtitle}}</v-subheader>
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
            'red lighten-3': isValid(item)
            }">
        <td>{{item.datum}}</td>
        <td>{{item.pocetak}}</td>
        <td>{{item.kraj}}</td>
        <td>{{item.lekar}}</td>
        <td>{{item.tipPregleda}}</td>
        <td>{{item.vrsta}}</td>
        <td>{{item.konacnaCena}}</td>
        <td>{{item.pacijent}}</td>
      </tr>
    </template>
  </v-data-table>
</template>

<script>
export default {
  name: 'Pregledi',
  props: ['pregledi'],
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
          text: 'Vrsta',
          value: 'vrsta',
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
  },
  methods: {
    isValid(item){
      let start = item.pocetak_date;
      let end = item.kraj_date;
      let start2 = this.pregledi.pocetak;
      let end2 = this.pregledi.kraj;
      return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
    }
  }
}
</script>

<style>

</style>