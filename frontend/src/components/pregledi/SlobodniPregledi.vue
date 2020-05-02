<template>
  <v-data-table
    :headers="headers"
    :items="data"
    :search="search"
    class="elevation-1"
    >
    <template v-slot:top>
      <v-toolbar flat color="blue lighten-3">
        <v-toolbar-title>Slobodni pregledi unutar klinike</v-toolbar-title>
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
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="1000px">
          <template v-slot:activator="{ on }">
            <v-btn color="primary" dark class="mb-2" v-on="on">Dodaj</v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">{{formTitle}}</span>
            </v-card-title>
            <hr>
            <v-stepper v-model="stepIndex" vertical>
              <span
                v-for="step in stepperData"
                :key="step.index"
              >
                <v-stepper-step :complete="stepIndex > 1" :step="step.index" :editable="step.index < stepIndex">
                  {{step.title}}
                  <small>{{step.subtitle}}</small>
                </v-stepper-step>

                <v-stepper-content :step="step.index">
                  <component
                    class="mb-8"
                    height="200px"
                    v-bind:is="step.componentName"
                    @changeStatus="changeStatus"
                    @previousStep="decrement"
                    @add="add"
                    :index="step.index - 1"
                    :currentIndex="stepIndex - 1"
                  ></component>
                  <v-btn v-if="stepIndex < stepperData.length" color="primary" @click="stepIndex += 1" :disabled="!stepperData[step.index-1].done">Next step</v-btn>
                  <v-btn v-if="stepIndex > 1 && stepIndex < stepperData.length" text @click="stepIndex -= 1">Previous step</v-btn>
                  <v-btn v-if="stepIndex == 1" text @click="reset()">Cancel</v-btn>
                </v-stepper-content>
              </span>
            </v-stepper>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon
        small
        class="mr-2"
        @click="editItem(item)"
      >
        mdi-pencil
      </v-icon>
      <v-icon
        small
        @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon>
    </template>
  </v-data-table>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import DatePicker from './DatePicker';
import TabelaTipovaPregleda from './TabelaTipovaPregleda';
import TabelaLekara from './TabelaLekara';
import TabelaSala from './TabelaSala';
import PopustPicker from './PopustPicker';
export default {
  name: "SlobodniPregledi",
  props: ["data"],
  components:{
    DatePicker,
    TabelaTipovaPregleda,
    TabelaLekara,
    TabelaSala,
    PopustPicker
  },
  data: function(){
    return {
      dialog: false,
      update: false,
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
          text: 'Actions',
          value: 'actions',
          sortable: false,
          align: 'end'
        }
      ],
      newItem: {
        pocetakPregleda: null,
        krajPregleda: null,
        lekar: null,
        tipPregleda: null,
        sala: null,
        cena: null,
        popust: null,
        konacnaCena: null
      },
      stepIndex: 1,
      stepperData: [
        {
          title: "Izaberite vreme pregleda",
          subtitle: "Izbor vremena pregleda ce uticati na kasniji izbor lekara i sala pregleda",
          index: 1,
          componentName: "DatePicker",
          done: false
        },
        {
          title: "Izaberite tip pregleda",
          subtitle: "Mozete izabrati samo tip pregleda unutar vase klinike. Izbor tipa pregleda ce uticati na kasniji izbor lekara",
          index: 2,
          componentName: "TabelaTipovaPregleda",
          done: false
        },
        {
          title: "Izaberite lekara specijalistu",
          subtitle: "Mozete izabrati samo lekara unutar vase klinike. Lekar mora biti specijalizovan za prethodno odabrani tip pregleda i mora biti slobodan za odabranu satnicu",
          index: 3,
          componentName: "TabelaLekara",
          done: false
        },
        {
          title: "Izaberite salu",
          subtitle: "Mozete izabrati samo salu unutar vase klinike. Sala mora biti slobodna za odabranu satnicu",
          index: 4,
          componentName: "TabelaSala",
          done: false
        },
        {
          title: "Odaberite popust",
          subtitle: "Trenutna cena je preuzeta iz stavke cenovnika odabranog tipa pregleda. Imate mogucnost da definisete popust",
          index: 5,
          componentName: "PopustPicker",
          done: true
        }
      ]
    };
  },
  computed: {
    ...mapGetters({
      pocetak: "pregledDialog/getPocetak",
      kraj: "pregledDialog/getKraj",
      lekar: "pregledDialog/getLekar",
      sala: "pregledDialog/getSala",
      tipPregleda: "pregledDialog/getTipPregleda",
      cena: "pregledDialog/getCena",
      popust: "pregledDialog/getPopust",
      konacnaCena: "pregledDialog/getKonacnaCena",
      klinika: "klinike/getKlinikaAdmina"
    }),
    formTitle: function(){
       return this.update ? 'Izmena pregleda': 'Dodavanje novog pregleda';
    }
  },
  methods: {
    ...mapActions({
      addPregled: 'preglediAdmin/addPregled',
      updatePregled: 'preglediAdmin/updatePregled',
      removePregled: 'preglediAdmin/removePregled'
    }),
    editItem(pregled){
      return pregled;
    },
    deleteItem(pregled){
      return pregled;
    },
    add(){
      let obj = {
        pocetakPregleda: this.pocetak,
        krajPregleda: this.kraj,
        cena: this.cena,
        popust: this.popust,
        konacnaCena: this.konacnaCena,
        sala: { id: this.sala.id },
        lekar: { id: this.lekar.id, pozicija: 'lekar' },
        tipPregleda: { id: this.tipPregleda.id },
        klinika: { id: this.klinika.id },
        poseta: null
      };
      this.addPregled(obj);
      this.reset();
    },
    reset(){
      this.dialog = false;
      this.stepIndex = 1;
    },
    changeStatus({index, done}){
      this.stepperData[index].done = done;
    },
    decrement(){
      this.stepIndex -= 1;
    }
  },
}
</script>

<style>

</style>