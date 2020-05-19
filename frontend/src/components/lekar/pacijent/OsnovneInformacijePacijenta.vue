<template>
  <v-card>
    <v-card-title>Osnovne informacije</v-card-title>
    <v-card-text>
      <v-text-field v-for="elem in _info" :key="elem.id" readonly v-model="elem.value" :label="elem.label"></v-text-field>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  name: "OsnovneInformacijePacijenta",
  computed: {
    ...mapGetters({
      pacijent: "pacijenti/getOdabraniPacijent",
    }),
    _info(){
      let retval = [];
      retval.push({id: 1, value: this.pacijent.ime, label: "Ime"});
      retval.push({id: 2, value: this.pacijent.prezime, label: "Prezime"});
      retval.push({id: 3, value: this.pacijent.jbo, label: "Jedinstveni broj osiguranika"});
      retval.push({id: 4, value: this.pacijent.brojTelefona, label: "Broj telefona"});
      retval.push({id: 5, value: this.pacijent.grad, label: "Grad"});
      retval.push({id: 6, value: this.pacijent.drzava, label: "Drzava"});
      retval.push({id: 7, value: this.pacijent.adresa, label: "Adresa"});
      return retval;
    }
  }
}
</script>

<style>

</style>