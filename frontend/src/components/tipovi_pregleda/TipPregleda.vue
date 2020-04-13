<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="getAll"
      :search="search"
      sort-by="calories"
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
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="newItem.naziv" label="Naziv"></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field v-model="newItem.opis" label="Opis"></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                <v-btn color="blue darken-1" text @click="save">Save</v-btn>
              </v-card-actions>
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
        opis: ''
      },
      update: false
    };
  },
  computed: {
    ...mapGetters(
      {
        getAll: 'tipoviPregleda/getTipoviPregleda',
        get: 'tipoviPregleda/getTipPregleda'
      }
    ),
    formTitle: function(){
       return this.update ? 'Izmena tipa pregleda': 'Dodavanje novog tipa prelgeda';
    }
  },
  created(){
    this.fetchData();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'tipoviPregleda/loadTipoviPregleda',
        addTipPregleda: 'tipoviPregleda/addTipPregleda',
        updateTipPregleda: 'tipoviPregleda/updateTipPregleda',
        removeTipPregleda: 'tipoviPregleda/removeTipPregleda'
      }
    ),
    resetNewItem(){
      this.newItem = {
        naziv: '',
        opis: ''
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
      this.newItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item){
      this.removeTipPregleda(item.id);
    }
  }
}
</script>

<style>

</style>