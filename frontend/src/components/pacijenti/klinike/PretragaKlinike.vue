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
      </v-list>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapMutations, mapState } from 'vuex';
export default {
  name: 'PretragaKlinike',
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
      'dostupnaSortiranja'
    ]),
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
      'setPretrage'
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