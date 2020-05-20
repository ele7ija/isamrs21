<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="_sale"
      :search="search"
      class="elevation-1"
      :no-data-text="'Nema sala u klinici.'"
      show-select
      single-select
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Sale</v-toolbar-title>
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
              v-model="_kraj"
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
          <v-btn class="my-4 mx-2" color="primary" @click="promenaDatuma=true" v-if="slobodneSale.length==0">Promena datuma</v-btn>
        </v-toolbar>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-btn
          small
          class="mr-2"
          @click="showPreglediSale(item)"
        >
          Pregledi
        </v-btn>
      </template>
      <template v-slot:item.data-table-select="{ item }">
        <v-simple-checkbox v-if="item.slobodna=='da'" v-model="item.selected" @input="rowSelect(item)" ></v-simple-checkbox>
      </template>
    </v-data-table>
    <v-btn class="my-4 mx-2" color="primary" @click="incStep()" :disabled="!btnEnabled || slobodneSale.length==0">Dalje</v-btn>
    <v-dialog v-model="dialog1">
      <Pregledi :pregledi="preglediSale"/>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import Pregledi from './Pregledi';
export default {
  name: "TabelaSala",
  props: ["upit", "slobodneSale"],
  components: {
    Pregledi
  },
  data: function(){
    return{
      textFieldProps: {
        appendIcon: 'event'
      },
      promenaDatuma: false,
      btnEnabled: false,
      selected: [],
      headers: [
        {
          text: 'Oznaka',
          value: 'oznaka',
          sortable: true,
        },
        {
          text: 'Slobodna',
          value: 'slobodna',
          sortable: true,
        },
        { 
          text: 'Actions',
          value: 'actions',
          sortable: false,
        }
      ],
      dialog1: false,
      search: '',
      preglediSale: {
        sala: null,
        _pregledi: [],
        pocetak: null,
        kraj: null,
        title: null,
        subtitle: null
      }
    };
  },
  computed: {
    ...mapGetters(
      {
        sale: 'sale/getSale',
        pregledi: 'preglediAdmin/getPreglediKlinike',
        osobljeKlinike: 'osoblje/getMedicinskoOsoblje',
        tipoviPregledaKlinike: 'tipoviPregleda/getTipoviPregleda'
      }
    ),
    _pocetak: {
      get(){
        return this.upit.pocetak;
      },
      set(newValue){
        this.$emit("setDates", {pocetak: newValue, refresh: true});
      }
    },
    _kraj: {
      get(){
        return this.upit.kraj;
      },
      set(newValue){
        console.log(newValue);
      }
    },
    _sale(){
      let retval = [];
      for(let sala of this.sale){
        let index = this.selected.findIndex(x => x.id == sala.id);
        let selected = false;
        if(index != -1){
          selected = this.selected[index].selected;
        }
        let temp = {
          id: sala.id,
          oznaka: sala.oznaka,
          selected: selected
        };
        if(this.slobodneSale.filter(x => x.id == sala.id).length == 1){          
          temp.slobodna = 'da';
        }else{
          temp.slobodna = 'ne';
        }
        retval.push(temp);
      }
      return retval;
    }
  },
  methods: {
    showPreglediSale(sala){
      this.preglediSale.sala = sala;
      this.preglediSale._pregledi = this.pregledi.filter(x => x.sala.id == sala.id);
      this.preglediSale._pregledi = this.preglediSale._pregledi.map(x => {
        let date1 = x.pocetakPregleda;
        let date2 = x.krajPregleda;
        let retval = {
          id: x.id,
          version: x.version,
          datum: this.$utility.formatDate2(date1),
          pocetak: this.$utility.formatDate3(date1),
          kraj: this.$utility.formatDate3(date2),
          pocetak_date: x.pocetakPregleda,
          kraj_date: x.krajPregleda,
          lekar: `${x.lekar.ime} ${x.lekar.prezime}`,
          tipPregleda: x.tipPregleda.naziv,
          vrsta: x.tipPregleda.vrsta,
          konacnaCena: parseInt(x.konacnaCena, 10),
        };
        let pacijent = this.getPacijent(x);
        if(pacijent != null){
          retval.pacijent = pacijent;
        }
        return retval;
      });
      this.preglediSale.pocetak = this.upit.pocetak;
      this.preglediSale.kraj = this.upit.kraj;
      this.preglediSale.title = `Kalendar pregleda sale: ${sala.oznaka}`;
      this.preglediSale.subtitle = "Crvenom bojom su označeni pregledi zbog kojih sala nije slobodna za datum iz upita";
      this.dialog1 = true;
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
    incStep(){
      let selectedSalaId = this.selected.filter(x => x.selected)[0].id;
      let selectedSala = this.sale.filter(x => x.id == selectedSalaId)[0];
      this.$emit("incStep", selectedSala);
    }
  }
}
</script>

<style>
.datetime-picker{
  margin-top: 1.3rem;
  margin-right: 1rem;
}
.datetime-picker{
  margin-top: 1.3rem;
  margin-right: 1rem;
}
</style>