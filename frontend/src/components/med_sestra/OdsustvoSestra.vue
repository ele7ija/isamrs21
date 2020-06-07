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
                  <v-date-picker v-model="pocetak" no-title scrollable :min="new Date().toISOString().substr(0, 10)">
                    <v-spacer></v-spacer>
                    <v-btn text color="red lighten-1" @click="menu1 = false">Odustani</v-btn>
                    <v-btn text color="green lighten-1" @click="$refs.menu1.save(pocetak)">Potvrdi</v-btn>
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
                      :disabled="pocetak==null"
                      v-model="krajFormatted"
                      label="Poslednji dan odsustva"
                      prepend-icon="mdi-calendar-range"
                      readonly
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="kraj" no-title scrollable :min="pocetak">
                    <v-spacer></v-spacer>
                    <v-btn text color="red lighten-1" @click="menu2 = false">Odustani</v-btn>
                    <v-btn text color="green lighten-1" @click="$refs.menu2.save(kraj)">Potvrdi</v-btn>
                  </v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="12" md="4">
                <v-btn
                  class="mt-3"
                  color="blue darken-1"
                  @click="podnesiZahtev"
                  :disabled="kraj==null">
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
      <v-col cols="12" md="5">
        <v-card>
          <v-card-title>
            Zahtevi na čekanju
          </v-card-title>
          <v-card-subtitle>
            Ovo su zahtevi koje administrator klinike još uvek nije obradio
          </v-card-subtitle>
          <v-card-text>
            <TabelaPoslatihUpita :key="key"/>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" md="7">
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
import {mapActions, mapGetters} from 'vuex';
import TabelaPoslatihUpita from './zahtevi/TabelaPoslatihUpita';
import TabelaNeobradjenihUpita from './zahtevi/TabelaNeobradjenihUpita';
export default{
  components: {
    TabelaPoslatihUpita,
    TabelaNeobradjenihUpita,
  },
  data: function(){
    return{
      key: 0,
      
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
  computed: {
    ...mapGetters({
      profil: 'profil/getProfil',
    })
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
      fetchMojiZahtevi: 'zahteviZaGodisnjiOsoblje/fetchMojiZahtevi',
      addZahtev: 'zahteviZaGodisnjiOsoblje/addZahtev'
    }),
    podnesiZahtev(){
      let profil = this.profil;
      let obj = {
        radniKalendar: {
          id: profil.radniKalendar.id,
          medicinskoOsoblje: {
            id: profil.id,
            pozicija: 'medicinska sestra'
          }
        },
        prviDanGodisnjeg: this.pocetakDate,
        poslednjiDanGodisnjeg: this.krajDate,
        odobreno: false,
        adminObradio: false,
        osobljeObradilo: false,
        razlogOdbijanja: ''
      };
      this.addZahtev(obj);
      this.ponisti();
      this.key += 1;
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