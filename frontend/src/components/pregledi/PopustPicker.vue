<template>
  <v-container fluid>
    <v-row>
      <v-col cols=12 md="4">
        <v-text-field type="number" v-model="osnovnaCena" label="Osnovna cena" disabled></v-text-field>
      </v-col>
      <v-col cols=12 md="4">
        <v-text-field type="number" v-model="popust" label="Popust u procentima" :rules="notEmptyRule" :min="0" :max="99"></v-text-field>
      </v-col>
      <v-col cols=12 md="4">
        Cena: {{ukupnaCena}}
      </v-col>
    </v-row>
    <v-btn color="primary" @click="add()">Add</v-btn>
    <v-btn text @click="previous()">Previous step</v-btn>
  </v-container>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: "PopustPicker",
  props: ["index", "currentIndex"],
  data: function(){
    return{
      popust: 0
    };
  },
  computed:{
    ...mapGetters({
      odabraniTipPregleda: "pregledDialog/getTipPregleda",
    }),
    osnovnaCena: function(){
      if(this.index == this.currentIndex){
        return this.odabraniTipPregleda.cenovnik.iznosUDinarima;
      }else{
        return null;
      }
    },
    ukupnaCena: function(){
      if(this.index == this.currentIndex){
        return this.osnovnaCena - this.popust * this.osnovnaCena / 100;
      }else{
        return null;
      }
    },
    notEmptyRule: () => {
      const rules = [];
      const rule1 = v => !!v || `Ovo polje mora imati vrednost.`;
      rules.push(rule1);
      return rules;
    },
  },
  methods:{
    add(){
      this.$store.commit("pregledDialog/setCena", this.osnovnaCena);
      this.$store.commit("pregledDialog/setPopust", this.popust);
      this.$store.commit("pregledDialog/setKonacnaCena", this.ukupnaCena);

      //TODO emituj akciju na bek

      this.$emit('reset'); //zatvori dijalog
    },
    previous(){
      this.$emit('previousStep');
    }
  }
}
</script>

<style>

</style>