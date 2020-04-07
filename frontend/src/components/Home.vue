<!-- Home page za neulog. korisnika -->

<template>
  <div>
    Sadrzaj dala Home.vue komponenta. Ovo je homepage za neulog. korisnika <br>
    <v-btn to="/registracija">Registracija</v-btn> Ne radi jos<br> 
    <v-btn 
      @click.native='setGlobalLayout("pacijenti-layout")' 
      to="/pacijenti">
      Uloguj se kao pacijent
    </v-btn> <br> 

    <!-- ovde ne treba da bude /home vec home page za admina klinike -->
    <v-btn 
      @click.native='setGlobalLayout("admin-klinike-layout")' 
      to="/home">Uloguj se kao admin klinike
    </v-btn> <br>
  </div>
</template>

<script>
import {mapMutations} from 'vuex'

export default {
  name: "Home",
  methods: {
    ...mapMutations([
      'setGlobalLayout'
    ])
  }
}
</script>

<style>

</style>