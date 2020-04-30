<template>
  <v-container>
    Cao {{klinikaId}}
  </v-container>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  name: 'KlinikaPage',
  props: ['klinikaId'],
  methods: {
    ...mapMutations('klinike', [
      'setOdabranaKlinika'
    ]),
  },
  created() {
    // todo
    // pronadji kliniku
    // this.setOdabranaKlinika()
  }
}
</script>

<style>

</style>