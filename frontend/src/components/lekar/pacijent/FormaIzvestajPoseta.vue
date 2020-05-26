<template>
<div >
  <v-btn 
  color="primary" 
  @click="zapocni()" 
  :disabled="!pregledMozeDaSeZapocne(this.posetaId)">
  Započni pregled
  </v-btn>


  <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
  <v-card>
    <v-toolbar dark color="green">
      <v-btn icon dark @click="dialog = false">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>Pregled u toku...</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>

    <v-form v-model="isFormValid" ref="myForm">
      <v-card>
        <!-- naslov forme -->
        <v-card-title>
          <span class="headline">Izvestaj o pregledu</span>
        </v-card-title>
        <hr>

        <!-- opis pregleda -->
        <v-card-text>
            <v-textarea
            outlined
            label="Opis pregleda" 
            hint="Napisati opis pregleda i kakve probleme pacijent ima."
            :rules=opisRule
            v-model="newItem.opis"
            ></v-textarea>
        </v-card-text> 


        <!-- izbor dijagnoze -->
        <v-card-text>
          <v-data-table
            :headers="headers"
            :items="dijagnozaItems"
            :search="searchDijagnoza"
            v-model="newItem.selectedDijagnoza"
            :single-select="true"
            item-key="sifra"
            show-select
            class="elevation-2">
            <template v-slot:top>
              <v-toolbar flat color="white">
                <v-toolbar-title>Izbor dijagnoze</v-toolbar-title>
                <v-spacer></v-spacer>       
                <!-- search bar  -->
                <v-text-field
                  v-model="searchDijagnoza"
                  append-icon="mdi-magnify"
                  label="Search"
                  single-line
                  hide-details
                ></v-text-field>
              </v-toolbar>
            </template>

            <!-- tipovi -->
            <template v-slot:item.tip = "{ item }">
              <v-chip :style="getStyle(item.tip)" light>
                {{item.tip}}
              </v-chip>
            </template>
          </v-data-table>
        </v-card-text>

        <!-- izbor lekova -->
        <v-card-text>
          <v-data-table
            :headers="headers"
            :items="lekItems"
            :search="searchLekova"
            v-model="newItem.selectedLekovi"
            :single-select="false"
            item-key="sifra"
            show-select
            class="elevation-2">
            <template v-slot:top>
              <v-toolbar flat color="white">
                <v-toolbar-title>Izbor dijagnoze</v-toolbar-title>
                <v-spacer></v-spacer>       
                <!-- search bar  -->
                <v-text-field
                  v-model="searchDijagnoza"
                  append-icon="mdi-magnify"
                  label="Search"
                  single-line
                  hide-details
                ></v-text-field>
              </v-toolbar>
            </template>

            <!-- tipovi -->
            <template v-slot:item.tip = "{ item }">
              <v-chip :style="getStyle(item.tip)" light>
                {{item.tip}}
              </v-chip>
            </template>
          </v-data-table>
        </v-card-text>

        <!-- izmena zdravstvenog kartona -->
        <v-card>
          izmena zdravstvenog kartona
        </v-card>
        
        <!-- zakazivanje dodatnog poregleda ili operacije @Milan -->
        


        <!-- sacuvati posetu objekat i dodati posetu u zdravstveni karton -->
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1"  text  @click="dialog=false" :disabled="!isFormValid">
          Sačuvaj
        </v-btn>
        <v-btn color="blue darken-1" text @click="close()">
          Poništi
        </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
    <v-card>
      {{posetaId}}
    </v-card>
  </v-card>
  </v-dialog>

</div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'FormaIzvestajPregleda',
  props: ["posetaId"],
  data () {
    return {
      isFormValid: true,
      dialog: false,
      searchDijagnoza: '',
      searchLekova: '',
      headers: [
        {
          text: 'Šifra',
          value: 'sifra',
        },
        {
          text: 'Naziv',
          value: 'naziv',
        },
        {
          text: 'Tip',
          value: 'tip',
        },
      ],
      newItem: {
        posetaId: undefined,
        opis: undefined,
        selectedDijagnoza: [],
        selectedLekovi: [],
      },

      //rules
      opisRule: [
        v => !!v || 'Opis trenutne posete je obavezan',
        v => (v && v.length <= 1000) || 'Opis ima najviše 1000 karaktera'
      ],
    }
  },

  computed: {
    ...mapGetters({
      pregledMozeDaSeZapocne: 'pacijenti/pregledMozeDaSeZapocne',
      getDijagnozeLekovi: 'sifarnik/getDijagnozeLekovi',
    }),

    dijagnozaItems: function () {
      var dijagnoze = this.getDijagnozeLekovi.filter(
        function(dijagnozaIliLek){
          return dijagnozaIliLek.tip == 'DIJAGNOZA';
        }
      );
      return dijagnoze;
    },
    lekItems: function () {  
      var lekovi = this.getDijagnozeLekovi.filter(
        function(dijagnozaIliLek){
          return dijagnozaIliLek.tip == 'LEK';
        }
      );
      return lekovi;
    }
  },

  created(){
    this.fetchSifarnikData();
  },

  methods: {
    ...mapActions({
      fetchSifarnikData: 'sifarnik/fetchDijagnozeLekovi',
      updatePoseta: 'pacijenti/updatePoseta',
    }),

    zapocni(){
      this.dialog = true;
      console.log("pregled poceo");
    },
    save(){
      this.newItem.posetaId = this.posetaId;
      this.newItem.selectedDijagnoza = this.newItem.selectedDijagnoza[0];
      this.updatePoseta(this.newItem);
      this.close();
    },
    resetForm(){
      this.$refs.myForm.resetValidation(); //internal vue method
      this.newItem = {
        posetaId: undefined,
        opis: undefined,
        selectedDijagnoza: [],
        selectedLekovi: [],
      };
    },
    close(){
      this.resetForm();
      this.dialog = false;
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