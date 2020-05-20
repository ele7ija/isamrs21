<template>
  <v-card :disabled='pretrazeniPregledi.length==0'>
    <v-card-title>
      {{lekar.ime}} {{lekar.prezime}}
    </v-card-title>
    <v-card-subtitle>
      {{lekar.email}}
    </v-card-subtitle>
    <v-divider></v-divider>
    <v-card-text class='pb-2'>
      <v-list>
        <v-list-item
          v-for='pregled in pretrazeniPregledi'
          :key='pregled.id'
          two-line
          >
          <v-list-item-content>
            <v-list-item-title>
              {{pregled.tipPregleda.naziv}}
            </v-list-item-title>
            <v-list-item-subtitle>
              {{formatDate(pregled.pocetakPregleda)}} - 
              {{formatDateTime(pregled.krajPregleda)}}
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-list-item-action-text>
              <v-btn
                color='green'
                class='mb-0'
                outlined
                @click='rezervisiPostojeci(pregled)'>
                <v-icon>mdi-check</v-icon>
                Rezerviši
              </v-btn>
            </v-list-item-action-text>
          </v-list-item-action>
        </v-list-item>
        <v-list-item
          v-for='pregled in nepretrazeniPregledi'
          :key='pregled.id'
          two-line
          >
          <v-list-item-content style="opacity: 0.6">
            <v-list-item-title>
              {{pregled.tipPregleda.naziv}}
            </v-list-item-title>
            <v-list-item-subtitle>
              {{formatDate(pregled.pocetakPregleda)}} - 
              {{formatDateTime(pregled.krajPregleda)}}
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-list-item-action-text>
              <v-btn
                color='orange'
                class='mb-0'
                outlined
                @click='rezervisiPostojeci(pregled)'>
                <v-icon>mdi-check</v-icon>
                Rezerviši
              </v-btn>
            </v-list-item-action-text>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card-text>
    <v-card-actions class="justify-center">
      <v-btn text
      @click="show = !show">
        Kreiraj svoj pregled
        <v-icon>
          {{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}
        </v-icon>
      </v-btn>
    </v-card-actions>
    <v-expand-transition>
      <div v-show="show">
        <v-divider></v-divider>
        <v-card-text>
          <v-form ref='pregledForm' v-model='valid'>
            <v-container fluid>
              <v-row>
                <v-col class='pt-0 pb-2'>
                  <v-select
                    v-model='iOdabraniTipPregleda'
                    :items='iDostupniTipoviPregleda'
                    :rules='tipPregledaPravila'
                    label='Tip pregleda'>
                  </v-select>
                </v-col>
              </v-row>
              <v-row justify="space-around">
                <v-col :cols=7 class='pt-0 pb-0'>
                  <v-datetime-picker
                    v-model="cdatetimeStart"
                    label="Početak pregleda"
                    dateFormat="dd.MM.yyyy"
                    @input="setKraj()"
                  >
                    <template slot="actions" slot-scope="{ parent }">
                      <v-btn color="error darken-1" @click="parent.clearHandler">Odustani</v-btn>
                      <v-btn color="success darken-1" @click="parent.okHandler">Potvrdi</v-btn>
                    </template>
                  </v-datetime-picker>
                </v-col>
                <v-col class='pt-0 pb-0'>
                  <v-text-field
                    disabled
                    v-model="cdatetimeEnd"
                    label="Kraj pregleda"
                    readonly
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row justify="center">
                <v-col :cols=6>
                  <v-btn
                    :disabled="!validAggregate"
                    color="success"
                    @click="rezervisi"
                  >
                    Rezerviši
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
        </v-card-text>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script>
import {mapGetters, mapState, mapMutations} from 'vuex';
export default {
  name: 'LekarCard',
  props: ['lekar'],
  data: function() {
    return {
      show: false,
      datetimeStart: null,
      datetimeEnd: null,
      valid: true,
      tipPregledaPravila: [
        v => !!v || 'TipPregleda je obavezan',
        v => (v && v!=='Svi') || 'Tip pregleda ne sme biti `Svi`'
      ],
    }
  },
  computed: {
    cdatetimeStart: {
      get: function() {
        if (this.datetimeStart == null) {
          return null;
        }
        return this.formatDate(this.datetimeStart)
      },
      set: function(val) {
        this.datetimeStart = val;
      }
    },
    cdatetimeEnd: function() {
      if (this.datetimeStart == null) {
        return null;
      }
      return this.formatDateTime(this.datetimeEnd)
    },
    ...mapGetters('klinike', [
      'getPretrazeniPreglediLekara',
      'getNepretrazeniPreglediLekara',
      'dostupniTipoviPregleda'
    ]),
    ...mapState('klinike', [
      'odabraniTipPregleda'
    ]),
    pretrazeniPregledi: function() {
      return this.getPretrazeniPreglediLekara({klinikaId: this.lekar.klinika.id, lekarId: this.lekar.id})
    },
    nepretrazeniPregledi: function() {
      return this.getNepretrazeniPreglediLekara({klinikaId: this.lekar.klinika.id, lekarId: this.lekar.id})
    },
    iDostupniTipoviPregleda: {
      get: function() {
        let retval = [];
        for (let tipPregleda of this.dostupniTipoviPregleda) {
          retval.push(tipPregleda.naziv)
        }
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
        // if (val == 'Svi'){
        //   this.setOdabraniTipPregleda(null);
        //   return;
        // }
        let tip = this.dostupniTipoviPregleda.filter(
          (x) => x.naziv == val);
        this.setOdabraniTipPregleda(tip[0]);
      }
    },
    validAggregate: function() {
      return this.valid && this.datetimeStart != null;
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
      'setOdabraniPregled'
    ]),
    formatDate: function(date) {
      let temp = new Date(date);
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
      // return ("0" + d.getDate()).slice(-2) + '.' +
      //   ("0" + (d.getMonth()+1)).slice(-2) + '.' +
      //   d.getFullYear() + '. ' +
      //   d.getHours() + ':' + ('0' + d.getMinutes()).slice(-2);
    },
    formatDateTime: function(date) {
      let d = new Date(date);
      return d.getHours() + ':' + ('0' + d.getMinutes()).slice(-2);
    },
    setKraj: function(){
      let obj = {
        done: this.datetimeStart != null
      }
      let temp = null;
      if(!obj.done){
        this.datetimeEnd = null;
      }else{
        temp = new Date(this.datetimeStart.getTime() + this.odabraniTipPregleda.trajanjeMinuti*60000);
        this.datetimeEnd=temp;
      }
      // this.$store.commit('pregledDialog/setPocetak', this.datetimeStart);
      // this.$store.commit('pregledDialog/setKraj', temp);
      // this.$emit('changeStatus', obj);
    },
    rezervisi: function() {
      let obj = {
        pocetakPregleda: this.datetimeStart,
        krajPregleda: this.datetimeEnd,
        tipPregleda: this.odabraniTipPregleda,
        lekar: this.lekar,
        klinika: this.lekar.klinika,
        id: null,
        version: null,
      };
      this.setOdabraniPregled(obj);
      this.$router.push('/pacijent/rezervacija-pregleda');
    },
    rezervisiPostojeci: function(pregled) {
      this.setOdabraniPregled(pregled);
      this.$router.push('/pacijent/rezervacija-pregleda');
    }
  },
}
</script>

<style>

</style>