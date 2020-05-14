<template>
  <Sala :sale="sale" :pregledi="pregledi"></Sala>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import Sala from './Sala';
export default {
  name: "SalaMain",
  components: {
    Sala
  },
  computed: {
    ...mapGetters({
      sale: "sale/getSale",
      pregledi: "preglediAdmin/getPreglediKlinike"
    })
  },
  created(){
    this.fetchPregledi();
    this.fetchSale();
    this.$store.commit("salaFilter/reset", {}, {root:true});
  },
  methods: {
    ...mapActions({
      fetchSale: "sale/loadSale",
      fetchPregledi: "preglediAdmin/fetchPreglediKlinike",
    })
  }

}
</script>

<style>

</style>