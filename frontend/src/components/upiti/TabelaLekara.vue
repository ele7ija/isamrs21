<template>
  <v-card-text class="pt-5">
    <v-data-table
      :headers="headers"
      :items="_lekari"
      :search="search"
      class="elevation-1"
      :no-data-text="'Nema lekara u klinici.'"
      show-select
      single-select
      show-expand
      single-expand
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Lekari</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
          
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
          <v-spacer></v-spacer>
          <span class="datetime-picker">
            <v-datetime-picker
              v-model="_pocetak"
              label="Početak pregleda"
              dateFormat="dd.MM.yyyy"
              :textFieldProps="textFieldProps"
              :disabled="!promenaDatuma"
            >
              <template slot="actions" slot-scope="{ parent }">
                <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
              </template>
            </v-datetime-picker>
          </span>
          <span class="datetime-picker">
            <v-datetime-picker
              v-model="upit.kraj"
              label="Kraj pregleda"
              dateFormat="dd.MM.yyyy"
              :textFieldProps="textFieldProps"
              disabled
            >
              <template slot="actions" slot-scope="{ parent }">
                <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
              </template>
            </v-datetime-picker>
          </span>
          <v-btn class="my-4 mx-2" color="primary" @click="promenaDatuma=true" v-if="slobodniLekari.length==0">Promena datuma</v-btn>
        </v-toolbar>
      </template>
      <template v-slot:item.data-table-select="{ item }">
        <v-simple-checkbox v-if="item.slobodan=='da'" v-model="item.selected" @input="rowSelect(item)" ></v-simple-checkbox>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <span v-if="isSpecijalizovan(item, upit.tipPregleda.value.id)">
            <Pregledi :pregledi="getPreglediLekara(item)" class="my-5" />
            <ZahteviZaOdsustvo :zahtevi="getZahteviZaOdsustvo(item)" class="my-5" />
          </span>
          <span v-else>
            Lekar nije specijalizovan za ovaj tip pregleda
          </span>
        </td>
      </template>
    </v-data-table>
    <v-btn class="my-4 mx-2" color="primary" @click="commit()" :disabled="!btnEnabled || slobodniLekari.length==0">Potvrdi</v-btn>
    <v-btn class="my-4 mx-2" @click="close()">Odustani</v-btn>
  </v-card-text>
</template>

