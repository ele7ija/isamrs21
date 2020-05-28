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
          <span class="headline">Izveštaj o pregledu </span>
          <v-spacer></v-spacer>
          <span class="display-1 text--green"> {{zdravstveniKarton.pacijent.ime}} {{zdravstveniKarton.pacijent.prezime}}</span>
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

        <v-container>
        <v-card>
        <v-card-title class="green lighten-4 justify-center">
          Izbor dijagnoze i lekova
        </v-card-title>
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
                <v-toolbar-title>Izbor lekova</v-toolbar-title>
                <v-spacer></v-spacer>       
                <!-- search bar  -->
                <v-text-field
                  v-model="searchLekova"
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
        </v-card>
        </v-container>

        <!-- izmena zdravstvenog kartona -->
        <v-container>
        <v-card class="justify-center mx-auto" max-width="500px">
          <v-card-title class="green lighten-4 justify-center">
            Izmena zdravstvenog kartona
          </v-card-title>
          <v-card-text>
            <v-text-field
            v-model="newItem.dioptrija"
            label="dioptrija"
            ></v-text-field>
          </v-card-text>

          <v-card-text>
            <v-select
            v-model="newItem.krvnaGrupa"
            :items="krvneGrupe"
            label="krvna grupa"
            chips
            ></v-select>
          </v-card-text>

          <v-card-text>
            <v-text-field
            v-model="newItem.visina"
            label="visina"
            hint="visina u cm"
            type="number"
            min="0"
            max="300"
            :rules="visinaRule"
            ></v-text-field>
          </v-card-text>

          <v-card-text>
          <v-text-field
            v-model="newItem.tezina"
            label="težina"
            type="number"
            hint="težina u kg"
            :rules="tezinaRule"
            ></v-text-field>
          </v-card-text>
          
        </v-card>
        </v-container>

        <!-- zakazivanje dodatnog poregleda ili operacije @Milan -->
        
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1"  text  @click="save()" :disabled="!isFormValid">
          Sačuvaj
        </v-btn>
        <v-btn color="blue darken-1" text @click="close()">
          Poništi
        </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-card>
  </v-dialog>

</div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'FormaIzvestajPregleda',
  props: ["posetaId", "zdravstveniKarton"],
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
        dioptrija: this.zdravstveniKarton.dioptrija,
        krvnaGrupa: this.zdravstveniKarton.kvrnaGrupa,
        visina: this.zdravstveniKarton.visina,
        tezina: this.zdravstveniKarton.tezina,
      },

      krvneGrupe: [ "A", "B", "AB", "0"],

      //rules
      opisRule: [
        v => !!v || 'Opis trenutne posete je obavezan',
        v => (v && v.length <= 1000) || 'Opis ima najviše 1000 karaktera'
      ],
      visinaRule: [
        v => !!v || 'Visina je obavezno polje',
        v => ( v &&  ( 0<= parseInt(v) && parseInt(v) <= 300) ) || 'Visina mora biti izmedju 0 i 300 cm '
      ],
      tezinaRule: [
        v => !!v || 'Tezina je obavezno polje',
        v => ( v &&  ( 0<= parseInt(v) && parseInt(v) <= 300) ) || 'Tezina mora biti izmedju 0 i 300 kg '
      ]

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
        dioptrija: this.zdravstveniKarton.dioptrija,
        krvnaGrupa: this.zdravstveniKarton.kvrnaGrupa,
        visina: this.zdravstveniKarton.visina,
        tezina: this.zdravstveniKarton.tezina,
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