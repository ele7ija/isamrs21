<template>
  <v-app v-if="userType.role != null">
    <v-navigation-drawer
      v-model="drawer"
      app>
      
      <OpcijeKorisnika :userType="userType.role"></OpcijeKorisnika>
      
    </v-navigation-drawer>
      <v-app-bar
      app
      color="indigo"
      dark
    >
        <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
        <v-toolbar-title>Klinika menadzer</v-toolbar-title>
      </v-app-bar>
    <v-content>
      <router-view :userType="userType.role"></router-view>
    </v-content>
  </v-app>
</template>

<script>
import OpcijeKorisnika from '../components/user_options/OpcijeKorisnika';
import {mapGetters, mapActions} from 'vuex';
export default {
  name: "LekarLayout",
  components: {
    OpcijeKorisnika
  },
  data: function(){
    return {
      drawer: false,
    }
  },
  computed: mapGetters({
    'userType': 'korisnici/getKorisnik',
  }),
  methods: {
    ...mapActions({
      fetchKlinikaUlogovanogKorisnika: 'klinike/fetchKlinikaUlogovanogKorisnika'
    })
  },
  created(){
    this.fetchKlinikaUlogovanogKorisnika();
  }
  
}
</script>

<style>

</style>