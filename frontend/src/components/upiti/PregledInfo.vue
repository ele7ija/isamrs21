<template>
  <div>
    <v-subheader>Možete da definišete popust za pregled, kao i da definišete dodatne lekare ukoliko je u pitanju operacija. Ukoliko lekar ne može da izvrši ovaj pregled, morate ga promeniti.</v-subheader>
    <v-form v-model="isFormValid" ref="form">
      <v-container fluid>
        <v-row>
          <v-col cols=12 md="6">
            <v-text-field v-model="upit.tipPregleda.text" label="Tip pregleda" readonly></v-text-field>
          </v-col>
          <v-col cols=12 md="6">
            <v-text-field v-model="upit.vrsta" label="Vrsta pregleda" readonly></v-text-field>
          </v-col>
          <v-col cols=12 :md="isLekarValid ? 6 : 4">
            <v-text-field v-model="upit.lekar.text" label="Lekar" readonly :rules="lekarRule" persistent-hint ></v-text-field>
          </v-col>
          <v-col v-if="!isLekarValid" cols=12 md="2">
            <v-btn color="primary" class="mt-3" @click="dialog1=true">Promeni lekara</v-btn>
          </v-col>
          <v-col cols=12 md="6" v-if="upit.sala">
            <v-text-field v-model="upit.sala.text" label="Sala" readonly></v-text-field>
          </v-col>
          <v-col cols=12 md="6">
            <v-text-field v-model="upit._pocetak" label="Početak pregleda" readonly></v-text-field>
          </v-col>
          <v-col cols=12 md="6">
            <v-text-field v-model="upit._kraj" label="Kraj pregleda" readonly></v-text-field>
          </v-col>
          <v-col cols=12 md="4">
            <v-text-field type="number" v-model="upit.cena" label="Osnovna cena" readonly></v-text-field>
          </v-col>
          <v-col cols=12 md="4">
            <v-slider
              v-model="popust"
              label="Popust u procentima"
              min="0"
              max="99"
              thumb-label
              class="mt-5"
            ></v-slider>
          </v-col>
          <v-col cols=12 md="4">
            <p class="mt-6 ml-4">Cena: {{ukupnaCena}}</p>
          </v-col>
        </v-row>
        <v-btn color="primary" @click="add()" :disabled="!isFormValid">Odobri upit</v-btn>
      </v-container>
    </v-form>
    <v-dialog v-model="dialog1" max-width="1300px" @input="close">
      <v-card>
        <TabelaLekara :upit="upit" :slobodniLekari="slobodniLekari" @setDates="setDates" @setLekar="setLekar" @close="close"></TabelaLekara>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import TabelaLekara from './TabelaLekara';
import {mapGetters} from 'vuex';
export default {
  name: "PregledInfo",
  props: ["upit"],
  components: {
    TabelaLekara
  },
  data: function(){
    return {
      textFieldProps: {
        appendIcon: 'event'
      },
      popust: 0,
      isFormValid: true,
      dialog1: false
    };
  },
  mounted(){
    this.$refs.form.validate();
    console.log("mountujem");
  },
  computed: {
    ...mapGetters({
      pregledi: "preglediAdmin/getPreglediKlinike",
      lekari: "osoblje/getLekari"
    }),
    ukupnaCena(){
      return this.upit.cena - 0.01 * this.popust * this.upit.cena;
    },
    isLekarValid(){
      let lekar = this.upit.lekar.value;
      return this.isLekarValidMethod(lekar);
    },
    slobodniLekari(){
      let retval = [];
      for(let lekar of this.lekari){
        if(this.isLekarValidMethod(lekar)){
          retval.push(lekar);
        }
      }
      return retval;
    },
    notEmptyRule: function(){
      const rules = [];
      const rule1 = v => !!v || 'Morate popuniti ovo polje';
      rules.push(rule1);
      return rules;
    },
    lekarRule: function(){
      const rules = [];
      const rule1 = v => this.isLekarValid || `Lekar ${v} ne može da izvrši ovaj pregled`;
      rules.push(rule1);
      return rules;
    }
  },
  methods: {
    isLekarValidMethod(lekar){
      let tipPregleda = this.upit.tipPregleda.value;

      //proveri da li je lekar specijalizovan za ovaj tip pregleda
      if(lekar.tipovi_pregleda.filter(x => x.id == tipPregleda.id).length == 0)
        return false;
      
      //proveri da li je lekar slobodan za ovo vreme(u odnosu na pregled)
      let preglediLekara = this.pregledi.filter(x => x.lekar.id == lekar.id);
      for(let pregled of  preglediLekara){
        let start = this.upit.pocetak;
        let end = this.upit.kraj;
        let start2 = new Date(pregled.pocetakPregleda);
        let end2 = new Date(pregled.krajPregleda);
        if((start.getTime() <= start2.getTime() && start2.getTime() <= end.getTime())
          || (start2.getTime() <= start && start <= end2.getTime())){
            return false;
        }
      }

      //proveri da li je lekar slobodan za ovo vreme(u odnosu na dodatne operacije)
      let dodatneOperacijeLekara = this.pregledi.filter(x => x.dodatniLekari.filter(y => y.id == lekar.id).length != 0);
      for(let pregled of  dodatneOperacijeLekara){
        let start = this.upit.pocetak;
        let end = this.upit.kraj;
        let start2 = new Date(pregled.pocetakPregleda);
        let end2 = new Date(pregled.krajPregleda);
        if((start.getTime() <= start2.getTime() && start2.getTime() <= end.getTime())
          || (start2.getTime() <= start && start <= end2.getTime())){
            return false;
        }
      }
      
      return true;
    },
    setDates(newValue){
      this.$emit('setDates', {pocetak: newValue, refresh: false});
    },
    setLekar(newValue){
      this.$emit('setLekar', newValue);
      this.dialog1 = false;
    },
    close(){
      this.$emit('resetDates');
      this.dialog1 = false;
    }
  }
}
</script>

<style>

</style>