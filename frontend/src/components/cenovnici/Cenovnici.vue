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
                        <v-text-field type="number" v-model="newItem.iznosUDinarima" label="Iznos u dinarima" :rules="notEmptyRule('Iznos u dinarima')"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="12" md="12" v-if="_tipoviPregleda.length != 0">
                        <v-select
                          v-model="newItem.tipoviPregleda"
                          :items="_tipoviPregleda"
                          label="Tipovi pregleda"
                          multiple
                          chips
                          deletable-chips
                          :hint="update ? 'Tipovi pregleda mogu se odabrati samo pri dodavanju nove stavke cenovnika': 'Izaberite neki od SLOBODNIH tipova pregleda pod ovom stavkom.'"
                          persistent-hint
                          :disabled="update"
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
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          Tipovi pregleda pod ovom stavkom cenovnika:
          <div
            v-for="tip in item.tipoviPregleda"
            :key="tip.id"
            
          >{{tip.id}}</div>
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
          :disabled="item.tipoviPregleda.length != 0"
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
  name: "Cenovnici",
  data: function(){
    return {
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
    _tipoviPregleda(){
      let filtered = [
        ...this.newItem.tipoviPregleda,
        ...this.tipoviPregleda.filter(x => x.cenovnik == null)
      ];
      return filtered.map(x => {
        return{
          text: x.naziv,
          value: x
        };
      });
    },

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
        this.updateCenovnik(this.newItem);
      }else{
        this.addCenovnik(this.newItem);
      }
      for(let tipPregleda of this.newItem.tipoviPregleda)
        this.$store.commit('tipoviPregleda/setCenovnikOfTipPregleda', {tipPregleda: tipPregleda, idCenovnika: this.newItem.id })
      this.close();
    },

    editItem(item){
      this.force_recompute += 1;
      this.update = true;
      this.newItem = Object.assign({}, item);
      this.newItem.tipoviPregleda = this.newItem.tipoviPregleda.map(x => {
          return this.tipoviPregleda.filter(i => i.id == x.id)[0];
      });
      this.dialog = true;
    },

    deleteItem(item){
      this.removeCenovnik(item.id);
    },

    validateRules(){
      for(let rule of this.notEmptyRule){
        if(rule != true){ //mora ovako
          return true;
        }
      }
    }
  }
}
</script>

<style>

</style>