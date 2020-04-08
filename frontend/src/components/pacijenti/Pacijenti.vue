<template>
  <v-container fluid>
    <v-list>
      <v-list-item v-for="pacijent in getAll"
        :key='pacijent.id'>
        {{pacijent.ime}} {{pacijent.prezime}}
      </v-list-item>
    </v-list>
  </v-container>
</template>

<script>
import {mapGetters, mapActions} from 'vuex'

export default {
  name: 'Pacijenti',
  computed: mapGetters({
    getAll: 'pacijenti/getPacijenti'
  }),
  created() {
    this.loadPacijenti()
  },
  methods: {
    ...mapActions({
      loadPacijenti: 'pacijenti/loadPacijenti'
    })
  }
}
</script>

<style>

</style>