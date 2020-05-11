<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="_tipoviPregleda"
      :search="search"
      class="elevation-1"
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Tipovi Pregleda</v-toolbar-title>
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
          
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
              <v-btn color="primary" dark class="mb-2" v-on="on">Dodaj</v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{formTitle}}</span>
              </v-card-title>
              <hr>
              <v-form v-model="isFormValid">
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="6">
                        <v-text-field v-model="newItem.naziv" label="Naziv" :rules="nazivRules"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="6">
                        <v-text-field v-model="newItem.opis" label="Opis" :rules="opisRules"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="6">
                        <v-text-field type="number" v-model="newItem.trajanjeMinuti" label="Trajanje u minutima" :rules="minutiRules" :min="1"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="6">
                        <v-select
                          v-model="newItem.cenovnik"
                          :items="_cenovnici"
                          :label="cenovnici.length != 0 ? 'Cenovnik' : 'Cenovnik (ne postoji definisana nijedna stavka)'"
                          chips
                          deletable-chips
                          hint="Odaberite predefinisanu stavku cenovnika"
                          persistent-hint
                          :rules="cenovnikRules"
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="save" :disabled="!isFormValid">Save</v-btn>
                </v-card-actions>
              </v-form>
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
export default {
  name: "TipPregleda",
  data: function(){
    return {
      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null,
      dialog: false,
      search: '',
      isFormValid: false,
      headers: [
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: true,

        },
        {
          text: 'Opis',
          value: 'opis',
          sortable: false
        },
        {
          text: 'Trajanje u minutima',
          value: 'trajanjeMinuti',
          sortable: true
        },
        {
          text: 'Stavka cenovnika',
          value: 'stavkaCenovnika',
          sortable: true
        },
        {
          text: 'Dinarski iznos',
          value: 'iznosUDinarima',
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
        naziv: '',
        opis: '',
        version: '',
        cenovnik: null,
        trajanjeMinuti: 60,
        lekari: []
      },
      update: false
    };
  },

  computed: {
    ...mapGetters(
      {
        getAll: 'tipoviPregleda/getTipoviPregleda',
        get: 'tipoviPregleda/getTipPregleda',
        cenovnici: 'cenovnici/getCenovnici'
      }
    ),

    formTitle: function(){
       return this.update ? 'Izmena tipa pregleda': 'Dodavanje novog tipa prelgeda';
    },

    nazivRules: function(){
      const rules = [];
      
      const rule1 = v => !!v || 'Naziv ne sme ostati prazan';
      rules.push(rule1);

      let rule2 = null;
      if(this.update){
        rule2 = v => this.getAll.findIndex(x => x.naziv == v && x.id != this.newItem.id) == -1 || 'Naziv mora biti jedinstven';
      }else{
        rule2 = v => this.getAll.findIndex(x => x.naziv == v) == -1 || 'Naziv mora biti jedinstven';
      }
      rules.push(rule2);
      return rules;
    },

    opisRules: function(){
      const rules = [];
      const rule1 = v => !!v || 'Opis ne sme ostati prazan';
      rules.push(rule1);
      return rules;
    },

    minutiRules: function(){
      const rules = [];
      const rule1 = v => !!v || 'Trajanje u minutima ne sme ostati prazno';
      rules.push(rule1);
      return rules;
    },

    cenovnikRules: function(){
      const rules = [];
      const rule1 = v => !!v || 'Cenovnik ne sme ostati prazan';
      rules.push(rule1);
      return rules;
    },
    _cenovnici(){
      return this.cenovnici.map(x => { return {text: `${x.naziv}, ${x.iznosUDinarima} din`, value: x}; })
    },
    _tipoviPregleda(){
      let retval = [];
      for(let element of this.getAll){
        if(element.cenovnik != null){
          let indeks = this.cenovnici.findIndex(x => x.id == element.cenovnik.id);
          if(indeks != -1){
            retval.push({
              id: element.id,
              version: element.version,
              naziv: element.naziv,
              opis: element.opis,
              trajanjeMinuti: element.trajanjeMinuti,
              lekari: element.lekari,
              cenovnik: this.cenovnici[indeks], //ZA BEKEND
              stavkaCenovnika: this.cenovnici[indeks].naziv, //ZA PRIKAZ
              iznosUDinarima: this.cenovnici[indeks].iznosUDinarima //ZA PRIKAZ
            });
          }
        }else{
          retval.push(element);
        }
      }
      return retval;
    }

    
  },

  created(){
    this.fecthCenovnici();
    this.fetchData();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'tipoviPregleda/loadTipoviPregleda',
        fecthCenovnici: 'cenovnici/loadCenovnici',
        addTipPregleda: 'tipoviPregleda/addTipPregleda',
        updateTipPregleda: 'tipoviPregleda/updateTipPregleda',
        removeTipPregleda: 'tipoviPregleda/removeTipPregleda'
      }
    ),

    resetNewItem(){
      this.newItem = {
        naziv: '',
        opis: '',
        version: '',
        cenovnik: null,
        trajanjeMinuti: 60,
        lekari: []
      };
    },

    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },

    save(){
      this.newItem.cenovnik = this.newItem.cenovnik ? {id: this.newItem.cenovnik.id, version: this.newItem.cenovnik.version} : null;
      if(this.update){
        this.updateTipPregleda(this.newItem).then(null, (error) => {
          this.snackbarText = error;
          this.snackbar = true;
        });
      }else{
        this.addTipPregleda(this.newItem);
      }
      this.close();
    },

    editItem(item){
      this.update = true;
      this.newItem = JSON.parse(JSON.stringify(item));
      this.newItem.cenovnik = item.cenovnik ? this.cenovnici.filter(x => x.id == item.cenovnik.id)[0]: null;
      this.newItem.lekari = this.newItem.lekari.map(x => {
        return {
          id: x.id,
          pozicija: 'lekar'
        };
      });
      this.dialog = true;
    },

    deleteItem(item){
      this.removeTipPregleda({idTipaPregleda: item.id, version: item.version}).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    },

    validateRules(){
      for(let rule of this.nazivRules){
        if(rule != true){ //mora ovako
          return true;
        }
      }
      for(let rule of this.opisRules){
        if(rule != true){ //mora ovako
          return true;
        }
      }
      return false;
    }
  }
}
</script>

<style>

</style>