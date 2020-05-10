<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="getAll"
      :search="search"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="id"
      show-expand
      class="elevation-1"
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Stavke cenovnika</v-toolbar-title>
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
                        <v-text-field v-model="newItem.naziv" label="Naziv" :rules="notEmptyRule('Naziv')"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="6">
                        <v-text-field type="number" v-model="newItem.iznosUDinarima" label="Iznos u dinarima" :rules="notEmptyRule('Iznos u dinarima')" :min="1"></v-text-field>
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
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <p class="text-center mt-2 mb-n4 pb-n4">Tipovi pregleda pod ovom stavkom cenovnika</p>
          <br>
          <span
            v-for="tip in tipoviPregleda"
            :key="tip.id">
            <v-chip
              class="ma-2"
              v-if="item.tipoviPregleda.find(x => x.id == tip.id)"
            >
              {{tip.naziv}}
            </v-chip>
          </span>
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
          class="mr-2"
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
  name: "Cenovnici",
  data: function(){
    return {
      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null,
      update: false,
      dialog: false,
      search: '',
      isFormValid: false,
      expanded: [],
      singleExpand: false,
      headers: [
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: true,

        },
        {
          text: 'Iznos u dinarima',
          value: 'iznosUDinarima',
          sortable: false
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
      newItem: {
        naziv: '',
        iznosUDinarima: '',
        version: '',
        tipoviPregleda: [],
      }
    };
  },
  computed: {
    ...mapGetters(
      {
        getAll: 'cenovnici/getCenovnici',
        get: 'cenovnici/getCenovnik',
        tipoviPregleda: 'tipoviPregleda/getTipoviPregleda'
      },
    ),

    formTitle: function(){
       return this.update ? 'Izmena stavke cenovnika': 'Dodavanje nove stavke cenovnika';
    },

    notEmptyRule: () => (property) => {
      const rules = [];
      const rule1 = v => !!v || `${property} mora imati vrednost.`;
      rules.push(rule1);
      return rules;
    }
  },
  created(){
    this.fetchData();
    this.fetchTipoviTregleda();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'cenovnici/loadCenovnici',
        fetchTipoviTregleda: 'tipoviPregleda/loadTipoviPregleda',
        addCenovnik: 'cenovnici/addCenovnik',
        updateCenovnik: 'cenovnici/updateCenovnik',
        removeCenovnik: 'cenovnici/removeCenovnik',
      }
    ),

    resetNewItem(){
      this.newItem = {
        naziv: '',
        adresa: '',
        version: '',
        tipoviPregleda: [],
      };
    },

    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },

    save(){
      if(this.update){
        this.updateCenovnik(this.newItem).then(null, (error) => {
          this.snackbarText = error;
          this.snackbar = true;
        });
      }else{
        this.addCenovnik(this.newItem);
      }
      this.close();
    },

    editItem(item){
      this.update = true;
      this.newItem = Object.assign({}, item);
      this.newItem.tipoviPregleda = this.newItem.tipoviPregleda.map(x => {
          return{
            id: x.id
          };
      });
      
      this.dialog = true;
    },

    deleteItem(item){
      console.log(item.version);
      this.removeCenovnik({idCenovnika: item.id, version: item.version}).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    }
  }
}
</script>

<style>

</style>