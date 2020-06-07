<template>
<div>
  <!-- neovereni recepti -->
  <!-- TODO: DODATI API ZA GET RECEPTA -->
  <!-- overeni recepti -->
</div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data(){
    return {

    }
  },

  created() {
    this.fetchRecepti();
  },

  methods:{
    ...mapActions({
      fetchRecepti: 'recepti/loadRecepti',
    }),
  }
}
</script>

<style>

</style>