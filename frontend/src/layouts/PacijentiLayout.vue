<template>
  <v-app v-if="userType != null">
     <v-navigation-drawer
      v-model="drawer"
      app>
      <OpcijeKorisnika :userType="userType.role"></OpcijeKorisnika>
    </v-navigation-drawer>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>
        Klinika - Pacijent dashboard
      </v-toolbar-title>
    </v-app-bar>
    <v-content>
      <router-view :userType='userType.role'></router-view>
    </v-content>
  </v-app>
</template>

<script>
import {mapGetters} from 'vuex';
import OpcijeKorisnika from '@/components/user_options/OpcijeKorisnika';


export default {
  name: 'PacijentiLayout',
  components: {
    OpcijeKorisnika
  },
  data: function(){
    return {
      drawer: false,
    }
  },
  computed: mapGetters({
    'userType': 'korisnici/getKorisnik'
  })
}
</script>

<style>

</style>