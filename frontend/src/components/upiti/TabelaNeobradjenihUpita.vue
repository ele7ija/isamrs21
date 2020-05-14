<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="all"
      :search="search"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="id"
      show-expand
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
          v-if="item.pregled==null"
          @click="edit(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
          small
          class="mr-2"
          @click="reject(item)"
        >
          mdi-close-box
        </v-icon>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <p class="text-center mt-2 mb-n4 pb-n4" v-if="item.pregled">Jos neki podaci o ovom pregledu</p>
          <br>
          <span v-if="item.pregled">
            Cena: {{item.pregled.cena}} <br>
            Popust: {{item.pregled.popust}} <br>
            KonacnaCena: {{item.pregled.konacnaCena}} <br>
          </span>
          <span v-if="!item.pregled">
            Ovo je upit za custom pregled
          </span>
        </td>
      </template>
    </v-data-table>
    <v-dialog v-model="dialog" @input="close()">
      <v-card>
        <v-card-title>
          <span class="headline">Odgovor na upit za pregled pacijenta</span>
        </v-card-title>
        <hr>
        <v-stepper v-model="stepIndex" vertical>
          <span
            v-for="step in stepperData"
            :key="step.index"
          >
            <v-stepper-step :complete="stepIndex > step.index" :step="step.index" :editable="step.index <= stepIndex">
              {{step.title}}
              <small>{{step.subtitle}}</small>
            </v-stepper-step>

            <v-stepper-content :step="step.index">
              <component
                class="mb-8"
                height="200px"
                v-bind:is="step.componentName"
                :slobodneSale="slobodneSale"
                :upit="editableItem"
                :key="step.unique"
                @incStep="incStep"
                @decStep="decStep"
                @setDates="setDates"
                @setLekar="setLekar"
                @resetDates="resetDates"
              ></component>
            </v-stepper-content>
          </span>
        </v-stepper>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import TabelaSala from './TabelaSala';
import PregledInfo from './PregledInfo';
export default {
  name: "TabelaNeobradjenihUpita",
  props: ["all"],
  components: {
    TabelaSala,
    PregledInfo
  },
  data: function(){
    return {
      search: '',
      dialog: false,
      expanded: [],
      singleExpand: true,
      oldKraj: null,
      oldPocetak: null,
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
        },
        {
          text: 'Detalji',
          value: 'data-table-expand'
        }
      ],
      editableItem: null,
      stepIndex: 1,
      stepperData: [
        {
          title: "Izaberite salu pregleda",
          subtitle: "Odaberite neku od slobodnih sala da biste nastavili.",
          index: 1,
          componentName: "TabelaSala",
          unique: 1
        },
        {
          title: "Pregled informacija",
          subtitle: "Na osnovu datih informacija će se kreirati rezervacija",
          index: 2,
          componentName: "PregledInfo",
          unique: 2
        }
      ]
    };
  },
  computed: {
    ...mapGetters({
      upiti: "upitiPreglediAdmin/getUpiti",
      sale: "sale/getSale",
      pregledi: "preglediAdmin/getPreglediKlinike",
      osoblje: "osoblje/getMedicinskoOsoblje"
    }),
    slobodneSale(){
      if(this.editableItem != null){
        //prvo dobavi preglede koji se preklapaju sa datumom pocetka i kraja upita koji je kreiran
        let filteredPregledi = this.pregledi.filter(x => {
            let start = new Date(x.pocetakPregleda);
            let end = new Date(x.krajPregleda);
            return (start.getTime() <= this.editableItem.pocetak.getTime() && end.getTime() >= this.editableItem.pocetak.getTime()) ||
              (this.editableItem.pocetak.getTime() <= start.getTime() && this.editableItem.kraj.getTime() >= start.getTime())
          });

        //zatim izvuci id-jeve sala ovih pregleda
        let zauzeteSaleIds = filteredPregledi.map(x => x.sala.id);

        //zatim izbaci sale sa ovim id-jevima iz skupa svih slobodnih sala
        return this.sale.filter(x => zauzeteSaleIds.findIndex(y => y == x.id) == -1);
      }else{
        return [];
      }
    }
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
        pocetak: new Date(updatedItem.pocetakPregleda),
        kraj: new Date(updatedItem.krajPregleda),
        lekar: {text: `${updatedItem.lekar.ime} ${updatedItem.lekar.prezime}`, value: updatedItem.lekar},
        tipPregleda: {text: updatedItem.tipPregleda.naziv, value: updatedItem.tipPregleda},
        vrsta: updatedItem.tipPregleda.vrsta,
        cena: updatedItem.tipPregleda.cenovnik.iznosUDinarima,
        sala: null
      };
      this.editableItem._pocetak = this.formatDate(this.editableItem.pocetak);
      this.editableItem._kraj = this.formatDate(this.editableItem.kraj);
      this.rerender();
      this.dialog = true;
    },
    rerender(){
      for(let step of this.stepperData){
        step.unique += 1;
      }
    },
    incStep(object){
      if(this.stepIndex == this.stepperData.length){
        alert("Emitujem akciju na bek");
      }else{
        this.editableItem.sala = {
          text: object.oznaka,
          value: object
        };
        this.stepIndex++;
      }
    },
    decStep(){
      this.stepIndex--;
    },
    close(){
      this.rerender();
      this.stepIndex = 1;
    },
    formatDate(date){
      let day = date.getDate();
      let month = date.getMonth() + 1;
      let hour = date.getHours();
      let minute = date.getMinutes();
      if((String(day)).length==1)
        day='0'+day;
      if((String(month)).length==1)
        month='0'+month;
      if((String(hour)).length==1)
        hour='0'+hour;
      if((String(minute)).length==1)
        minute='0'+minute;
      return `${day}.${month}.${date.getFullYear()} ${hour}:${minute}`;
    },
    setDates({pocetak, refresh}){
      if(!this.oldPocetak){
        this.oldPocetak = this.editableItem.pocetak;
      }
      if(!this.oldKraj){
        this.oldKraj = this.editableItem.kraj;
      }

      this.editableItem.pocetak = pocetak;
      this.editableItem._pocetak = this.formatDate(pocetak);
      let kraj = new Date(pocetak.getTime() + this.editableItem.tipPregleda.value.trajanjeMinuti*60000);
      this.editableItem.kraj = kraj;
      this.editableItem._kraj = this.formatDate(kraj);

      //remount component two in order to validate form
      if(refresh)
        this.stepperData[1].unique += 1;
    },
    setLekar(lekar){
      this.editableItem.lekar.text = `${lekar.ime} ${lekar.prezime}`;
      this.editableItem.lekar.value = lekar;
      this.stepperData[1].unique += 1; //remount component two in order to validate form
    },
    resetDates(){
      this.editableItem.pocetak = this.oldPocetak;
      this.editableItem._pocetak = this.formatDate(this.oldPocetak);
      this.editableItem.kraj = this.oldKraj;
      this.editableItem._kraj = this.formatDate(this.oldKraj);
      this.stepperData[1].unique += 1; //remount component two in order to validate form
    }
  }
}
</script>

<style>

</style>