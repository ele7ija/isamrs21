<template>
  <v-card>
    <v-card-title>
        ID: {{klinika.id}}
    </v-card-title>
    <v-card-actions>
      <v-btn 
        outlined
        @click.stop='dialogStartup'>
      <v-icon left>mdi-calendar</v-icon>
      Pregledi
      </v-btn>
      <PreglediDialog
        v-bind:pregledi='pregledi'
        v-bind:dialog='dialog'
        v-bind:klinika='klinika'
        @update-dialog='dialog=false'>
      </PreglediDialog>
    </v-card-actions>
    </v-card>
</template>

<script>
import PreglediDialog from './PreglediDialog'
import {mapState, mapActions} from 'vuex';

export default {
  name: 'KlinikaCard',
  props: ['klinika'],
  components: {
    PreglediDialog
  },
  data: function(){
    return {
      dialog: false
    }
  },
  computed: {
    ...mapState('pregledi', [
      'pregledi'
    ])
  },
  methods: {
    ...mapActions('pregledi', [
      'loadAllPregledi'
    ]),
    dialogStartup() {
      this.loadAllPregledi(this.klinika.id);
      this.dialog = true;
    }
  },
  created() {
  }
}
</script>

<style>

</style>