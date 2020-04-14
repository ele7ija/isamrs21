<!-- Login page -->

<template>
  <div>
    Sadrzaj dala Login.vue komponenta. Ovo je homepage za neulog. korisnika <br>
    <v-btn 
      to="/registracija">
      Registracija
    </v-btn> <br> 
    <v-btn 
      @click.native='setGlobalLayout("pacijenti-layout")' 
      to="/pacijenti">
      Uloguj se kao pacijent
    </v-btn> <br> 
    <v-btn 
      @click.native='setGlobalLayout("admin-klinike-layout")' 
      to="/home/adminKlinike">Uloguj se kao admin klinike
    </v-btn> <br>
  </div>
</template>

<script>
import {mapMutations} from 'vuex'

export default {
  name: "Login",
  methods: {
    ...mapMutations([
      'setGlobalLayout'
    ])
  }
}
</script>

<style>

</style>