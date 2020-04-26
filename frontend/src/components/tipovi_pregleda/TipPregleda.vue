<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="getAll"
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
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="newItem.naziv" label="Naziv" :rules="nazivRules"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="newItem.opis" label="Opis" :rules="opisRules"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4" v-if="_cenovnici.length != 0">
                        <v-select
                          v-model="newItem.cenovnik"
                          :items="_cenovnici"
                          label="Cenovnik"
                          chips
                          hint="Odaberite definisanu tarifu"
                          persistent-hint
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
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: "TipPregleda",
  data: function(){
    return {
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
          text: 'Actions',
          value: 'actions',
          sortable: false,
          align: 'end'
        }
      ],
      newItem: {
        naziv: '',
        opis: '',
        cenovnik: null
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

    _cenovnici(){
      return this.cenovnici.map(x => {
        return{
          text: `${x.naziv}, cena ${x.iznosUDinarima} dinara`,
          value: x
        };
      });
    }
  },

  created(){
    this.fetchData();
    this.fecthCenovnici();
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
        cenovnik: null
      };
    },

    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },

    save(){
      if(this.update){
        this.updateTipPregleda(this.newItem);
      }else{
        this.addTipPregleda(this.newItem);
      }
      this.close();
    },

    editItem(item){
      this.update = true;
      this.newItem = Object.assign({}, item)
      if(item.cenovnik != null)
        this.newItem.cenovnik = JSON.parse(JSON.stringify(item.cenovnik))
      this.dialog = true;
    },

    deleteItem(item){
      this.removeTipPregleda(item.id);
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