<template>
  <v-app id="app">
    <component v-bind:is="globalLayout"></component>
  </v-app>
</template>

<script>
import AdminKlinikeLayout from '@/layouts/AdminKlinikeLayout';
import NeulogovanKorisnikLayout from '@/layouts/NeulogovanKorisnikLayout';
import {mapGetters} from 'vuex';
import PacijentiLayout from '@/layouts/PacijentiLayout.vue';
import AdminKlinickogCentraLayout from '@/layouts/AdminKlinickogCentraLayout.vue';

export default {
  name: 'App',
  computed: mapGetters({globalLayout: 'layout/getGlobalLayout'}),
  components: {
    'admin-klinike-layout': AdminKlinikeLayout,
    'neulogovani-korisnik-layout': NeulogovanKorisnikLayout,
    'pacijent-layout': PacijentiLayout,
    'admin-klinickog-centra-layout': AdminKlinickogCentraLayout,
    // Mesto za nove layoute
  }
}
</script>

<style>

</style>
