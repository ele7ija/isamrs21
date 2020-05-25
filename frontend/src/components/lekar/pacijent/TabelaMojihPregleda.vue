<template>
<div>
  <v-card>
    <v-card-text>
      <v-data-table
        :items="_posete"
        :headers="headers"
      >
        <template v-slot:top>
          <v-toolbar flat color="red lighten-3">
            <v-toolbar-title>Neobavljeni pregledi</v-toolbar-title>
          </v-toolbar>
        </template>
        <template v-slot:body.prepend>
          <tr>
            <td>
              <v-datetime-picker v-model="pocetak" :text-field-props="textFieldProps" date-format="dd.MM.yyyy" :key="key1"></v-datetime-picker>
            </td>
            <td>
              <v-datetime-picker v-model="kraj" :text-field-props="textFieldProps" date-format="dd.MM.yyyy" :key="key2"></v-datetime-picker>
            </td>
            <td>
              <v-select :items="_tipoviPregleda" v-model="tipPregleda"></v-select>
            </td>
            <td>
              <v-btn @click="ponisti()">Poništi</v-btn>
            </td>
          </tr>
        </template>
        <template v-slot:item.actions="{ item }">

          <FormaIzvestajPoseta :posetaId="item.id" />
        </template>

      </v-data-table>
    </v-card-text>
  </v-card>
  
</div>
</template>

<script>
import FormaIzvestajPoseta from './FormaIzvestajPoseta'
import {mapGetters} from 'vuex';
export default {
  name: 'TabelaMojihPregleda',
  components: {
    FormaIzvestajPoseta,
  },
  data: function(){
    return {
      key1: 0,
      key2: 2,
      pocetak: null,
      kraj: null,
      tipPregleda: '',
      textFieldProps: {
        appendIcon: 'event'
      },
      headers: [
        { text: 'Početak pregleda', value: 'pocetak', sortable: false,
          filter: (value) => {
            if(!this.pocetak)
              return true;
            let date = this.$utility.stringToDate(value);
            if(!this.kraj)
              return date.getTime() == this.pocetak.getTime();
            return date >= this.pocetak && date <= this.kraj;
          }
        },
        { text: 'Kraj pregleda', value: 'kraj', sortable: false,
          filter: (value) => {
            if(!this.kraj)
              return true;
            let date = this.$utility.stringToDate(value);
            if(!this.pocetak)
              return date.getTime() == this.kraj.getTime();
            return date >= this.pocetak && date <= this.kraj;
          }
        },
        { text: 'Tip pregleda', value: 'tipPregleda', sortable: false,
          filter: (value) => {
            if(!this.tipPregleda)
              return true;
            return value == this.tipPregleda;
          }
        },
        { text: 'Akcije', value: 'actions' }
      ]
    };
  },
  computed: {
    ...mapGetters({
      posete: 'pacijenti/getPoseteOdabranogPacijenta',
      tipoviPregleda: 'tipoviPregleda/getTipoviPregleda',
    }),
    _posete(){
      if(this.posete){
        let neobavljenePosete = this.posete.filter(x => {
          return !x.opis && !x.bolest
        });
        return neobavljenePosete.map(x => {
          let date1 = new Date(x.pregled.pocetakPregleda);
          let date2 = new Date(x.pregled.krajPregleda);
          date1 = this.$utility.handleTimeZone(date1);
          date2 = this.$utility.handleTimeZone(date2);
          return{
            id: x.id,
            pocetak: this.$utility.formatDate(date1),
            kraj: this.$utility.formatDate(date2),
            tipPregleda: x.pregled.tipPregleda.naziv,
            bolest: x.bolest,
            opis: x.opis,
            zdravstveniKarton : x.zdravstveniKarton,
          };
        });
      }else
        return [];
    },
    _tipoviPregleda(){
      return this.tipoviPregleda.map(x => x.naziv);
    },
  },
  methods: {
    ponisti(){
      this.pocetak = null;
      this.kraj = null;
      this.tipPregleda = '';
      this.key1 += 1;
      this.key2 += 1;
    },

  }
}
</script>

<style>

</style>