<template>
  <v-data-table
    :items="oceneLekara"
    :headers="headers"
    :search="search"
  >
    <template v-slot:top>
      <v-toolbar flat color="blue lighten-3">
        <v-toolbar-title class="headline">Prosečne ocene lekara klinike {{nazivKlinike}}</v-toolbar-title>
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
    <template v-slot:body.prepend>
      <tr>
        <td>
          <v-text-field v-model="imePrezime" label="Filtriraj" clearable></v-text-field>
        </td>
      </tr>
    </template>
    <template v-slot:item="{ item }">
    <tr>
      <td>{{item.imePrezime}}</td>
      <td v-if="Math.round(item.prosecnaOcena)">
        <v-rating
          :value="Math.round(item.prosecnaOcena)"
          readonly
          color="yellow darken-3"
          background-color="grey darken-1"
          length=10
        ></v-rating>
      </td>
      <td v-else>{{item.prosecnaOcena}}</td>
    </tr>
  </template>
  </v-data-table>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default{
  name: "ProsecnaOcenaLekara",
  data: function(){
    return{
      imePrezime: '',
      search: '',
      headers: [
        { text: 'Ime i prezime', value: 'imePrezime', sortable: true,
          filter: (value) => {
            if(!this.imePrezime)
              return true;
            return value.indexOf(this.imePrezime) != -1 || this.imePrezime.indexOf(value) != -1;
          }
        },
        { text: 'Prosečna ocena', value: 'prosecnaOcena', sortable: true}
      ]
    };
  },
  computed: {
    ...mapGetters({
      prosecneOceneLekara: "izvestaji/getProsecneOceneLekara",
      nazivKlinike: "izvestaji/getNazivKlinike"
    }),
    oceneLekara(){
      if(this.prosecneOceneLekara){
        return this.prosecneOceneLekara.map(x => {
          return {
            imePrezime: `${x.lekar.ime} ${x.lekar.prezime}`,
            prosecnaOcena: x.prosecnaOcena
          };
        });
      }else{
        return [];
      }
    }
  },
  created(){
    this.fetchOceneLekaraUnutarKlinike();
  },
  methods: {
    ...mapActions({
      fetchOceneLekaraUnutarKlinike: "izvestaji/fetchOceneLekaraUnutarKlinike"
    })
  }
}
</script>