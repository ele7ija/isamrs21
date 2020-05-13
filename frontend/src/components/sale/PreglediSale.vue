<template>
  <v-data-table
    :items="preglediView"
    :headers="headers"
    :search="search">
    <template v-slot:top>
      <v-toolbar flat color="blue lighten-3">
        <v-toolbar-title>Pregledi</v-toolbar-title>
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
import {mapGetters} from 'vuex';
export default {
  name: "PreglediSale",
  props: ["idSale"],
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
          sortable: false
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
    }
  },
  computed: {
    ...mapGetters({
      pregledi: 'preglediAdmin/getPreglediKlinike'
    }),
    preglediFiltered(){
      return this.pregledi.filter(x => x.sala.id == this.idSale);
    },
    preglediView(){
      return this.preglediFiltered.map(x => {
        return {
          id: x.id,
          datum: new Date(x.pocetakPregleda).toLocaleDateString(),
          pocetak: new Date(x.pocetakPregleda).toLocaleTimeString(),
          kraj: new Date(x.krajPregleda).toLocaleTimeString(),
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          tipPregleda: x.tipPregleda.naziv,
          sala: x.sala.oznaka,
          konacnaCena: parseInt(x.konacnaCena, 10),
          upiti: x.upiti,
          pacijent: this.getPacijent(x)
        };
      });
    }
  },
  methods: {
    getPacijent(x){
      /*
      x - instanca pregleda
      returns - ime i prezime pacijentaukoliko je pregled rezervisan, null inace 
      */
      if(x.poseta != null)
        return `${x.poseta.zdravstveniKarton.pacijent.ime} ${x.poseta.zdravstveniKarton.pacijent.prezime}`;
      for(let upit of x.upiti){
        if(upit.potvrdjen){
          return `${upit.pacijent.ime} ${upit.pacijent.prezime}`;
        }
        if(upit.odobren && !upit.pacijentObradio){
          return `${upit.pacijent.ime} ${upit.pacijent.prezime}`;
        }
      }
      return null;
    }
  }
}
</script>

<style>

</style>