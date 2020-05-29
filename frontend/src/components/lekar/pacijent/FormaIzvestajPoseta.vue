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
      <v-btn icon dark @click="close()">
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
          <span class="display-1 text--green"> {{getZdravstveniKarton.pacijent.ime}} {{getZdravstveniKarton.pacijent.prezime}}</span>
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

        <v-container fluid class="mt-n8">
          <v-card>
            <v-card-title class="green lighten-2 justify-center">
              Izbor dijagnoze i lekova
            </v-card-title>
            <v-card-text>
              <v-row>
                <v-col cols="12" md="6">
                  <!-- izbor dijagnoze -->
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
                      <v-toolbar flat color="green lighten-4">
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
                </v-col>
                <!-- izbor lekova -->
                <v-col cols="12" md="6">
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
                      <v-toolbar flat color="green lighten-4">
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
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>
        </v-container>

        <v-container fluid>
          <v-card>
            <v-card-title class="blue lighten-2 justify-center">
              Zdravstveni karton i dodatni pregledi ili operacije
            </v-card-title>
            <v-card-text>
              <v-row>
                <v-col cols="12" md="6">
                  <!-- izmena zdravstvenog kartona -->
                  <v-card class="justify-center mx-auto" max-width="500px">
                    <v-card-title class="blue lighten-4 justify-center">
                      Izmena zdravstvenog kartona
                    </v-card-title>
                    <v-card-text class="mt-2">
                      <v-text-field
                      v-model="newItem.dioptrija"
                      label="dioptrija"
                      ></v-text-field>
                    </v-card-text>
                    <v-card-text class="mt-n8">
                      <v-select
                      v-model="newItem.krvnaGrupa"
                      :items="krvneGrupe"
                      label="krvna grupa"
                      chips
                      ></v-select>
                    </v-card-text>
                    <v-row class="mt-n10">
                      <v-col cols="12" md="6">
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
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-card-text>
                        <v-text-field
                          v-model="newItem.tezina"
                          label="težina"
                          type="number"
                          hint="težina u kg"
                          :rules="tezinaRule"
                          ></v-text-field>
                        </v-card-text>
                      </v-col>
                    </v-row>
                  </v-card>
                </v-col>
                <v-col cols="12" md="6">
                  <!-- zakazivanje dodatnog pregleda ili operacije @Milan -->
                  <v-card class="justify-center mx-auto" max-width="500px">
                    <v-card-title class="blue lighten-4 justify-center">
                      Zakazivanje dodatnih pregleda i operacija
                    </v-card-title>
                    <v-card-text>
                      <v-datetime-picker
                        class="mt-1"
                        v-model="datetimeStart"
                        label="Datum i vreme početka"
                        dateFormat="dd.MM.yyyy"
                        :datePickerProps="{min: new Date(new Date().getTime()+1000*60*60*24).toISOString()}"
                        :key="datePickerKey"
                      >
                        <template slot="actions" slot-scope="{ parent }">
                          <v-btn color="error darken-1" @click="parent.clearHandler">Clear</v-btn>
                          <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
                        </template>
                      </v-datetime-picker>
                      <v-text-field
                        v-model="datetimeEnd"
                        label="Datum i vreme kraja"
                        readonly
                      ></v-text-field>
                      <v-select
                        v-model="tipPregleda"
                        :items="_specijalizacijeLekara"
                        label="Tip pregleda"
                        chips
                      >
                        <template v-slot:item="{item}">
                          <v-icon v-if="item.value.vrsta=='pregled'" class="mr-2">
                            mdi-magnify
                          </v-icon>
                          <v-icon v-else class="mr-2">
                            mdi-knife
                          </v-icon>
                          {{ item.text }}
                        </template>
                      </v-select>
                    </v-card-text> 
                    <v-card-actions>
                      <v-btn
                        @click="reset"
                        color="red darken-1"
                        class="mt-n8 ml-2"
                      >
                        Resetuj
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>
        </v-container>
        
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
  props: ["posetaId"],
  data () {
    return {
      datetimeStart: null,
      tipPregleda: null,
      datePickerKey: 0,

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
        dioptrija: null,
        krvnaGrupa: null,
        visina: null,
        tezina: null,
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
      ],
    }
  },

  computed: {
    ...mapGetters({
      pregledMozeDaSeZapocne: 'pacijenti/pregledMozeDaSeZapocne',
      getDijagnozeLekovi: 'sifarnik/getDijagnozeLekovi',
      getZdravstveniKarton: 'pacijenti/getZdravstveniKarton',
      specijalizacijeLekara: 'pacijenti/getSpecijalizacijeLekara',
      profil: 'profil/getProfil'
    }),

    datetimeEnd() {
      if(this.tipPregleda == null || this.datetimeStart == null)
        return null;
      let temp = null;
      temp = new Date(this.datetimeStart.getTime() + this.tipPregleda.trajanjeMinuti*60000);
      let day = temp.getDate();
      let month = temp.getMonth() + 1;
      let hour = temp.getHours();
      let minute = temp.getMinutes();
      if((String(day)).length==1)
        day='0'+day;
      if((String(month)).length==1)
        month='0'+month;
      if((String(hour)).length==1)
        hour='0'+hour;
      if((String(minute)).length==1)
        minute='0'+minute;

      return `${day}.${month}.${temp.getFullYear()} ${hour}:${minute}`;
    },

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
    },
    _specijalizacijeLekara(){
      if(!this.specijalizacijeLekara)
        return [];
      return this.specijalizacijeLekara.map(x => {
        return{
          text: `${x.naziv}`,
          value: x
        };
      });
    }
  },
  created(){
    this.fetchSifarnikData();
  },

  methods: {
    ...mapActions({
      fetchSifarnikData: 'sifarnik/fetchDijagnozeLekovi',
      updatePoseta: 'pacijenti/updatePoseta',
      addUpitZaPregled: 'upitZaPregled/kreirajUpit'
    }),

    zapocni(){
      this.dialog = true;
      this.populateNewItem();
    },
    save(){
      this.newItem.posetaId = this.posetaId;
      this.newItem.selectedDijagnoza = this.newItem.selectedDijagnoza[0];
      this.updatePoseta(this.newItem).then(
        () => {
          //rezervisi pregled
          if(this.datetimeEnd){
            let upitZaPregled = {
              odobren: false,
              potvrdjen: false,
              pocetakPregleda: this.datetimeStart,
              krajPregleda: this.$utility.stringToDate(this.datetimeEnd),
              datumKreiranjaUpita: new Date(),
              pacijent: this.getZdravstveniKarton.pacijent.email,
              lekar: this.profil.id,
              tipPregleda: this.tipPregleda.id,
              klinika: this.profil.klinika.id,
              opis: 'Lekarov upit',
              lekarVerzija: this.profil.version,
              tipPregledaVerzija: this.tipPregleda.version
            };
            this.addUpitZaPregled(upitZaPregled);
          }
        },
        null
      );
      this.close();
    },
    resetForm(){
      this.$refs.myForm.resetValidation(); //internal vue method
      this.newItem = {
        posetaId: undefined,
        opis: undefined,
        selectedDijagnoza: [],
        selectedLekovi: [],
        dioptrija: null,
        krvnaGrupa: null,
        visina: null,
        tezina: null,
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
    populateNewItem(){
      var k = this.getZdravstveniKarton;
      this.newItem.dioptrija = k.dioptrija;
      this.newItem.krvnaGrupa = k.krvnaGrupa;
      this.newItem.visina = k.visina;
      this.newItem.tezina = k.tezina;
    },
    setKraj(){
      if(this.tipPregleda == null || this.datetimeStart == null)
        return;
      let temp = null;
      temp = new Date(this.datetimeStart.getTime() + this.tipPregleda.trajanjeMinuti*60000);
      let day = temp.getDate();
      let month = temp.getMonth() + 1;
      let hour = temp.getHours();
      let minute = temp.getMinutes();
      if((String(day)).length==1)
        day='0'+day;
      if((String(month)).length==1)
        month='0'+month;
      if((String(hour)).length==1)
        hour='0'+hour;
      if((String(minute)).length==1)
        minute='0'+minute;

      this.datetimeEnd=`${day}.${month}.${temp.getFullYear()} ${hour}:${minute}`;
    },
    reset(){
      this.tipPregleda = null;
      this.datetimeStart = null;
      this.datePickerKey += 1;
    }
  }
}
</script>

<style>
.pregled{
  background-color: #E6EE9C;
}
.operacija{
  background-color: #FFAB91;
}
</style>