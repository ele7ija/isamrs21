<template>
  <v-card outlined>
    <v-card-title>
      Pretraga pregleda
    </v-card-title>
    <v-card-subtitle>
      Unesite podatke o pregledu koji želite da obavite.
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
                hide-details
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
                hide-details=""
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
            <v-select
              class='mb-6' 
              outlined
              hide-details=""
              v-model='iOdabraniTipPregleda'
              :items='iDostupniTipoviPregleda'
              label='Tip pregleda'
              >
            </v-select>
          </v-list-item-content>
        </v-list-item>
        <v-list-item >
          <v-list-item-content>
            <v-select 
              hide-details=""
              outlined
              v-model='iOdabraniGrad'
              :items='dostupniGradovi'
              label='Lokacija'
              >
            </v-select>
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-range-slider
              class='mt-6 pr-2'
              v-model='iMinMaxOcena'
              label='Ocena klinike'
              thumb-label="always"
              :thumb-size="26"
              min=0
              max=10
              step=0.1
              >
            </v-range-slider>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card-text>
    <v-divider></v-divider>
    <v-card-text>
      <v-list subheader>
        <v-subheader>
          Sortiranja
        </v-subheader>
        <v-list-item v-if='sortiranjeKlinika!=null'>
          <v-list-item-action>
            <v-btn icon @click='izbrisiSort' color='primary'>
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </v-list-item-action>
          <v-list-item-content>
            <v-text-field 
              disabled 
              solo 
              outlined
              v-model='sortiranjeKlinika.naziv'>
            </v-text-field>
          </v-list-item-content>
            <v-switch 
              v-model='sortiranjeKlinika.rastuce' 
              label='ASC'
              class='pl-4 overline'>
            </v-switch>
          
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-select outlined
              v-model='iOdabranSort'
              :items='internalDostupnaSortiranja'
              label='Odaberi sortiranje'
              @input="dodajSort"
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
  name: 'PretragaKlinika',
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
      'odabraniTipPregleda',
      'odabraniGrad',
      'minOcena',
      'maxOcena'
    ]),
    ...mapGetters('klinike', [
      'dostupniTipoviPregleda',
      'dostupniGradovi',
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
        if (val == 'Svi') {
          this.setOdabraniTipPregleda(null);
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
    // internalSortiranja: {
    //   get: function() {
    //     let retval = [];
    //     for (let sortiranje of this.sortiranja) {
    //       retval.push(sortiranje.naziv);
    //     }
    //     return retval;
    //   },
    //   set: function(value) {
    //     let retval = [];
    //     console.log(value)
    //     for (let sortiranjeNaziv of value) {
    //       let sortiranje = this.dostupnaSortiranja.filter(
    //         (obj) => obj.naziv === sortiranjeNaziv);
    //       retval.push(sortiranje[0]);
    //     }
    //     this.setSortiranja(retval);
    //   }
    // },
    iOdabraniGrad: {
      get: function() { return this.odabraniGrad; },
      set: function(val) { this.setOdabraniGrad(val); }
    },
    iMinMaxOcena: {
      get: function() { return [this.minOcena, this.maxOcena]; },
      set: function(val) { this.setMinOcena(val[0]); this.setMaxOcena(val[1]) }
    },
    iMaxOcena: {
      get: function() { return this.maxOcena; },
      set: function(val) { this.setMaxOcena(val); }
    }
  },
  methods: {
    ...mapMutations('klinike', [
      'setPocetniDatum',
      'setKrajnjiDatum',
      'setTipPregleda',
      'setSortiranjeKlinika',
      'setPretrage',
      'setOdabraniTipPregleda',
      'setOdabraniGrad',
      'setMinOcena',
      'setMaxOcena'
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