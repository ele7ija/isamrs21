<template>
  <div>
    <v-navigation-drawer
      v-model="drawer"
      app>
      <AdminKlinikaOptions v-if="true"/>
      
      <template v-slot:append>
        <div class="pa-2">
          <v-btn block dark>Logout</v-btn>
        </div>
      </template>
    </v-navigation-drawer>
  </div>
</template>

<script>
import AdminKlinikaOptions from './AdminKlinikaOptions'
export default {
  name: "OptionToolbar",
  props: [
    "drawer"
  ],
  components: {
    AdminKlinikaOptions
  }
  /* TODO: Napuniti toolbar sa opcijama na osnovu tipa ulogovanog korisnika
           To se moze uraditi tako sto cepamo v-if u zavisnosti od tipa ulogovanog korisnika(kojeg dobijamo iz korisnik.js modula)
           ispred svakog v-list taga
           A moze mozda i na neki pametniji nacin
           Za sada hardkodovana jedna komponenta

  */
  
}
</script>

<style>

</style>