<script>
import { mapGetters } from 'vuex';
import Pregledi from "./Pregledi";
import ZahteviZaOdsustvo from "./ZahteviZaOdsustvo";
export default {
  name: 'TabelaLekara',
  props: ["upit", "slobodniLekari"],
  components: {
    Pregledi,
    ZahteviZaOdsustvo
  },
  data: function(){
    return {
      search: '',
      textFieldProps: {
        appendIcon: 'event'
      },
      promenaDatuma: false,
      btnEnabled: false,
      selected: [],
      headers: [
        {
          text: 'Ime',
          value: 'ime',
          sortable: true,

        },
        {
          text: 'Prezime',
          value: 'prezime',
          sortable: false
        },
        {
          text: 'E-mail',
          value: 'email',
          sortable: true,
        },
        {
          text: 'Slobodan',
          value: 'slobodan',
          sortable: true,
        },
        {
          text: 'Detalji',
          value: 'data-table-expand'
        }
      ],
    };
  },
  computed: {
    ...mapGetters({
      lekari: "osoblje/getLekari",
      pregledi: "preglediAdmin/getPreglediKlinike",
      zahtevi: "zahteviZaGodisnjiAdmin/getAllZahtevi"
    }),
    _pocetak: {
      get(){
        return this.upit.pocetak;
      },
      set(newValue){
        this.$emit("setDates", newValue);
      }
    },
    _lekari(){
      let retval = [];
      for(let lekar of this.lekari){
        let index = this.selected.findIndex(x => x.id == lekar.id);
        let selected = false;
        if(index != -1){
          selected = this.selected[index].selected;
        }
        let temp = {
          id: lekar.id,
          ime: lekar.ime,
          prezime: lekar.prezime,
          email: lekar.email,
          selected: selected,
          tipovi_pregleda: lekar.tipovi_pregleda
        };
        if(this.slobodniLekari.filter(x => x.id == lekar.id).length == 1){          
          temp.slobodan = 'da';
        }else{
          temp.slobodan = 'ne';
        }
        retval.push(temp);
      }
      return retval;
    },
  },
  methods: {
    rowSelect: function (item) {
      let index = this.selected.findIndex(x => x.id == item.id);
      if(index == -1){
        this.selected.push({id: item.id, selected: false});
        index = this.selected.length - 1;
      }
      if(item.selected){
        for(let sel of this.selected)
          sel.selected = false;
        this.btnEnabled = true;
        this.selected[index].selected = true;
      }else{
        this.btnEnabled = false;
        this.selected[index].selected = false;
      }
    },
    commit(){
      let selectedLekarId = this.selected.filter(x => x.selected)[0].id;
      let selectedLekar = this.lekari.filter(x => x.id == selectedLekarId)[0];
      this.$emit("setLekar", selectedLekar);
    },
    close(){
      this.$emit("close");
    },
    getPreglediLekara(lekar){
      let retval = {};
      retval.lekar = lekar;
      retval._pregledi = this.pregledi.filter(x => x.lekar.id == lekar.id);
      retval._pregledi = retval._pregledi.map(x => {
        let date1 = x.pocetakPregleda;
        let date2 = x.krajPregleda;
        let obj = {
          id: x.id,
          datum: this.$utility.formatDate2(date1),
          pocetak: this.$utility.formatDate3(date1),
          kraj: this.$utility.formatDate3(date2),
          pocetak_date: x.pocetakPregleda,
          kraj_date: x.krajPregleda,
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          vrsta: x.tipPregleda.vrsta,
          tipPregleda: x.tipPregleda.naziv,
          konacnaCena: parseInt(x.konacnaCena, 10),
        };
        let pacijent = this.getPacijent(x);
        if(pacijent != null){
          obj.pacijent = pacijent;
        }else{
          obj.pacijent = "/";
        }
        return obj;
      })
      retval.pocetak = this.upit.pocetak;
      retval.kraj = this.upit.kraj;
      retval.title = `Kalendar lekara: ${lekar.ime} ${lekar.prezime}`;
      retval.subtitle = "Crvenom bojom su označeni pregledi zbog kojih lekar nije slobodan za datum iz upita";
      return retval;
    },
    getZahteviZaOdsustvo(lekar){
      let retval = {};
      retval.lekar = lekar;
      retval._zahtevi = this.zahtevi.filter(x => x.radniKalendar.medicinskoOsoblje.id == lekar.id && x.odobreno == true);
      retval._zahtevi = retval._zahtevi.map(x => {
        let date1 = x.prviDanGodisnjeg;
        let date2 = x.poslednjiDanGodisnjeg;
        let obj = {
          id: x.id,
          prviDanGodisnjeg: this.$utility.formatDate2(date1),
          poslednjiDanGodisnjeg: this.$utility.formatDate2(date2),
        };
        return obj;
      })
      retval.pocetak = this.upit.pocetak;
      retval.kraj = this.upit.kraj;
      retval.title = `Termini odsustva lekara: ${lekar.ime} ${lekar.prezime}`;
      retval.subtitle = "Crvenom bojom su označena odsustva zbog kojih lekar nije slobodan za datum iz upita";
      return retval;
    },
    getPacijent(x){
      /*
      x - instanca pregleda
      returns - ime i prezime pacijentaukoliko je pregled rezervisan, null inace 
      */
      if(x.poseta != null)
        return `${x.poseta.zdravstveniKarton.pacijent.ime} ${x.poseta.zdravstveniKarton.pacijent.prezime}`;
      for(let upit of x.upiti){
        if(upit.potvrdjen){
          return `${upit.pacijent.ime} ${upit.pacijent.prezime}`;
        }
        if(upit.odobren && !upit.pacijentObradio){
          return `${upit.pacijent.ime} ${upit.pacijent.prezime}`;
        }
      }
      return null;
    },
    isSpecijalizovan(lekar, idTipaPregleda){
      return lekar.tipovi_pregleda.filter(x => x.id == idTipaPregleda).length == 1;
    }
  },
}
</script>

<style>

</style>