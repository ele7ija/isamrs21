<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col lg='3' md='3' sm='6'>
        <PretragaKlinike>
        </PretragaKlinike>
      </v-col>
      <v-col cols=5>
        <v-card outlined="">
          <v-img
            gradient="to bottom, rgba(0,0,0,.0), rgba(0,0,0,.3)"
            class='align-end'
            height=250
            :src='odabranaKlinika.slika'>
          </v-img>
          <v-card-title>
            <v-container fluid class='pa-0'>
              <v-row no-gutters="">
                <v-col class='pa-0'>
                  Klinika: "{{odabranaKlinika.naziv}}"
                </v-col>
              </v-row>
            </v-container>
          </v-card-title>
          <v-card-subtitle class='pb-0'>
            {{odabranaKlinika.adresa}}, 
            {{odabranaKlinika.grad}},
            {{odabranaKlinika.drzava}}
          </v-card-subtitle>
          <v-card-subtitle>
            <v-container fluid class='pa-0'>
              <v-row no-gutters justify="center">
                <v-col cols=8 class='pa-0'>
                  <v-rating
                    background-color="grey darken-1"
                    v-model='prosecnaOcena'
                    small
                    readonly=""
                    length=10>
                  </v-rating>
                </v-col>
                  
                <v-col cols=2 align-self="center" class='font-weight-bold'>
                  {{prosecnaOcena==0 ? '--' : prosecnaOcena.toFixed(2)}}
                </v-col>
              </v-row>
            </v-container>
            
          </v-card-subtitle>
          <v-card-text>
            <v-container class='pa-0'>
              <v-row>
                <v-col>
                  <v-btn
                    color='primary'
                    outlined
                    @click.stop='dialog=true'
                    class='mr-4'>
                    <v-icon left>mdi-calendar</v-icon>
                    Pregledi
                  </v-btn>
                  <PreglediDialog
                    v-bind:dialog='dialog'
                    v-bind:klinika='odabranaKlinika'
                    @update-dialog='dialog=false'>
                  </PreglediDialog>
                  <v-btn
                    outlined
                    @click='navigate(klinikaId)'>
                    <v-icon left>mdi-account-group</v-icon>
                    Lekari
                  </v-btn>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols=8 class='text-justify'>
                  {{odabranaKlinika.opis}}
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
import PretragaKlinike from './PretragaKlinike'
import PreglediDialog from './PreglediDialog'
import oceneAPI from '@/api/ocene';

export default {
  name: 'KlinikaPage',
  props: ['klinikaId'],
  data: function() {
    return {
      dialog: false,
      ocene: []
    }
  },
  components: {
    PretragaKlinike,
    PreglediDialog
  },
  computed: {
    ...mapState('klinike', [
      'odabranaKlinika'
    ]),
    ...mapGetters('klinike',[
      ''
    ]),
    prosecnaOcena: function() {
      let sum = 0;
      for (let ocena of this.ocene) {
        sum = sum + ocena.vrednost;
      }
      return this.ocene.length != 0 ? sum / this.ocene.length : 0;
    }
  },
  methods: {
    ...mapActions('klinike', [
      'dobaviPodatkeKlinikaPage'
    ]),
    dialogStartup() {
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
  },
  created() {
    this.dobaviPodatkeKlinikaPage(this.klinikaId);
    this.dobavi(this.klinikaId)
  }
}
</script>

<style>

</style>