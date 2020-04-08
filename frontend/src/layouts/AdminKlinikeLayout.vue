<template>
  <v-app>
    <v-navigation-drawer
      v-model="drawer"
      app>
      
      <OpcijeKorisnika :userType="userType"></OpcijeKorisnika>
      
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
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
import OpcijeKorisnika from '../components/user_options/OpcijeKorisnika';
export default {
  name: "AdminKlinikeLayout",
  components: {
    OpcijeKorisnika
  },
  data: function(){
    return {
      drawer: false,
      userType: "adminKlinike"
    }
  },
  
}
</script>

<style>

</style>