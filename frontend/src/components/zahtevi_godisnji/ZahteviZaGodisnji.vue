<template>
  <v-container fluid>
    <div class="ml-5 mr-5">
      <NeobradjeniZahteviZaGodisnji class="mt-5"></NeobradjeniZahteviZaGodisnji>
    </div>
  </v-container>
</template>

<script>
import {mapActions} from 'vuex';
import NeobradjeniZahteviZaGodisnji from './NeobradjeniZahteviZaGodisnji';
export default {
  name: "ZahteviZaGodisnji",
  components: {
    NeobradjeniZahteviZaGodisnji
  },
  created(){
    this.fetchAllZahtevi();
  },
  methods: {
    ...mapActions({
      fetchAllZahtevi: "zahteviZaGodisnjiAdmin/fetchAllZahtevi",
    })
  }
}
</script>

<style>

</style>