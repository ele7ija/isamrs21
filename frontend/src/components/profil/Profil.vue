<template>
  <div>
    <ProfilAdminaKlinike v-if="role=='admin-klinike'"></ProfilAdminaKlinike>
    <ProfilAdminaKlinickogCentra v-if="role=='admin-klinickog-centra'"></ProfilAdminaKlinickogCentra>
    <ProfilLekara v-if="role=='lekar'"></ProfilLekara>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import ProfilAdminaKlinike from './ProfilAdminaKlinike';
import ProfilAdminaKlinickogCentra from './ProfilAdminaKlinickogCentra';
import ProfilLekara from './ProfilLekara';
export default {
  name: 'Profil',
  components: {
    ProfilAdminaKlinike,
    ProfilAdminaKlinickogCentra,
    ProfilLekara
  },
  computed: {
    ...mapGetters({
      role: 'korisnici/getKorisnikRole'
    })
  }
}
</script>

<style>

</style>