<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="getAll"
      :search="search"
      class="elevation-1"
      :single-expand="true"
      item-key="id"
      show-expand
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
              <v-btn color="primary"  class="mb-2" v-on="on">Dodaj</v-btn>
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
                    <v-text-field 
                    label="naziv" 
                    v-model="newItem.naziv" 
                    :rules="nazivRule"
                    required
                    ></v-text-field>

                    <vuetify-google-autocomplete
                      id='map2'
                      ref='lokacija'
                      append-icon='mdi-map-marker'
                      v-bind:disable='true'
                      placeholder="Lokacija"
                      v-on:placechanged="getAddressData"
                      :options="{fields: ['geometry', 'address_components', 
                        'formatted_address']}"
                    >
                    </vuetify-google-autocomplete>

                    <v-text-field
                    label="opis"
                    v-model="newItem.opis"
                    >
                    <v-switch></v-switch>
                    
                    </v-text-field>

                  </v-container>
                </v-card-text>
                
                
                <!-- akcije -->
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close">Nazad</v-btn>
                  <v-btn color="blue darken-1" text @click="save" :disabled="!isFormValid">Sačuvaj</v-btn>
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
          @click="openLocation(item)"
        >
          mdi-map-marker
        </v-icon>
      </template>

<!-- opis klinike ide na expand -->
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <v-textarea class="mt-4" label="Opis" readonly outlined v-model="item.opis"></v-textarea>
        </td>
      </template>
    </v-data-table>

    <v-dialog v-model="dialogLocation">
      <v-card height=640>
        <Map :klinika="locationKlinika" :key="mapKey"/>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import Map from '../maps/Map';
export default {
  name: "Klinike",
  components: {Map},
  data: function(){
    return {
      dialog: false,
      dialogLocation: false,
      locationKlinika: null,
      mapKey: 0,
      search: '',
      isFormValid: true,
      headers: [
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: true,
        },
        {
          text: 'Adresa',
          value: 'lokacija.adresa',
          sortable: true,
        },
        {
          text: 'Grad',
          value: 'lokacija.grad',
          sortable: true,
        },
        {
          text: 'Drzava',
          value: 'lokacija.drzava',
          sortable: true,
        },
        {
          text: 'opis',
          value: 'data-table-expand',
        },
        {
          text: 'mapa',
          value: 'actions',
          sortable: 'false'
        },
      ],

      newItem: {
        naziv: '',
        adresa: '',
        grad: '',
        drzava: '',
        opis: '',
        geografskaDuzina: '',
        geografskaSirina: ''
      },
      update: false,

      //rules
      nazivRule: [
        v => !!v || 'Naziv je obavezan',
        v => (v && v.length <= 50) || 'Naziv ima najviše 50 karaktera'
      ]
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

  },
  created(){
    //load klinike
    this.fetchData();
    
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'klinike/loadKlinike',
        addKlinika: 'klinike/addKlinika',
        updateKlinika: 'klinike/updateKlinikaFromAdminCentra',
        // removeSala: 'sale/removeSala'
      }
    ),

    getAddressData(data){
      this.newItem.adresa = data.route;
      if(data.street_number)
        this.newItem.adresa += " " + data.street_number;
      this.newItem.grad = data.locality;
      this.newItem.drzava = data.country;
      this.newItem.geografskaDuzina = data.longitude;
      this.newItem.geografskaSirina = data.latitude;
    },

    resetNewItem(){
      this.newItem = {
        naziv: '',
        adresa: '',
        grad: '',
        drzava: '',
        opis: '',
        geografskaDuzina: '',
        geografskaSirina: ''
      };
      this.$refs.lokacija.clear();
    },
    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },
    save(){
      if(this.update){
        this.updateKlinika(this.newItem);
      }else{
        //adding klinika
        this.addKlinika(this.newItem);
      }
      this.close();
    },
    editItem(item){
      this.update = true;
      
      //this.newItem = Object.assign({}, item);
      this.newItem = {
        id: item.id,
        naziv: item.naziv,
        adresa: item.adresa,
        grad: item.grad,
        drzava: item.drzava,
        opis: item.opis,
      }
      this.dialog = true;
    },
    openLocation(klinika){
      this.locationKlinika = klinika;
      this.mapKey += 1;
      this.dialogLocation = true;
    }
  }
}
</script>

<style>

</style>