<template>
  <v-app v-if="userType.role != null">
    <!-- ovo je dashboard -->
    <v-navigation-drawer
      v-model="drawer"
      app>
      <h1>ovo je dashboard</h1>
      <OpcijeKorisnika :userType="userType.role"></OpcijeKorisnika>
      
    </v-navigation-drawer>

    <v-app-bar  app color="indigo" dark >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>Administrator kliničkog centra</v-toolbar-title>
    </v-app-bar>
    
    <!-- ovo je centralni meni -->
    <v-content>
      <router-view :userType="userType.role"></router-view>
    </v-content>
  </v-app>
</template>

<script>
import OpcijeKorisnika from '../components/user_options/OpcijeKorisnika';
import {mapGetters} from 'vuex';
//import {mapActions} from 'vuex';
export default {
  name: "AdminKlinikeLayout",
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

/*
//djuricevo: 

  methods: {
    ...mapActions({
      fetchKlinikaUlogovanogKorisnika: 'klinike/fetchKlinikaUlogovanogKorisnika'
    })
  },
  created(){
    this.fetchKlinikaUlogovanogKorisnika();
  }
*/
}
</script>

<style>

</style>