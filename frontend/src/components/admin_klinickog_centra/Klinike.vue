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
          <v-toolbar-title>Klinike</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
<!-- search bar  -->
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


<!-- forma za unos ili izmenu unutar dialoga-->
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
              <v-btn color="primary" dark class="mb-2" v-on="on">Dodaj</v-btn>
            </template>
            <v-form v-model="isFormValid">
              <v-card>
                <!-- naslov forme -->
                <v-card-title>
                  <span class="headline">{{formTitle}}</span>
                </v-card-title>
                <hr>
                <!-- elementi -->
                <v-card-text>
                  <v-container>                      
                        <v-text-field v-model="newItem.naziv" label="naziv klinike" :rules="klinikaRules"></v-text-field>
                  </v-container>
                </v-card-text>
                
                <!-- akcije -->
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


<!-- akcije edit and delete -->
      <template v-slot:item.actions="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)">
          mdi-pencil
        </v-icon>
        <v-icon small @click="deleteItem(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';

export default {
  name: "Klinike",
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
          text: 'Actions',
          value: 'actions',
          sortable: false,
          align: 'end'
        }
      ],

      tempItems: [
        {
          naziv: 1,
        },
        {
          naziv: 2
        },
        {
          naziv: 3
        },
        {
          naziv: 4
        },
      ],
      
      newItem: {
        naziv: '',
      },
      update: false
    };
  },

  computed: {

    ...mapGetters(
      {
        getAll: 'klinike/getKlinike',
        
        // get: 'sale/getSala'
      }
    ),
    
    
    formTitle: function(){
       return this.update ? 'Izmena klinike': 'Dodavanje nove klinike';
    },
    
    klinikaRules: function(){
      const rules = [];
      // pravilo za prazan unos naziva klinike
      rules.push(v => !!v || "Naziv ne sme ostati prazan");
      //pravilo za jedinstvenost naziva klinike pri akciji update
      if(this.update){
        rules.push(v => this.tempItems.findIndex(x => x.naziv == v && x.id != this.newItem.id) == -1 || "Naziv mora biti jedinstven");
      }
      //pravilo za jedinstvenost naziva klinike pri akciji add
      else{
        rules.push(v => this.tempItems.findIndex(x => x.naziv == v) == -1 || "Naziv mora biti jedinstven");
      }
      
      return rules;
    },
    f: function(){
      return console.log(this.$store.getters['klinike/getKlinike'])
    },
  },
  created(){
    //load klinike
    this.fetchData();
    
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'klinike/loadKlinike',
        // addSala: 'sale/addSala',
        // updateSala: 'sale/updateSala',
        // removeSala: 'sale/removeSala'
      }
    ),
    resetNewItem(){
      this.newItem = {
        naziv: '',
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