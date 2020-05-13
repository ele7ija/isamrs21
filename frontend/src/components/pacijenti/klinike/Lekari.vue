<template>
  <div>
    <div v-for='lekar in lekari' :key='lekar.id'>
      {{lekar.ime}}
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'Lekari',
  props: ['klinikaId'],
  data: function() {
    return {

    }
  },
  computed: {
    ...mapState('osobljePacijent', [
      'lekari'
    ]),
  },
  methods: {
    ...mapActions('osobljePacijent', [
      'pronadjiLekare'
    ])
  },
  created() {
    this.pronadjiLekare(this.klinikaId);
  }
}
</script>

<style>

</style>