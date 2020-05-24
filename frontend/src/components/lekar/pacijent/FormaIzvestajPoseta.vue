<template>
<div >
  <v-btn 
  color="primary" 
  @click="zapocni()" 
  :disabled="!pregledMozeDaSeZapocne(this.posetaId)">
  Započni pregled
  </v-btn>


  <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
  <v-card>
    <v-toolbar dark color="primary">
      <v-btn icon dark @click="dialog = false">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>Settings</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items>
        <v-btn dark text @click="dialog = false">Save</v-btn>
      </v-toolbar-items>
    </v-toolbar>

    <v-card>
      {{posetaId}}
    </v-card>
  </v-card>
  </v-dialog>

</div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FormaIzvestajPregleda',
  props: ["posetaId"],
  data () {
    return {
      dialog: false,
    }
  },

  computed: {
        ...mapGetters({
      pregledMozeDaSeZapocne: 'pacijenti/pregledMozeDaSeZapocne'
    }),
  },

  methods: {
    zapocni(){
      this.dialog = true;
      console.log("pregled poceo");
    }
  }
}
</script>

<style>

</style>