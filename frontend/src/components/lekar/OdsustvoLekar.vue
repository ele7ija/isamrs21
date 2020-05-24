<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12" md="12">
        <v-card>
          <v-card-title class="headline justify-center">
            Podnošenje zahteva za godišnji odmor
          </v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="12" md="4">
                <v-menu
                  ref="menu1"
                  v-model="menu1"
                  :close-on-content-click="false"
                  :return-value.sync="pocetak"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      v-model="pocetakFormatted"
                      label="Prvi dan odsustva"
                      prepend-icon="mdi-calendar-range"
                      readonly
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="pocetak" no-title scrollable>
                    <v-spacer></v-spacer>
                    <v-btn text color="decline" @click="menu1 = false">Odustani</v-btn>
                    <v-btn text color="accept" @click="$refs.menu1.save(pocetak)">Potvrdi</v-btn>
                  </v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="12" md="4">
                <v-menu
                  ref="menu2"
                  v-model="menu2"
                  :close-on-content-click="false"
                  :return-value.sync="kraj"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      v-model="krajFormatted"
                      label="Poslednji dan odsustva"
                      prepend-icon="mdi-calendar-range"
                      readonly
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="kraj" no-title scrollable :min="pocetak">
                    <v-spacer></v-spacer>
                    <v-btn text color="decline" @click="menu2 = false">Odustani</v-btn>
                    <v-btn text color="accept" @click="$refs.menu2.save(kraj)">Potvrdi</v-btn>
                  </v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="12" md="4">
                <v-btn
                  class="mt-3"
                  color="blue darken-1"
                  @click="podnesiZahtev">
                  Podnesi zahtev
                </v-btn>
                <v-btn
                  class="mt-3 ml-3"
                  color="red darken-1"
                  @click="ponisti">
                  Odustani
                </v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>
            Zahtevi na čekanju
          </v-card-title>
          <v-card-subtitle>
            Ovo su zahtevi koje administrator klinike još uvek nije obradio
          </v-card-subtitle>
          <v-card-text>
            <TabelaPoslatihUpita/>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title>
            Obrađeni zahtevi
          </v-card-title>
          <v-card-subtitle>
            Ovo su zahtevi koje administrator klinike obradio
          </v-card-subtitle>
          <v-card-text>
            <TabelaNeobradjenihUpita/>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import {mapActions} from 'vuex';
import TabelaPoslatihUpita from './zahtevi/TabelaPoslatihUpita';
import TabelaNeobradjenihUpita from './zahtevi/TabelaNeobradjenihUpita';
export default{
  name: "OdsustvoLekar",
  components: {
    TabelaPoslatihUpita,
    TabelaNeobradjenihUpita
  },
  data: function(){
    return{
      menu1: false,
      menu2: false,

      pocetak: null, //string
      pocetakDate: null, //date
      pocetakFormatted: null, //formated string
      
      kraj: null, //string
      krajDate: null, //date
      krajFormatted: null, //formatted string
    };
  },
  watch: {
    pocetak (newValue) {
      if(newValue == null){
        this.pocetakDate = null;
        this.pocetakFormatted = null;
      }else{
        this.pocetakDate = new Date(newValue);
        this.pocetakFormatted = this.formatDate(this.pocetakDate);
      }
    },
    kraj (newValue) {
      if(newValue == null){
        this.krajDate = null;
        this.krajFormatted = null;
      }else{
        this.krajDate = new Date(newValue);
        this.krajFormatted = this.formatDate(this.krajDate);
      }
    },
  },
  created(){
    this.fetchMojiZahtevi();
  },
  methods: {
    ...mapActions({
      fetchMojiZahtevi: 'zahteviZaGodisnjiOsoblje/fetchMojiZahtevi'
    }),
    podnesiZahtev(){
      console.log("emituj akciju na bek");
    },
    ponisti(){
      this.pocetak = null;
      this.kraj = null;
    },
    formatDate(date){
      return date.toLocaleDateString('sr');
    },
  }
}
</script>

<style>

</style>