<template>
  <div v-if="prosecnaOcenaKlinike!=null">
    <v-rating
      v-if="Math.round(prosecnaOcenaKlinike)"
      :value="Math.round(prosecnaOcenaKlinike)"
      readonly
      color="yellow darken-3"
      background-color="grey darken-1"
      length=10
    ></v-rating>
    <v-text-field
      v-else
      outlined
      readonly
      :label="`Prosečna ocena klinike ${nazivKlinike}`"
      v-model="prosecnaOcenaKlinike">
    </v-text-field>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default{
  name: "ProsecnaOcenaKlinike",
  computed: {
    ...mapGetters({
      prosecnaOcenaKlinike: "izvestaji/getProsecnaOcenaKlinike",
      nazivKlinike: "izvestaji/getNazivKlinike"
    })
  },
  created(){
    this.fetchOceneKlinike();
  },
  methods: {
    ...mapActions({
      fetchOceneKlinike: "izvestaji/fetchOceneKlinike"
    })
  }
}
</script>