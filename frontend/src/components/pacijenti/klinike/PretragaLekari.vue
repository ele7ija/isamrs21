<template>
  <v-card outlined>
    <v-card-title>
      Pretraga pregleda
    </v-card-title>
    <v-card-subtitle>
      Pretražite vreme, i tip pregleda koji želite da obavite u ovoj klinici
    </v-card-subtitle>
    <v-card-text>
      <v-list subheader>
        <!-- <v-subheader>
          Pretrage
        </v-subheader> -->
        <v-list-item>
          <v-list-item-content>
            <v-menu
            v-model="menu1"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                hide-details=""
                v-model="iPocetniDatum"
                label="Početni datum"
                append-icon="mdi-calendar"
                readonly
                v-on="on"
                outlined
              ></v-text-field>
            </template>
            <v-date-picker 
              v-model="iPocetniDatum" 
              @input="menu1 = false"
              :min='minPocetni'
              ></v-date-picker>
          </v-menu>

          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-menu
            v-model="menu2"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                hide-details=""
                v-model="iKrajnjiDatum"
                label="Krajnji datum"
                append-icon="mdi-calendar"
                readonly
                v-on="on"
                outlined
              ></v-text-field>
            </template>
            <v-date-picker 
              v-model="iKrajnjiDatum" 
              @input="menu2 = false"
              :min='minKrajnji'
              ></v-date-picker>
          </v-menu>

          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-select outlined
              v-model='iOdabraniTipPregleda'
              :items='iDostupniTipoviPregleda'
              label='Tip pregleda'
              >
            </v-select>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapMutations, mapState, mapGetters } from 'vuex';
export default {
  name: 'PretragaLekari',
  data: function() {
    return {
      iOdabranSort: '',
      menu1: false,
      menu2: false,
    }
  },
  computed: {
    ...mapState('klinike', [
      'pocetniDatum',
      'krajnjiDatum',
      'tipPregleda',
      'sortiranjeKlinika',
      'dostupnaSortiranja',
      'odabraniTipPregleda'
    ]),
    ...mapGetters('klinike', [
      'dostupniTipoviPregleda',
    ]),
    iDostupniTipoviPregleda: {
      get: function() {
        let retval = [];
        for (let tipPregleda of this.dostupniTipoviPregleda) {
          retval.push(tipPregleda.naziv)
        }
        retval.push('Svi');
        return retval;
      }
    },
    iOdabraniTipPregleda: {
      get: function() {
        if (this.odabraniTipPregleda == null){
          return 'Svi';
        }
        return this.odabraniTipPregleda.naziv;
      },
      set: function(val) {
        if (val == 'Svi'){
          this.setOdabraniTipPregleda(null);
          return;
        }
        let tip = this.dostupniTipoviPregleda.filter(
          (x) => x.naziv == val);
        this.setOdabraniTipPregleda(tip[0]);
      }
    },
    iPocetniDatum: {
      get: function() {
        return this.pocetniDatum;
      },
      set: function(value) {
        this.setPocetniDatum(value);
      }
    },
    iKrajnjiDatum: {
      get: function() {
        return this.krajnjiDatum;
      },
      set: function(value) {
        this.setKrajnjiDatum(value);
      }
    },
    minPocetni: function() {
      let today = new Date();
      return today.toISOString()
    },
    minKrajnji: function() {
      if (this.iPocetniDatum === '') {
        return new Date().toISOString();
      }
      return this.iPocetniDatum;
    },
    internalDostupnaSortiranja: function() {
      let retval = [];
      for (let sortiranje of this.dostupnaSortiranja) {
        if (this.sortiranjeKlinika == null) {
          retval.push(sortiranje.naziv);
          continue;
        }
        if (sortiranje.naziv !== this.sortiranjeKlinika.naziv) {
          retval.push(sortiranje.naziv);
        }
        
      }
      return retval;
    },
  },
  methods: {
    ...mapMutations('klinike', [
      'setPocetniDatum',
      'setKrajnjiDatum',
      'setTipPregleda',
      'setSortiranjeKlinika',
      'setPretrage',
      'setOdabraniTipPregleda'
    ]),
    dodajSort: function() {
      let sort = this.dostupnaSortiranja.filter(
        (srt) => srt.naziv == this.iOdabranSort
      )
      this.setSortiranjeKlinika(sort[0]);
      this.iOdabranSort = '';
    },
    izbrisiSort: function() {
      this.setSortiranjeKlinika(null);
    }
  }
}
</script>

<style scoped>
  .v-messages {
    display: none;
  }
  .v-text-field__details {
    display: none;
  }
  .v-list-item__content {
    padding-bottom: 0;
  }
</style>