<template>
  <v-data-table
    :headers="headers"
    :items="zahtevi._zahtevi"
    :search="search"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-toolbar-title>{{zahtevi.title}}</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-subheader>{{zahtevi.subtitle}}</v-subheader>
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
        <td>{{item.prviDanGodisnjeg}}</td>
        <td>{{item.poslednjiDanGodisnjeg}}</td>
      </tr>
    </template>
  </v-data-table>
</template>

<script>
export default {
  name: 'ZahteviZaOdsustvo',
  props: ['zahtevi'],
  data: function(){
    return {
      search: '',
      headers: [
        {
          text: 'Prvi dan odsustva',
          value: 'prviDanGodisnjeg',
          sortable: false

        },
        { 
          text: 'Poslednji dan odsustva',
          value: 'poslednjiDanGodisnjeg',
          sortable: false
        }
      ]
    };
  },
  methods: {
    isValid(item){
      let start = this.$utility.stringToDate2(item.prviDanGodisnjeg);
      let end = this.$utility.stringToDate2(item.poslednjiDanGodisnjeg);
      let start2 = new Date(this.zahtevi.pocetak);
      start2.setHours(0,0,0,0);
      let end2 = new Date(this.zahtevi.kraj);
      end2.setHours(0,0,0,0);
      return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
    }
  }
}
</script>

<style>

</style>