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
          <v-toolbar-title>Administratori klinika</v-toolbar-title>
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
            <v-form v-model="isFormValid" ref="myForm">
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
                    label="ime" 
                    v-model="newItem.ime" 
                    :rules="poljeRule"
                    required
                    ></v-text-field>
                    
                    <v-text-field
                    label="prezime"
                    v-model="newItem.prezime"
                    :rules="poljeRule"
                    required
                    ></v-text-field>

                    <v-text-field
                    label="email"
                    v-model="newItem.email"
                    :rules="poljeRule"
                    required
                    ></v-text-field>

                    <v-text-field
                    label="šifra"
                    v-model="newItem.sifra"
                    :rules="poljeRule"
                    required
                    :append-icon="prikaziLozinku ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="prikaziLozinku ? 'text': 'password'"
                    @click:append="prikaziLozinku = !prikaziLozinku"
                    ></v-text-field>


                    <v-select
                    label="klinika"
                    v-model="newItem.klinikaId"
                    :items="getKlinike"
                    item-text="naziv"
                    item-value="id"
                    
                    :rules="klinikaRule"
                    chips
                    required
                  ></v-select>

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

<!-- opis klinike ide na expand -->
      <template v-slot:expanded-item="{ headers, item }">

        <td :colspan="headers.length" style="padding:15px " class=" grey--text ">
        
        <div>
          <v-data-table
          :headers="klinikaHeaders"
          :items="getKlinika(item.klinikaAdmina.id)"
          class="elevation-1 color: grey lighten-3 "
          hide-default-footer
          ></v-data-table>
        </div>
        </td>
      </template>

    </v-data-table>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';

export default {
  name: "AdminiKlinike",
  data: function(){
    return {
      dialog: false,
      search: '',
      isFormValid: true,
      prikaziLozinku: false,
      headers: [
        {
          text: 'Ime',
          value: 'ime',
          sortable: true,
        },
        {
          text: 'Prezime',
          value: 'prezime',
          sortable: true,
        },
        {
          text: 'Email',
          value: 'email',
          sortable: true,
        },
        {
          text: 'Klinika',
          value: 'data-table-expand',
          sortable: false,
        },
      ],
      newItem: {
        ime: '',
        prezime: '',
        email: '',
        sifra: '',
        klinikaId: undefined, 
      },
      
      //rules
      poljeRule: [
        v => !!v || 'Polje je obavezno',
        v => (v && v.length <= 50) || 'Polje ima najviše 50 karaktera'
      ],
      klinikaRule: [
        v => !!v || 'Klinika administratora je obavezno polje'
      ],

      //klinike za administratore
      klinikaHeaders:[
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: false,
        },
        {
          text: 'Adresa',
          value: 'lokacija.adresa',
          sortable: false,
        },
        {
          text: 'Grad',
          value: 'lokacija.grad',
          sortable: false,
        },
        {
          text: 'Drzava',
          value: 'lokacija.drzava',
          sortable: false,
        },
      ],
    };
  },

  computed: {

    ...mapGetters(
      {
        getAll: 'adminiKlinike/getAdminiKlinike',
        getKlinike: 'klinike/getKlinike',
      }
    ),

    formTitle: function(){
       return 'Dodavanje novog administratora klinike';
    },
  },
  created(){
    //load klinike
    this.fetchData();
    this.loadKlinike();
    
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'adminiKlinike/fetchAdminiKlinike',
        loadKlinike: 'klinike/loadKlinike',
        addAdminKlinike: 'adminiKlinike/addAdminKlinike',
      }
    ),

    resetForm(){
      this.$refs.myForm.resetValidation(); //internal vue method
      this.newItem = {
        ime: '',
        prezime: '',
        email: '',
        sifra: '',
        klinikaId: '',
      }
    },
    close(){
      this.resetForm();
      this.dialog = false;
    },
    save(){
      this.addAdminKlinike(this.newItem);
      this.close();
    },


    getKlinika(idKlinike){
      var klinika = (this.getKlinike.find(
        klinika => {
          return klinika.id == idKlinike 
        }));
      return [klinika];
    }
  }
}
</script>

<style>

</style>