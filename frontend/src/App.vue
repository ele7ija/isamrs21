<template>
  <v-app id="app">
    <component v-bind:is="globalLayout"></component>
  </v-app>
</template>

<script>
import AdminKlinikeLayout from '@/layouts/AdminKlinikeLayout';
import NeulogovanKorisnikLayout from '@/layouts/NeulogovanKorisnikLayout';
import {mapState} from 'vuex';
import PacijentiLayout from '@/layouts/PacijentiLayout.vue';

export default {
  name: 'App',
  computed: {
    ...mapState([
      ['globalLayout']
    ])
  },
  components: {
    'admin-klinike-layout': AdminKlinikeLayout,
    'neulogovani-korisnik-layout': NeulogovanKorisnikLayout,
    'pacijent-layout': PacijentiLayout
    // Mesto za nove layoute
  }
}
</script>

<style>

</style>
