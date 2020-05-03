<template>
  <v-card>
    <router-link :to='"/pacijent/klinika/" + klinika.id'>
      <v-img 
        :src='klinika.slika'
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.8)"
        class='align-end'
        height=200>
        <v-card-title class='white--text font-weight-medium'>
          Klinika "{{klinika.naziv}}"
        </v-card-title>
        <v-card-subtitle class='white--text'>
            {{klinika.adresa}}, {{klinika.grad}}, {{klinika.drzava}}
        </v-card-subtitle>
      </v-img>
    </router-link>
    <v-card-actions>
      <v-btn 
        color='primary'
        outlined
        @click.stop='dialogStartup'>
      <v-icon left>mdi-calendar</v-icon>
      Pregledi
      </v-btn>
      <v-btn
        outlined
        @click.stop='dialogStartup'
        class=''>
      <v-icon left>mdi-account-group</v-icon>
      Lekari
      </v-btn>
      <PreglediDialog
        v-bind:dialog='dialog'
        v-bind:klinika='klinika'
        @update-dialog='dialog=false'>
      </PreglediDialog>
    </v-card-actions>
    </v-card>
</template>

<script>
import PreglediDialog from './PreglediDialog'
import { mapGetters, mapMutations} from 'vuex';

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
    // ...mapState('pregledi', [
    //   'pregledi'
    // ])
    ...mapGetters('klinike', [
      'getPretrazeniPregledi'
    ]),
    pregledi: function() {
      return this.getPretrazeniPregledi(this.klinika.id);
    }
  },
  methods: {
    // ...mapActions('pregledi', [
    //   'loadAllPregledi'
    // ]),
    ...mapMutations('klinike', [
      'setOdabranaKlinika'
    ]),
    dialogStartup: function() {
      this.setOdabranaKlinika(this.klinika);
      // this.loadAllPregledi(this.klinika.id);
      this.dialog = true;
    }
  },
  created() {
  }
}
</script>

<style scoped>
  a {
    text-decoration: none;
  }
</style>