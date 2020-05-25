<template>
  <div>
    <v-data-table
      :items="_zahtevi"
      :headers="headers"
    >
      <template v-slot:top>
        <v-toolbar flat color="blue lighten-3">
          <v-toolbar-title>Neobrađeni zahtevi</v-toolbar-title>
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
            <v-text-field label="Ime i prezime" v-model="imePrezime"></v-text-field>
          </td>
          <td>
            <v-select label="Pozicija" v-model="pozicija" :items="['lekar', 'medicinska sestra']"></v-select>
          </td>
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
      <template v-slot:item.actions="{ item }">
        <v-btn
          color="green lighten-1"
          @click="odorbi(item.id)"
        >
          Odobri
        </v-btn>
        <v-btn
          color="red lighten-1"
          class="ml-2"
          @click="pripremiOdbijanje(item.id)"
        >
          Odbi
        </v-btn>
      </template>
    </v-data-table>
    <v-snackbar
      v-model="snackbar"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>
    <v-dialog v-model="dialog" max-width="500">
      <v-card>
        <v-card-title
          class="headline justify-center"
        >
          Odbijanje zateva za odsustvo
        </v-card-title>
        <v-card-text>
          <v-textarea
            label="Razlog odbijanja"
            outlined
            v-model="razlog"
          >
          </v-textarea>
        </v-card-text>
        <v-card-actions>
          <div class="mt-n10 ml-5">
            <v-btn
              color="green light-1"
              @click="odbi"
              :disabled="razlog==''"
            >
              Odbi
            </v-btn>
            <v-btn
              class="ml-3"
              color="red light-1"
              @click="zatvori"
            >
              Odustani
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
  name: 'NeobradjeniZahteviZaGodisnji',
  data: function(){
    return{
      dialog: false,
      idZahtevaZaOdbijanje: null,
      razlog: '',

      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null,

      imePrezime: '',
      pozicija: '',
      menu1: false,
      menu2: false,
      pocetak: null, //string
      pocetakDate: null, //date
      pocetakFormatted: null, //formated string
      
      kraj: null, //string
      krajDate: null, //date
      krajFormatted: null, //formatted string
      headers: [
        { text: 'Ime i prezime', value: 'imePrezime', sortable: true,
          filter: (value) => {
            if(!this.imePrezime)
              return true;
            return this.imePrezime.indexOf(value) != -1 || value.indexOf(this.imePrezime) != -1;
          }
        },
        { text: 'Pozicija', value: 'pozicija', sortable: true,
          filter: (value) => {
            if(!this.pozicija)
              return true;
            return this.pozicija == value;
          }
        },
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
      zahtevi: 'zahteviZaGodisnjiAdmin/getNeobradjeniZahteviZaGodisnji'
    }),
    _zahtevi(){
      if(this.zahtevi){
        return this.zahtevi.map(x => {
          return {
            id: x.id,
            imePrezime: `${x.radniKalendar.medicinskoOsoblje.ime} ${x.radniKalendar.medicinskoOsoblje.prezime}`,
            pozicija: x.radniKalendar.medicinskoOsoblje.pozicija,
            prviDanGodisnjeg: this.$utility.formatDate2(new Date(x.prviDanGodisnjeg)),
            poslednjiDanGodisnjeg: this.$utility.formatDate2(new Date(x.poslednjiDanGodisnjeg)),
          };
        });
      }else{
        return [];
      }
    }
  },
  methods: {
    ...mapActions({
      updateZahtev: 'zahteviZaGodisnjiAdmin/updateZahtev'
    }),
    formatDate(date){
      return this.$utility.formatDate2(date);
    },
    ponisti(){
      this.pocetak = null;
      this.kraj = null;
      this.pozicija = null;
      this.imePrezime = null;
    },
    pripremiOdbijanje(idZahteva){
      this.idZahtevaZaOdbijanje = idZahteva;
      this.dialog = true;
    },
    zatvori(){
      this.idZahtevaZaOdbijanje = null;
      this.razlog = '';
      this.dialog = false;
    },
    odorbi(idZahteva){
      let zahtev = this.zahtevi.filter(x => x.id == idZahteva)[0];
      zahtev.adminObradio = true;
      zahtev.odobreno = true;
      this.updateZahtev(zahtev).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    },
    odbi(){
      let idZahteva = this.idZahtevaZaOdbijanje
      let zahtev = this.zahtevi.filter(x => x.id == idZahteva)[0];
      zahtev.adminObradio = true;
      zahtev.odobreno = false;
      zahtev.razlogOdbijanja = this.razlog
      this.updateZahtev(zahtev).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
      this.zatvori();
    }
  }
}
</script>

<style>

</style>