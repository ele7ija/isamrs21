<template>
  <div>
    <ProfilAdminaKlinike v-if="role=='admin-klinike'"></ProfilAdminaKlinike>
    <ProfilAdminaKlinickogCentra v-if="role=='admin-klinickog-centra'"></ProfilAdminaKlinickogCentra>
    <ProfilLekara v-if="role=='lekar'"></ProfilLekara>
    <ProfilMedSestre v-if="role=='medicinska-sestra'"/>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import ProfilAdminaKlinike from './ProfilAdminaKlinike';
import ProfilAdminaKlinickogCentra from './ProfilAdminaKlinickogCentra';
import ProfilLekara from './ProfilLekara';
import ProfilMedSestre from './ProfilMedSestre'
export default {
  name: 'Profil',
  components: {
    ProfilAdminaKlinike,
    ProfilAdminaKlinickogCentra,
    ProfilLekara,
    ProfilMedSestre,
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