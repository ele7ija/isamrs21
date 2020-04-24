<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
    >
      <router-link to='/login'>
        <v-toolbar-title class='white--text font-weight-medium pl-4'>
            <v-icon color="white" class='pr-4'>mdi-hospital-building</v-icon>
            <span>Klinika 'Asdf'</span>
        </v-toolbar-title>
      </router-link>
      <v-spacer></v-spacer>
      <router-link to='/registracija'>
        <v-btn text class='white--text font-weight-medium'>
          Registracija
        </v-btn>
      </router-link>
      <router-link to='/o-nama'>
        <v-btn text class='white--text font-weight-medium'>
          O nama
        </v-btn>
      </router-link>
    </v-app-bar>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: 'NeulogovanKorisnikLayout'
}
</script>

<style>

</style>