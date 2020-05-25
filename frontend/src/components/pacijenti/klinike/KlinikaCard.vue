<template>
  <v-card>
    <router-link :to='"/pacijent/klinika/" + klinika.id'>
      <v-img 
        :src='klinika.slika'
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.8)"
        class='align-end'
        height=200>
        <v-card-title class='white--text font-weight-medium subtitle-1'>
          Klinika "{{klinika.naziv}}"
        </v-card-title>
        <v-card-subtitle class='white--text caption'>
            {{klinika.adresa}}, {{klinika.grad}}, {{klinika.drzava}}
        </v-card-subtitle>
      </v-img>
    </router-link>
    <v-card-text class='pt-0 pb-2'>
      <v-list>
        <v-list-item class='pr-0 pl-0'>
          <v-list-item-avatar>
            <v-icon>
              mdi-cash-usd-outline
            </v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title class='subtitle-2'>
              Cena pregleda:
              <span class='subtitle-2 font-weight-bold pb-0'>
                {{minimalnaCena}}.00 RSD
              </span>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item class='pr-0 pl-0'>
          <v-list-item-avatar>
            <v-icon>
              mdi-star
            </v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title class='subtitle-2'>
              Prosečna ocena: <span class='subtitle-2 font-weight-bold'>
                {{minimalnaCena}}
              </span>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card-text>
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
        @click='navigate(klinika.id)'
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
    },
    minimalnaCena: function() {
      if (this.pregledi.length == 0) {
        return '--'
      }
      let min = 1000000;
      this.pregledi.forEach(x => {
        let cena = x.cena - x.popust/100 * x.cena; 
        if(cena < min) {
          min = cena;
        } 
      })
      return min;
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
    },
    navigate: function(klinikaId) {
      this.$router.push({ name: 'pacijent-lekari', params: { klinikaId } })
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