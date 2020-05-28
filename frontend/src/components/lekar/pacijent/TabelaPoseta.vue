<template>
  <v-data-table
    :items="_posete"
    :headers="headers"
    show-expand
    single-expand
  >
    <template v-slot:top>
      <v-toolbar flat color="blue lighten-3">
        <v-toolbar-title>Obavljeni pregledi</v-toolbar-title>
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
    <template v-slot:expanded-item="{ headers, item }">
      <td :colspan="headers.length">
        <!-- Milane ovde mozes da vidis da ucitava lepo item.bolest, ali ne moze item.bolest.naziv iz nekog razloga -->
        {{item.bolest}}
        <v-text-field v-model="item.bolest.naziv" readonly label="Utvrđena bolest" class="mt-5"/>
        <v-textarea
          outlined
          :value="item.opis"
          label="Opis simtpoma"
          readonly
        ></v-textarea>
      </td>
    </template>
  </v-data-table>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: 'TabelaPoseta',
  data: function(){
    return {
      key1: 0,
      key2: 2,
      pocetak: null,
      textFieldProps: {
        appendIcon: 'event'
      },
      kraj: null,
      tipPregleda: '',
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
        { text: 'Detalji', value: 'data-table-expand' }
      ]
    };
  },
  computed: {
    ...mapGetters({
      posete: 'pacijenti/getPoseteOdabranogPacijenta',
      tipoviPregleda: 'tipoviPregleda/getTipoviPregleda'
    }),
    _posete(){
      if(this.posete){
        let obavljenePosete = this.posete.filter(x => {
          return !!x.opis 
        });
        return obavljenePosete.map(x => {
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
            opis: x.opis
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
    }
  }
}
</script>

<style>

</style>