<template>
  <v-container>
    <div v-for='poseta in posete'
      :key='poseta.id'>
      {{poseta.id}}
    </div>
  </v-container>
</template>

<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'Istorija',
  computed: {
    ...mapState('klinike',
    ['posete'])
  },
  methods: {
    ...mapActions('klinike', [
      'dobaviSvePosete'
    ])
  },
  created() {
    this.dobaviSvePosete();
  }
}
</script>

<style>

</style>