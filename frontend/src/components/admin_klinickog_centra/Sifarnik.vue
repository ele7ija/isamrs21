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
        <v-toolbar-title>Šifarnik dijagnoza i lekova</v-toolbar-title>
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
                <span class="headline">Dodavanje nove dijagnoze ili leka</span>
              </v-card-title>
              <hr>
              
              <v-card-text>
              <v-container>
                <v-text-field
                label="šifra"
                v-model="newItem.sifra"
                :rules="sifraRule"
                required
                ></v-text-field>

                <v-text-field
                label="naziv"
                v-model="newItem.naziv"
                :rules="nazivRule"
                required
                ></v-text-field>

                <v-select
                label="dijagnoza ili lek"
                v-model="newItem.tip"
                :items="tipovi"
                :rules="tipRule"
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

    <!-- tipovi -->
    <template v-slot:item.tip = "{ item }">
      <v-chip :style="getStyle(item.tip)" light>
        {{item.tip}}
      </v-chip>
    </template>

  </v-data-table>
</div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
export default {
  name: "Sifarnik",
  data: function (){
    return {
      dialog: false,
      search: '',
      isFormValid: true,
      headers: [
        {
          text: 'Šifra',
          value: 'sifra',
          sortable: true,
        },
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: true,
        },
        {
          text: 'Tip',
          value: 'tip',
          sortable: true,
        },
      ],
      newItem: {
        sifra: undefined,
        naziv: undefined,
        tip: undefined,
      },
      tipovi : ["DIJAGNOZA", "LEK"],

    }
  },

  computed: {
    ...mapGetters({
      getAll: 'sifarnik/getDijagnozeLekovi',
    }),

    //pravila ne stoje u data jer se moraju preracunati pri svakom pozivu
    sifraRule (){
      var rules = [];
      rules.push(v => !!v || 'Polje je obavezno');
      rules.push( v => (v && v.length <= 50) || 'Polje ima najviše 50 karaktera');    
      /*
      pojasnjenje: v je ono sto smo uneli u polje
      trazimo da li postoji
      x (dijagnoza ili lek) takav da je njegova sifra jednaka unetoj sifri
      ako ne postoji onda ce metoda findIndex vratiti -1 i polje je validno
      ako je metoda findIndex vratila index x onda sifra vec postoji, nevalidno
      */
      rules.push(
        v => 
        this.getAll.findIndex(x => x.sifra.toUpperCase()== v.toUpperCase()) == -1 
        || "Šifra mora biti jedinstvena"
      );
      return rules;
    },
    nazivRule (){
      var rules = [];
      rules.push(v => !!v || 'Polje je obavezno');
      rules.push( v => (v && v.length <= 50) || 'Polje ima najviše 50 karaktera');  
      rules.push(
        v => 
        this.getAll.findIndex(x => x.naziv.toUpperCase() == v.toUpperCase()) == -1 
        || "Naziv mora biti jedinstven"
      );
      return rules;
    },
    tipRule (){
      var rules = [];
      rules.push(v => !!v || 'Polje je obavezno');
      return rules;
    },
  },

  created(){
    this.fetchData();
  },

  methods: {
    ...mapActions({
      fetchData: 'sifarnik/fetchDijagnozeLekovi',
      addDijagnozaIliLek: 'sifarnik/addDijagnozaIliLek',
    }),

    save(){
      this.close();
      this.newItem.sifra = this.newItem.sifra.toUpperCase();
      this.addDijagnozaIliLek(this.newItem)
    },
    close() {
      this.resetForm();
      this.dialog = false;
    },

    resetForm(){
      this.$refs.myForm.resetValidation(); //internal vue method
      this.newItem = {
        sifra: undefined,
        naziv: undefined,
        tip: undefined,
      };
    },

    getStyle(tip){   
      if(tip=="LEK")
        return {
          backgroundColor: 'khaki',
          fontSize: '11px'
        }
      else if(tip=="DIJAGNOZA")
        return {
          backgroundColor: 'lavender',
          fontSize: '11px',
        }
    },
  }
}
</script>

<style>
  
</style>