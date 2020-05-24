<template>
  <v-data-table
    :items="_neobradjeni"
    :headers="headers"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Zahtevi</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-btn @click="ponisti" color="accent">Poništi filter</v-btn>
      </v-toolbar>
    </template>
    <template v-slot:body.prepend>
      <tr>
        <td>
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
        </td>
        <td>
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
            <v-date-picker v-model="kraj" no-title scrollable min="00010101T000000-0500">
              <v-spacer></v-spacer>
              <v-btn text color="decline" @click="menu2 = false">Odustani</v-btn>
              <v-btn text color="accept" @click="$refs.menu2.save(kraj)">Potvrdi</v-btn>
            </v-date-picker>
          </v-menu>
        </td>
      </tr>
    </template>
  </v-data-table>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: 'TabelaPoslatihUpita',
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
      headers: [
        { text: 'Prvi dan odsustva', value: 'prviDanGodisnjeg', sortable: false,
          filter: (value) => {
            if(!this.pocetakDate)
              return true;
            let date = this.$utility.stringToDate(value);
            if(!this.krajDate)
              return date.getTime() == this.pocetakDate.getTime();
            return date >= this.pocetakDate && date <= this.krajDate;
          }
        },
        { text: 'Poslednji dan odsustva', value: 'poslednjiDanGodisnjeg', sortable: false,
          filter: (value) => {
            if(!this.krajDate)
              return true;
            let date = this.$utility.stringToDate(value);
            if(!this.pocetakDate)
              return date.getTime() == this.krajDate.getTime();
            return date >= this.pocetakDate && date <= this.krajDate;
          }
        }
      ]
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
  computed: {
    ...mapGetters({
      neobradjeni: 'zahteviZaGodisnjiOsoblje/getPoslatiZahteviZaGodisnji'
    }),
    _neobradjeni(){
      if(this.neobradjeni){
        return this.neobradjeni.map(x => {
          return {
            id: x.id,
            prviDanGodisnjeg: this.$utility.formatDate(new Date(x.prviDanGodisnjeg)),
            poslednjiDanGodisnjeg: this.$utility.formatDate(new Date(x.poslednjiDanGodisnjeg))
          };
        });
      }else{
        return [];
      }
    }
  },
  methods: {
    formatDate(date){
      return date.toLocaleDateString('sr');
    },
    ponisti(){
      this.pocetak = null;
      this.kraj = null;
    }
  }
}
</script>

<style>

</style>