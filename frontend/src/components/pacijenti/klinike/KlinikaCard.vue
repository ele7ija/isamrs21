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
            {{klinika.lokacija.adresa}}, {{klinika.lokacija.grad}}, {{klinika.lokacija.drzava}}
        </v-card-subtitle>
      </v-img>
    </router-link>
    <v-card-text class='pt-0 pb-2'>
      <v-list>
        <v-list-item class='pr-0 pl-0' dense>
          <v-list-item-avatar class='mt-0 mb-0'>
            <v-icon>
              mdi-cash-usd-outline
            </v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title class='subtitle-2'>
              Cena pregleda:
              <span class='subtitle-2 font-weight-bold pb-0'>
                {{minimalnaCena}} RSD
              </span>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item class='pr-0 pl-0' dense="">
          <v-list-item-avatar class='mt-0 mb-0'>
            <v-icon>
              mdi-star
            </v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title class='subtitle-2'>
              Prosečna ocena: <span class='subtitle-2 font-weight-bold'>
                {{prosecnaOcena==0 ? '--' : prosecnaOcena.toFixed(2)}}
              </span>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item class='pr-0 pl-0' dense>
          <v-list-item-avatar class='mt-0 mb-0'>
            <v-icon left @click='openLocation'>mdi-map-marker</v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title class='subtitle-2'>
              Prikaz lokacije
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card-text>
    <v-card-actions>
      <v-btn 
        color='primary'
        outlined
        small
        @click.stop='dialogStartup'>
        <v-icon left>mdi-calendar</v-icon>
        Pregledi
      </v-btn>
      <v-btn
        outlined
        @click='navigate(klinika.id)'
        small
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
    <v-dialog v-model="dialogLocation">
      <v-card height=640>
        <Map :klinika="klinika" :key="mapKey"/>
      </v-card>
    </v-dialog>
    </v-card>
</template>

<script>
import PreglediDialog from './PreglediDialog'
import { mapGetters, mapMutations} from 'vuex';
import oceneAPI from '@/api/ocene';
import Map from '../../maps/Map';
export default {
  name: 'KlinikaCard',
  props: ['klinika'],
  components: {
    PreglediDialog,
    Map
  },
  data: function(){
    return {
      dialogLocation: false,
      dialog: false,
      mapKey: 0,
      ocene: []
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
    },
    prosecnaOcena: function() {
      let sum = 0;
      for (let ocena of this.ocene) {
        sum = sum + ocena.vrednost;
      }
      return this.ocene.length != 0 ? sum / this.ocene.length : 0;
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
    },
    dobavi: function(id) {
      oceneAPI.pronadjiOceneKlinike(id)
      .then(({data}) => {
        this.ocene = data;
      })
    },
    openLocation(){
      this.mapKey += 1;
      this.dialogLocation = true;
    }
  },
  created() {
    this.dobavi(this.klinika.id)
  }
}
</script>

<style scoped>
  a {
    text-decoration: none;
  }
</style>