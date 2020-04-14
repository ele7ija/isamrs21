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
          <v-toolbar-title>Sale</v-toolbar-title>
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
            <v-form v-model="isFormValid">
              <v-card>
                <v-card-title>
                  <span class="headline">{{formTitle}}</span>
                </v-card-title>
                <hr>
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
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
  name: "Sala",
  data: function(){
    return {
      dialog: false,
      search: '',
      isFormValid: false,
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
          align: 'end'
        }
      ],
      newItem: {
        oznaka: '',
      },
      update: false
    };
  },
  computed: {
    ...mapGetters(
      {
        getAll: 'sale/getSale',
        get: 'sale/getSala'
      }
    ),
    
    formTitle: function(){
       return this.update ? 'Izmena sale': 'Dodavanje nove sale';
    },
    
    salaRules: function(){
      const rules = [];
      rules.push(v => !!v || "Oznaka ne sme ostati prazna");
      if(this.update){
        rules.push(v => this.getAll.findIndex(x => x.oznaka == v && x.id != this.newItem.id) == -1 || "Oznaka mora biti jedinstvena");
      }else{
        rules.push(v => this.getAll.findIndex(x => x.oznaka == v) == -1 || "Oznaka mora biti jedinstvena");
      }
      
      return rules;
    }
  },
  created(){
    this.fetchData();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'sale/loadSale',
        addSala: 'sale/addSala',
        updateSala: 'sale/updateSala',
        removeSala: 'sale/removeSala'
      }
    ),
    resetNewItem(){
      this.newItem = {
        oznaka: '',
      };
    },
    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },
    save(){
      if(this.update){
        this.updateSala(this.newItem);
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
      this.removeSala(item.id);
    }
  }
}
</script>

<style>

</style>