<template>
  <v-card>
    <v-card-title class="justify-center headline">Zdravstveni karton</v-card-title>
    <v-card-text>
      <TabelaPoseta />
    </v-card-text>
  </v-card>
</template>

<script>
import TabelaPoseta from './TabelaPoseta';
export default {
  name: "ZdravstveniKarton",
  components: {
    TabelaPoseta
  }
}
</script>

<style>

</style>