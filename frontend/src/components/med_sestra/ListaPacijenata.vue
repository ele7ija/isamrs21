<template>
  <v-container fluid>
    <v-data-table
      :headers="headers"
      :items="_pacijenti"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title class="headline">Pacijenti kliničkog centra</v-toolbar-title>
        </v-toolbar>
      </template>
      <template v-slot:body.prepend>
        <tr>
          <td>
            <v-text-field v-model="imePrezime" label="Filtriraj"></v-text-field>
          </td>
          <td>
            <v-text-field v-model="jbo" label="Filtriraj"></v-text-field>
          </td>
          <td>
            <v-btn @click="resetFilters">Poništi</v-btn>
          </td>
        </tr>
      </template>

    </v-data-table>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
export default {
  name: "PacijentiSearch",
  data: function(){
    return{
      LekarIliSestra: 'sestra',
      imePrezime: '', //FILTER
      jbo: '', //FILTER
      headers: [
        { text: 'Ime i prezime', value: 'imePrezime', sortable: true,
          filter: (value) => {
            if(!this.imePrezime)
              return true;
            return value.indexOf(this.imePrezime) != -1 || this.imePrezime.indexOf(value) != -1;
          }
        },
        { text: 'Jedinstveni broj osiguranika', value: 'jbo', sortable: true,
          filter: (value) => {
            if(!this.jbo)
              return true;
            return value.indexOf(this.jbo) != -1 || this.jbo.indexOf(value) != -1;
          }
        },
        { text: 'Akcije', value: 'actions', sortable: false},
      ]
    };
  },
  computed: {
    ...mapGetters({
      pacijenti: 'pacijenti/getPacijenti',
    }),
    _pacijenti(){
      if(this.pacijenti) //vue caveats
        return this.pacijenti.map(x => {
          return {
            id: x.id,
            imePrezime: `${x.ime} ${x.prezime}`,
            jbo: x.jbo
          };
        })
      else
        return [];
    }
  },
  created(){
    this.fetchPacijenti();
  },
  methods: {
    ...mapActions({
      fetchPacijenti: 'pacijenti/loadPacijenti',
    }),
    viewProfile(item){
      let pacijent = this.pacijenti.filter(x => x.id == item.id)[0];
      this.$store.commit("pacijenti/setOdabraniPacijent", pacijent, {root: true});
      this.$router.push({name: "ProfilPacijenta"});
    },
    resetFilters(){
      this.imePrezime = '';
      this.jbo = '';
    }
  }
}
</script>

<style>

</style>