<template>
  <div>
    <v-data-table
      :items="_neobradjeni"
      :headers="headers"
    >
      <template v-slot:top>
        <v-toolbar flat color="blue lighten-3">
          <v-toolbar-title>Zahtevi</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
          <v-btn @click="ponisti" color="orange lighten-1">Poništi filter</v-btn>
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
                <v-btn text color="red lighten-1" @click="menu1 = false">Odustani</v-btn>
                <v-btn text color="green lighten-1" @click="$refs.menu1.save(pocetak)">Potvrdi</v-btn>
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
                <v-btn text color="red lighten-1" @click="menu2 = false">Odustani</v-btn>
                <v-btn text color="green lighten-1" @click="$refs.menu2.save(kraj)">Potvrdi</v-btn>
              </v-date-picker>
            </v-menu>
          </td>
        </tr>
      </template>
      <template v-slot:item="{ item }">
        <tr :class="{
              'red lighten-3': !item.odobreno,
              'green lighten-3': item.odobreno,
              }">
          <td>{{item.prviDanGodisnjeg}}</td>
          <td>{{item.poslednjiDanGodisnjeg}}</td>
          <td>
            <span>
            <v-btn
              v-if="!item.odobreno"
              small
              color="primary"
              @click="detalji(item.id)"
            >
              Detalji
            </v-btn>
            <v-btn
              small
              :class="{'ml-2': !item.odobreno}"
              color="accent"
              @click="potvrdi(item.id)"
            >
              Potvrdi
            </v-btn>
            </span>
          </td>
        </tr>
      </template>
    </v-data-table>
    <v-dialog v-model="dialog" max-width="500">
      <v-card>
        <v-card-title
          class="headline justify-center"
        >
          Zahtev je odbijen
        </v-card-title>
        <v-card-text>
          <v-textarea
            label="Razlog odbijanja"
            outlined
            readonly
            v-model="razlog"
          >
          </v-textarea>
        </v-card-text>
        <v-card-actions>
          <div class="mt-n10 ml-5">
            <v-btn
              class="ml-3"
              color="red light-1"
              @click="zatvori"
            >
              Zatvori
            </v-btn>
          </div>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: 'TabelaNeobradjenihUpita',
  data: function(){
    return{
      dialog: false,
      razlog: '',

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
            let date = this.$utility.stringToDate2(value);
            if(!this.krajDate)
              return date.getTime() == this.pocetakDate.getTime();
            return date >= this.pocetakDate && date <= this.krajDate;
          }
        },
        { text: 'Poslednji dan odsustva', value: 'poslednjiDanGodisnjeg', sortable: false,
          filter: (value) => {
            if(!this.krajDate)
              return true;
            let date = this.$utility.stringToDate2(value);
            if(!this.pocetakDate)
              return date.getTime() == this.krajDate.getTime();
            return date >= this.pocetakDate && date <= this.krajDate;
          }
        },
        { text: 'Akcije', value: 'actions' }
      ]
    };
  },
  watch: {
    pocetak (newValue) {
      if(newValue == null){
        this.pocetakDate = null;
        this.pocetakFormatted = null;
      }else{
        this.pocetakDate = this.$utility.handleTimeZone(new Date(newValue));
        this.pocetakFormatted = this.formatDate(this.pocetakDate);
      }
    },
    kraj (newValue) {
      if(newValue == null){
        this.krajDate = null;
        this.krajFormatted = null;
      }else{
        this.krajDate = this.$utility.handleTimeZone(new Date(newValue));
        this.krajFormatted = this.formatDate(this.krajDate);
      }
    },
  },
  computed: {
    ...mapGetters({
      neobradjeni: 'zahteviZaGodisnjiOsoblje/getNeobradjeniZahteviZaGodisnji'
    }),
    _neobradjeni(){
      if(this.neobradjeni){
        return this.neobradjeni.map(x => {
          return {
            id: x.id,
            prviDanGodisnjeg: this.$utility.formatDate2(new Date(x.prviDanGodisnjeg)),
            poslednjiDanGodisnjeg: this.$utility.formatDate2(new Date(x.poslednjiDanGodisnjeg)),
            odobreno: x.odobreno
          };
        });
      }else{
        return [];
      }
    }
  },
  methods: {
    ...mapActions({
      obradiOsoblje: 'zahteviZaGodisnjiOsoblje/obradiOsoblje'
    }),
    formatDate(date){
      return this.$utility.formatDate2(date);
    },
    ponisti(){
      this.pocetak = null;
      this.kraj = null;
    },
    potvrdi(idZahteva){
      this.obradiOsoblje(idZahteva);
    },
    detalji(idZahteva){
      let zahtev = this.neobradjeni.filter(x => x.id == idZahteva)[0];
      this.razlog = zahtev.razlogOdbijanja;
      this.dialog = true;
    },
    zatvori(){
      this.razlog = '';
      this.dialog = false;
    }
  }
}
</script>

<style>

</style>