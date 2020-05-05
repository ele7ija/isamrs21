<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="all"
      :search="search"
      class="elevation-1"
      >
      <template v-slot:top>
        <v-toolbar flat color="blue lighten-3">
          <v-toolbar-title>Neobradjeni upiti za preglede</v-toolbar-title>
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
          @click="accept(item)"
        >
          mdi-check-decagram
        </v-icon>
        <v-icon
          small
          class="mr-2"
          v-if="item.pregled!=null"
          @click="reject(item)"
        >
          mdi-close-box
        </v-icon>
        <v-icon
          small
          class="mr-2"
          v-if="item.pregled==null"
          @click="edit(item)"
        >
          mdi-pencil
        </v-icon>
      </template>
    </v-data-table>
    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>
          <span class="headline">Odgovor na upit za pregled pacijenta</span>
        </v-card-title>
        <hr>
        <v-card-text>
          TODO: Dodeljivanje sale pregledu i eventualna promena pregleda ukoliko ne postoji nijedna sala
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: "TabelaNeobradjenihUpita",
  props: ["all"],
  data: function(){
    return {
      search: '',
      dialog: false,
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
        }
      ],
      editableItem: {
        pacijent: null,
        pocetakPregleda: null,
        krajPregleda: null,
        lekar: null,
        tipPregleda: null,
        sala: null
      }
    };
  },
  computed: {
    ...mapGetters({
      upiti: "upitiPreglediAdmin/getUpiti"
    }),
  },
  methods: {
    accept(item){
      this.$emit("accept", item);
    },
    reject(item){
      this.$emit("reject", item);
    },
    edit(item){
      //SAMO za UPIT ZA CUSTOM PREGLED
      let updatedItem = this.upiti.filter(x => x.id == item.id)[0];
      this.editableItem = {
        id: item.id,
        pacijent: {text: `${updatedItem.pacijent.ime} ${updatedItem.pacijent.prezime}`, value: updatedItem.pacijent},
        pocetak: new Date(updatedItem.pocetakPregleda).toLocaleString(),
        kraj: new Date(updatedItem.krajPregleda).toLocaleTimeString(),
        lekar: {text: `${updatedItem.lekar.ime} ${updatedItem.lekar.prezime}`, value: updatedItem.lekar},
        tipPregleda: {text: updatedItem.tipPregleda.naziv, value: updatedItem.tipPregleda},
        sala: null
      }
      this.dialog = true;
    }
  }
}
</script>

<style>

</style>