<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12" :md="_md">
        <OsnovneInformacijePacijenta/>
      </v-col>
      <v-col v-if="_md==4" cols="12" md="8">
        <ZdravstveniKarton/>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import ZdravstveniKarton from './ZdravstveniKarton';
import OsnovneInformacijePacijenta from './OsnovneInformacijePacijenta';
export default {
  name: "ProfilPacijenta",
  components: {
    ZdravstveniKarton,
    OsnovneInformacijePacijenta
  },
  created(){
    this.checkLekarovPacijent();
  },
  computed: {
    ...mapGetters({
      pacijent: "pacijenti/getOdabraniPacijent",
      lekarovPacijent: "pacijenti/isLekarovPacijent"
    }),
    _md(){
      return this.lekarovPacijent ? 4 : 12;
    }
  },
  methods: {
    ...mapActions({
      checkLekarovPacijent: 'pacijenti/checkLekarovPacijent'
    })
  }
}
</script>

<style>

</style>