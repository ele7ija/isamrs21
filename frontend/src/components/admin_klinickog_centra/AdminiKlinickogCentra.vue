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
          <v-toolbar-title>Administratori klinickog centra</v-toolbar-title>
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
    </v-data-table>

    <!-- reject dialog se pojavi kada nije ulogovan predefinisan admin -->
    <v-dialog v-model="rejectDialog" persistent max-width="400">
      <v-card>
        <v-card-title class="headline error--text" > Zabranjen Pristup</v-card-title>
        <v-divider></v-divider>
        <v-card-text class="title "> Niste ovlašćeni da gledate ili dodajete nove administratore kliničkog centra. </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="error--text" text @click="this.closeRejectDialog">Nazad</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';

export default {
  name: "AdminiKlinickogCentra",
  data: function(){
    return {
      dialog: false,
      rejectDialog: false,
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

      ],
      newItem: {
        ime: '',
        prezime: '',
        email: '',
        sifra: '',
      },
      
      //rules
      poljeRule: [
        v => !!v || 'Polje je obavezno',
        v => (v && v.length <= 50) || 'Polje ima najviše 50 karaktera'
      ],
    };
  },

  computed: {

    ...mapGetters(
      {
        getAll: 'adminiCentra/getAdminiCentra',
      }
    ),

    formTitle: function(){
       return 'Dodavanje novog administratora klinickog centra';
    },


  },

  created(){
    this.tryToLoad();
    //var predefinisan = this.checkPredefinisan();
    //console.log(predefinisan)
    //this.fetchData()
    
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'adminiCentra/fetchAdminiCentra',
        addAdminCentra: 'adminiCentra/addAdminCentra',
        checkPredefinisan: 'adminiCentra/checkPredefinisan',
      }
    ),

    resetForm(){
      this.$refs.myForm.resetValidation(); //internal vue method
      this.newItem = {
        ime: '',
        prezime: '',
        email: '',
        sifra: '',
      }
    },
    close(){
      this.resetForm();
      this.dialog = false;
    },
    save(){
      this.addAdminCentra(this.newItem);
      this.close();
    },

    async tryToLoad () {
      let predefinisan = await this.checkPredefinisan();
      if (predefinisan == true){
        //samo predefinisani administrator moze da ucita ostale administratore
        this.fetchData();
      }
      else{
        this.rejectDialog= true;
      }
      return predefinisan;
    },

    closeRejectDialog() {
      this.rejectDialog = false;
      this.$router.push('/home');
    }
  }
}
</script>

<style>

</style>