<template>
  <div>
    <div :key="tipPregleda.id" v-for="tipPregleda in getAll">
      {{tipPregleda.name}}
    </div>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: "TipPregleda",
  computed: mapGetters(
    {
      getAll: 'tipoviPregleda/getTipoviPregleda',
      get: 'tipoviPregleda/getTipPregleda'
    }
  ),
  created(){
    this.setKlinika();
    this.fetchTipoviPregleda();
  },
  methods: {
    ...mapActions(
      {
        setKlinika: 'tipoviPregleda/setKlinika',
        fetchTipoviPregleda: 'tipoviPregleda/loadTipoviPregleda',
        addTipPregleda: 'tipoviPregleda/addTipPregleda',
        updateTipPregleda: 'tipoviPregleda/updateTipPregleda',
        deleteTipPregleda: 'tipoviPregleda/deleteTipPregleda'
      }
    )
  }
}
</script>

<style>

</style>