<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="_sale"
      :search="search"
      show-expand
      single-expand
      :expanded.sync="expanded"
      item-key="id"
      class="elevation-1 mt-2"
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>{{title}}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
          <v-spacer></v-spacer>
          <span class="datetime-picker">
            <v-datetime-picker
              v-model="_dateTimeStart"
              label="Početak pregleda"
              dateFormat="dd.MM.yyyy"
              :textFieldProps="textFieldProps"
            >
              <template slot="actions" slot-scope="{ parent }">
                <v-btn color="error darken-1" @click="clearDates(parent)">Clear</v-btn>
                <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
              </template>
            </v-datetime-picker>
          </span>
          <span class="datetime-picker">
            <v-datetime-picker
              v-model="_dateTimeEnd"
              label="Kraj pregleda"
              dateFormat="dd.MM.yyyy"
              :textFieldProps="textFieldProps"
              :disabled="dateTimeStart==null"
              :key="k"
            >
              <template slot="actions" slot-scope="{ parent }">
                <v-btn color="error darken-1" @click="parent.clearHandler">Clear</v-btn>
                <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
              </template>
            </v-datetime-picker>
          </span>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
              <v-btn color="primary" dark class="mb-2" v-on="on">Dodaj</v-btn>
            </template>
            <v-form v-model="isFormValid">
              <v-card>
                <v-card-title>
                  <span class="headline">{{formTitle}}</span>
                </v-card-title>
                <hr>
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="12" md="12">
                        <v-text-field v-model="newItem.oznaka" label="Oznaka" :rules="salaRules"></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="save" :disabled="!isFormValid">Save</v-btn>
                </v-card-actions>
              </v-card>
            </v-form>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <p class="headline mt-7">Radni kalendar sale: {{item.oznaka}}</p>
          <PreglediSale :idSale="item.id" class="mb-3"></PreglediSale>
        </td>
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
    <v-snackbar
      v-model="snackbar"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import PreglediSale from './PreglediSale'
export default {
  name: "Sala",
  props: [
    "sale",
    "pregledi"
  ],
  components: {
    PreglediSale
  },
  data: function(){
    return {
      k: 0,
      textFieldProps: {
        appendIcon: 'event'
      },
      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null,
      dialog: false,
      search: '',
      isFormValid: false,
      expanded: [],
      headers: [
        {
          text: 'Oznaka',
          value: 'oznaka',
          sortable: true,

        },
        { 
          text: 'Actions',
          value: 'actions',
          sortable: false,
        },
        {
          text: 'Kalendar',
          value: 'data-table-expand'
        }
      ],
      newItem: {
        oznaka: '',
        version: '',
      },
      update: false
    };
  },
  computed: {
    ...mapGetters({
      dateTimeStart: 'salaFilter/dateTimeStart',
      dateTimeEnd: 'salaFilter/dateTimeEnd'
    }),
    _dateTimeStart: {
      get(){
        return this.dateTimeStart;
      },
      set(newValue){
        if(newValue == null){
          this.$store.commit("salaFilter/setDateTimeEnd", null, {root: true});
        }
        this.$store.commit("salaFilter/setDateTimeStart", newValue, {root: true});
      }
    },
    _dateTimeEnd: {
      get(){
        return this.dateTimeEnd;
      },
      set(newValue){
        this.$store.commit("salaFilter/setDateTimeEnd", newValue, {root: true});
      }
    },
    _sale(){
      if(this.dateTimeStart != null && this.dateTimeEnd != null){
        return this.sale.filter(x => {
          let preglediSale = this.pregledi.filter(y => y.sala.id == x.id);
          return preglediSale.filter(y => {
            let start = y.pocetakPregleda;
            let end = new Date(y.krajPregleda);
            let start2 = this.dateTimeStart;
            let end2 = this.dateTimeEnd;
            return this.$utility.timeIntervalsIntersect(start, end, start2, end2);
          }).length == 0;
        });
      }else{
        return this.sale;
      }
    },
    formTitle: function(){
       return this.update ? 'Izmena sale': 'Dodavanje nove sale';
    },
    title: function(){
      return this.dateTimeEnd == null ? 'Sale' : 'Slobodne sale';
    },
    salaRules: function(){
      const rules = [];
      rules.push(v => !!v || "Oznaka ne sme ostati prazna");
      if(this.update){
        rules.push(v => this.sale.findIndex(x => x.oznaka == v && x.id != this.newItem.id) == -1 || "Oznaka mora biti jedinstvena");
      }else{
        rules.push(v => this.sale.findIndex(x => x.oznaka == v) == -1 || "Oznaka mora biti jedinstvena");
      }
      
      return rules;
    }
  },
  methods: {
    ...mapActions(
      {
        addSala: 'sale/addSala',
        updateSala: 'sale/updateSala',
        removeSala: 'sale/removeSala',
      }
    ),
    resetNewItem(){
      this.newItem = {
        oznaka: '',
        version: '',
      };
    },
    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },
    save(){
      if(this.update){
        this.updateSala(this.newItem).then(null, (error) => {
          this.snackbarText = error;
          this.snackbar = true;
        });
      }else{
        this.addSala(this.newItem);
      }
      this.close();
    },
    editItem(item){
      this.update = true;
      this.newItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item){
      this.removeSala({idSale: item.id, version: item.version}).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    },
    clearDates(parent){
      this._dateTimeEnd = null;
      parent.clearHandler();
      this.k++;
    }
  }
}
</script>

<style>
.datetime-picker{
  margin-top: 1.2rem;
  margin-right: 1rem;
}
.datetime-picker{
  margin-top: 1.2rem;
  margin-right: 1rem;
}
</style>